package com.foi.air1712.instad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.foi.air1712.core.DataLoadedListener;
import com.foi.air1712.core.DataLoader;
import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.database.Lokacije;
import com.foi.air1712.instad.loaders.DbDataLoader;
import com.foi.air1712.instad.loaders.WsDataLoader;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;

/**
 * Created by Nikola on 29.10.2017..
 */

public class PrikazSvihActivity extends AppCompatActivity implements DataLoadedListener {

    private RecyclerView rv;
    private ArrayList<Dogadaji> DohvaceniDogadaji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_pregledsvih);
        FlowManager.init(new FlowConfig.Builder(this).build());

        //za risajklera
        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        //dohvaćanje podataka
        DataLoader dataLoader;
        if(Dogadaji.getAll().isEmpty()){
            System.out.println("Loading web data");
            Toast.makeText(this, "Z neta uzimam", Toast.LENGTH_SHORT).show();
            dataLoader = new WsDataLoader();
        } else {
            System.out.println("Loading local data");
            Toast.makeText(this, "Lokalno uzimam", Toast.LENGTH_SHORT).show();
            dataLoader = new DbDataLoader();
        }
        dataLoader.loadData(this);

    }


    @Override
    public void onDataLoaded(ArrayList<Dogadaji> dogadaji, ArrayList<Lokacije> lokacije) {
        DohvaceniDogadaji = dogadaji;

        System.out.println("Data is here... ");

        /*
        String[] listItems = new String[dogadaji.size()];

        for (int i = 0; i < dogadaji.size(); i++) {
            listItems[i] = "Objekt: " + dogadaji.get(i).getObjekt() +
                    ", \nAdresa: " + dogadaji.get(i).getAdresa() +
                    ", \nPočetak: " + dogadaji.get(i).getDatum_pocetka()+
                    ", \nLatitude: " + dogadaji.get(i).getLatitude()+
                    ", \nLongitude: " + dogadaji.get(i).getLongitude()+
                    ", \nOpis: " + dogadaji.get(i).getOpis();
        }
*/
        initializeAdapter();
    }

    private void initializeAdapter(){
        PrikazSvihAdapter adapter = new PrikazSvihAdapter(DohvaceniDogadaji, getApplicationContext());
        rv.setAdapter(adapter);
    }
}
