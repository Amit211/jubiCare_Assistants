package com.indev.jubicare_assistants.videocallingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if( status==null || status.isEmpty()) {
            status="No Internet Connection";
            Toast.makeText(context, status, Toast.LENGTH_LONG).show();
        }

    }
}
