package com.foi.air1712.instad.fragmenti;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foi.air1712.core.DataLoadedListener;
import com.foi.air1712.core.DataLoader;
import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.instad.PrikazSvihAdapter;
import com.foi.air1712.instad.R;
import com.foi.air1712.instad.loaders.DbDataLoader;
import com.foi.air1712.instad.loaders.WsDataLoader;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;

/**
 * Created by Nikola on 7.12.2017..
 */


public class PrikazSvihFragment extends Fragment implements DataLoadedListener {

    private RecyclerView rv;
    private ArrayList<Dogadaji> DohvaceniDogadaji;

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
    public void onDataLoaded(ArrayList<Dogadaji> dogadaji) {
        DohvaceniDogadaji = dogadaji;
        initializeAdapter();

    }

    private void initializeAdapter(){
        PrikazSvihAdapter adapter = new PrikazSvihAdapter(DohvaceniDogadaji);
        rv.setAdapter(adapter);
    }
}