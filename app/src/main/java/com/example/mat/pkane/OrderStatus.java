package com.example.mat.pkane;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mat.pkane.Common.Common;
import com.example.mat.pkane.Common.Requests;
import com.example.mat.pkane.Database.Database;
import com.example.mat.pkane.Interface.ItemClickListener;
import com.example.mat.pkane.Model.Order;
import com.example.mat.pkane.Model.Request;
import com.example.mat.pkane.ViewHolder.CardAdapter;
import com.example.mat.pkane.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;

    TextView textTotal,textWaktu,textGender;
    RecyclerView listCart;

    List<Order> cart = new ArrayList<>();

    CardAdapter card_adapter;

    Request requested;

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


    private void showAlertDialog(final int index) {
        // Get Current Date
        final int mYear, mDay, mMonth;
        final int[] mHour = new int[1];
        final int[] mMinute = new int[1];
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(OrderStatus.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, final int year,
                                          final int monthOfYear, final int dayOfMonth) {
                        // Get Current Time
                        final Calendar cs = Calendar.getInstance();
                        mHour[0] = cs.get(Calendar.HOUR_OF_DAY);
                        mMinute[0] = cs.get(Calendar.MINUTE);
                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(OrderStatus.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, final int hourOfDay,
                                                          final int minute) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderStatus.this);
                                        builder.setTitle("Pilih Jenis Kelamin Therapist")
                                                .setItems(R.array.gender_array, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        AlertDialog.Builder konfirm = new AlertDialog.Builder(OrderStatus.this);
                                                        konfirm.setTitle("Apakah Pesanan anda sudah benar?");
                                                        LayoutInflater inflater;
                                                        inflater = getLayoutInflater();
                                                        View dialogView =inflater.inflate(R.layout.confirm_layout,null);
                                                        textTotal = dialogView.findViewById(R.id.total);
                                                        textWaktu = dialogView.findViewById(R.id.waktu);
                                                        textGender = dialogView.findViewById(R.id.gender);
                                                        listCart = dialogView.findViewById(R.id.listCart);

                                                        listCart.hasFixedSize();
                                                        layoutManager = new LinearLayoutManager(OrderStatus.this);
                                                        listCart.setLayoutManager(layoutManager);

                                                        requested = Requests.request.get(index);
                                                        cart = requested.getPijat();
                                                        card_adapter = new CardAdapter(cart,OrderStatus.this);
                                                        listCart.setAdapter(card_adapter);


                                                        textTotal.setText(requested.getTotal());
                                                        textWaktu.setText((dayOfMonth+"/"+monthOfYear+"/"+year)+" "+(hourOfDay+":"+minute));
                                                        textGender.setText((which==0?"Laki-Laki":"Perempuan"));

                                                        konfirm.setView(dialogView)
                                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        Request newRequest = Requests.request.get(index);
                                                                        newRequest.setDate(dayOfMonth + "/" + monthOfYear + "/" + year);
                                                                        newRequest.setTime(hourOfDay + ":" + minute);
                                                                        newRequest.setGender((which==0?"Laki-Laki":"Perempuan"));
                                                                        request.child(adapter.getRef(index).getKey()).setValue(newRequest);
                                                                        new Database(getBaseContext()).clearCart();
                                                                        Toast.makeText(OrderStatus.this,"Pesanan berhasil diubuah",Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                    }
                                                                })
                                                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });


                                                        konfirm.show();
                                                    }
                                                });
                                        builder.show();
                                    }
                                }, mHour[0], mMinute[0], false);
                        timePickerDialog.show();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final MenuItem menuItem= item;
        if(item.getTitle().equals("Update")){
            showAlertDialog(item.getOrder());
        }else if(item.getTitle().equals("Delete")){
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
