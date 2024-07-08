package com.droidev.bitcoinalerts;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BitcoinNewsFetcher {

    private static final String API_KEY = "fa7b8fc02c3948b0905fe0d6ae46dad3";
    private static final String NEWS_API_URL = "https://newsapi.org/v2/everything?q=Bitcoin&apiKey=" + API_KEY;

    public interface NewsFetchListener {
        void onNewsFetched(List<Article> articles);
        void onFetchError(String error);
    }

    public static void fetchBitcoinNews(NewsFetchListener listener) {
        new FetchNewsTask(listener).execute();
    }

    private static class FetchNewsTask extends AsyncTask<Void, Void, List<Article>> {
        private NewsFetchListener listener;

        public FetchNewsTask(NewsFetchListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Article> doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(NEWS_API_URL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    Type articleListType = new TypeToken<List<Article>>(){}.getType();
                    NewsResponse newsResponse = new Gson().fromJson(json, NewsResponse.class);
                    return newsResponse.articles;
                } else {
                    Log.e("BitcoinNewsFetcher", "Failed to fetch news: " + response.message());
                }
            } catch (IOException e) {
                Log.e("BitcoinNewsFetcher", "Error fetching news", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            if (articles != null) {
                listener.onNewsFetched(articles);
            } else {
                listener.onFetchError("Failed to fetch news");
            }
        }
    }

    public static class NewsResponse {
        public List<Article> articles;
    }

    public static class Article {
        public String title;
        public String description;
        public String url;
        public String urlToImage;
        public String publishedAt;
    }
}


