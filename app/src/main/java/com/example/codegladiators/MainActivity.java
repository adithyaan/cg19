package com.example.codegladiators;

import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FragmentBookedHistory.OnFragmentInteractionListener,HomeFragment.OnFragmentInteractionListener,ShippingFragment.OnFragmentInteractionListener,ProfileFragment.OnFragmentInteractionListener{

    DrawerLayout drawerLayout;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");
        final Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorWhite));
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        loadFragment(new HomeFragment());
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        toolbar.setTitle("Home");
                        drawerLayout.closeDrawers();
                        loadFragment(new HomeFragment());
                        return true;
                    case R.id.booked_history:
                        toolbar.setTitle("Shipping");
                        drawerLayout.closeDrawers();
                        loadFragment(new FragmentBookedHistory());
                        return true;
                    case R.id.profile:
                        toolbar.setTitle("Profile");
                        drawerLayout.closeDrawers();
                        loadFragment(new ProfileFragment());
                        return true;
                }

                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }


    public  void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit(); // save the changes
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {

        android.os.Process.killProcess(android.os.Process.myPid());
        if (back_pressed + 2000 > System.currentTimeMillis()){
            System.exit(1);
            super.onBackPressed();
        }
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

//    boolean doubleBackToExitPressedOnce = false;


//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            System.exit(0);
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
//    }
}
