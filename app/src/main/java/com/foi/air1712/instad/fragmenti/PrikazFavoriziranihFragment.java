package com.foi.air1712.instad.fragmenti;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.foi.air1712.core.DataLoadedListener;
import com.foi.air1712.core.DataLoader;
import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.database.Lokacije;
import com.foi.air1712.instad.PrikazFavoriziranihAdapter;
import com.foi.air1712.instad.R;
import com.foi.air1712.instad.loaders.DbDataLoader;
import com.foi.air1712.instad.loaders.WsDataLoader;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Darko on 10.1.2018..
 */

public class PrikazFavoriziranihFragment extends Fragment implements DataLoadedListener{
    private RecyclerView rv;
    private ArrayList<Dogadaji> DohvaceniDogadaji;

    public static Fragment newInstance() {
        PrikazFavoriziranihFragment prikazFavoriziranihFragment = new PrikazFavoriziranihFragment();
        return prikazFavoriziranihFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_favorizirani, container, false);
        FlowManager.init(new FlowConfig.Builder(this.getContext()).build());
        rv=(RecyclerView)rootView.findViewById(R.id.rv_favorizirani);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setHasFixedSize(true);
        DataLoader dataLoader;
        if(Dogadaji.getAll().isEmpty()){
            dataLoader = new WsDataLoader();
        } else { dataLoader = new DbDataLoader();
        }
        dataLoader.loadData(this);
        return rootView;
    }

    @Override
    public void onDataLoaded(ArrayList<Dogadaji> dogadaji, ArrayList<Lokacije> lokacije) {
        DohvaceniDogadaji = dogadaji;
        initializeAdapter();
    }
    private void initializeAdapter(){
        ArrayList<Dogadaji> azuriraniDogadaji = new ArrayList<Dogadaji>();
        for(Dogadaji dogadaj : DohvaceniDogadaji){
            String dtStart = dogadaj.getDatum_kraj();
            Date date = new Date();
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(dtStart);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(!date.before(new Date())&&dogadaj.getObjekt().equalsIgnoreCase("foi")){
                azuriraniDogadaji.add(dogadaj);
            }
        }
        PrikazFavoriziranihAdapter adapter = new PrikazFavoriziranihAdapter(azuriraniDogadaji, getContext());
        rv.setAdapter(adapter);
    }

}
