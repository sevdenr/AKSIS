package com.example.tasarim_calismasi_2.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasarim_calismasi_2.Model.Kitap;
import com.example.tasarim_calismasi_2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class KitapAdapter extends RecyclerView.Adapter<KitapAdapter.KitapViewHolder> {
    ArrayList<Kitap> kitapArrayList;
    private Context context;

    DatabaseReference databaseReference;
    private FirebaseFirestore firebaseFirestore;

    public KitapAdapter(ArrayList<Kitap> kitapArrayList,Context context) {
        this.kitapArrayList = kitapArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public KitapAdapter.KitapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kitap, parent, false);
        return new KitapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KitapAdapter.KitapViewHolder holder, int position) {
        Kitap kitap = kitapArrayList.get(position);

        holder.kitapAdi.setText(kitapArrayList.get(position).kitapAd);
        holder.blokAdi.setText(kitapArrayList.get(position).blokId);
        holder.rafAdi.setText(kitapArrayList.get(position).rafId);
        holder.salonAdi.setText(kitapArrayList.get(position).salonId);
        holder.yazarAdi.setText(kitapArrayList.get(position).yazarId);
        holder.kitapKodu.setText(kitapArrayList.get(position).kitapKodu);


        if (!kitap.kitapDurumu) {
            holder.kitapDurumu.setText("Kitap mevcut");
        } else {
            holder.kitapDurumu.setText("Kitap mevcut değil");
        }
    }

    @Override
    public int getItemCount() {
        if (kitapArrayList != null) {
            return kitapArrayList.size();
        } else {
            return 0;
        }
    }

    public static class KitapViewHolder extends RecyclerView.ViewHolder {
        TextView kitapDurumu, kitapKodu, kitapAdi, rafAdi,blokAdi,salonAdi, yazarAdi;


        public KitapViewHolder(View itemView) {
            super(itemView);
            kitapDurumu = itemView.findViewById(R.id.kitapDurumu);
            kitapKodu = itemView.findViewById(R.id.kitapKodu);
            kitapAdi = itemView.findViewById(R.id.kitapAdi);
            rafAdi = itemView.findViewById(R.id.rafAdi);
            salonAdi = itemView.findViewById(R.id.salonAdi);
            blokAdi = itemView.findViewById(R.id.blokAdi);
            yazarAdi = itemView.findViewById(R.id.yazarAdi);
        }
    }
}
