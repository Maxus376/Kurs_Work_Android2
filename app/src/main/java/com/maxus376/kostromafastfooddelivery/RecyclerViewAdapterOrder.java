package com.maxus376.kostromafastfooddelivery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class RecyclerViewAdapterOrder extends RecyclerView.Adapter<RecyclerViewAdapterOrder.ViewHolder> {
    ArrayList<ListDataOrder> list;

    public RecyclerViewAdapterOrder(ArrayList<ListDataOrder> list, Context context) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_order, viewGroup, false);
        v.getBackground().setAlpha(0);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder orderViewHolder, int i) {
        //orderViewHolder.id.setText("Заказ №"+list.get(i).getId());
        orderViewHolder.time.setText(list.get(i).getTime() + " мин.");
        //orderViewHolder.price.setText(list.get(i).getPrice() + " руб.");
        orderViewHolder.where.setText(list.get(i).getWhere());
        orderViewHolder.success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)view.getContext()).onClickStatus();
            }
        });

        orderViewHolder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)view.getContext()).onClickLocation();
            }
        });
    }





    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView time, price, where, id;
        public Button success, location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //id = itemView.findViewById(R.id.numOrder);
            time = itemView.findViewById(R.id.time);
            //price = itemView.findViewById(R.id.costOrder);
            where = itemView.findViewById(R.id.adress);
            success = itemView.findViewById(R.id.orderComplete);
            location = itemView.findViewById(R.id.showOnMap);
        }
    }
}