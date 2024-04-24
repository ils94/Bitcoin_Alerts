package com.droidev.bitcoinalerts;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitcoinPriceFetcher extends AsyncTask<Void, Void, BitcoinPrice> {

    private static final String API_URL = "https://api.coingecko.com/api/v3/simple/price";
    private static final String CRYPTO_ID = "bitcoin";
    private static final String VS_CURRENCIES = "usd,brl";

    private final BitcoinPriceListener listener;

    public BitcoinPriceFetcher(BitcoinPriceListener listener) {
        this.listener = listener;
    }

    @Override
    protected BitcoinPrice doInBackground(Void... voids) {
        try {
            URL url = new URL(API_URL + "?ids=" + CRYPTO_ID + "&vs_currencies=" + VS_CURRENCIES);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            bufferedReader.close();
            inputStream.close();
            connection.disconnect();

            JSONObject jsonResponse = new JSONObject(response.toString());
            double usdValue = jsonResponse.getJSONObject(CRYPTO_ID).getDouble("usd");
            double brlValue = jsonResponse.getJSONObject(CRYPTO_ID).getDouble("brl");

            return new BitcoinPrice(usdValue, brlValue);
        } catch (IOException | JSONException e) {
            Log.e("BitcoinPriceFetcher", "Error fetching Bitcoin price", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(BitcoinPrice bitcoinPrice) {
        super.onPostExecute(bitcoinPrice);
        if (bitcoinPrice != null) {
            listener.onBitcoinPriceFetched(bitcoinPrice);
        }
    }

    public interface BitcoinPriceListener {
        void onBitcoinPriceFetched(BitcoinPrice bitcoinPrice);
    }
}
