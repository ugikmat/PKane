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

import com.example.mat.pkane.Model.Therapis;
import com.example.mat.pkane.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TambahTherapist extends AppCompatActivity {

    Button btnDaftar;
    TextView id, name, email, address, phone;

    Spinner gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_therapist);

        id = findViewById(R.id.txtId);
        name = (TextView) findViewById(R.id.txtName);
        email = (TextView) findViewById(R.id.txtEmail);
        address = (TextView) findViewById(R.id.txtAddress);
        phone = (TextView) findViewById(R.id.txtPhone);

        gender = findViewById(R.id.spinner_gender);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gender.setAdapter(adapter);


        btnDaftar = (Button) findViewById(R.id.buttonDaftar);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_therapist = database.getReference("Therapis");

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(TambahTherapist.this);
                mDialog.setMessage("Mohon Tunggu Sebentar....");
                mDialog.show();


                table_therapist.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (id.getText().toString().equals("")
                                || name.getText().toString().equals("")
                                || email.getText().toString().equals("")
                                || phone.getText().toString().equals("")
                                || address.getText().toString().equals("")) {
                            Toast.makeText(TambahTherapist.this, "Kolom belum terisi semua.", Toast.LENGTH_SHORT).show();
                        } else {

                            if (dataSnapshot.child(id.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(TambahTherapist.this, "Username sudah ada.", Toast.LENGTH_SHORT).show();

                            } else {
                                Therapis therapis = new Therapis(name.getText().toString(), phone.getText().toString(), address.getText().toString(), email.getText().toString(), gender.getSelectedItem().toString());
                                table_therapist.child(id.getText().toString()).setValue(therapis);
                                Toast.makeText(TambahTherapist.this, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Toast.makeText(TambahTherapist.this, "Database belum dibuat", Toast.LENGTH_SHORT);
                mDialog.dismiss();
            }
        });

    }
}
