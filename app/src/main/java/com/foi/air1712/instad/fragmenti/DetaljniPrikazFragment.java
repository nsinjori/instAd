package com.foi.air1712.instad.fragmenti;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.instad.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nikola on 9.12.2017..
 */


public class DetaljniPrikazFragment extends Fragment implements OnMapReadyCallback {

    private ImageView dogadajSlika;
    private TextView dogadajNaziv;
    private TextView dogadajOpis;
    private TextView dogadajDatumP;
    private TextView dogadajDatumK;
    private Dogadaji dogadajDohvacen = null;

    private FrameLayout mapaPrikaz;
    private com.google.android.gms.maps.MapFragment mapFragment;
    private GoogleMap map = null;

    private Button uputeKarta;
    private Button webLink;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detaljni_prikaz, container, false);

        dogadajSlika=(ImageView)rootView.findViewById(R.id.dogadaj_slika);
        dogadajNaziv=(TextView)rootView.findViewById(R.id.dogadaj_naziv);
        dogadajOpis=(TextView)rootView.findViewById(R.id.dogadaj_opis);
        dogadajDatumP=(TextView)rootView.findViewById(R.id.dogadaj_datump);
        dogadajDatumK=(TextView)rootView.findViewById(R.id.dogadaj_datumk);

        uputeKarta=(Button)rootView.findViewById(R.id.btn_upute);
        webLink=(Button)rootView.findViewById(R.id.btn_web);

        Bundle bundle = getArguments();
        ArrayList<Dogadaji> listaDohvacena = bundle.getParcelableArrayList("event");
        dogadajDohvacen = listaDohvacena.get(0);

        Context context=dogadajSlika.getContext();
        Picasso.with(context).load(dogadajDohvacen.getSlika()).into(dogadajSlika);
        dogadajNaziv.setText(dogadajDohvacen.getNaziv());
        dogadajOpis.setText(dogadajDohvacen.getOpis());
        dogadajDatumP.setText("Početak: " + lijepiDatum(dogadajDohvacen.getDatum_pocetka()));
        dogadajDatumK.setText("Kraj: " + lijepiDatum(dogadajDohvacen.getDatum_kraj()));

        mapaPrikaz=(FrameLayout)rootView.findViewById(R.id.mapa_prikaz);
        //dodavanje google karte u layout

        mapFragment = new com.google.android.gms.maps.MapFragment();

        mapFragment.getMapAsync(this);

        getActivity().getFragmentManager().beginTransaction().add(R.id.mapa_prikaz, mapFragment).commit();

        mapaPrikaz.setFocusable(true);

        uputeKarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.google.com/maps/dir/?api=1&destination="+dogadajDohvacen.getLatitude()+","+dogadajDohvacen.getLongitude();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        webLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = dogadajDohvacen.getUrl();

                otvoriUrl(url);
            }
        });


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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        double dogadajLatitude = Double.parseDouble(dogadajDohvacen.getLatitude());
        double dogadajLongitude = Double.parseDouble(dogadajDohvacen.getLongitude());
        LatLng dogadajKoordinate = new LatLng(dogadajLatitude, dogadajLongitude);

        Marker marker=map.addMarker(new MarkerOptions()
                .title(dogadajDohvacen.getObjekt())
                //.snippet("Nekaj")
                .position(dogadajKoordinate));
        marker.showInfoWindow();
        //Pozicionira i zumirana lokaciju od koordinata
        CameraPosition cameraPosition = new CameraPosition.Builder().target(dogadajKoordinate).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void otvoriUrl(String url){
        if (!url.startsWith("http://") && !url.startsWith("https://")) url = "http://" + url;
        Uri uri = Uri.parse(url);
        Intent intent= new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }


}
