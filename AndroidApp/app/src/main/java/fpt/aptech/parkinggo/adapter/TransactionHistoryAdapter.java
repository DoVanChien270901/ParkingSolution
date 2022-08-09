package fpt.aptech.parkinggo.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
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
import fpt.aptech.parkinggo.domain.response.ParkingRes;
import fpt.aptech.parkinggo.domain.response.TransactionRes;

public class TransactionHistoryAdapter extends BaseAdapter {
    private TransactionRes[] transactionRes;
    private Activity context;
    public TransactionHistoryAdapter(Activity context, TransactionRes[] transactionRes) {
        this.transactionRes = transactionRes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return transactionRes.length;
    }

    @Override
    public Object getItem(int position) {
        return transactionRes[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup viewGroup) {
        // Get the data item for this position
        TransactionRes item = transactionRes[position];
        // Check if an existing view is being reused, otherwise inflate the view
        View viewContact;
        if (view == null){
            viewContact = View.inflate(viewGroup.getContext(), R.layout.list_transaction_history, null);
        }else{
            viewContact = view;
        }
        TextView tvType = (TextView) viewContact.findViewById(R.id.lv_list_transaction_history_tv_type);
        TextView tvStartTime = (TextView) viewContact.findViewById(R.id.lv_list_transaction_history_tv_starttime);
        TextView tvTransNo = (TextView) viewContact.findViewById(R.id.lv_list_transaction_history_tv_transno);
        TextView tvAmount = (TextView) viewContact.findViewById(R.id.lv_list_transaction_history_tv_amount);
        //set text
        tvType.setText(item.getStype());
        tvStartTime.setText(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(item.getDatetime()));
        tvTransNo.setText(item.getTransno());
        if(item.getStype().equals("e-Booking")){
            tvAmount.setText("- "+formatInteger(item.getAmount().toString())+"đ");
            tvAmount.setTextColor(Color.parseColor("#ff5722"));
            return viewContact;
        }else{
            tvAmount.setText("+ "+formatInteger(item.getAmount().toString())+"đ");
            tvAmount.setTextColor(Color.parseColor("#3C8100"));
            return viewContact;
        }
    }
    private String formatInteger(String str) {
        BigDecimal parsed = new BigDecimal(str);
        DecimalFormat formatter =
                new DecimalFormat( "#,###", new DecimalFormatSymbols(Locale.US));
        return formatter.format(parsed);
    }

}
