package com.example.mat.pkane;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat.pkane.Model.Order;
import com.example.mat.pkane.Model.Therapis;
import com.example.mat.pkane.Model.User;
import com.example.mat.pkane.ViewHolder.TherapisViewHolder;
import com.example.mat.pkane.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TherapisList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fabAdd;

    FirebaseDatabase database;
    DatabaseReference therapisList;

    FirebaseRecyclerAdapter<Therapis,TherapisViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapis_list);

        fabAdd = findViewById(R.id.fab_add);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TherapisList.this,TambahTherapist.class);
                startActivity(intent);
            }
        });

        //firebase
        database = FirebaseDatabase.getInstance().getInstance();
        therapisList = database.getReference("Therapis");




        recyclerView = (RecyclerView) findViewById(R.id.therapis_list);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadListTherapis();
    }

    private void loadListTherapis() {
        adapter = new FirebaseRecyclerAdapter<Therapis,TherapisViewHolder>(
                Therapis.class,
                R.layout.therapis_layout,
                TherapisViewHolder.class,
                therapisList
        ) {
            @Override
            protected void populateViewHolder(TherapisViewHolder viewHolder, Therapis model, int position) {
                viewHolder.textName.setText(model.getNama());
                viewHolder.textPhone.setText(model.getPhone());
                viewHolder.textGender.setText(model.getGender());
                viewHolder.textEmail.setText(model.getEmail());
                viewHolder.textAddress.setText(model.getAddress());

            }
        };
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        if(item.getTitle().equals("Update")){
            toUpdate(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals("Delete")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(TherapisList.this);
            alertDialog.setTitle("Konfirmasi");
            alertDialog.setMessage("Apakah anda ingin menghapus ? ");
            alertDialog.setIcon(R.drawable.ic_shopping_cart);

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    therapisList.child(adapter.getRef(item.getOrder()).getKey()).removeValue();
                    Toast.makeText(TherapisList.this,"Therapis Berhasil Dihapus ",Toast.LENGTH_SHORT).show();
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

    private void toUpdate(String key, Therapis item) {
        Intent intent = new Intent(TherapisList.this,EditProfil.class);
        intent.putExtra("username",key);
        intent.putExtra("name",item.getNama());
        intent.putExtra("email",item.getEmail());
        intent.putExtra("phone",item.getPhone());
        intent.putExtra("address",item.getAddress());
        intent.putExtra("gender",item.getGender());
        startActivity(intent);
    }
}
