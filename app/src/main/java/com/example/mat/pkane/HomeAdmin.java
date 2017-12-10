package com.example.mat.pkane;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat.pkane.Common.Common;
import com.example.mat.pkane.Database.Database;
import com.example.mat.pkane.Model.Order;
import com.example.mat.pkane.Model.Pijat;
import com.example.mat.pkane.ViewHolder.AdminMenuViewHolder;
import com.example.mat.pkane.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeAdmin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference pijat;

    TextView txtFullName;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Pijat, AdminMenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init Firebase
        database = FirebaseDatabase.getInstance();
        pijat = database.getReference("Pijat");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdmin.this,TambahPijat.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView) headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getNama());

        //load Menu
        recycler_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();

    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Pijat, AdminMenuViewHolder>(Pijat.class, R.layout.admin_menu_item, AdminMenuViewHolder.class, pijat) {
            @Override
            protected void populateViewHolder(final AdminMenuViewHolder viewHolder, final Pijat model, final int position) {
                viewHolder.txtMenuName.setText(model.getName());
                viewHolder.txtMenuDesc.setText(model.getDesc());
                viewHolder.txtMenuPrice.setText(model.getPrice());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);

            }
        };
        recycler_menu.setAdapter(adapter);

    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        if(item.getTitle().equals("Update")){
            toUpdate(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals("Delete")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeAdmin.this);
            alertDialog.setTitle("Konfirmasi");
            alertDialog.setMessage("Apakah anda ingin menghapus ? ");
            alertDialog.setIcon(R.drawable.ic_shopping_cart);

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pijat.child(adapter.getRef(item.getOrder()).getKey()).removeValue();
                    Toast.makeText(HomeAdmin.this,"Menu Pijat Berhasil Dihapus ",Toast.LENGTH_SHORT).show();
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

    private void toUpdate(String key, Pijat item) {
        Intent intent = new Intent(HomeAdmin.this,EditPijat.class);
        intent.putExtra("key",key);
        intent.putExtra("pijat_name", item.getName());
        intent.putExtra("pijat_desc", item.getDesc());
        intent.putExtra("pijat_price", item.getPrice());
        intent.putExtra("pijat_disc", item.getDiscount());
        intent.putExtra("pijat_image", item.getImage());
        intent.putExtra("pijat_id", item.getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_home) {
            intent = new Intent(HomeAdmin.this, HomeAdmin.class);
            startActivity(intent);
        } else if (id == R.id.nav_profil) {
            intent = new Intent(HomeAdmin.this, Profil.class);
            startActivity(intent);
        } else if (id == R.id.nav_user) {
            intent = new Intent(HomeAdmin.this, UserList.class);
            intent.putExtra("Admin",false);
            startActivity(intent);
        } else if (id == R.id.nav_admin) {
            intent = new Intent(HomeAdmin.this, UserList.class);
            intent.putExtra("Admin",true);
            startActivity(intent);
        } else if (id == R.id.nav_terapist) {
            intent = new Intent(HomeAdmin.this, TherapisList.class);
            startActivity(intent);
        } else if (id == R.id.nav_order) {
            intent = new Intent(HomeAdmin.this, OrderList.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            intent = new Intent(HomeAdmin.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
