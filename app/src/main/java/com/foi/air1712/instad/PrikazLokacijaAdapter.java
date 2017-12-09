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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darko on 9.12.2017..
 */

public class PrikazLokacijaAdapter extends RecyclerView.Adapter<PrikazLokacijaAdapter.LokacijaViewHolder>{
    private final List<Lokacije> lokacije;
    private final Context context;
    List<String> favoriti;

    public PrikazLokacijaAdapter(List<Lokacije> lokacije, Context context) {
        this.lokacije = lokacije;
        this.context = context;
        favoriti = ucitajPodatke();
    }

    private List<String> ucitajPodatke() {
        List<String> podatki = new ArrayList<>();
        podatki.add("Bard Varaždin");
        podatki.add("Aerodrom Varaždin");

        return podatki;
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
                String naziv = holder.naziv.getText().toString();
                if (favoriti.contains(naziv)){
                    favoriti.remove(naziv);
                    holder.slika.setImageResource(R.drawable.heart_blank);
                }else{
                    holder.slika.setImageResource(R.drawable.heart_full);
                    favoriti.add(naziv);
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
