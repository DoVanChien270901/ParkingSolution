package fpt.aptech.parkinggo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.fragment.HomeF;
import fpt.aptech.parkinggo.fragment.ProfileF;
import fpt.aptech.parkinggo.statics.Session;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Variables
    String currentTitleF = "Home";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private TextView tvName;
    private TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /*--------------------------------Load Fragment--------------------------*/
        replaceFragment(new HomeF());

        /*---------------------------------Hooks-----------------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.user_nav_view);
        toolbar = findViewById(R.id.toolbar);
        /*--------------------------------Load TextView Header --------------------------*/
        View header = navigationView.getHeaderView(0);
        tvName = header.findViewById(R.id.hd_tv_name);
        tvEmail = header.findViewById(R.id.hd_tv_email);
        LoginRes loginRes = (LoginRes) Session.getSession();
        tvName.setText(loginRes.getFullname());
        tvEmail.setText(loginRes.getEmail());
        /*--------------------------------Tool Bar--------------------------*/
        //setSupportActionBar(toolbar);
        /*--------------------------------Navigation Drawer Menu--------------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //no do thing
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String id  = item.toString();
        switch (id) {
            case "Home":
                if (!currentTitleF.equals("Home")) {
                    currentTitleF = "Home";
                    replaceFragment(new HomeF());

                }break;

            case "Profile":
                if (!currentTitleF.equals("Profile")) {
                    currentTitleF = "Profile";
                    replaceFragment(new ProfileF());

                }break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
}