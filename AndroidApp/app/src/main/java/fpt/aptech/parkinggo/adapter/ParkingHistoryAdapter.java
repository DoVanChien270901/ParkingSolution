package fpt.aptech.parkinggo.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.domain.response.ParkingHistoryRes;
import fpt.aptech.parkinggo.domain.response.TransactionRes;

public class ParkingHistoryAdapter extends BaseAdapter {
    private ParkingHistoryRes[] parkingHistoryRes;
    private Activity context;
    public ParkingHistoryAdapter(Activity context, ParkingHistoryRes[] parkingHistoryRes) {
        this.parkingHistoryRes = parkingHistoryRes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return parkingHistoryRes.length;
    }

    @Override
    public Object getItem(int position) {
        return parkingHistoryRes[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup viewGroup) {
        // Get the data item for this position
        ParkingHistoryRes item = parkingHistoryRes[position];
        // Check if an existing view is being reused, otherwise inflate the view
        View viewContact;
        if (view == null){
            viewContact = View.inflate(viewGroup.getContext(), R.layout.list_parking_history, null);
        }else{
            viewContact = view;
        }
        TextView tvParkingName = (TextView) viewContact.findViewById(R.id.lv_list_parking_history_tv_parkingname);
        TextView tvDatetime = (TextView) viewContact.findViewById(R.id.lv_list_parking_history_tv_datetime);
        TextView tvCarName = (TextView) viewContact.findViewById(R.id.lv_list_parking_history_tv_carname);
        TextView tvLicensePlates = (TextView) viewContact.findViewById(R.id.lv_list_parking_history_tv_license_plates);
        //set text
        tvParkingName.setText(item.getParkingname());
        tvDatetime.setText(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(item.getStarttime()));
        tvCarName.setText(item.getCarname());
        tvLicensePlates.setText(item.getLisenceplates());
        return viewContact;
    }
}
