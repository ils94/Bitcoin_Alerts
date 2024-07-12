package com.droidev.bitcoinalerts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PriceHistoryAdapter extends RecyclerView.Adapter<PriceHistoryAdapter.ViewHolder> {

    private final List<String> priceHistoryList;

    public PriceHistoryAdapter(List<String> priceHistoryList) {
        this.priceHistoryList = priceHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String history = priceHistoryList.get(position);
        holder.priceHistoryItemText.setText(history);
    }

    @Override
    public int getItemCount() {
        return priceHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView priceHistoryItemText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            priceHistoryItemText = itemView.findViewById(R.id.priceHistoryItemText);
        }
    }

    // Add this method to clear the data
    public void clearData() {
        priceHistoryList.clear();
        notifyDataSetChanged();
    }
}
