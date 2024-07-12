package com.droidev.bitcoinalerts;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class FeesNotification {

    public static void showNotification(Context context, int fastestFee, int halfHourFee, int hourFee) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                "bitcoin_fee_channel",
                "Bitcoin Fee Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        notificationManager.createNotificationChannel(channel);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "bitcoin_fee_channel")
                .setContentTitle("Bitcoin Alerts - Tx fees")
                .setContentText("High priority: " + fastestFee + " sat/vB." +
                        "\nMedium priority: " + halfHourFee + " sat/vB." +
                        "\nLow priority: " + hourFee + " sat/vB.")
                .setSmallIcon(R.drawable.bitcoin)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText("High priority: " + fastestFee + " sat/vB." +
                        "\nMedium priority: " + halfHourFee + " sat/vB." +
                        "\nLow priority: " + hourFee + " sat/vB.");

        builder.setStyle(bigTextStyle);

        notificationManager.notify(1, builder.build());
    }
}
