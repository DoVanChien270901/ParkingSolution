/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.implementations;

import com.google.gson.Gson;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import fpt.aptech.ParkingApi.dto.enumm.TitleQrCode;
import fpt.aptech.ParkingApi.dto.qrcontent.ProfileQrContent;
import fpt.aptech.ParkingApi.dto.qrcontent.RechargeQrContent;
import fpt.aptech.ParkingApi.interfaces.IQrCode;
import fpt.aptech.ParkingApi.dto.request.RechargeByQrCodeReq;
import fpt.aptech.ParkingApi.dto.request.RechargeByQrCodeReq;
import fpt.aptech.ParkingApi.dto.request.ScanQrCodeReq;
import fpt.aptech.ParkingApi.dto.response.QrCodeRes;
import fpt.aptech.ParkingApi.entities.Account;
import fpt.aptech.ParkingApi.entities.Profile;
import fpt.aptech.ParkingApi.entities.Qrcode;
import fpt.aptech.ParkingApi.utils.QrCodeUtil;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import java.util.Base64;
import fpt.aptech.ParkingApi.repositorys.AccountRepo;
import fpt.aptech.ParkingApi.repositorys.ProfileRepo;
import fpt.aptech.ParkingApi.repositorys.QrCodeRepo;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;

/**
 *
 * @author CHIEN
 */
@Service
public class QrCodeService implements IQrCode {

    @Autowired
    private QrCodeRepo _qrcodeRepository;
    @Autowired
    private ModelMapperUtil _mapper;
    @Autowired
    private AccountRepo _accountRepository;
    @Autowired
    private QrCodeUtil _qrCodeUtil;
    @Autowired
    private ModelMapperUtil _modelMapper;
    @Autowired
    private ProfileRepo _profileRepository;

    @Override
    public void create(Qrcode qrCode, Object obContent) {
        //Profile profile = _profileRepository.getByUsername(username);
        byte[] byteContent = _qrCodeUtil.generQrCode(obContent, 650, 650);
        qrCode.setContent(byteContent);
        _qrcodeRepository.save(qrCode);
    }

    @Override
    public Boolean delete(Qrcode qrCode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean edit(ProfileQrContent profileQrContent, String username, String title) {
        Qrcode qrcode = _qrcodeRepository.getByUserName(username, title);
        if (qrcode != null) {
//decode Qr
//                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(qrcode.getContent());
//                BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
//                BufferedImageLuminanceSource bufferedImageLuminanceSource = new BufferedImageLuminanceSource(bufferedImage);
//                HybridBinarizer hybridBinarizer = new HybridBinarizer(bufferedImageLuminanceSource);
//                BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
//                MultiFormatReader multiFormatReader = new MultiFormatReader();
//                Result result = multiFormatReader.decode(binaryBitmap);
//                String stringJson = result.getText();
//                Gson g = new Gson();
//                ProfileQrContent objectContent = g.fromJson(stringJson, ProfileQrContent.class);
            byte[] byteContent = _qrCodeUtil.generQrCode(profileQrContent, 650, 650);
            qrcode.setContent(byteContent);
            _qrcodeRepository.save(qrcode);
            return true;
        }
        return false;
    }

    @Override
    public QrCodeRes qrCodeRes(String accountid) {
        Qrcode qrcode = _qrcodeRepository.getByUserName(accountid, TitleQrCode.PROFILE.toString());
        QrCodeRes qrCodeRes = _modelMapper.map(qrcode, QrCodeRes.class);
        return qrCodeRes;
    }

    @Override
    public boolean RechargeByQrContent(ScanQrCodeReq scanQrCodeReq) {
        Object obContent = _qrCodeUtil.extractAllClaims(scanQrCodeReq.getQrcontent());
        RechargeQrContent rechargeQrContent = _mapper.map(obContent, RechargeQrContent.class);
        Profile profile = _profileRepository.getByUsername(rechargeQrContent.getUsername());
        if (profile != null) {
            _profileRepository.updateBalanceByUsername(rechargeQrContent.getAmount() + profile.getBalance(), rechargeQrContent.getUsername());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object getContent(ScanQrCodeReq scanQrCodeReq) {
        Object obContent = _qrCodeUtil.extractAllClaims(scanQrCodeReq.getQrcontent());
        return obContent;
    }

}
