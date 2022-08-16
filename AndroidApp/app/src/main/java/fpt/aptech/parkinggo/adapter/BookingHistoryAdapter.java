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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

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

        TextView tvParkingName = viewContact.findViewById(R.id.a_bookinglist_tv_parkingname);
        TextView tvFromDate = viewContact.findViewById(R.id.a_bookinglist_tv_fromdate);
        TextView tvAmount = viewContact.findViewById(R.id.a_bookinglist_tv_amount);
        TextView tvToDate = viewContact.findViewById(R.id.a_bookinglist_tv_todate);

        tvParkingName.setText(bookingRes.getParkingname());
        tvToDate.setText(" - "+DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(bookingRes.getStarttime().plusHours(bookingRes.getStarttime().getMonthValue()+ bookingRes.getTimenumber())));
        tvAmount.setText(formatInteger(String.valueOf(bookingRes.getPrice()))+"đ");
        tvFromDate.setText("from "+DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(bookingRes.getStarttime()));
        return viewContact;
    }
    private String formatInteger(String str) {
        BigDecimal parsed = new BigDecimal(str);
        DecimalFormat formatter =
                new DecimalFormat( "#,###", new DecimalFormatSymbols(Locale.US));
        return formatter.format(parsed);
    }
}
