package com.example.tasarim_calismasi_2.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasarim_calismasi_2.R;
import com.example.tasarim_calismasi_2.databinding.ActivityGirisEkraniBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class GirisEkrani extends AppCompatActivity {

    private TextView singup_hata;
    private  TextView password_error;


    private boolean isEmailErrorShown = false;

    private boolean isPasswordErrorShown = false;
    private ActivityGirisEkraniBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //find by Id komutu ile tanımlama yapmamak için binding kullanıyoruz
        binding = ActivityGirisEkraniBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        auth= FirebaseAuth.getInstance();

        String mail= binding.emailText.getText().toString();
        String password = binding.passwordText.getText().toString();
        TextView email_hata = findViewById(R.id.email_hata);

        TextView sifreUnuttum =findViewById(R.id.sifreUnuttum);
        sifreUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SifreUnuttum();
            }
        });

    }
    public void  UyeOl(View view){
        Intent intent =new Intent(GirisEkrani.this,KayitEkrani.class);
        startActivity(intent);

    }
    public void  GirisYap(View view){
        String mail= binding.emailText.getText().toString();
        String password = binding.passwordText.getText().toString();
        if (mail.isEmpty() || password.isEmpty()) {
            Toast.makeText(GirisEkrani.this, "Lütfen e-mail ve şifrenizi giriniz.", Toast.LENGTH_SHORT).show();
        }
        else {
            auth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(GirisEkrani.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(GirisEkrani.this,"Hoşgeldiniz",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GirisEkrani.this, AnaSayfa.class));
                        finish();
                    }
                    else {
                        Exception exception = task.getException();
                        //Hatalı şifre girildiğinde
                        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            // Geçersiz şifre
                            Toast.makeText(GirisEkrani.this, "Geçersiz şifre. Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
                        }
                        else if (exception instanceof FirebaseAuthInvalidUserException) {
                            // Kullanıcı bulunamadı
                            Toast.makeText(GirisEkrani.this, "Kullanıcı bulunamadı. E-mail adresinizi kontrol ediniz.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // Diğer hatalar
                            Toast.makeText(GirisEkrani.this, "Kimlik doğrulama başarısız oldu. Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
    public void SifreUnuttum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GirisEkrani.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
        EditText emailBox = dialogView.findViewById(R.id.emailBox);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = emailBox.getText().toString().trim();
                if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(GirisEkrani.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(GirisEkrani.this, "Chech your email", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(GirisEkrani.this, "Unable to send, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialogView.findViewById(R.id.btnCanel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (dialog.getWindow()!= null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.show();
    }

}