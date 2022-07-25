/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fpt.aptech.ParkingApi.interfaces;

import fpt.aptech.ParkingApi.dto.response.PageParkingHistoryRes;
import fpt.aptech.ParkingApi.dto.response.ParkingRes;
import fpt.aptech.ParkingApi.entities.Parkinglocation;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author CHIEN
 */
public interface IParking {
    ParkingRes getByParkingName(String parkingName);
    List<ParkingRes> listParking();
    PageParkingHistoryRes getHistoryByUserName(String username, int page, int size);
    PageParkingHistoryRes getHistoryByUserName(String username, LocalDate fromDate, LocalDate toDate, int page, int size);
    PageParkingHistoryRes getHistory(LocalDate fromDate, LocalDate toDate, int page, int size);
    PageParkingHistoryRes getHistory(int page, int size);
}
