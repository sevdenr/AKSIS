package com.example.tasarim_calismasi_2.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.tasarim_calismasi_2.databinding.ActivityKitapIslemleriEkraniBinding;

public class KitapIslemleriEkrani extends AppCompatActivity {

    private ActivityKitapIslemleriEkraniBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKitapIslemleriEkraniBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

    }
    public void KitapAra(View view){
        Intent intent = new Intent(KitapIslemleriEkrani.this, KitapBulmaEkrani.class);
        startActivity(intent);
    }
    public void KitapIslemleri(View view){
        Intent intent = new Intent(KitapIslemleriEkrani.this, KitapOduncAlmaEkrani.class);
        startActivity(intent);
    }
}