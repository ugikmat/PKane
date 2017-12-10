package com.example.mat.pkane;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat.pkane.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class SignUpActivity extends AppCompatActivity {

    Button btnDaftar;
    TextView username,name,email,address,phone,password,confirm;

    Spinner gender;

    Bundle extras;
    Boolean admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = (TextView) findViewById(R.id.txtUsername);
        name = (TextView) findViewById(R.id.txtName);
        email = (TextView)findViewById(R.id.txtEmail);
        address = (TextView)findViewById(R.id.txtAddress);
        phone = (TextView)findViewById(R.id.txtPhone);
        password = (TextView)findViewById(R.id.txtPassword);
        confirm = (TextView)findViewById(R.id.txtConfirmPassword);

        gender = findViewById(R.id.spinner_gender);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gender.setAdapter(adapter);

        extras = getIntent().getExtras();
        admin = (extras.getBoolean("Admin")?extras.getBoolean("Admin"):false);

        btnDaftar = (Button) findViewById(R.id.buttonDaftar);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Users");

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                mDialog.setMessage("Mohon Tunggu Sebentar....");
                mDialog.show();


                table_user.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(username.getText().toString().equals("")
                                ||name.getText().toString().equals("")
                                ||email.getText().toString().equals("")
                                ||phone.getText().toString().equals("")
                                ||address.getText().toString().equals("")
                                ||password.getText().toString().equals("")
                                ||confirm.getText().toString().equals("")){
                            Toast.makeText(SignUpActivity.this,"Kolom belum terisi semua.", Toast.LENGTH_SHORT).show();
                        }else {

                            if(dataSnapshot.child(username.getText().toString()).exists()){
                                mDialog.dismiss();
                                Toast.makeText(SignUpActivity.this,"Username sudah ada.", Toast.LENGTH_SHORT).show();

                            }else{
                                mDialog.dismiss();
                                if(!password.getText().toString().equals(confirm.getText().toString())){
                                    Toast.makeText(SignUpActivity.this,"Password tidak cocok", Toast.LENGTH_SHORT).show();
                                    confirm.requestFocus();
                                }else{
                                    User user = new User(email.getText().toString(),name.getText().toString(),address.getText().toString(),gender.getSelectedItem().toString(),password.getText().toString(),phone.getText().toString(),admin);
                                    table_user.child(username.getText().toString()).setValue(user);

                                    Toast.makeText(SignUpActivity.this,"Pendaftaran Berhasil", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Toast.makeText(SignUpActivity.this,"Database belum dibuat", Toast.LENGTH_SHORT);
                mDialog.dismiss();
            }
        });
    }
}
