package com.example.tasarim_calismasi_2.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.example.tasarim_calismasi_2.R;
import com.example.tasarim_calismasi_2.databinding.ActivityAnaSayfaBinding;
import com.google.android.material.navigation.NavigationView;


public class AnaSayfa extends AppCompatActivity  {
    private  ActivityAnaSayfaBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnaSayfaBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

    }
    public void YerBul(View view){
        Intent intent = new Intent(AnaSayfa.this, YerBulmaEkrani.class);
        startActivity(intent);
    }
    public void KitapIsleri(View view){
        Intent intent = new Intent(AnaSayfa.this, KitapIslemleriEkrani.class);
        startActivity(intent);
    }
}
