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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Darko on 10.1.2018..
 */

public class PrikazFavoriziranihFragment extends Fragment implements DataLoadedListener{
    private RecyclerView rv;
    private ArrayList<Dogadaji> DohvaceniDogadaji;
    private ArrayList<String> favoriti=new ArrayList<>();

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
        ucitajPodatke();
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
            if(!date.before(new Date())&&favoriti.contains(dogadaj.getObjekt())){
                azuriraniDogadaji.add(dogadaj);
            }
        }
        PrikazFavoriziranihAdapter adapter = new PrikazFavoriziranihAdapter(azuriraniDogadaji, getContext());
        rv.setAdapter(adapter);
    }
    void ucitajPodatke() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = database.getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("favs");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot!=null) {
                    Map<String, String> mapData = (Map) snapshot.getValue();
                    if(mapData!=null) {
                        for (Map.Entry<String, String> entry : mapData.entrySet()) {
                            String fav = entry.getKey().toString();
                            if (!favoriti.contains(fav))
                                favoriti.add(fav);
                        }
                    }
                    initializeAdapter();
                }else{
                    System.out.println("Nema favorita!");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }
}
