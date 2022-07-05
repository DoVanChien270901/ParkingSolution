/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.implementations;

import fpt.aptech.ParkingApi.dto.enumm.TitleQrCode;
import fpt.aptech.ParkingApi.dto.request.EditProfileReq;
import fpt.aptech.ParkingApi.dto.request.RegisterReq;
import fpt.aptech.ParkingApi.interfaces.IProfile;
import fpt.aptech.ParkingApi.dto.response.PageProfileRes;
import fpt.aptech.ParkingApi.dto.response.ProfileRes;
import fpt.aptech.ParkingApi.entities.Account;
import fpt.aptech.ParkingApi.entities.Profile;
import fpt.aptech.ParkingApi.entities.Qrcode;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import fpt.aptech.ParkingApi.repositorys.ProfileRepo;
import fpt.aptech.ParkingApi.repositorys.QrCodeRepo;

/**
 *
 * @author CHIEN
 */
@Service
public class ProfileService implements IProfile {

    @Autowired
    private ProfileRepo _profileRepository;
    @Autowired
    private QrCodeRepo _qrCodeRepository;
    @Autowired
    private ModelMapperUtil _mapper;
    

    @Override
    public PageProfileRes findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Profile> pageprofile = _profileRepository.findAll(pageRequest);
            List<ProfileRes> listpro = _mapper.mapList(pageprofile.getContent(), ProfileRes.class);
            PageProfileRes pageProfileRes = new PageProfileRes();
            pageProfileRes.setListProfile(listpro);
            pageProfileRes.setCurrentPage(pageprofile.getPageable().getPageNumber());
            pageProfileRes.setSize(pageprofile.getSize());
            pageProfileRes.setTotalPages(pageprofile.getTotalPages());
        return pageProfileRes;
    }

    @Override
    public ProfileRes getByUserName(String username) {
        Profile profile =  _profileRepository.getByUsername(username);
        ProfileRes pres = _mapper.map(profile, ProfileRes.class);
        String title = TitleQrCode.PROFILE.toString();
        Qrcode qrcode = _qrCodeRepository.getByUserName(username, TitleQrCode.PROFILE.toString());
        pres.setQrcontent(qrcode.getContent());
        return pres;
    }

    @Override
    public Profile create(RegisterReq registerReq) {
        Profile profile = _mapper.map(registerReq, Profile.class);
        _profileRepository.save(profile);
        return profile;
    }

    @Override
    public boolean edit(EditProfileReq editProfileReq, String username) {
        Profile profile = _profileRepository.getByUsername(username);
        if (profile != null) {
            profile = _mapper.map(editProfileReq, Profile.class);
            profile.setUsername(username);
            _profileRepository.save(profile);
            return true;
        } else {
            return false;
        }
    }
}
