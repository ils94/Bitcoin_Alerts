package com.droidev.bitcoinalerts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class PriceHistoryActivity extends AppCompatActivity {


    TextView priceHistory;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_history);

        setTitle("Histórico de Preços");

        tinyDB = new TinyDB(this);

        priceHistory = findViewById(R.id.priceHistoryTextView);

        ArrayList<String> arrayList;

        arrayList = tinyDB.getListString("priceHistory");
        ArrayList<String> invertedList = new ArrayList<>();

        for (int i = arrayList.size() - 1; i >= 0; i--) {
            invertedList.add(arrayList.get(i));
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (String element : invertedList) {
            stringBuilder.append(element).append("\n\n");
        }

        priceHistory.setText(stringBuilder.toString());
    }
}