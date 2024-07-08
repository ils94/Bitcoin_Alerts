package com.droidev.bitcoinalerts;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FeeHistoryActivity extends AppCompatActivity {

    TextView feeHistory;
    TinyDB tinyDB;

    Menu menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_history);

        setTitle("Tx History");

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

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.clearFeeHistory) {

            tinyDB.remove("feeHistory");

            feeHistory.setText("");

            Toast.makeText(this, "History deleted.", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fee_history_menu, menu);

        menuItem = menu;

        return super.onCreateOptionsMenu(menu);
    }
}