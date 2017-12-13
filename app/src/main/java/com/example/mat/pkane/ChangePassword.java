package com.example.mat.pkane;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mat.pkane.Common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangePassword extends AppCompatActivity {

    EditText oldPassword,newPassword;
    Button btnCancel,btnSave;


    FirebaseDatabase database;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        database = FirebaseDatabase.getInstance();
        user = database.getReference("Users");

        oldPassword = findViewById(R.id.text_old_password);
        newPassword = findViewById(R.id.text_new_password);

        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=oldPassword.getText().toString();
                MessageDigest digest;
                try
                {
                    digest = MessageDigest.getInstance("MD5");
                    digest.update(password.getBytes(Charset.forName("US-ASCII")),0,password.length());
                    byte[] magnitude = digest.digest();
                    BigInteger bi = new BigInteger(1, magnitude);
                    String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
                    password=hash;
                }
                catch (NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }

                if(password.equals(Common.currentUser.getPassword())){{
                    password=newPassword.getText().toString();
                    try
                    {
                        digest = MessageDigest.getInstance("MD5");
                        digest.update(password.getBytes(Charset.forName("US-ASCII")),0,password.length());
                        byte[] magnitude = digest.digest();
                        BigInteger bi = new BigInteger(1, magnitude);
                        String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
                        password=hash;
                    }
                    catch (NoSuchAlgorithmException e)
                    {
                        e.printStackTrace();
                    }
                    Common.currentUser.setPassword(password);
                    user.child(Common.username).setValue(Common.currentUser);
                    Toast.makeText(ChangePassword.this,"Berhasil Mengubah Password",Toast.LENGTH_SHORT);
                    onBackPressed();
                }}else{
                    Toast.makeText(ChangePassword.this,"Password Salah!",Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
