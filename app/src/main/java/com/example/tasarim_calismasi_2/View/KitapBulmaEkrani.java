package com.example.tasarim_calismasi_2.View;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tasarim_calismasi_2.Adapter.KitapAdapter;
import com.example.tasarim_calismasi_2.Model.Kitap;
import com.example.tasarim_calismasi_2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class KitapBulmaEkrani extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    KitapAdapter kitapAdapter;
    ArrayList<Kitap> kitapArrayList;
    EditText editText;
    String KitapKodu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitap_bulma_ekrani);

        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference kitapRef = firebaseFirestore.collection("Kitaplar");

        RecyclerView recyclerView = findViewById(R.id.KitapBulRecyclerView);
        int spanCount=1;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager( layoutManager);


        kitapArrayList = new ArrayList<>();
        kitapAdapter = new KitapAdapter(kitapArrayList, this);
        recyclerView.setAdapter(kitapAdapter);

        editText = findViewById(R.id.editTextNumber2);
        KitapKodu = editText.getText().toString();

        Button oduncAl= findViewById(R.id.button4);
        oduncAl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KitapKodu = editText.getText().toString();
                OduncAl(KitapKodu);
            }
        });

        Button bul= findViewById(R.id.button3);
        bul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KitapKodu = editText.getText().toString();
                getData(KitapKodu);
            }
        });

    }


    private void getData(String KitapKodu) {

        firebaseFirestore.collection("Kitaplar").whereEqualTo("kitapKodu", KitapKodu)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(KitapBulmaEkrani.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if (value != null) {
                            kitapArrayList.clear();
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                Log.d("Firestore", "Snapshot Data: " + snapshot.getData());
                                Map<String, Object> data = snapshot.getData();
                                if (data != null) {
                                    Log.d("Firestore", "Data Map: " + data.toString());

                                    String kitapAd = (String) data.get("kitapAd");
                                    String kitapId= (String) data.get("kitapId");
                                    String blokId= (String) data.get("blokId");
                                    String rafId= (String) data.get("rafId");
                                    String salonId= (String) data.get("salonId");
                                    String yazarId= (String) data.get("yazarId");
                                    String kitapKodu= (String) data.get("kitapKodu");
                                    Boolean kitapDurumu= (Boolean) data.get("kitapDurumu");

                                    Kitap kitap = new Kitap(kitapAd, kitapId,blokId,rafId,salonId,yazarId,kitapKodu,kitapDurumu);
                                    kitapArrayList.add(kitap);
                                }
                            }
                            kitapAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    private void OduncAl(String kod) {
        firebaseFirestore.collection("Kitaplar").document(KitapKodu)
                .update("kitapDurumu", true)  // veya false olarak güncelleyin
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showSuccessDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Güncelleme başarısız
                        e.printStackTrace();
                    }
                });
    }
    private void showSuccessDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(KitapBulmaEkrani.this);
        builder.setTitle("Başarılı");
        builder.setMessage("Kitap ödünç alma işlemi başarılı olmuştur. 14 gün süreniz vardır.");
        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(KitapBulmaEkrani.this, AnaSayfa.class);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

}