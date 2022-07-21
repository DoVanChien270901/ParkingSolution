/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.implementations;

import fpt.aptech.ParkingApi.dto.enumm.StatusBooking;
import fpt.aptech.ParkingApi.dto.qrcontent.BookingQrContent;
import fpt.aptech.ParkingApi.dto.request.NewBookingReq;
import fpt.aptech.ParkingApi.dto.response.ScanQrCodeBookingRes;
import fpt.aptech.ParkingApi.dto.response.BookingDetailRes;
import fpt.aptech.ParkingApi.dto.response.BookingRes;
import fpt.aptech.ParkingApi.dto.response.ScanBookingRes;
import fpt.aptech.ParkingApi.entities.*;
import fpt.aptech.ParkingApi.interfaces.IBooking;
import fpt.aptech.ParkingApi.repositorys.BookingRepo;
import fpt.aptech.ParkingApi.repositorys.ParkingRepo;
import fpt.aptech.ParkingApi.repositorys.ProfileRepo;
import fpt.aptech.ParkingApi.repositorys.QrCodeRepo;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import fpt.aptech.ParkingApi.utils.QrCodeUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public Booking create(NewBookingReq bookingReq) {
        Booking booking = _mapper.map(bookingReq, Booking.class);
        booking.setStatus(StatusBooking.NONE.toString());
        booking.setPrice(_parkingRepo.getRencostByName(bookingReq.getParkingname()) * bookingReq.getTimenumber());
        booking.setAccountid(_profileRepo.getByUsername(bookingReq.getUsername()));
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

    @Override
    public ScanQrCodeBookingRes getBookingById(int id) {
        ScanQrCodeBookingRes res = new ScanQrCodeBookingRes();
        Booking booking = _bookingRepo.getBookingById(id);
        res.setCarname(booking.getCarname());
        res.setLisenceplates(booking.getLisenceplates());
        res.setParkingname(booking.getParkingname().getName());
        res.setPrice(booking.getPrice());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        res.setStarttime(dateTimeFormatter.format(booking.getStarttime()));
        LocalDateTime endTime = booking.getStarttime().plusHours(booking.getTimenumber());
        res.setEndtime(dateTimeFormatter.format(endTime));
        if (booking.getStatus().equals(StatusBooking.NONE.toString())) {
            res.setStatus("CHECK IN");
        } else if (booking.getStatus().equals(StatusBooking.IN.toString())) {
            res.setStatus("CHECK OUT");
            res.setCheckin(dateTimeFormatter.format(booking.getCheckin()));
        }
        return res;
    }

}
