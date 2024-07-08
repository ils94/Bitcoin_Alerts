package com.droidev.bitcoinalerts;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BitcoinNewsActivity extends AppCompatActivity implements BitcoinNewsFetcher.NewsFetchListener {

    private RecyclerView recyclerView;
    private ArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitcoin_news);

        setTitle("Bitcoin News");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BitcoinNewsFetcher.fetchBitcoinNews(this);
    }

    @Override
    public void onNewsFetched(List<BitcoinNewsFetcher.Article> articles) {
        // Handle the fetched news articles here
        adapter = new ArticleAdapter(articles, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFetchError(String error) {
        // Handle the error here
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
