/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.implementations;

import fpt.aptech.ParkingApi.dto.qrcontent.BookingQrContent;
import fpt.aptech.ParkingApi.dto.request.NewBookingReq;
import fpt.aptech.ParkingApi.dto.response.BookingDetailRes;
import fpt.aptech.ParkingApi.dto.response.BookingRes;
import fpt.aptech.ParkingApi.entities.*;
import fpt.aptech.ParkingApi.interfaces.IBooking;
import fpt.aptech.ParkingApi.repositorys.BookingRepo;
import fpt.aptech.ParkingApi.repositorys.ParkingRepo;
import fpt.aptech.ParkingApi.repositorys.ProfileRepo;
import fpt.aptech.ParkingApi.repositorys.QrCodeRepo;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import fpt.aptech.ParkingApi.utils.QrCodeUtil;
import java.util.List;
import org.modelmapper.ModelMapper;
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
    private QrCodeRepo _qrCodeService;
    @Autowired
    private QrCodeUtil _qrCodeUtil;
    @Autowired
    private ModelMapperUtil _mapper;

    @Override
    public Booking create(NewBookingReq bookingReq, String username) {
        Booking booking = _mapper.map(bookingReq, Booking.class);
        booking.setPrice(_parkingRepo.getRencostByName(bookingReq.getParkingname()) * bookingReq.getTimenumber());
        booking.setAccountid(_profileRepo.getByUsername(username));
        booking.setParkingname(_parkingRepo.getByName(bookingReq.getParkingname()));
        return _bookingRepo.save(booking);
    }

    @Override
    public List<BookingRes> getListBookingByUsername(String username) {
        return _bookingRepo.getListBookingByUsername(username);
    }

    @Override
    public BookingDetailRes getDetailBookingById(int id, String username) {
        BookingDetailRes res = _bookingRepo.getDetailBookingById(id);
        List<Qrcode> qrcodes = _qrCodeService.getByUserNameAndTBooking(username);
        for (Qrcode qrcode : qrcodes) {
            ModelMapper modelMapper = new ModelMapper();
            BookingQrContent b = _mapper.map(_qrCodeUtil.decode1(qrcode.getContent()), BookingQrContent.class);
            if (b.getBookingid() == id) {
                res.setQrcontent(qrcode.getContent());
                break;
            }
        }
        return res;
    }

}
