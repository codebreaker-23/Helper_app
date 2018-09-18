package com.example.ayush.helper_app;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap map;

    private BroadcastReceiver receiver;

    TextView speedTextView;


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


        Button btnSendSMS = findViewById(R.id.location);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.sendSMSWithCheck(MainActivity.this, "9717500910", "1"); //sending msg
            }
        });

        speedTextView = findViewById(R.id.speed);
        speedTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.sendSMSWithCheck(MainActivity.this, "9717500910", "2"); //sending msg
            }
        });

        Button increase = findViewById(R.id.up);
        increase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.sendSMSWithCheck(MainActivity.this, "8618795068", "3"); //sending msg
            }
        });

        Button decrease = findViewById(R.id.down);
        decrease.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.sendSMSWithCheck(MainActivity.this, "9717500910", "4"); //sending msg
            }
        });

        Button call = findViewById(R.id.callback);
        call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.sendSMSWithCheck(MainActivity.this, "8618795068", "5"); //sending msg
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setMap();

       /* String temp = "-34.8799074,174.7565664";
        String[] loc = temp.split(",");
        double lat = Double.parseDouble(loc[0]);
        double lng = Double.parseDouble(loc[1]);
        LatLng latLng = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions().position(latLng).title("Default"));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
            */
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSmsListener(this);
    }

    public void setSmsListener(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

        receiver = getSms(filter);

        context.registerReceiver(receiver, filter);
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

    @NeedsPermission({Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS})
    public static void sendSMS(String phoneNumber, String message) {

        android.telephony.SmsManager sms = android.telephony.SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);

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
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    public BroadcastReceiver getSms(IntentFilter filter) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Objects.requireNonNull(intent.getAction()).equals("android.provider.Telephony.SMS_RECEIVED")) {
                    Bundle bundle = intent.getExtras(); //---get the SMS message passed in---
                    SmsMessage[] msgs = null;
                    if (bundle != null) {
                        //---retrieve the SMS message received---
                        try {
                            Object[] pdus = (Object[]) bundle.get("pdus");
                            msgs = pdus != null ? new SmsMessage[pdus.length] : new SmsMessage[0];
                            for (int i = 0; i < msgs.length; i++) {
                                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                                String msgBody = msgs[i].getMessageBody();

                                if (!msgBody.isEmpty() && msgBody.length() < 5) {
                                    Log.e("TAG", "okay");
                                    speedTextView.setText(msgBody);
                                } else if (!msgBody.isEmpty()) {
                                    String[] loc = msgBody.split(",");
                                    double lat = Double.parseDouble(loc[0]);
                                    double lng = Double.parseDouble(loc[1]);
                                    LatLng latLng = new LatLng(lat, lng);
                                    map.addMarker(new MarkerOptions().position(latLng).title("System Location"));
                                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    map.animateCamera(CameraUpdateFactory.zoomTo(15));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        };
    }

}