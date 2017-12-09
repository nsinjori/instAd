package com.foi.air1712.instad;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foi.air1712.database.Lokacije;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Darko on 9.12.2017..
 */
public class PrikazLokacijaAdapter extends RecyclerView.Adapter<PrikazLokacijaAdapter.LokacijaViewHolder>{
    private final List<Lokacije> lokacije;
    private final Context context;
    List<String> favoriti = new LinkedList<>();

    public PrikazLokacijaAdapter(List<Lokacije> lokacije, Context context) {
        this.lokacije = lokacije;
        this.context = context;
        ucitajPodatke();
    }

    void ucitajPodatke() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = database.getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("favs");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot!=null) {
                    Map<String, String> mapData = (Map) snapshot.getValue();
                    if(mapData!=null) {
                        for (Map.Entry<String, String> entry : mapData.entrySet()) {
                            String fav = entry.getKey().toString();
                            if (!favoriti.contains(fav))
                                favoriti.add(fav);
                        }
                    }
                    notifyDataSetChanged();
                }else{
                    System.out.println("Nema favorita!");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

        });
    }

    public static class LokacijaViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView naziv;
        TextView adresa;
        ImageView slika;

        public LokacijaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_lokacija);
            naziv = (TextView)itemView.findViewById(R.id.cv_lokacija_naziv);
            adresa = (TextView)itemView.findViewById(R.id.cv_adresa_lokacije);
            slika = (ImageView)itemView.findViewById(R.id.cv_lokacija_slika);
        }
    }
    @Override
    public LokacijaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lokacija,parent,false);
        LokacijaViewHolder lokacijaViewHolder = new LokacijaViewHolder(v);
        return lokacijaViewHolder;
    }

    @Override
    public void onBindViewHolder(final LokacijaViewHolder holder, final int position) {
        String nazivLokacije = lokacije.get(position).getNaziv();
        String adresaLokacije = lokacije.get(position).getAdresa();
        holder.naziv.setText(nazivLokacije);
        holder.adresa.setText(adresaLokacije);
        if (favoriti.contains(nazivLokacije)){
            holder.slika.setImageResource(R.drawable.heart_full);
        }else
            holder.slika.setImageResource(R.drawable.heart_blank);

        holder.slika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference rf = database.getReference();
                String naziv = holder.naziv.getText().toString();
                if (favoriti.contains(naziv)){
                    favoriti.remove(naziv);
                    holder.slika.setImageResource(R.drawable.heart_blank);
                    rf.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favs").child(naziv).removeValue();
                }else{
                    holder.slika.setImageResource(R.drawable.heart_full);
                    favoriti.add(naziv);
                    rf.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favs").child(naziv).setValue("dodan");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return lokacije.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
