package com.example.tasarim_calismasi_2.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String masaId = intent.getStringExtra("masaId");

        // Firebase referansını alın
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Masa").child(masaId);

        // Masanın durumunu geri "boş" olarak ayarlayın
        databaseReference.child("durum").setValue(false);

        // Bildirimi iptal edin
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(1);
    }
}
