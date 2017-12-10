package com.example.mat.pkane;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mat.pkane.Common.Common;
import com.example.mat.pkane.Database.Database;
import com.example.mat.pkane.Model.Order;
import com.example.mat.pkane.Model.Request;
import com.example.mat.pkane.ViewHolder.CardAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView textTotal,textWaktu,textGender;
    RecyclerView listCart;

    TextView txt_total;
    Button btnPesan;

    List<Order> cart = new ArrayList<>();

    CardAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //firebase
        database = FirebaseDatabase.getInstance().getInstance();
        requests = database.getReference("Request");


        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txt_total = (TextView)findViewById(R.id.total);
        btnPesan = (Button)findViewById(R.id.btnKonfirmPesan);

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();

            }
        });

        loadListFood();
    }

    private void showAlertDialog() {
        // Get Current Date
        final int mYear, mDay, mMonth;
        final int[] mHour = new int[1];
        final int[] mMinute = new int[1];
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);



        final DatePickerDialog datePickerDialog = new DatePickerDialog(CartActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, final int year,
                                          final int monthOfYear, final int dayOfMonth) {
                        // Get Current Time
                        final Calendar cs = Calendar.getInstance();
                        mHour[0] = cs.get(Calendar.HOUR_OF_DAY);
                        mMinute[0] = cs.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(CartActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, final int hourOfDay,
                                                          final int minute) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                        builder.setTitle("Pilih Jenis Kelamin Therapist")
                                                .setItems(R.array.gender_array, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        AlertDialog.Builder konfirm = new AlertDialog.Builder(CartActivity.this);
                                                        konfirm.setTitle("Apakah Pesanan anda sudah benar?");
                                                        LayoutInflater inflater;
                                                        inflater = getLayoutInflater();
                                                        View dialogView =inflater.inflate(R.layout.confirm_layout,null);
                                                        textTotal = dialogView.findViewById(R.id.total);
                                                        textWaktu = dialogView.findViewById(R.id.waktu);
                                                        textGender = dialogView.findViewById(R.id.gender);
                                                        listCart = dialogView.findViewById(R.id.listCart);

                                                        listCart.hasFixedSize();
                                                        layoutManager = new LinearLayoutManager(CartActivity.this);
                                                        listCart.setLayoutManager(layoutManager);

                                                        cart = new Database(CartActivity.this).getCart();
                                                        adapter = new CardAdapter(cart,CartActivity.this);
                                                        listCart.setAdapter(adapter);

                                                        textTotal.setText(txt_total.getText().toString());
                                                        textWaktu.setText((dayOfMonth+"/"+monthOfYear+"/"+year)+" "+(hourOfDay+":"+minute));
                                                        textGender.setText((which==0?"Laki-Laki":"Perempuan"));

                                                        konfirm.setView(dialogView)
                                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        Request request = new Request(
                                                                                Common.currentUser.getPhone(),
                                                                                Common.currentUser.getNama(),
                                                                                (which==0?"Laki-Laki":"Perempuan"),
                                                                                (dayOfMonth+"/"+monthOfYear+"/"+year),
                                                                                (hourOfDay+":"+minute),
                                                                                txt_total.getText().toString(),
                                                                                cart
                                                                        );
                                                                        requests.child(String.valueOf(System.currentTimeMillis()))
                                                                                .setValue(request);
                                                                        new Database(getBaseContext()).clearCart();
                                                                        Toast.makeText(CartActivity.this,"Thanks",Toast.LENGTH_SHORT).show();
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

    private void loadListFood() {
        cart = new Database(this).getCart();
        adapter = new CardAdapter(cart,this);
        recyclerView.setAdapter(adapter);
        int total=0;
        for(Order order:cart){
            total+=(Integer.parseInt(order.getPrice()))-((Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getDiscount())));
        }
        Locale locale = new Locale("en","US");
        NumberFormat fmt= NumberFormat.getCurrencyInstance(locale);

        txt_total.setText(fmt.format(total));
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        if(item.getTitle().equals("Delete")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
            alertDialog.setTitle("Konfirmasi");
            alertDialog.setMessage("Apakah anda ingin menghapus ? ");
            alertDialog.setIcon(R.drawable.ic_shopping_cart);

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cart.remove(item.getOrder());
                    new Database(CartActivity.this).removeCart(adapter.getOrder(item.getOrder()).getProductID());
                    adapter.notifyDataSetChanged();
                    loadListFood();
                    Toast.makeText(CartActivity.this,"Cart Berhasil Dihapus ",Toast.LENGTH_SHORT).show();
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
