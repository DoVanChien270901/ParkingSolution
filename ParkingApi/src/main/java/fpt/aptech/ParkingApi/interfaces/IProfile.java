/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.interfaces;

import fpt.aptech.ParkingApi.dto.request.RegisterReq;
import fpt.aptech.ParkingApi.dto.request.EditProfileReq;
import fpt.aptech.ParkingApi.dto.response.PageProfileRes;
import fpt.aptech.ParkingApi.dto.response.ProfileRes;
import fpt.aptech.ParkingApi.entities.Account;

/**
 *
 * @author CHIEN
 */
public interface IProfile {
    PageProfileRes findAll(int page, int size);
    ProfileRes getByUserName(String username);
    void create(RegisterReq registerReq);
    boolean edit(EditProfileReq editProfileReq, String username);
}
