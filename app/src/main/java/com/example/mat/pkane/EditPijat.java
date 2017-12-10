package com.example.mat.pkane;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mat.pkane.Model.Pijat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class EditPijat extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference pijat;
    FirebaseStorage storage;
    StorageReference storageReference;

    Pijat newPijat;

    EditText textNama,textDesc,textHarga,textDisc;
    Button btnUpload,btnSave;

    Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 71;
    String key;
    String image;
    String id;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pijat);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");
        String name  = extras.getString("pijat_name");
        String desc  = extras.getString("pijat_desc");
        String price  = extras.getString("pijat_price");
        String disc  = extras.getString("pijat_disc");
        image  = extras.getString("pijat_image");
        id  = extras.getString("pijat_id");

        database = FirebaseDatabase.getInstance();
        pijat = database.getReference("Pijat");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        textNama = findViewById(R.id.text_nama);
        textDesc = findViewById(R.id.text_deskripsi);
        textHarga = findViewById(R.id.text_harga);
        textDisc = findViewById(R.id.text_diskon);

        textNama.setText(name);
        textDesc.setText(desc);
        textHarga.setText(price);
        textDisc.setText(disc);

        btnSave = findViewById(R.id.btn_save);
        btnUpload = findViewById(R.id.btn_upload_picture);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMenu();
            }
        });
    }

    public void chooseImage(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data !=null && data.getData()!=null){

            saveUri = data.getData();
            btnUpload.setText("Foto telah dipilih");

        }
    }

    private void uploadMenu(){
        //progress Dialog
        final ProgressDialog mDialog = new ProgressDialog(EditPijat.this);
        mDialog.setMessage("Mohon Tunggu Sebentar....");
        mDialog.show();
        if(saveUri!=null){
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFolder.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            mDialog.dismiss();
                                            newPijat = new Pijat(id,
                                                    textNama.getText().toString(),
                                                    uri.toString(),
                                                    textDesc.getText().toString(),
                                                    textHarga.getText().toString(),
                                                    textDisc.getText().toString()
                                                    );
                                            pijat.child(key).setValue(newPijat);
                                            Toast.makeText(EditPijat.this,"Berhasil mengubah menu pijat",Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(EditPijat.this,"Gagal mengubah menu pijat",Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded "+progress+"%");

                }
            });
        }else{
            newPijat = new Pijat(id,
                    textNama.getText().toString(),
                    image,
                    textDesc.getText().toString(),
                    textHarga.getText().toString(),
                    textDisc.getText().toString()
                    );
            pijat.child(key).setValue(newPijat);
            Toast.makeText(EditPijat.this,"Berhasil mengubah menu pijat",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }
}
