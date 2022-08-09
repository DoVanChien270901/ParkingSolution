package fpt.aptech.parkinggo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.domain.response.BookingRes;


public class BookingHistoryAdapter extends BaseAdapter {

    ArrayList<BookingRes> bookingRes;
    Activity activity;

    public BookingHistoryAdapter(ArrayList<BookingRes> bookingRes, Activity activity) {
        this.bookingRes = bookingRes;
        this.activity = activity;
    }

    public BookingHistoryAdapter(ArrayList<BookingRes> bookingRes) {
        this.bookingRes = bookingRes;
    }

    @Override
    public int getCount() {
        return bookingRes.size();
    }

    @Override
    public Object getItem(int position) {
        return bookingRes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bookingRes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewContact;
        if (convertView==null){
            viewContact = View.inflate(parent.getContext(), R.layout.all_booking_history, null);
        }else {
            viewContact = convertView;
        }

        BookingRes bookingRes = (BookingRes) getItem(position);

        TextView tvNo = viewContact.findViewById(R.id.a_bookinglist_tv_no);
        TextView tvParkingname = viewContact.findViewById(R.id.a_bookinglist_tv_parkingname);
        TextView tvAmount = viewContact.findViewById(R.id.a_bookinglist_tv_amount);
        TextView tvDatetime = viewContact.findViewById(R.id.a_bookinglist_tv_datetime);
        TextView tvTimenum = viewContact.findViewById(R.id.a_bookinglist_tv_timenum);

        tvAmount.setText(String.valueOf(bookingRes.getPrice()));
        tvDatetime.setText(bookingRes.getStarttime().toString());
        tvTimenum.setText(String.valueOf(bookingRes.getTimenumber()));
        tvNo.setText(String.valueOf(bookingRes.getId()));
        tvParkingname.setText(bookingRes.getParkingname());

        return viewContact;
    }
}
