package com.droidev.bitcoinalerts;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class BitcoinNewsActivity extends AppCompatActivity implements BitcoinNewsFetcher.NewsFetchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitcoin_news);

        BitcoinNewsFetcher.fetchBitcoinNews(this);
    }

    @Override
    public void onNewsFetched(List<BitcoinNewsFetcher.Article> articles) {
        // Handle the fetched news articles here
        for (BitcoinNewsFetcher.Article article : articles) {
            System.out.println(article.title
            + "\n" + article.url);
        }
    }

    @Override
    public void onFetchError(String error) {
        // Handle the error here
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
