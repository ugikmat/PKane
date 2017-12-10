package com.example.mat.pkane.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.mat.pkane.Interface.ItemClickListener;
import com.example.mat.pkane.R;

/**
 * Created by Mat on 11/26/17.
 */

public class TherapisViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

    public TextView textName,textPhone,textGender,textEmail,textAddress;

    private ItemClickListener itemClickListener;

    public TherapisViewHolder(View itemView) {
        super(itemView);


        textName = itemView.findViewById(R.id.text_name);

        textPhone = itemView.findViewById(R.id.text_phone);

        textGender = itemView.findViewById(R.id.text_gender);

        textEmail = itemView.findViewById(R.id.text_email);

        textAddress = itemView.findViewById(R.id.text_address);

        itemView.setOnCreateContextMenuListener(this);
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
        menu.add(0,0,getAdapterPosition(),"Update");
        menu.add(0,1,getAdapterPosition(),"Delete");
    }
}
