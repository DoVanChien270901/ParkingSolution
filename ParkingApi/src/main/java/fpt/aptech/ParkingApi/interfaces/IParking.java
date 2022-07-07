/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fpt.aptech.ParkingApi.interfaces;

import fpt.aptech.ParkingApi.dto.response.ParkingRes;
import fpt.aptech.ParkingApi.entities.Parkinglocation;
import java.util.List;

/**
 *
 * @author CHIEN
 */
public interface IParking {
    Parkinglocation getByParkingName(String parkingName);
    List<ParkingRes> listParking();
}
