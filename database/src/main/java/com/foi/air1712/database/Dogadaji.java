package com.foi.air1712.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by Nikola on 29.10.2017..
 */

@Table(database = MainDatabase.class)
public class Dogadaji extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    int id;

    @Column String adresa;

    @Column String naziv;

    @Column String hash;

    @Column String slika;

    @Column String opis;

    @Column String longitude;

    @Column String latitude;

    @Column String datum_kraj;

    @Column int ajdi;

    @Column String objekt;

    @Column String url;

    @Column String datum_pocetka;

    public Dogadaji() {
    }

    public Dogadaji(String adresa, String naziv, String hash, String slika, String opis, String longitude, String latitude, String datum_kraj, int ajdi, String objekt, String url, String datum_pocetka) {
        this.adresa = adresa;
        this.naziv = naziv;
        this.hash = hash;
        this.slika = slika;
        this.opis = opis;
        this.longitude = longitude;
        this.latitude = latitude;
        this.datum_kraj = datum_kraj;
        this.ajdi = ajdi;
        this.objekt = objekt;
        this.url = url;
        this.datum_pocetka = datum_pocetka;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDatum_kraj() {
        return datum_kraj;
    }

    public void setDatum_kraj(String datum_kraj) {
        this.datum_kraj = datum_kraj;
    }

    public int getAjdi() {
        return ajdi;
    }

    public void setAjdi(int ajdi) {
        this.ajdi = ajdi;
    }

    public String getObjekt() {
        return objekt;
    }

    public void setObjekt(String objekt) {
        this.objekt = objekt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatum_pocetka() {
        return datum_pocetka;
    }

    public void setDatum_pocetka(String datum_pocetka) {
        this.datum_pocetka = datum_pocetka;
    }

    public static List<Dogadaji> getAll(){
        return SQLite.select().from(Dogadaji.class).queryList();
    }

    @Override
    public String toString()
    {
        return "ClassPojo [adresa = "+adresa+", naziv = "+naziv+", hash = "+hash+", slika = "+slika+", opis = "+opis+", longitude = "+longitude+", latitude = "+latitude+", datum_kraj = "+datum_kraj+", ajdi = "+ajdi+", objekt = "+objekt+", url = "+url+", datum_pocetka = "+datum_pocetka+"]";
    }
}
