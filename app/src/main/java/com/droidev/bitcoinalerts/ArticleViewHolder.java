package com.droidev.bitcoinalerts;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView textViewTitle;
    public TextView textViewDescription;
    public TextView textViewPublishedAt;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewDescription = itemView.findViewById(R.id.textViewDescription);
        textViewPublishedAt = itemView.findViewById(R.id.textViewPublishedAt);
    }
}

