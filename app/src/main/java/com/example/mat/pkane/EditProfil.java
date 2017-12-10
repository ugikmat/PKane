package com.example.mat.pkane;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat.pkane.Common.Common;
import com.example.mat.pkane.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfil extends AppCompatActivity {

    TextView textUsername;
    EditText textName,textEmail,textPhone,textAddress;
    Button btnCancel,btnSave,btnDelete;

    Spinner gender;

    FirebaseDatabase database;
    DatabaseReference user;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        database = FirebaseDatabase.getInstance();
        user = database.getReference("Users");

        extras = getIntent().getExtras();
        final String username = extras.getString("username");
        final String name = extras.getString("name");
        String email = extras.getString("email");
        String phone = extras.getString("phone");
        String address = extras.getString("address");
        int selected = (extras.getString("gender").equalsIgnoreCase("Perempuan")?1:0);
        final String pass = extras.getString("pass");


        textUsername = findViewById(R.id.txt_username);
        textName = findViewById(R.id.txt_name);
        textEmail = findViewById(R.id.txt_email);
        textPhone = findViewById(R.id.txt_phone);
        textAddress = findViewById(R.id.txt_address);

        gender = findViewById(R.id.spinner_gender);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gender.setAdapter(adapter);

        textUsername.setText(username);
        textName.setText(name);
        textEmail.setText(email);
        textPhone.setText(phone);
        textAddress.setText(address);

        gender.setSelection(selected);

        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User newUser = new User(textEmail.getText().toString(),textName.getText().toString(),textAddress.getText().toString(),gender.getSelectedItem().toString(),pass,textPhone.getText().toString(),false);
                user.child(username).setValue(newUser);
                Toast.makeText(EditProfil.this,"Berhasil Mengubah Profil",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfil.this,Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditProfil.this);
                alertDialog.setTitle("Konfirmasi");
                alertDialog.setMessage("Apakah anda ingin menghapus ? ");
                alertDialog.setIcon(R.drawable.ic_shopping_cart);

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.child(Common.username).removeValue();
                        Toast.makeText(EditProfil.this,"Menu Pijat Berhasil Dihapus ",Toast.LENGTH_SHORT).show();
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
}
