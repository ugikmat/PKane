package com.example.mat.pkane.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.mat.pkane.Common.Common;
import com.example.mat.pkane.Interface.ItemClickListener;
import com.example.mat.pkane.R;

/**
 * Created by Mat on 11/22/17.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

    public TextView textDate,textTime, textPrice, textGender;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        textDate = itemView.findViewById(R.id.order_date);
        textTime = itemView.findViewById(R.id.order_time);
        textPrice = itemView.findViewById(R.id.order_price);
        textGender = itemView.findViewById(R.id.order_gender);

        if (!Common.currentUser.isAdmin()) {
            itemView.setOnCreateContextMenuListener(this);
        }

        itemView.setOnClickListener(this);
        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLong) {

            }
        };

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select an action");
//        menu.add(0,0,getAdapterPosition(),"Update");
        menu.add(0,1,getAdapterPosition(),"Delete");
    }
}
