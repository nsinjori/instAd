package com.foi.air1712.instad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.foi.air1712.database.Dogadaji;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

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
        mListView.setAdapter(adapter);
    }
}
