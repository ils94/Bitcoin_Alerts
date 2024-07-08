package com.droidev.bitcoinalerts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {

    private List<BitcoinNewsFetcher.Article> articles;
    private Context context;

    public ArticleAdapter(List<BitcoinNewsFetcher.Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        BitcoinNewsFetcher.Article article = articles.get(position);

        holder.textViewTitle.setText(article.title);
        holder.textViewDescription.setText(article.description);
        holder.textViewPublishedAt.setText(article.publishedAt);
        Glide.with(context).load(article.urlToImage).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.url));
            context.startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}


