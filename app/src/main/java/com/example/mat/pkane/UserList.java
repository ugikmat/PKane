package com.example.mat.pkane;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mat.pkane.Model.Order;
import com.example.mat.pkane.Model.User;
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

    Bundle extra;
    Boolean admin;

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
        if (item.getTitle().equals("Delete")) {
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
}
