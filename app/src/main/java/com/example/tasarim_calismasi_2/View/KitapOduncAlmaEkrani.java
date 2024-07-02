package com.example.tasarim_calismasi_2.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tasarim_calismasi_2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class KitapOduncAlmaEkrani extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitap_odunc_alma_ekrani);

        firebaseFirestore = FirebaseFirestore.getInstance();
        editText = findViewById(R.id.editTextNumber2);

        Button odunc_al = findViewById(R.id.button2);
        Button teslimEt = findViewById(R.id.button3);

        odunc_al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kitapKodu = editText.getText().toString().trim();
                if (!kitapKodu.isEmpty()) {
                    oduncAl(kitapKodu);
                } else {
                    Toast.makeText(KitapOduncAlmaEkrani.this, "Lütfen kitap kodunu girin.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        teslimEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kitapKodu = editText.getText().toString().trim();
                if (!kitapKodu.isEmpty()) {
                    TeslimEt(kitapKodu);
                } else {
                    Toast.makeText(KitapOduncAlmaEkrani.this, "Lütfen kitap kodunu girin.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void oduncAl(String kod) {
        //databaseReference.child(user.getUid()).child("user_name").setValue(userName);
        firebaseFirestore.collection("Kitaplar").document(kod)
                .update("kitapDurumu", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        OduncAlindiDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void TeslimEt(String kod) {
        firebaseFirestore.collection("Kitaplar").document(kod)
                .update("kitapDurumu", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        TeslimEdildiDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void OduncAlindiDialog() {
        if (!isFinishing() && !isDestroyed()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(KitapOduncAlmaEkrani.this);
            builder.setTitle("Başarılı");
            builder.setMessage("Kitap ödünç alma işlemi başarılı olmuştur. 14 gün süreniz vardır.");
            builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(KitapOduncAlmaEkrani.this, AnaSayfa.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.show();
        }
    }

    private void TeslimEdildiDialog() {
        if (!isFinishing() && !isDestroyed()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(KitapOduncAlmaEkrani.this);
            builder.setTitle("Başarılı");
            builder.setMessage("Kitap teslim işlemi başarılı olmuştur.");
            builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(KitapOduncAlmaEkrani.this, AnaSayfa.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.show();
        }
    }

}
