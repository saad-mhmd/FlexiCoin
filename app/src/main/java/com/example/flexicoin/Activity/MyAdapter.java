package com.example.flexicoin.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flexicoin.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<AddCash> data;

    public MyAdapter(List<AddCash> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AddCash item = data.get(position);
        holder.cardName.setText("Card Name: " + item.getCardName());
        holder.cardNo.setText("Card No: " + item.getCardNo());
        holder.cvv.setText("CVV: " + item.getCvv());
        holder.cashAmount.setText("Cash Amount: " + item.getCashAmount());
        holder.date.setText("Date: " + item.getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cardName, cardNo, cvv, cashAmount, date;

        MyViewHolder(View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.textCardName);
            cardNo = itemView.findViewById(R.id.textCardNo);
            cvv = itemView.findViewById(R.id.textCvv);
            cashAmount = itemView.findViewById(R.id.textCashAmount);
            date = itemView.findViewById(R.id.textDate);
        }
    }
}
