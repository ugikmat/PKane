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

import com.example.mat.pkane.Common.Requests;
import com.example.mat.pkane.Database.Database;
import com.example.mat.pkane.Model.Order;
import com.example.mat.pkane.Model.Request;
import com.example.mat.pkane.Model.User;
import com.example.mat.pkane.ViewHolder.CardAdapter;
import com.example.mat.pkane.ViewHolder.OrderViewHolder;
import com.example.mat.pkane.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference userList;

    TextView txt_total;
    Button btnPesan;
    Bundle extra;
    Boolean admin;
    FloatingActionButton fabAdd;

    List<Order> user = new ArrayList<>();

    FirebaseRecyclerAdapter<User,UserViewHolder>  adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        //firebase
        database = FirebaseDatabase.getInstance().getInstance();
        userList = database.getReference("Users");

        extra = getIntent().getExtras();
        admin = extra.getBoolean("Admin");

        fabAdd = findViewById(R.id.fab_add);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserList.this,SignUpActivity.class);
                intent.putExtra("Admin",admin);
                startActivity(intent);

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.user_list);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadListUser();
    }

    private void loadListUser() {

        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                R.layout.user_layout,
                UserViewHolder.class,
                userList.orderByChild("admin").equalTo(admin)
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserList.this);
            alertDialog.setTitle("Konfirmasi");
            alertDialog.setMessage("Apakah anda ingin menghapus ? ");
            alertDialog.setIcon(R.drawable.ic_shopping_cart);

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userList.child(adapter.getRef(item.getOrder()).getKey()).removeValue();
                    Toast.makeText(UserList.this,"User berhasil Dihapus ",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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

    private void toUpdate(String key, User item) {
        Intent intent = new Intent(UserList.this,EditProfil.class);
        intent.putExtra("username",key);
        intent.putExtra("name",item.getNama());
        intent.putExtra("email",item.getEmail());
        intent.putExtra("phone",item.getPhone());
        intent.putExtra("pass",item.getPassword());
        startActivity(intent);
    }
}
