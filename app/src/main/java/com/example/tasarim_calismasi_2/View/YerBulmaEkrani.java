package com.example.tasarim_calismasi_2.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasarim_calismasi_2.Adapter.MasaAdapter;
import com.example.tasarim_calismasi_2.Model.Masa;
import com.example.tasarim_calismasi_2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class YerBulmaEkrani extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 101;
    MasaAdapter masaAdapter;
    ArrayList<Masa> masaList;

    DatabaseReference databaseReference;
    NotificationManager notificationManager;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yer_bulma_ekrani);
        editText = findViewById(R.id.editTextNumber2);

        notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        RecyclerView recyclerView = findViewById(R.id.rW);
        int spanCount = 1;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        masaList = new ArrayList<>();
        masaAdapter = new MasaAdapter(masaList, this);
        recyclerView.setAdapter(masaAdapter);

        masaAdapter.setOnItemClickListener(new MasaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Masa masa) {
                showMasaDialog(masa);
            }
        });

        Button yerBul = findViewById(R.id.button2);
        yerBul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kisiSayisiStr = editText.getText().toString();
                try {
                    int kisiSayisi = Integer.parseInt(kisiSayisiStr);
                    if (kisiSayisi > 4) {
                        Toast.makeText(YerBulmaEkrani.this, "Girilen sayı 4'ten büyük olamaz", Toast.LENGTH_SHORT).show();
                    } else {
                        Bul(kisiSayisiStr);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(YerBulmaEkrani.this, "Lütfen geçerli bir sayı giriniz", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Bul(String kisiSayisi) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Masa");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                masaList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        Masa masa = dataSnapshot.getValue(Masa.class);
                        if (masa != null) {
                            if (masa.getKisiSayisi() instanceof Long) {
                                masa.setKisiSayisi(String.valueOf(masa.getKisiSayisi()));
                            }
                            if (masa.getKisiSayisi().equals(kisiSayisi)) {
                                masaList.add(masa);
                                Log.d("FirebaseDatabase", String.valueOf(masaList));
                            }
                        }
                    } catch (Exception e) {
                        Log.d("FirebaseDatabase", "Veri dönüştürme hatası: " + e.getMessage());
                    }
                }
                masaAdapter.notifyDataSetChanged();

                if (masaList.isEmpty()) {
                    Toast.makeText(YerBulmaEkrani.this, "Veri bulunamadı", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(YerBulmaEkrani.this, "Error reading user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMasaDialog(Masa masa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_masa, null);
        builder.setView(dialogView);

        TextView masaInfo = dialogView.findViewById(R.id.masaInfo);
        masaInfo.setText("Masa ID: " + masa.getMasaId() + "\nSalon Adı: " + masa.getSalonId()+"\nKişi Sayısı: " + masa.getKisiSayisi());

        AppCompatButton btnRezerve = dialogView.findViewById(R.id.btnRezerve);
        AppCompatButton btnIptal = dialogView.findViewById(R.id.btnIptal);

        AlertDialog dialog = builder.create();

        btnRezerve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rezerve(masa.getMasaId());
                dialog.dismiss();
            }
        });

        btnIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void Rezerve(String masaId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Masa").child(masaId);
        databaseReference.child("masaDurumu").setValue(true);

        if (checkPermission()) {
            planNotification(masaId);
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void planNotification(String masaId) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            sendNotification(masaId);
        }, 10 * 60 * 1000); // 10 dakika
    }

    private void sendNotification(String masaId) {
        createNotificationChannel();

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("masaId", masaId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Rezervasyon Onayı")
                .setContentText("Rezervasyonunuzu onaylayınız.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_launcher_foreground, "Onayla", pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bildirim izni verildi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bildirim izni reddedildi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Rezervasyon Kanalı";
            String description = "Rezervasyon bildirim kanalı";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
