package com.foi.air1712.instad;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.instad.fragmenti.DetaljniPrikazFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Darko on 11.1.2018..
 */

public class PrikazFavoriziranihAdapter extends RecyclerView.Adapter<PrikazFavoriziranihAdapter.DogadajViewHolder>{
    List<Dogadaji> dogadaji;
    Context context;

    public static class DogadajViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView objekt;
        TextView naziv;
        ImageView slika;

        DogadajViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            objekt = (TextView)itemView.findViewById(R.id.objektcv);
            naziv = (TextView)itemView.findViewById(R.id.nazivcv);
            slika = (ImageView)itemView.findViewById(R.id.slikacv);
        }
    }
    public PrikazFavoriziranihAdapter(List<Dogadaji> dogadaji, Context context){
        this.dogadaji = dogadaji;
        this.context = context;
        if (dogadaji.size()==0){
            Toast.makeText(context, "Nema događaja na mjestima koja su u Vašim favoritima!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PrikazFavoriziranihAdapter.DogadajViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dogadaj, viewGroup, false);
        PrikazFavoriziranihAdapter.DogadajViewHolder dvh = new PrikazFavoriziranihAdapter.DogadajViewHolder(v);
        return dvh;
    }
    @Override
    public void onBindViewHolder(PrikazFavoriziranihAdapter.DogadajViewHolder dogadajViewHolder, final int i) {
        String dtStart = dogadaji.get(i).getDatum_pocetka();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat izlFormat = new SimpleDateFormat("EEEE, d MMM yyyy");
        Date date = new Date();
        String formatiraniDatum = "";
        try {
            date = format.parse(dtStart);
            formatiraniDatum = izlFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dogadajViewHolder.objekt.setText(dogadaji.get(i).getObjekt() + "\n" + formatiraniDatum);
        dogadajViewHolder.naziv.setText(dogadaji.get(i).getNaziv());
        final Context context = dogadajViewHolder.slika.getContext();
        Picasso.with(context).load(dogadaji.get(i).getSlika()).into(dogadajViewHolder.slika);
        final int index=i+1;
        final ArrayList<Dogadaji> dogadajArrayList=new ArrayList<>();
        dogadajArrayList.add(dogadaji.get(i));
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            dogadajViewHolder.cv.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) context;
                    Fragment detaljniDogadaj = new DetaljniPrikazFragment();
                    Bundle bundle = new Bundle();
                    activity.getSupportFragmentManager().beginTransaction();
                    bundle.putParcelableArrayList("event", dogadajArrayList);
                    detaljniDogadaj.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame_layout, detaljniDogadaj).commit();
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return dogadaji.size();
    }
}
