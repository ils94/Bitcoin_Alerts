package com.droidev.bitcoinalerts;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PriceHistoryActivity extends AppCompatActivity {


    TextView priceHistory;
    TinyDB tinyDB;

    Menu menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_history);

        setTitle("Price History");

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

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.clearPriceHistory) {

            tinyDB.remove("priceHistory");

            priceHistory.setText("");

            Toast.makeText(this, "History deleted.", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.price_history_menu, menu);

        menuItem = menu;

        return super.onCreateOptionsMenu(menu);
    }
}