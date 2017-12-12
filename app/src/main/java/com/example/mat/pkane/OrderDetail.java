package com.example.mat.pkane;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat.pkane.Common.Requests;
import com.example.mat.pkane.Model.Order;
import com.example.mat.pkane.Model.Request;
import com.example.mat.pkane.ViewHolder.CardAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderDetail extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    TextView txt_total,textName,textPhone;

    FirebaseDatabase database;
    DatabaseReference request;

    List<Order> cart = new ArrayList<>();

    CardAdapter adapter;

    Request requested;

    int position;
    String key;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        database = FirebaseDatabase.getInstance();
        request = database.getReference("Request");

        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        textName = findViewById(R.id.text_name);
        textPhone = findViewById(R.id.text_phone);
        txt_total = (TextView)findViewById(R.id.total);


        extras = getIntent().getExtras();
        position = extras.getInt("position");
        key = extras.getString("key");
        loadListFood(position);

    }

    private void loadListFood(int i) {
        requested = Requests.request.get(i);
        cart = requested.getPijat();
        adapter = new CardAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        textName.setText(requested.getName());
        textPhone.setText(requested.getPhone());

        updatePrice();
    }

    public void updatePrice(){
        int total=0;
        for(Order order:cart){
            total+=(Integer.parseInt(order.getPrice()))-((Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getDiscount())));
        }

        Locale locale = new Locale("in", "ID");
        NumberFormat fmt= NumberFormat.getCurrencyInstance(locale);
        txt_total.setText(fmt.format(total));
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final MenuItem menuItem= item;
        if(item.getTitle().equals("Delete")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetail.this);
            alertDialog.setTitle("Konfirmasi");
            alertDialog.setMessage("Apakah anda ingin menghapus ? ");
            alertDialog.setIcon(R.drawable.ic_shopping_cart);

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requested.getPijat().remove(menuItem.getOrder());
                    requested.updatePrice();
                    request.child(key).setValue(requested);
                    Toast.makeText(OrderDetail.this,"Pesanan Pijat Berhasil Dihapus ",Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    updatePrice();
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
