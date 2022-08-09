/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fpt.aptech.ParkingApi.interfaces;

import fpt.aptech.ParkingApi.dto.request.AddParkingReq;
import fpt.aptech.ParkingApi.dto.request.UpdateParkingReq;
import fpt.aptech.ParkingApi.dto.response.LoadStatusParking;
import fpt.aptech.ParkingApi.dto.response.PageParkingHistoryRes;
import fpt.aptech.ParkingApi.dto.response.ParkingHistoryRes;
import fpt.aptech.ParkingApi.dto.response.ParkingRes;
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
    PageParkingHistoryRes getHistory(String parkingname, LocalDate fromDate, LocalDate toDate, int page, int size);
    PageParkingHistoryRes getHistory(int page, int size);
    PageParkingHistoryRes getHistoryByName(String name, int page, int size);
    boolean add(AddParkingReq parking);
    void delete(String id);
    boolean update(UpdateParkingReq parking);
    List<ParkingRes> seachByName(String name);
    int fillOneSlot(String name);
    int freeOneSlot(String name);
    List<LoadStatusParking> allStatusParking();
    LoadStatusParking StatusParkingByName(String parkingname);
    List<ParkingHistoryRes> getAllHistoryByUserName(String username);
    List<ParkingHistoryRes> getAllHistoryByUserSearch(String username, LocalDate fromDate, LocalDate toDate);
}
