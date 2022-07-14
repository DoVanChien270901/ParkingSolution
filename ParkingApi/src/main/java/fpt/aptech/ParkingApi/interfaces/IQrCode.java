/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fpt.aptech.ParkingApi.interfaces;

import fpt.aptech.ParkingApi.dto.qrcontent.ProfileQrContent;
import fpt.aptech.ParkingApi.dto.request.ScanQrCodeReq;
import fpt.aptech.ParkingApi.dto.response.QrCodeRes;
import fpt.aptech.ParkingApi.entities.Qrcode;

/**
 *
 * @author CHIEN
 */
public interface IQrCode {
    void create(Qrcode qrcode, Object qrContent);
    Boolean delete(Qrcode qrcode);
    Boolean edit(ProfileQrContent profileQrContent, String username, String title);
    QrCodeRes qrCodeRes(String accountid);
    boolean RechargeByQrContent(ScanQrCodeReq scanQrCodeReq);
    Object getContent(ScanQrCodeReq scanQrCodeReq);
}
