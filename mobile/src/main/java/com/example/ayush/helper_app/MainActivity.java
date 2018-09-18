package com.example.ayush.helper_app;


import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

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
                MainActivityPermissionsDispatcher.sendSMSWithCheck(MainActivity.this, "+15555215556", "1"); //sending msg
            }
        });

        Button increase = (Button) findViewById(R.id.up);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.sendSMSWithCheck(MainActivity.this, "5556", "2"); //sending msg
            }
        });

        Button decrease = (Button) findViewById(R.id.down);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.sendSMSWithCheck(MainActivity.this, "5556", "3"); //sending msg
            }
        });

        Button call = (Button) findViewById(R.id.callback);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.sendSMSWithCheck(MainActivity.this, "5556", "5"); //sending msg
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

    private void initMap() {
        MapFragment mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.maps, mapFragment, "MAP").commitAllowingStateLoss();
        mapFragment.getMapAsync(this);
    }

    private void setMap() {
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

    @NeedsPermission(Manifest.permission.SEND_SMS)
    public static void sendSMS(String phoneNumber, String message) {

        /*PendingIntent sentToast = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent success", Toast.LENGTH_SHORT).show();
                        break;
                    case android.telephony.SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                        break;
                    case android.telephony.SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    case android.telephony.SmsManager.RESULT_ERROR_NULL_PDU:
                    case android.telephony.SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "sent failure", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT")); */

        android.telephony.SmsManager sms = android.telephony.SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }


}
