/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.implementations;

import fpt.aptech.ParkingApi.dto.request.NewBookingReq;
import fpt.aptech.ParkingApi.dto.response.BookingDetailRes;
import fpt.aptech.ParkingApi.dto.response.BookingRes;
import fpt.aptech.ParkingApi.entities.Booking;
import fpt.aptech.ParkingApi.entities.Parkinglocation;
import fpt.aptech.ParkingApi.interfaces.IBooking;
import fpt.aptech.ParkingApi.repositorys.BookingRepo;
import fpt.aptech.ParkingApi.repositorys.ParkingRepo;
import fpt.aptech.ParkingApi.repositorys.ProfileRepo;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author CHIEN
 */
@Service
public class BookingService implements IBooking {

    @Autowired
    private ProfileRepo _profileRepo;
    @Autowired
    private ParkingRepo _parkingRepo;
    @Autowired
    private BookingRepo _bookingRepo;
    @Autowired
    private ModelMapperUtil _mapper;

    @Override
    public Booking create(NewBookingReq bookingReq, String username) {
        Booking booking = _mapper.map(bookingReq, Booking.class);
        booking.setPrice(_parkingRepo.getRencostByName(bookingReq.getParkingname())* bookingReq.getTimenumber());
        booking.setAccountid(_profileRepo.getByUsername(username));
        booking.setParkingname(_parkingRepo.getByName(bookingReq.getParkingname()));
        return _bookingRepo.save(booking);
    }

    @Override
    public List<BookingRes> getListBookingByUsername(String username) {
        return _bookingRepo.getListBookingByUsername(username);
    }

    @Override
    public BookingDetailRes getDetailBookingById(int id) {
        return _bookingRepo.getDetailBookingById(id);
    }
    
    
}
