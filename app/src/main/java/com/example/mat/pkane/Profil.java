package com.example.mat.pkane;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mat.pkane.Common.Common;

public class Profil extends AppCompatActivity {

    TextView username,name,email,phone,address,gender;
    Button changePassword,editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        username = findViewById(R.id.txt_username);
        name = findViewById(R.id.txt_name);
        email = findViewById(R.id.txt_email);
        phone = findViewById(R.id.txt_phone);
        gender = findViewById(R.id.txt_gender);
        address = findViewById(R.id.txt_address);

        username.setText(Common.username);
        name.setText(Common.currentUser.getNama());
        email.setText(Common.currentUser.getEmail());
        phone.setText(Common.currentUser.getPhone());
        gender.setText(Common.currentUser.getGender());
        address.setText(Common.currentUser.getAddress());

        changePassword = findViewById(R.id.btn_change_password);
        editProfile = findViewById(R.id.btn_edit_profil);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profil.this,ChangePassword.class);
                startActivity(intent);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profil.this,EditProfil.class);
                intent.putExtra("username",Common.username);
                intent.putExtra("name",Common.currentUser.getNama());
                intent.putExtra("email",Common.currentUser.getEmail());
                intent.putExtra("phone",Common.currentUser.getPhone());
                intent.putExtra("gender",Common.currentUser.getGender());
                intent.putExtra("address",Common.currentUser.getAddress());
                intent.putExtra("pass",Common.currentUser.getPassword());

                startActivity(intent);
            }
        });
    }
}
