package com.example.ayush.helper_app;


import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

    public GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        initMap();

        /*
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);           */

        Button btnSendSMS = (Button) findViewById(R.id.location);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SmsManager.sendSMS("+15555215556", "1"); //sending msg
            }
        });

        Button increase = (Button) findViewById(R.id.up);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SmsManager.sendSMS("5556", "2"); //sending msg
            }
        });

        Button decrease = (Button) findViewById(R.id.down);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SmsManager.sendSMS("5556", "3"); //sending msg
            }
        });

        Button call = (Button) findViewById(R.id.callback);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SmsManager.sendSMS("5556", "5"); //sending msg
            }
        });
    }

    //test case "-34.8799074,174.7565664"
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setMap();

        String temp = "-34.8799074,174.7565664";
        String[] loc = temp.split(",");

        //String[] loc = (SmsManager.str).split(",");
        double lat = Double.parseDouble(loc[0]);
        double lng = Double.parseDouble(loc[1]);
        LatLng latLng = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions().position(latLng).title("System Location"));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void initMap(){
        MapFragment mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.maps, mapFragment, "MAP").commitAllowingStateLoss();
        mapFragment.getMapAsync(this);
    }

    private void setMap(){
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException se) {

        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

}
