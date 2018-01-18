package com.foi.air1712.instad.fragmenti;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foi.air1712.core.DataLoadedListener;
import com.foi.air1712.core.DataLoader;
import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.database.Lokacije;
import com.foi.air1712.instad.PrikazSvihAdapter;
import com.foi.air1712.instad.R;
import com.foi.air1712.instad.loaders.DbDataLoader;
import com.foi.air1712.instad.loaders.WsDataLoader;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Nikola on 7.12.2017..
 */


public class PrikazSvihFragment extends Fragment implements DataLoadedListener {

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rv;
    private ArrayList<Dogadaji> DohvaceniDogadaji;
    private ArrayList<Lokacije> dohvaceneLokacije;

    public static PrikazSvihFragment newInstance() {
        PrikazSvihFragment fragment = new PrikazSvihFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView =  inflater.inflate(R.layout.fragment_prikaz_svih, container, false);

        FlowManager.init(new FlowConfig.Builder(this.getContext()).build());

        //za pull to refresh
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                obrisiDohvati();
            }
        });

        //za risajklera
        rv=(RecyclerView)rootView.findViewById(R.id.rv_fragment);
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        //dohvaćanje podataka
        DataLoader dataLoader;
        if(Dogadaji.getAll().isEmpty()){
            System.out.println("Loading web data");
            Toast.makeText(this.getContext(), "Z neta uzimam", Toast.LENGTH_SHORT).show();
            dataLoader = new WsDataLoader();
        } else {
            System.out.println("Loading local data");
            Toast.makeText(this.getContext(), "Lokalno uzimam", Toast.LENGTH_SHORT).show();
            dataLoader = new DbDataLoader();
        }
        dataLoader.loadData(this);


        return rootView;



        //return inflater.inflate(R.layout.fragment_prikaz_svih, container, false);

    }

    @Override
    public void onDataLoaded(ArrayList<Dogadaji> dogadaji, ArrayList<Lokacije> lokacije) {
        DohvaceniDogadaji = dogadaji;
        dohvaceneLokacije = lokacije;   
        initializeAdapter();

        System.out.println("Dohvacene lokacije z baze/firebase --> " + dohvaceneLokacije.size());
        swipeContainer.setRefreshing(false);

    }

    private void initializeAdapter(){
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

        /*
        Set<Dogadaji> hs = new HashSet<>();
        hs.addAll(azuriraniDogadaji);
        azuriraniDogadaji.clear();
        azuriraniDogadaji.addAll(hs);
        */

        PrikazSvihAdapter adapter = new PrikazSvihAdapter(azuriraniDogadaji, getContext());
        rv.setAdapter(adapter);
    }

    public void obrisiDohvati(){
        Dogadaji.deleteAllDogadaji();
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