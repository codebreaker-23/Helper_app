package com.example.ayush.helper_app;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


public class SmsManager extends BroadcastReceiver{
    private String TAG = "xxxxxxxxxxxxxxxx";

    public  static String str = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs = null;

        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received
            for (int i=0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";

                //message content
                str += msgs[i].getMessageBody().toString();
                str += "\n";
            }
            Log.d(TAG, str); //location string

            //if(str.length() > 0)

        }
    }


}

