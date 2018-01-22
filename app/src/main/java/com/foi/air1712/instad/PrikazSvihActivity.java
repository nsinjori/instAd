package com.foi.air1712.instad;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nikola on 29.10.2017..
 */

public class PrikazSvihActivity extends AppCompatActivity implements DataLoadedListener {

    private RecyclerView rv;
    private ArrayList<Dogadaji> DohvaceniDogadaji;
    private SwipeRefreshLayout swipeContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_pregledsvih);
        FlowManager.init(new FlowConfig.Builder(this).build());

        //za pull to refresh
        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                obrisiDohvati();
            }
        });

        //za risajklera
        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        //dohvaćanje podataka
        DataLoader dataLoader;
        if(Dogadaji.getAll().isEmpty()){
            System.out.println("Loading web data");
            //Toast.makeText(this, "Z neta uzimam", Toast.LENGTH_SHORT).show();
            dataLoader = new WsDataLoader();
        } else {
            System.out.println("Loading local data");
            //Toast.makeText(this, "Lokalno uzimam", Toast.LENGTH_SHORT).show();
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
        swipeContainer.setRefreshing(false);
    }

    private void initializeAdapter(){
        //PrikazSvihAdapter adapter = new PrikazSvihAdapter(DohvaceniDogadaji, getApplicationContext());
        //rv.setAdapter(adapter);

        ArrayList<Dogadaji> azuriraniDogadaji = new ArrayList<Dogadaji>();

        for(Dogadaji dogadaj : DohvaceniDogadaji){
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
                azuriraniDogadaji.add(dogadaj);
                System.out.println("918 - " + dogadaj);
            }
            /** kraj provjere datum **/
        }

        PrikazSvihAdapter adapter = new PrikazSvihAdapter(azuriraniDogadaji, this);
        rv.setAdapter(adapter);




    }

    public void obrisiDohvati(){
        Dogadaji.deleteAllDogadaji();
        Lokacije.deleteAllLokacije();
        DohvaceniDogadaji.clear();
        rv.setAdapter(null);
        //Toast.makeText(getActivity(),"Briseeem", Toast.LENGTH_SHORT).show();
        rv.removeAllViewsInLayout();
        DataLoader dataLoader;
        dataLoader = new WsDataLoader();
        dataLoader.loadData(this);


        //swipeContainer.setRefreshing(false);

    }


}
