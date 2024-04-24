package com.droidev.bitcoinalerts;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeesAlarmReceiver extends BroadcastReceiver {
    @SuppressLint("StaticFieldLeak")
    @Override
    public void onReceive(Context context, Intent intent) {

        new BitcoinFeeFetcher() {
            @Override
            protected void onPostExecute(int[] fees) {
                if (fees != null) {

                    TinyDB tinyDB = new TinyDB(context);

                    tinyDB.putString("fastestfee", "Taxa de Prioridade Alta: " + fees[0] + " sat/vB");
                    tinyDB.putString("halfhourfee", "Taxa de Prioridade Alta: " + fees[1] + " sat/vB");
                    tinyDB.putString("hourfee", "Taxa de Prioridade Alta: " + fees[2] + " sat/vB");

                    Date currentDate = new Date();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss", Locale.getDefault());

                    String formattedDate = dateFormat.format(currentDate);

                    tinyDB.putString("datetimefee", "Taxa atualizada em: " + formattedDate);

                    int altaPrioridade = tinyDB.getInt("altaprioridade");

                    if (fees[0] <= altaPrioridade) {

                        FeesNotification.showNotification(context, fees[0], fees[1], fees[2]);
                    }
                }
            }
        }.execute();
    }
}
