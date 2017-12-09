package com.foi.air1712.instad.fragmenti;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foi.air1712.core.DataLoadedListener;
import com.foi.air1712.core.DataLoader;
import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.database.Lokacije;
import com.foi.air1712.instad.PrikazLokacijaAdapter;
import com.foi.air1712.instad.R;
import com.foi.air1712.instad.loaders.DbDataLoader;
import com.foi.air1712.instad.loaders.WsDataLoader;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;

/**
 * Created by Nikola on 7.12.2017..
 */

public class PrikazFavoritiFragment extends Fragment implements DataLoadedListener{
    private RecyclerView rv;
    private ArrayList<Lokacije> listaLokacija;

    public static PrikazFavoritiFragment newInstance() {
        PrikazFavoritiFragment fragment = new PrikazFavoritiFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favoriti, container, false);

        FlowManager.init(new FlowConfig.Builder(this.getContext()).build());

        rv = (RecyclerView)rootView.findViewById(R.id.rv_lokacije);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        DataLoader dataLoader;
        if(Lokacije.getAll().isEmpty()){

            dataLoader = new WsDataLoader();
        }else{

            dataLoader = new DbDataLoader();
        }
        dataLoader.loadData(this);

        return rootView;
    }

    @Override
    public void onDataLoaded(ArrayList<Dogadaji> dogadaji, ArrayList<Lokacije> lokacije) {
        listaLokacija = lokacije;
        initializeAdapter();
    }

    private void initializeAdapter() {
        PrikazLokacijaAdapter adapter= new PrikazLokacijaAdapter(listaLokacija, getContext());
        rv.setAdapter(adapter);
    }
}