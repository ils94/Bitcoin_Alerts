package com.droidev.bitcoinalerts;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PriceAlarmReceiver extends BroadcastReceiver {
    @SuppressLint("StaticFieldLeak")
    @Override
    public void onReceive(Context context, Intent intent) {

        BitcoinPriceFetcher fetcher = new BitcoinPriceFetcher(bitcoinPrice -> {

            if (bitcoinPrice != null) {

                TinyDB tinyDB = new TinyDB(context);

                double usdValue = bitcoinPrice.getUsdValue();
                double brlValue = bitcoinPrice.getBrlValue();

                NumberFormat currencyFormatBRL = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                NumberFormat currencyFormatUSD = NumberFormat.getCurrencyInstance(new Locale("en", "US"));

                tinyDB.putString("btcbrl", "Preço do BTC em BRL: " + currencyFormatBRL.format(brlValue));
                tinyDB.putString("btcusd", "Preço do BTC em USD: " + currencyFormatUSD.format(usdValue));

                Date currentDate = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss", Locale.getDefault());

                String formattedDate = dateFormat.format(currentDate);

                tinyDB.putString("datetimeprice", "Taxa atualizada em: " + formattedDate);

                PriceNotification.showNotification(context, currencyFormatBRL.format(brlValue), currencyFormatUSD.format(usdValue));
            }
        });

        fetcher.execute();
    }
}
