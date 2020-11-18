package com.example.budegtapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class BroadcastService extends Service {
    private String TAG = "BroadcastService";
    public static final String COUNTDOWN_BR = "com.example.backgoundtimercount";
    Intent intent = new Intent(COUNTDOWN_BR);
    CountDownTimer countDownTimer = null;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Starting timer...");
        sharedPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
        sharedPreferences.edit().remove("time").apply();
        long millis = sharedPreferences.getLong("time",1800000);

        countDownTimer = new CountDownTimer(millis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                intent.putExtra("countdown",millisUntilFinished);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                //push Notification when task is ended
                Uri sound = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/quite_impressed.mp3" ) ;
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(BroadcastService. this, default_notification_channel_id )
                        .setSmallIcon(R.drawable. ic_launcher_foreground )
                        .setContentTitle( "task" )
                        .setSound(sound)
                        .setContentText( "your task is finshed" ) ;
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE );
                if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
                    AudioAttributes audioAttributes = new AudioAttributes.Builder()
                            .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                            .setUsage(AudioAttributes. USAGE_ALARM )
                            .build();
                    int importance = NotificationManager. IMPORTANCE_HIGH ;
                    NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
                    notificationChannel.enableLights( true ) ;
                    notificationChannel.setLightColor(Color. RED ) ;
                    notificationChannel.enableVibration( true ) ;
                    notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
                    notificationChannel.setSound(sound , audioAttributes) ;
                    mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
                    assert mNotificationManager != null;
                    mNotificationManager.createNotificationChannel(notificationChannel) ;
                }
                assert mNotificationManager != null;
                mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                        mBuilder.build()) ;
              Intent i=  new Intent(BroadcastService.this,HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);

            }
        };
        countDownTimer.start();
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
