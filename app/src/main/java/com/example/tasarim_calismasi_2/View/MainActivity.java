package com.example.tasarim_calismasi_2.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tasarim_calismasi_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(MainActivity.this, AnaSayfa.class);
            startActivity(intent);
            finish();
        }
    }
    public void girisYap(View view){
        Intent intent =new Intent(MainActivity.this,GirisEkrani.class);
        startActivities(new Intent[]{intent});
        finish();
    }
    public void  kayitOl(View view){
        Intent intent =new Intent(MainActivity.this,KayitEkrani.class);
        startActivity(intent);

    }
}