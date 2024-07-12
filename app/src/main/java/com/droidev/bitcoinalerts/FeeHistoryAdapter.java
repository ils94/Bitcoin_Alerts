package com.droidev.bitcoinalerts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FeeHistoryAdapter extends RecyclerView.Adapter<FeeHistoryAdapter.ViewHolder> {

    private final List<String> feeHistoryList;

    public FeeHistoryAdapter(List<String> feeHistoryList) {
        this.feeHistoryList = feeHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fee_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String history = feeHistoryList.get(position);
        holder.feeHistoryItemText.setText(history);
    }

    @Override
    public int getItemCount() {
        return feeHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView feeHistoryItemText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            feeHistoryItemText = itemView.findViewById(R.id.feeHistoryItemText);
        }
    }

    // Add this method to clear the data
    public void clearData() {
        feeHistoryList.clear();
        notifyDataSetChanged();
    }
}

