package com.foi.air1712.instad.fragmenti;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.instad.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nikola on 9.12.2017..
 */


public class DetaljniPrikazFragment extends Fragment{

    private ImageView dogadajSlika;
    private TextView dogadajNaziv;
    private TextView dogadajOpis;
    private TextView dogadajDatumP;
    private TextView dogadajDatumK;
    private Dogadaji dogadajDohvacen = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detaljni_prikaz, container, false);

        dogadajSlika=(ImageView)rootView.findViewById(R.id.dogadaj_slika);
        dogadajNaziv=(TextView)rootView.findViewById(R.id.dogadaj_naziv);
        dogadajOpis=(TextView)rootView.findViewById(R.id.dogadaj_opis);
        dogadajDatumP=(TextView)rootView.findViewById(R.id.dogadaj_datump);
        dogadajDatumK=(TextView)rootView.findViewById(R.id.dogadaj_datumk);

        Bundle bundle = getArguments();
        ArrayList<Dogadaji> listaDohvacena = bundle.getParcelableArrayList("event");
        dogadajDohvacen = listaDohvacena.get(0);

        Context context=dogadajSlika.getContext();
        Picasso.with(context).load(dogadajDohvacen.getSlika()).into(dogadajSlika);
        dogadajNaziv.setText(dogadajDohvacen.getNaziv());
        dogadajOpis.setText(dogadajDohvacen.getOpis());
        dogadajDatumP.setText("Početak: " + lijepiDatum(dogadajDohvacen.getDatum_pocetka()));
        dogadajDatumK.setText("Kraj: " + lijepiDatum(dogadajDohvacen.getDatum_kraj()));



        return rootView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private String lijepiDatum(String datum){
        String dtStart = datum;
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

        return formatiraniDatum;
    }


}
