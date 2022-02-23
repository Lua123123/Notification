package com.example.edittext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Locale;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("toastMessage");
        Intent intentMessage = new Intent(MainActivity.ACTION_RECEIVE_MESSAGE);
        // You can also include some extra data.
        intentMessage.putExtra("message", message);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentMessage);
    }
}
