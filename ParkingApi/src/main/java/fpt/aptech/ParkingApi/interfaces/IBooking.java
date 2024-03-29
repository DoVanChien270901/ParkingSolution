/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fpt.aptech.ParkingApi.interfaces;

import fpt.aptech.ParkingApi.dto.request.NewBookingReq;
import fpt.aptech.ParkingApi.dto.response.*;
import fpt.aptech.ParkingApi.entities.Booking;
import java.util.List;

/**
 *
 * @author CHIEN
 */
public interface IBooking {
    Booking create(NewBookingReq bookingReq);
    List<BookingRes> getListBookingByUsername(String username);
    BookingDetailRes getDetailBookingById(int id, String username);
    ScanQrCodeBookingRes getBookingById(int id);
    PageBookingRes findAll(int page, int size);
    List<String> getLocationCode(String Parkingname);
    PageBookingRes getByParkingName(String Parkingname, int page, int size);
}
