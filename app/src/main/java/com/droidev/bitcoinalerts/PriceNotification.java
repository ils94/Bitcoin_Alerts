package com.droidev.bitcoinalerts;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "bitcoin_price_channel")
                .setContentTitle("Bitcoin Alerts - Price")
                .setContentText("BRL Price: " + brl + "\nUSD Price: " + usd)
                .setSmallIcon(R.drawable.bitcoin)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText("BRL Price: " + brl + "\nUSD Price: " + usd);

        builder.setStyle(bigTextStyle);

        notificationManager.notify(2, builder.build());
    }
}
