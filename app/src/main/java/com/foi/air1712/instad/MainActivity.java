package com.foi.air1712.instad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.foi.air1712.core.DataLoadedListener;
import com.foi.air1712.core.DataLoader;
import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.instad.loaders.DbDataLoader;
import com.foi.air1712.instad.loaders.WsDataLoader;
import com.foi.air1712.webservice.AirWebServiceCaller;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//public class MainActivity extends AppCompatActivity {
public class MainActivity extends AppCompatActivity implements DataLoadedListener {

        @BindView(R.id.events_list)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    @OnClick(R.id.test_button)
    public void buttonClicked(View view){
        /*
        //Provjera dal web servisi rade
        AirWebServiceCaller webServiceCaller = new AirWebServiceCaller();
        webServiceCaller.getAll("getAll", Dogadaji.class);



        if(SQLite.select().from(Dogadaji.class).queryList().isEmpty()){
            System.out.println("Ovo ide kad je lokalna baza prazna");
            MockPodaci.writeAll();
        }

        final List<Dogadaji> dogadaji = SQLite.select().from(Dogadaji.class).queryList();
        String[] listItems = new String[dogadaji.size()];
        for(int i = 0; i < dogadaji.size(); i++){
            listItems[i] = dogadaji.get(i).getObjekt();
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);*/

        DataLoader dataLoader;
        if(Dogadaji.getAll().isEmpty()){
            System.out.println("Dohvacanje s weba");
            Toast.makeText(this, "Z neta uzimam", Toast.LENGTH_SHORT).show();
            dataLoader = new WsDataLoader();
        } else {
            System.out.println("Dohvacanje lokalno");
            Toast.makeText(this, "Lokalno uzimam", Toast.LENGTH_SHORT).show();
            dataLoader = new DbDataLoader();
        }
        dataLoader.loadData(this);
    }

    @Override
    public void onDataLoaded(ArrayList<Dogadaji> dogadaji) {
        System.out.println("Data is here... ");
        String[] listItems = new String[dogadaji.size()];

        for (int i = 0; i < dogadaji.size(); i++) {
            listItems[i] = dogadaji.get(i).getObjekt();
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);
    }
}
