package com.example.mat.pkane;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mat.pkane.Common.Requests;
import com.example.mat.pkane.Interface.ItemClickListener;
import com.example.mat.pkane.Model.Request;
import com.example.mat.pkane.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderList extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;


    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Requests.request = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        request = database.getReference("Request");

        recyclerView = findViewById(R.id.order_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrder();
    }

    private void loadOrder() {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                request
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                Requests.request.add(model);
                viewHolder.textDate.setText(model.getDate());
                viewHolder.textTime.setText(model.getTime());
                viewHolder.textPrice.setText(model.getTotal());
                viewHolder.textGender.setText(model.getGender());
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLong) {
                        Intent intent = new Intent(OrderList.this, OrderDetail.class);
                        intent.putExtra("position",position);
                        intent.putExtra("key",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };

        recyclerView.setAdapter(adapter);

    }
}
