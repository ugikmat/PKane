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

public class TambahPijat extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference pijat;
    FirebaseStorage storage;
    StorageReference storageReference;

    Pijat newPijat;

    EditText textNama,textDesc,textHarga,textDisc;
    Button btnUpload,btnSave;

    Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pijat);

        database = FirebaseDatabase.getInstance();
        pijat = database.getReference("Pijat");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        textNama = findViewById(R.id.text_nama);
        textDesc = findViewById(R.id.text_deskripsi);
        textHarga = findViewById(R.id.text_harga);
        textDisc = findViewById(R.id.text_diskon);

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
        if(saveUri!=null){
            //progress Dialog
            final ProgressDialog mDialog = new ProgressDialog(TambahPijat.this);
            mDialog.setMessage("Mohon Tunggu Sebentar....");
            mDialog.show();

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
                                            newPijat = new Pijat(UUID.randomUUID().toString(),
                                                    textNama.getText().toString(),
                                                    textDesc.getText().toString(),
                                                    textHarga.getText().toString(),
                                                    textDisc.getText().toString(),
                                                    uri.toString());
                                            pijat.push().setValue(newPijat);
                                            Toast.makeText(TambahPijat.this,"Berhasil menambah menu pijat",Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(TambahPijat.this,"Gagal menambah menu pijat",Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded "+progress+"%");

                }
            });
        }
    }
}
