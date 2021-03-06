package com.foi.air1712.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nikola on 29.10.2017..
 */

@Table(database = MainDatabase.class)
public class Dogadaji extends BaseModel implements Parcelable {

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

    protected Dogadaji(Parcel in) {
        id = in.readInt();
        adresa = in.readString();
        naziv = in.readString();
        hash = in.readString();
        slika = in.readString();
        opis = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        datum_kraj = in.readString();
        ajdi = in.readInt();
        objekt = in.readString();
        url = in.readString();
        datum_pocetka = in.readString();
    }

    public static final Parcelable.Creator<Dogadaji> CREATOR = new Parcelable.Creator<Dogadaji>() {
        @Override
        public Dogadaji createFromParcel(Parcel in) {
            return new Dogadaji(in);
        }

        @Override
        public Dogadaji[] newArray(int size) {
            return new Dogadaji[size];
        }
    };

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

    public static Dogadaji getDogadajByHash(String hash){
        Dogadaji d = new Dogadaji();
        for(Dogadaji dogadaj:getAll()){
            if(dogadaj.getHash().equals(hash)) d = dogadaj;
        }
        return d;
    }

    public static List<Dogadaji> getAll(){
        return SQLite.select().from(Dogadaji.class).queryList();
    }

    public static ArrayList<Dogadaji> dajSveAzuriraneDogadaje(){
        ArrayList<Dogadaji> dohvaceni = new ArrayList<Dogadaji>();

        for(Dogadaji dogadaj : SQLite.select().from(Dogadaji.class).queryList()){
            /** proba za datume da ih ne uzima ak su stari**/
            String dtStart = dogadaj.getDatum_kraj();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try {
                date = format.parse(dtStart);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date danas = new Date();

            if(!date.before(danas)){
                dohvaceni.add(dogadaj);
            }
            /** kraj provjere datum **/
        }
        return dohvaceni;
    }

    public static void deleteAllDogadaji(){
        Delete.table(Dogadaji.class);

    }

    @Override
    public String toString()
    {
        return "ClassPojo [adresa = "+adresa+", naziv = "+naziv+", hash = "+hash+", slika = "+slika+", opis = "+opis+", longitude = "+longitude+", latitude = "+latitude+", datum_kraj = "+datum_kraj+", ajdi = "+ajdi+", objekt = "+objekt+", url = "+url+", datum_pocetka = "+datum_pocetka+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(adresa);
        dest.writeString(naziv);
        dest.writeString(hash);
        dest.writeString(slika);
        dest.writeString(opis);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(datum_kraj);
        dest.writeString(datum_pocetka);
        dest.writeString(objekt);
        dest.writeString(url);
        dest.writeInt(ajdi);

    }

    public void readFromParcel(Parcel parcel) {
        this.adresa = parcel.readString();
        this.naziv = parcel.readString();
        this.hash = parcel.readString();
        this.slika = parcel.readString();
        this.opis = parcel.readString();
        this.longitude = parcel.readString();
        this.latitude = parcel.readString();
        this.datum_kraj = parcel.readString();
        this.datum_pocetka = parcel.readString();
        this.objekt = parcel.readString();
        this.url = parcel.readString();
        this.ajdi = parcel.readInt();

    }
}
