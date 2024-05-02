package com.droidev.bitcoinalerts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class FeeHistoryActivity extends AppCompatActivity {

    TextView feeHistory;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_history);

        setTitle("Histórico de Taxas");

        tinyDB = new TinyDB(this);

        feeHistory = findViewById(R.id.feeHistoryTextView);

        ArrayList<String> arrayList;

        arrayList = tinyDB.getListString("feeHistory");
        ArrayList<String> invertedList = new ArrayList<>();

        for (int i = arrayList.size() - 1; i >= 0; i--) {
            invertedList.add(arrayList.get(i));
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (String element : invertedList) {
            stringBuilder.append(element).append("\n\n");
        }

        feeHistory.setText(stringBuilder.toString());
    }
}