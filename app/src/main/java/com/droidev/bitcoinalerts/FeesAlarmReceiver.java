package com.droidev.bitcoinalerts;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

                    String fastestfee = "High priority: " + fees[0] + " sat/vB";
                    String halfhourfee = "Medium priority: " + fees[1] + " sat/vB";
                    String hourfee = "Low priority: " + fees[2] + " sat/vB";

                    tinyDB.putString("fastestfee", fastestfee);
                    tinyDB.putString("halfhourfee", halfhourfee);
                    tinyDB.putString("hourfee", hourfee);

                    Date currentDate = new Date();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss", Locale.getDefault());

                    String formattedDate = dateFormat.format(currentDate);

                    String date = "Tx updated at: " + formattedDate;

                    tinyDB.putString("datetimefee", date);

                    saveHistory(context, fastestfee, halfhourfee, hourfee, date);

                    int highPriorty = tinyDB.getInt("highPriority");

                    if (fees[0] <= highPriorty) {

                        FeesNotification.showNotification(context, fees[0], fees[1], fees[2]);
                    }
                }
            }
        }.execute();
    }

    private void saveHistory(Context context, String fastestFee, String halfHourFee, String hourFee, String dateTimeFee) {

        TinyDB tinyDB = new TinyDB(context);

        ArrayList<String> arrayListFee;

        arrayListFee = tinyDB.getListString("feeHistory");

        String fee = fastestFee + "\n" + halfHourFee + "\n" + hourFee + "\n" + dateTimeFee;

        arrayListFee.add(fee);

        tinyDB.putListString("feeHistory", arrayListFee);
    }
}
