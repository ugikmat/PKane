package com.example.mat.pkane;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mat.pkane.Common.Common;
import com.example.mat.pkane.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                if(oldPassword.getText().toString().equals(Common.currentUser.getPassword())){{
                    Common.currentUser.setPassword(newPassword.getText().toString());
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
