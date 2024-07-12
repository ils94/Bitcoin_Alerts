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

public class FeeHistoryActivity extends AppCompatActivity {

    RecyclerView feeHistoryRecyclerView;
    TinyDB tinyDB;
    Menu menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_history);

        setTitle("Tx Fees History");

        tinyDB = new TinyDB(this);

        feeHistoryRecyclerView = findViewById(R.id.feeHistoryRecyclerView);

        ArrayList<String> arrayList = tinyDB.getListString("feeHistory");
        ArrayList<String> invertedList = new ArrayList<>();

        for (int i = arrayList.size() - 1; i >= 0; i--) {
            invertedList.add(arrayList.get(i));
        }

        FeeHistoryAdapter adapter = new FeeHistoryAdapter(invertedList);
        feeHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        feeHistoryRecyclerView.setAdapter(adapter);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.clearFeeHistory) {
            tinyDB.remove("feeHistory");
            ((FeeHistoryAdapter) feeHistoryRecyclerView.getAdapter()).clearData();
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
