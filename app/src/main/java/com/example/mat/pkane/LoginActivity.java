package com.example.mat.pkane;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat.pkane.Common.Common;
import com.example.mat.pkane.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LoginActivity extends AppCompatActivity {

    Button btnMasuk;
    TextView username;
    TextView password;
    TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnMasuk = (Button) findViewById(R.id.buttonMasuk);

        username = (TextView) findViewById(R.id.txtUsername);
        password = (TextView) findViewById(R.id.txtPassword);
        signUp = findViewById(R.id.logSignUp);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Users");

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
                mDialog.setMessage("Mohon Tunggu Sebentar....");
                mDialog.setCancelable(false);
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(username.getText().toString()).exists()){
                            User user = dataSnapshot.child(username.getText().toString()).getValue(User.class);
                            mDialog.dismiss();
                            if(user.getPassword().equals(password.getText().toString())){
                                Toast.makeText(LoginActivity.this,"Login Berhasil ", Toast.LENGTH_SHORT).show();
                                Intent homeIntent;
                                Toast.makeText(LoginActivity.this,"As "+user.isAdmin(), Toast.LENGTH_SHORT).show();
                                if(user.isAdmin()){
                                    homeIntent = new Intent(LoginActivity.this,HomeAdmin.class);
                                }else{
                                    homeIntent = new Intent(LoginActivity.this,Home.class);
                                }
                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                Common.username=username.getText().toString();
                                Common.currentUser=user;
                                startActivity(homeIntent);
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this,"Login Gagal", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            mDialog.dismiss();
                            Toast.makeText(LoginActivity.this,"Akun tidak ada", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, "Canceled" + databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
