package fpt.aptech.parkinggo.adapter;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.domain.response.ParkingRes;

public class ParkingAdapter extends ArrayAdapter<ParkingRes> {
    LatLng ori;

    public ParkingAdapter(Context context, ArrayList<ParkingRes> list, LatLng origin) {
        super(context, 0, list);
        this.ori = origin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ParkingRes parking = getItem(position);
        float result[] = new float[10];
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_parking_info, parent, false);
        }
        // Lookup view for data population
        Location.distanceBetween(ori.latitude, ori.longitude, Double.valueOf(parking.getLatitude()), Double.valueOf(parking.getLongtitude()), result);

        TextView tvAddress = (TextView) convertView.findViewById(R.id.l_all_parking_info_tv_address);
        TextView tvName = (TextView) convertView.findViewById(R.id.l_all_parking_info_tv_name);
        TextView tvRadius = (TextView) convertView.findViewById(R.id.l_all_parking_info_tv_radius);
        TextView tvSeat = (TextView) convertView.findViewById(R.id.l_all_parking_info_tv_nop);
        TextView tvSalary = (TextView) convertView.findViewById(R.id.l_all_parking_info_tv_salary);
        String f = String.format("%.03f", result[0] / 1000);
        String fSalary = String.format("%.03f", parking.getRentcost());
        String pSeat = parking.getBlank() + "/" + parking.getNop();

        tvRadius.setText(f + " km");
        // Populate the data into the template view using the data object
        tvName.setText(parking.getName());
        tvAddress.setText(parking.getAddress());
        tvSalary.setText(fSalary + " đ");
        tvSeat.setText(pSeat);

        return convertView;

        // Return the completed view to render on screen
    }
}
