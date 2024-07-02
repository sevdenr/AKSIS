package com.example.tasarim_calismasi_2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasarim_calismasi_2.Model.Masa;
import com.example.tasarim_calismasi_2.Model.Salon;
import com.example.tasarim_calismasi_2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MasaAdapter extends RecyclerView.Adapter<MasaAdapter.MasaViewHolder> {
    private ArrayList<Masa> masaList;
    private Context context;
    private MasaAdapter.OnItemClickListener onItemClickListener;
    DatabaseReference databaseReference;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public MasaAdapter(ArrayList<Masa> masaList,Context context) {
        this.masaList = masaList;
        this.context= context;

    }

    @NonNull
    @Override
    public MasaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_masa, parent, false);
        return new MasaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MasaViewHolder holder, int position) {
        Masa masa = masaList.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference("Salon");

        databaseReference.child(masa.getSalonId()).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {

                        Salon salon = dataSnapshot.getValue(Salon.class);
                        holder.salonAdi.setText(salon.getSalonAdi());

                    }


                });
        holder.masaAdi.setText(masa.getMasaId());

        if (masa.isMasaDurumu()) {
            holder.masaDurumu.setText("Dolu");
        } else {
            holder.masaDurumu.setText("Boş");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(masa);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (masaList != null) {
            return masaList.size();
        } else {
            return 0;
        }
    }

    public static class MasaViewHolder extends RecyclerView.ViewHolder {
        TextView masaDurumu;
        TextView salonAdi;
        TextView masaAdi;

        public MasaViewHolder(@NonNull View itemView) {
            super(itemView);
            masaDurumu = itemView.findViewById(R.id.textView14);
            salonAdi = itemView.findViewById(R.id.textView13);
            masaAdi = itemView.findViewById(R.id.textView12);
        }
    }
    public interface  OnItemClickListener {
        void onItemClick(Masa masa);
    }

}
