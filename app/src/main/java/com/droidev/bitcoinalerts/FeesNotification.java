package com.droidev.bitcoinalerts;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "bitcoin_fee_channel")
                .setContentTitle("Bitcoin Alerts - Taxas")
                .setContentText("Prioridade Alta: " + fastestFee + " sat/vB." +
                        "\nPrioridade Média: " + halfHourFee + " sat/vB." +
                        "\nPrioridade Baixa: " + hourFee + " sat/vB.")
                .setSmallIcon(R.drawable.bitcoin)
                .setAutoCancel(true);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

        builder.setStyle(bigTextStyle);

        notificationManager.notify(1, builder.build());
    }
}
