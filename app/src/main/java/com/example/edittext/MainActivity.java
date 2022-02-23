package com.example.edittext;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;
    private TextView txtNotification;
    private static final String ACTION_SEND_MESSAGE = "action send message";
    private static final int NOTIFICATION_ID = 1;
    public static final String ACTION_RECEIVE_MESSAGE = "action_receive_message";

    Button btnSendNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = NotificationManagerCompat.from(this);
        btnSendNotification = findViewById(R.id.send_message);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextMessage = findViewById(R.id.edit_text_message);

        txtNotification = (TextView) findViewById(R.id.txtNotification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Message", "Message", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(ACTION_RECEIVE_MESSAGE));

    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            txtNotification.setText(message);

        }
    };


    //Ham tao mot Notification
    private void sendNotification() {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        //
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Message");
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        builder.setSmallIcon(R.drawable.ic_message);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.messenger));
        builder.setColor(getResources().getColor(android.R.color.holo_red_dark));
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        builder.addAction(R.mipmap.ic_launcher, "Toast", actionIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        if (notificationManager != null) {
            notificationManager.notify(getNotificationId(), builder.build());
        }
    }


    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }


    //Ham tao nhieu Notification voi nhieu click lien tiep
    private int getNotificationId() {
        return (int) new Date().getTime();
    }
}