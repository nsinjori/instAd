package com.foi.air1712.instad;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foi.air1712.database.Dogadaji;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nikola on 29.10.2017..
 */

public class PrikazSvihAdapter extends RecyclerView.Adapter<PrikazSvihAdapter.DogadajViewHolder> {


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

        List<Dogadaji> dogadaji;

    PrikazSvihAdapter(List<Dogadaji> dogadaji){
            this.dogadaji = dogadaji;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public DogadajViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dogadaj, viewGroup, false);
            DogadajViewHolder dvh = new DogadajViewHolder(v);
            return dvh;
        }

        @Override
        public void onBindViewHolder(DogadajViewHolder dogadajViewHolder, int i) {

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
        /*try {
            System.out.println("############# url: " + dogadaji.get(i).getSlika());
            InputStream input = new java.net.URL(dogadaji.get(i).getSlika()).openStream();
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeStream(input);
            personViewHolder.slika.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
            Context context = dogadajViewHolder.slika.getContext();
            Picasso.with(context).load(dogadaji.get(i).getSlika()).into(dogadajViewHolder.slika);
        }

        @Override
        public int getItemCount() {
            return dogadaji.size();
        }

    }
