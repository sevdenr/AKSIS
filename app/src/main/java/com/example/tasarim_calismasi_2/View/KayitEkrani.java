package com.example.tasarim_calismasi_2.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasarim_calismasi_2.R;
import com.example.tasarim_calismasi_2.databinding.ActivityKayitEkraniBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class KayitEkrani extends AppCompatActivity {

    private TextView singup_hata;
    private  TextView password_error;
    private boolean isEmailErrorShown = false;

    private boolean isPasswordErrorShown = false;
    private ActivityKayitEkraniBinding binding;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //find by Id komutu ile tanımlama yapmamak için binding kullanıyoruz
        binding = ActivityKayitEkraniBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();singup_hata = binding.singupHata; // Ekledik

        Button sing_up = findViewById(R.id.kayitOl2);
        password_error = binding.passwordError;
        String mail= binding.emailText.getText().toString();
        String password = binding.passwordText.getText().toString();
        TextView singup_hata = findViewById(R.id.singup_hata);
        TextView password_error = findViewById(R.id.password_error);

        sing_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayitOl(v);
            }
        });
        // Email alanı için TextWatcher ekle
        binding.emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sing_up.setEnabled(false);
                // Metin değişmeden önce bir şeyler yapabilirsiniz
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Metin değiştiğinde bir şeyler yapabilirsiniz
                String email = charSequence.toString(); // Eposta değişkeni güncellendi
                //Patterns ile email adresinin doğruluğunu kontrol edildi
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    clearEmailError();
                }else{
                    setEmailError("Geçerli bir eposta giriniz!");

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Metin değiştikten sonra bir şeyler yapabilirsiniz
                // Burada doğrulama yapabilirsiniz
            }
        });



        //güçlü şifre kontrolü
        //(?=.[a-z]) küçük harf ;(?=.[A-Z]) büyük harf (?=.*\\d) rakam
        //Pattern.compile("^(?=.[a-z])(?=.[A-Z])(?=.*\\d).{8,}$").matcher(password).matches()
        binding.passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sing_up.setEnabled(false);
                // Metin değişmeden önce bir şeyler yapabilirsiniz
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Metin değiştiğinde bir şeyler yapabilirsiniz
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Metin değiştikten sonra bir şeyler yapabilirsiniz
                // Burada doğrulama yapabilirsiniz
                String password = editable.toString();
                if(Pattern.compile("^(?=.[a-z])(?=.[A-Z])(?=.*\\d).{8,}$").matcher(password).matches()){
                    clearPasswordError();
                    sing_up.setEnabled(true);
                }else{
                    setPasswordError("Güçlü bir şifre giriniz! Şifrenizde en az birer tane " +
                            "büyük harf, küçük harf, rakam ve özel karakter bulundurmalı " +
                            "ve 8 karakterden oluşmalıdır.");
                    sing_up.setEnabled(false);
                }
            }
        });

    }

    public void kayitOl(View view){
        String mail= binding.emailText.getText().toString();
        String password = binding.passwordText.getText().toString();
        String name = binding.nameText.getText().toString();


        if(mail.equals("") || password.equals("")){
            Toast.makeText(KayitEkrani.this,"Email ve sifre girinz", Toast.LENGTH_LONG).show();
        }else{
            auth.createUserWithEmailAndPassword(mail,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    // Kullanıcı başarıyla kaydedildiğinde Firestore'a kullanıcı bilgilerini kaydet
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("email", mail);
                        userData.put("name", name);
                        userData.put("password", password);

                        db.collection("users")
                                .document(user.getUid())
                                .set(userData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Firestore'a veri ekleme başarılı
                                        Toast.makeText(KayitEkrani.this, "Kullanıcı başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), GirisEkrani.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Firestore'a veri ekleme başarısız
                                        Toast.makeText(KayitEkrani.this, "Kullanıcı bilgileri kaydedilirken bir hata oluştu", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    Intent intent = new Intent(getApplicationContext(),GirisEkrani.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(KayitEkrani.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                }
            });
        }


    }

    private void setEmailError(String errorMessage) {
        singup_hata.setText(errorMessage);
        singup_hata.setVisibility(View.VISIBLE);
        isEmailErrorShown = true;
    }

    private void clearEmailError() {
        singup_hata.setText("");
        singup_hata.setVisibility(View.GONE);
        isEmailErrorShown = false;
    }
    private void setPasswordError(String errorMessage) {
        password_error.setText(errorMessage);
        password_error.setVisibility(View.VISIBLE);
        isPasswordErrorShown = true;
    }

    private void clearPasswordError() {
        password_error.setText("");
        password_error.setVisibility(View.GONE);
        isPasswordErrorShown = false;
    }
}