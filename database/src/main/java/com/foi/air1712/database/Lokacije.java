package com.foi.air1712.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by Nikola on 9.12.2017..
 */


@Table(database = MainDatabase.class)
public class Lokacije extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    int id_lokacije;

    @Column String adresa;

    @Column String naziv;

    @Column String latitude;

    @Column String longitude;

    public Lokacije() {
    }

    public Lokacije(int id, String adresa, String naziv, String latitude, String longitude) {
        this.id_lokacije = id;
        this.adresa = adresa;
        this.naziv = naziv;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id_lokacije;
    }

    public void setId(int id) {
        this.id_lokacije = id;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public static List<Lokacije> getAll(){
        return SQLite.select().from(Lokacije.class).queryList();
    }

    public static void deleteAllLokacije(){
        Delete.table(Lokacije.class);

    }

    @Override
    public String toString() {
        return "Lokacije{" +
                "id=" + id_lokacije +
                ", adresa='" + adresa + '\'' +
                ", naziv='" + naziv + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
