package fpt.aptech.parkinggo.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fpt.aptech.parkinggo.R;
import fpt.aptech.*;
import fpt.aptech.parkinggo.adapter.ParkingAdapter;
import fpt.aptech.parkinggo.asynctask.LoadListParkingTask;
import fpt.aptech.parkinggo.asynctask.TransactionHistoryTask;
import fpt.aptech.parkinggo.callback.CallBack;
import fpt.aptech.parkinggo.callback.CustomProgressDialog;
import fpt.aptech.parkinggo.databinding.ActivityMapsBinding;
import fpt.aptech.parkinggo.domain.dto.BottomSheetListView;
import fpt.aptech.parkinggo.domain.dto.FetchURL;
import fpt.aptech.parkinggo.domain.dto.TaskLoadedCallback;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.domain.response.ParkingRes;
import fpt.aptech.parkinggo.domain.response.ParkingSortRes;
import fpt.aptech.parkinggo.domain.response.TransactionRes;
import fpt.aptech.parkinggo.statics.Session;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback, NavigationView.OnNavigationItemSelectedListener {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final long INTERVAL = 200;
    private static final long FASTEST_INTERVAL = 200;
    private int REQUEST_CODE = 111;
    LatLng origin;
    LatLng destination;
    Polyline currentPolyline;
    LatLng currentMarker;
    ImageButton menuList;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvBalance;
    private ImageButton ibtnEdit;
    private ImageView qrcode;
    private static final String TAG = "MapsActivity";

    ParkingRes[] parkingLocationList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        /*---------------------------------Hooks-----------------------------*/
        drawerLayout = findViewById(R.id.drawer_layout1);
        navigationView = findViewById(R.id.user_nav_view1);
        toolbar = findViewById(R.id.toolbar1);
        /*--------------------------------Load TextView Header --------------------------*/
        View header = navigationView.getHeaderView(0);
        tvName = header.findViewById(R.id.hd_tv_name);
        tvEmail = header.findViewById(R.id.hd_tv_email);
        tvBalance = header.findViewById(R.id.hd_tv_balance);
        //qrcode = header.findViewById(R.id.f_profile_imv_qrcode);
        ibtnEdit = header.findViewById(R.id.hd_ibtn_edit);
        LoginRes loginRes = (LoginRes) Session.getSession();
        tvName.setText(loginRes.getFullname());
        tvEmail.setText(loginRes.getEmail());
//        DecimalFormat formatter = new DecimalFormat("###,###,###");
//        tvBalance.setText(formatter.format(loginRes.getBalance()) + " VND");
        tvBalance.setText(formatInteger(loginRes.getBalance().toString())+"đ");
//        Bitmap bmp = BitmapFactory.decodeByteArray(loginRes.getQrcode(), 0, loginRes.getQrcode().length);
//        qrcode.setImageBitmap(Bitmap.createScaledBitmap(bmp, 650, 650, false));
        /*--------------------------------Tool Bar--------------------------*/
        //setSupportActionBar(toolbar);
        /*--------------------------------Navigation Drawer Menu--------------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toolbar.setTitle("ParkingGo");
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //call api
//        LoadListParkingTask task = new LoadListParkingTask(MapsActivity.this);
//        try {
//            ResponseEntity<?> res = task.execute().get();
//            parkingLocationList = (ParkingRes[]) res.getBody();
//        } catch (Exception e) {
//
//        }
        parkingLocationList = (ParkingRes[]) intent.getSerializableExtra("list");
        if (parkingLocationList == null) {
            LoadListParkingTask task = new LoadListParkingTask(MapsActivity.this);
            try {
                ResponseEntity<?> res = task.execute().get();
                parkingLocationList = (ParkingRes[]) res.getBody();
            } catch (Exception e) {

            }
        }
        //setContentView(R.layout.activity_maps);
        if (checkGooglePlayServices()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Google Play Services Not Available", Toast.LENGTH_SHORT).show();
        }

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // *important
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        checkGPS();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
        //onlick edit profile
        ibtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            //getCurrentLocation();
            LatLng defaultMarker = new LatLng(10.817985637856664, 106.63079600428019);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultMarker, 12));
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    checkGPS();
                    return true;
                }
            });
            arrPlace();
        }
    }

    // Add All Data
//    private void addData() {
//        parkingLocationList.add(parkingLocation1);
//    }


    // Menu List
    private void menuList() {
        menuList = findViewById(R.id.a_maps_ibtn_list_parking);
        menuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MapsActivity.this, "Menu List", Toast.LENGTH_SHORT).show();
                if (origin == null) {
                    checkGPS();
                } else {
                    ArrayList<ParkingRes> checkRadiusParking = new ArrayList<>();
                    float results[] = new float[10];
                    BottomSheetDialog dialog = new BottomSheetDialog(MapsActivity.this);
                    dialog.setContentView(R.layout.bottom_sheet_view);
                    BottomSheetListView listView = (BottomSheetListView) dialog.findViewById(R.id.listViewBtmSheet);

                    for (ParkingRes p : parkingLocationList) {
                        Location.distanceBetween(origin.latitude, origin.longitude, Double.valueOf(p.getLatitude()), Double.valueOf(p.getLongtitude()), results);
                        if ((results[0] / 1000) < 10.000) {
                            checkRadiusParking.add(p);
                        }
                    }
                    for (int i = 0; i<checkRadiusParking.size(); i++) {
                        Location.distanceBetween(origin.latitude, origin.longitude, Double.valueOf(checkRadiusParking.get(i).getLatitude()), Double.valueOf(checkRadiusParking.get(i).getLongtitude()), results);
                        for(int j = 0; j<checkRadiusParking.size(); j++){
                            float[] min = new float[10];
                            Location.distanceBetween(origin.latitude, origin.longitude, Double.valueOf(checkRadiusParking.get(j).getLatitude()), Double.valueOf(checkRadiusParking.get(j).getLongtitude()), min);
                            if (results[0]<min[0]) {
                                Collections.swap(checkRadiusParking, i, j);
                            }
                        }
                    }
                    //listView.setAdapter();
                    ParkingAdapter adapter = new ParkingAdapter(MapsActivity.this, (ArrayList<ParkingRes>) checkRadiusParking, origin);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            //check blank
                            if (parkingLocationList[position].getBlank() != 0) {
                                //Toast.makeText(MapsActivity.this, "Test Item", Toast.LENGTH_SHORT).show();
                                ParkingRes pl = checkRadiusParking.get(position);

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(pl.getLatitude()), Double.valueOf(pl.getLongtitude())), 16));
                                dialog.dismiss();
                            } else {
                                Toast.makeText(MapsActivity.this, "The parking lot is not available", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    // Get Current Location
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> taskLocation = fusedLocationProviderClient.getLastLocation();
            taskLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentMarker = new LatLng(location.getLatitude(), location.getLongitude());
                        if (origin == null) {
                            origin = currentMarker;
                        }
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    // Arraylist Place //
    private void arrPlace() {
        // Set Icon Marker Parking
        int height = 100;
        int width = 100;
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

        // For Each ParkingLocation with List
        for (ParkingRes p : parkingLocationList) {
            // Marker Options (LatLng, Title, Snippet)
            MarkerOptions op = new MarkerOptions();
            op.position(new LatLng(Double.valueOf(p.getLatitude()), Double.valueOf(p.getLongtitude())));
            op.title(p.getName());
            op.snippet(p.getAddress());
            mMap.addMarker(op.icon(smallMarkerIcon));
        }
        // Set On CLick Marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                // Move Camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));
                // Set Bottom Sheet
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MapsActivity.this);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.bottom_sheet_dialog, (LinearLayout) findViewById(R.id.bottomSheetDialog)
                );
                // Set TextView, ImageButton
                TextView name = bottomSheetView.findViewById(R.id.txtName);
                TextView snippet = bottomSheetView.findViewById(R.id.txtAddress);
                ImageButton direction = bottomSheetView.findViewById(R.id.direction);
                ImageButton getStart = bottomSheetView.findViewById(R.id.start);
                ImageButton booking = bottomSheetView.findViewById(R.id.booking);
                // Get Position and Put on Destination
                destination = marker.getPosition();
                // OnClick Direction
                direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (origin == null) {
                            checkGPS();
                        } else {
                            String url = getUrl(origin, destination, "driving");
                            new FetchURL(MapsActivity.this).execute(url, "driving");
                        }
                    }
                });
                // OnClick Get Start
                getStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (origin == null) {
                            checkGPS();
                        } else {
                            //Toast.makeText(MapsActivity.this, "Get Start", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + destination.latitude + "," + destination.longitude + "&mode=d"));
                            intent.setPackage("com.google.android.apps.maps");
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }
                    }
                });
                // OnClick Booking
                booking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //booking detail Actitvity
                        for (ParkingRes item : parkingLocationList) {
                            if (item.getName().equals(name.getText().toString())){
                                if (item.getBlank()!=0){
                                    Intent intent = new Intent(MapsActivity.this, BookingActivity.class);
                                    intent.putExtra("parkingname", name.getText().toString());
                                    intent.putExtra("rentCost", item.getRentcost());
                                    startActivity(intent);
                                    return;
                                }else{
                                    Toast.makeText(MapsActivity.this, "The parking lot is not available", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }

                    }
                });
                // Set Text, ContentView
                name.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());
                bottomSheetDialog.setContentView(bottomSheetView);
                // Show and Return
                bottomSheetDialog.show();
                return false;
            }
        });
        // Menu List On Button
        menuList();
    }

    // Get Url
    private String getUrl(LatLng ori, LatLng dest, String directionMode) {
        String str_origin = "origin=" + ori.latitude + "," + ori.longitude;
        String str_destination = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_destination + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.api_key);
        return url;
    }

    // Check Google Play Services
    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(result)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, 201, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(MapsActivity.this, "User Canceled Dialog", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }
        return false;
    }

    // Check GPS
    private void checkGPS() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        Task<LocationSettingsResponse> locationSettingsResponseTask = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());
        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    //Toast.makeText(MapsActivity.this, "GPS is already enable", Toast.LENGTH_SHORT).show();
                    getCurrentLocation();
                    if (currentMarker != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker, 16));
                    }
                } catch (ApiException e) {
                    if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult(MapsActivity.this, 101);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            sendIntentException.printStackTrace();
                        }
                    }
                    if (e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                        Toast.makeText(MapsActivity.this, "Settings is not already enable", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Activity Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MapsActivity.this, "Now GPS is enable", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MapsActivity.this, "Denied GPS enable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Polyline
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    // Point
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //no do thing
        }
    }

//    private String CurrentTitleAT = "Map";

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String id = item.toString();
        Intent intent;
        switch (id) {
            case "Direct Payment":
                intent = new Intent(this, DirectPaymentActivity.class);
                startActivity(intent);
                break;
            case "Electronic Payment":
                //call api
                LoadListParkingTask task = new LoadListParkingTask(MapsActivity.this);
                ArrayList<ParkingRes> parkingRes = new ArrayList<>();
                try {
                    ResponseEntity<?> res = task.execute().get();
                    parkingRes = (ArrayList<ParkingRes>) res.getBody();
                } catch (Exception e) {
                    intent = new Intent(MapsActivity.this, RechargeActivity.class);
                    startActivity(intent);
                    return true;
                }
            case "Transaction History":
                //call api
                TransactionHistoryTask task1 = new TransactionHistoryTask(this);
                //ArrayList<ParkingRes> parkingRes =  new ArrayList<>();
                TransactionRes[] transactionRes = null;
                try {
                    ResponseEntity<?> res = task1.execute().get();
                    transactionRes = (TransactionRes[]) res.getBody();
                } catch (Exception e) {

                }
                intent = new Intent(this, TransactionHistoryActivity.class);
                intent.putExtra("list", transactionRes);
                startActivity(intent);
//                    CurrentTitleAT = "Transaction History";
//                    Intent intent = new Intent(this, TransactionHistoryActivity.class);
//                    startActivity(intent);

                break;
            case "Parking History":
                intent = new Intent(this, ParkingHistoryActivity.class);
                startActivity(intent);
                break;
            case "Booked list":
                intent = new Intent(this, BookingHistoryActivity.class);
                startActivity(intent);
                break;
            case "Logout":
                Session.setSession(null);
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private String formatInteger(String str) {
        BigDecimal parsed = new BigDecimal(str);
        DecimalFormat formatter =
                new DecimalFormat( "#,###", new DecimalFormatSymbols(Locale.US));
        return formatter.format(parsed);
    }
//    @Override
//    public void callback(ResponseEntity<?> response) {
//            if (response.getStatusCode() == HttpStatus.OK){
//                parkingLocationList = (List<ParkingRes>) response.getBody();
//            }
//    }
}