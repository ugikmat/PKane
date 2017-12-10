package com.example.mat.pkane.ViewHolder;

import android.animation.ValueAnimator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mat.pkane.Interface.ItemClickListener;
import com.example.mat.pkane.R;

/**
 * Created by Mat on 11/27/17.
 */

public class AdminMenuViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnCreateContextMenuListener {
    public TextView txtMenuName;
    public ImageView imageView;
    public TextView txtMenuDesc;
    public TextView txtMenuPrice;
    public LinearLayout layoutMenu;

    int minHeight;


    CardView cardViewList;

    private ItemClickListener itemClickListener;

    public AdminMenuViewHolder(View itemView) {
        super(itemView);
        txtMenuName = (TextView) itemView.findViewById(R.id.menu_name);
        imageView = (ImageView) itemView.findViewById(R.id.menu_image);
        txtMenuDesc = (TextView) itemView.findViewById(R.id.menu_desc);
        txtMenuPrice = (TextView) itemView.findViewById(R.id.menu_price);


        layoutMenu = (LinearLayout) itemView.findViewById(R.id.layoutMenu);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

        //remove if error
        cardViewList = (CardView) itemView.findViewById(R.id.cardViewList);

        cardViewList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                cardViewList.getViewTreeObserver().removeOnPreDrawListener(this);
                minHeight = cardViewList.getHeight();
                ViewGroup.LayoutParams layoutParams = cardViewList.getLayoutParams();
                layoutParams.height = minHeight;
                cardViewList.setLayoutParams(layoutParams);

                return true;
            }
        });

        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLong) {
                toggleCardViewnHeight();

            }
        };
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    private void toggleCardViewnHeight() {

        if (cardViewList.getHeight() == minHeight) {
            // expand
            txtMenuDesc.setVisibility(View.VISIBLE);
            txtMenuPrice.setVisibility(View.VISIBLE);

            expandView(); //'height' is the height of screen which we have measured already.


        } else {
            // collapse
            collapseView();
            txtMenuDesc.setVisibility(View.GONE);
            txtMenuPrice.setVisibility(View.GONE);


        }
    }

    public void collapseView() {

        ValueAnimator anim = ValueAnimator.ofInt(cardViewList.getMeasuredHeightAndState(),
                minHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cardViewList.getLayoutParams();
                layoutParams.height = val;
                cardViewList.setLayoutParams(layoutParams);

            }
        });
        anim.start();
    }


    public void expandView() {
        ValueAnimator anim = ValueAnimator.ofInt(cardViewList.getMeasuredHeightAndState(),
                400);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cardViewList.getLayoutParams();
                layoutParams.height = val;
                cardViewList.setLayoutParams(layoutParams);
            }
        });

        anim.start();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select an action");
        menu.add(0,0,getAdapterPosition(),"Update");
        menu.add(0,1,getAdapterPosition(),"Delete");
    }
}
