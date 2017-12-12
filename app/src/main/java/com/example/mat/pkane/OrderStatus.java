package com.example.mat.pkane;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mat.pkane.Common.Common;
import com.example.mat.pkane.Common.Requests;
import com.example.mat.pkane.Interface.ItemClickListener;
import com.example.mat.pkane.Model.Request;
import com.example.mat.pkane.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;
    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        Requests.request = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        request = database.getReference("Request");

        recyclerView = findViewById(R.id.orderList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrder(Common.currentUser.getPhone());
    }

    private void loadOrder(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                request.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
//                viewHolder.textName.setText(adapter.getRef(position).getKey()); //To get Key
                Requests.request.add(model);
                viewHolder.textDate.setText(model.getDate());
                viewHolder.textTime.setText(model.getTime());
                viewHolder.textPrice.setText(model.getTotal());
                viewHolder.textGender.setText(model.getGender());
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLong) {
                        Intent intent = new Intent(OrderStatus.this, OrderDetail.class);
                        intent.putExtra("position",position);
                        intent.putExtra("key",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final MenuItem menuItem= item;
        if (item.getTitle().equals("Delete")) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatus.this);
            alertDialog.setTitle("Konfirmasi");
            alertDialog.setMessage("Apakah anda ingin menghapus ? ");
            alertDialog.setIcon(R.drawable.ic_shopping_cart);

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    request.child(adapter.getRef(menuItem.getOrder()).getKey()).removeValue();
                    Toast.makeText(OrderStatus.this,"Pesanan Berhasil Dihapus ",Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
        return super.onContextItemSelected(item);
    }
}
