package com.droidev.bitcoinalerts;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitcoinFeeFetcher extends AsyncTask<Void, Void, int[]> {
    private static final String API_URL = "https://mempool.space/api/v1/fees/recommended";

    @Override
    protected int[] doInBackground(Void... voids) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());

            return new int[]{
                    jsonObject.getInt("fastestFee"),
                    jsonObject.getInt("halfHourFee"),
                    jsonObject.getInt("hourFee")
            };
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
