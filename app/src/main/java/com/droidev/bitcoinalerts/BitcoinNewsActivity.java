package com.droidev.bitcoinalerts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class BitcoinNewsActivity extends AppCompatActivity implements BitcoinNewsFetcher.NewsFetchListener {

    private RecyclerView recyclerView;
    Menu menuItem, menu;
    private ProgressBar progressBar;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitcoin_news);

        setTitle("Bitcoin News");

        tinyDB = new TinyDB(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBarNews);

        progressBar.setVisibility(View.VISIBLE);

        BitcoinNewsFetcher.fetchBitcoinNews(BitcoinNewsActivity.this, BitcoinNewsActivity.this);
    }

    @Override
    public void onNewsFetched(List<BitcoinNewsFetcher.Article> articles) {

        ArticleAdapter adapter = new ArticleAdapter(articles, this);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);

        enableMenu();
    }

    @Override
    public void onFetchError(String error) {

        progressBar.setVisibility(View.GONE);

        enableMenu();

        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.reload) {

            progressBar.setVisibility(View.VISIBLE);

            disableMenu();

            BitcoinNewsFetcher.fetchBitcoinNews(BitcoinNewsActivity.this, BitcoinNewsActivity.this);

        }

        if (item.getItemId() == R.id.apiKEY) {

            final EditText editText = new EditText(this);
            editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            editText.setHint("API Key");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save your API Key");
            builder.setCancelable(false);

            builder.setView(editText);

            builder.setPositiveButton("OK", (dialog, which) -> {
                String text = editText.getText().toString().replace(" ", "").trim();

                tinyDB.remove("apikey");
                tinyDB.putString("apikey", text);
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (item.getItemId() == R.id.getAPIKey) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://newsapi.org/account"));
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bitcoin_news_menu, menu);

        this.menu = menu;
        menuItem = menu;

        return super.onCreateOptionsMenu(menu);
    }

    private void disableMenu() {

        if (menu != null) {
            MenuItem reload = menu.findItem(R.id.reload);

            if (reload != null) {
                reload.setEnabled(false);
            }
        }
    }

    private void enableMenu() {

        if (menu != null) {
            MenuItem reload = menu.findItem(R.id.reload);

            if (reload != null) {
                reload.setEnabled(true);
            }
        }

    }
}
