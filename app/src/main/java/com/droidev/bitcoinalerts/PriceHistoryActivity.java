package com.droidev.bitcoinalerts;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PriceHistoryActivity extends AppCompatActivity {

    RecyclerView priceHistoryRecyclerView;
    TinyDB tinyDB;
    Menu menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_history);

        setTitle("Price History");

        tinyDB = new TinyDB(this);

        priceHistoryRecyclerView = findViewById(R.id.priceHistoryRecyclerView);

        ArrayList<String> arrayList = tinyDB.getListString("priceHistory");
        ArrayList<String> invertedList = new ArrayList<>();

        for (int i = arrayList.size() - 1; i >= 0; i--) {
            invertedList.add(arrayList.get(i));
        }

        PriceHistoryAdapter adapter = new PriceHistoryAdapter(invertedList);
        priceHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        priceHistoryRecyclerView.setAdapter(adapter);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.clearPriceHistory) {
            tinyDB.remove("priceHistory");
            ((PriceHistoryAdapter) priceHistoryRecyclerView.getAdapter()).clearData();
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
