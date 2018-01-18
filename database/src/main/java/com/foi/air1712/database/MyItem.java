package com.foi.air1712.database;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Nikola on 18.1.2018..
 */

public class MyItem implements ClusterItem {

    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private String urlSlike;
    private String objekt;
    private Dogadaji dohvacenDogadaj;


    public MyItem(LatLng pozicija, Dogadaji dohvacen) {
        mPosition = pozicija;
        dohvacenDogadaj = dohvacen;
        mTitle = dohvacen.getNaziv();
        mSnippet = dohvacen.getOpis();

        urlSlike = dohvacen.getSlika();
        objekt = dohvacen.getObjekt();

    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public String getUrlSlike(){
        return urlSlike;
    }

    public String getObjektDogadaja(){
        return objekt;
    }

}
