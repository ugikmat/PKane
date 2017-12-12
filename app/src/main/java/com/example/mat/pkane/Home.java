package com.example.mat.pkane;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat.pkane.Common.Common;
import com.example.mat.pkane.Database.Database;
import com.example.mat.pkane.Model.Order;
import com.example.mat.pkane.Model.Pijat;
import com.example.mat.pkane.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference pijat;

    TextView txtFullName;


    FirebaseRecyclerAdapter<Pijat, MenuViewHolder> adapter;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //init Firebase
        database = FirebaseDatabase.getInstance();
        pijat = database.getReference("Pijat");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Name
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

        adapter = new FirebaseRecyclerAdapter<Pijat, MenuViewHolder>(Pijat.class, R.layout.menu_item, MenuViewHolder.class, pijat) {
            @Override
            protected void populateViewHolder(final MenuViewHolder viewHolder, final Pijat model, final int position) {

                Locale locale = new Locale("in", "ID");
                NumberFormat fmt= NumberFormat.getCurrencyInstance(locale);

                viewHolder.txtMenuName.setText(model.getName());
                viewHolder.txtMenuDesc.setText(model.getDesc());
                viewHolder.txtMenuPrice.setText(fmt.format(Integer.parseInt(model.getPrice())));
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                final Pijat clickItem = model;

                viewHolder.btnPesan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
                        alertDialog.setTitle("Konfirmasi");
                        alertDialog.setMessage("Apakah anda ingin memesan ");

                        alertDialog.setIcon(R.drawable.ic_shopping_cart);

                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Home.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                                new Database(getBaseContext()).addToCart(new Order (
                                        model.getId(),
                                        model.getName(),
                                        model.getPrice(),
                                        model.getDiscount()
                                ));
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
                });

            }
        };
        recycler_menu.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_profil) {
            intent = new Intent(this, Profil.class);
            startActivity(intent);
        }else if (id == R.id.nav_menu) {
            adapter.notifyDataSetChanged();
        }else if (id == R.id.nav_chart) {
            intent = new Intent(Home.this,CartActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_order) {
            intent = new Intent(Home.this,OrderStatus.class);
            startActivity(intent);
        }else if (id == R.id.nav_about) {
            intent = new Intent(Home.this,AboutUs.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout) {
            intent = new Intent(Home.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
