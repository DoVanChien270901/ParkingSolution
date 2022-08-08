package fpt.aptech.parkinggo.domain.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListBookingRes implements Serializable {
    private List<BookingRes> bookingRes;

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
