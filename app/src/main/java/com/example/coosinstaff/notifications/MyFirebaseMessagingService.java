package com.example.coosinstaff.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.coosinstaff.ChatActivity;
import com.example.coosinstaff.HomeActivity;
import com.example.coosinstaff.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID ="admin_channel";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        final Intent intent;
        //lay so dien thoai
        SharedPreferences SP = getApplicationContext().getSharedPreferences("PHONE",0);
        String currentUser = SP.getString("phone_num",null);
        if (remoteMessage.getData().get("title").trim().substring(0,14).equals("Tin nhắn từ KH")){
            intent = new Intent(this, ChatActivity.class);
            intent.putExtra("receiver",remoteMessage.getData().get("title").trim().substring(remoteMessage.getData().get("title").length()-28
                    ,remoteMessage.getData().get("title").length()-18));
        }else intent = new Intent(this, HomeActivity.class);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

      /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
      */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher_background);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(largeIcon)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);

        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if (remoteMessage.getData().get("title").trim().equals("CoOsin khách hàng")){
            notificationManager.notify(notificationID, notificationBuilder.build());
        }
        if (remoteMessage.getData().get("title").trim().equals("CoOsin báo nghỉ đến ["+currentUser+"]")){
            notificationManager.notify(notificationID, notificationBuilder.build());
        }
        if (remoteMessage.getData().get("title").trim().equals("CoOsin xác nhận đến ["+currentUser+"]")){
            notificationManager.notify(notificationID, notificationBuilder.build());
        }
        if (remoteMessage.getData().get("title").trim().equals("CoOsin xác nhận hoàn thành ["+currentUser+"]")){
            notificationManager.notify(notificationID, notificationBuilder.build());
        }
        if (remoteMessage.getData().get("title").trim().substring(remoteMessage.getData().get("title").length()-11).equals(currentUser+"]")){
            notificationManager.notify(notificationID, notificationBuilder.build());
        }
        Log.d("BBB",remoteMessage.getData().get("title").trim().substring(remoteMessage.getData().get("title").length()-11));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}