package com.droidev.bitcoinalerts;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

public class PriceNotification {

    public static void showNotification(Context context, String brl, String usd) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                "bitcoin_price_channel",
                "Bitcoin Price Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "bitcoin_price_channel")
                .setContentTitle("Bitcoin Alerts - Price")
                .setContentText("BRL Price: " + brl + "\nUSD Price: " + usd)
                .setSmallIcon(R.drawable.bitcoin)
                .setAutoCancel(true);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

        builder.setStyle(bigTextStyle);

        notificationManager.notify(2, builder.build());
    }
}
