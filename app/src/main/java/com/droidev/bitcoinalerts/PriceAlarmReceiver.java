package com.droidev.bitcoinalerts;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

                String brlFormat = currencyFormatBRL.format(brlValue);
                String usdFormat = currencyFormatUSD.format(usdValue);

                String brl = "Preço do BTC em BRL: " + brlFormat;
                String usd = "Preço do BTC em USD: " + usdFormat;

                tinyDB.putString("btcbrl", brl);
                tinyDB.putString("btcusd", usd);

                Date currentDate = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss", Locale.getDefault());

                String formattedDate = dateFormat.format(currentDate);

                String date = "Preço atualizada em: " + formattedDate;

                tinyDB.putString("datetimeprice", date);

                saveHistory(context, brl, usd, date);

                PriceNotification.showNotification(context, brlFormat, usdFormat);
            }
        });

        fetcher.execute();
    }

    private void saveHistory(Context context, String btcBRL, String btcUSD, String dateTimePrice) {

        TinyDB tinyDB = new TinyDB(context);

        ArrayList<String> arrayListPrice;

        arrayListPrice = tinyDB.getListString("priceHistory");

        String price = btcBRL + "\n" + btcUSD + "\n" + dateTimePrice;

        arrayListPrice.add(price);

        tinyDB.putListString("priceHistory", arrayListPrice);
    }
}
