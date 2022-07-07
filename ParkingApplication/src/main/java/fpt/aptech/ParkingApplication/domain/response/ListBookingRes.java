/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.domain.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CHIEN
 */
public class ListBookingRes implements Serializable{

    private List<BookingRes> bookingRes;

    public ListBookingRes() {
        bookingRes = new ArrayList<>();
    }

    public ListBookingRes(List<BookingRes> bookingRes) {
        this.bookingRes = bookingRes;
    }

    public List<BookingRes> getBookingRes() {
        return bookingRes;
    }

    public void setBookingRes(List<BookingRes> bookingRes) {
        this.bookingRes = bookingRes;
    }

}
