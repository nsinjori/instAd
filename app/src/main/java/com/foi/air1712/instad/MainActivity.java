package com.foi.air1712.instad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.foi.air1712.core.DataLoadedListener;
import com.foi.air1712.core.DataLoader;
import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.instad.accountManagement.LoginActivity;
import com.foi.air1712.instad.fragmenti.DetaljniPrikazFragment;
import com.foi.air1712.instad.fragmenti.PrikazFavoritiFragment;
import com.foi.air1712.instad.fragmenti.PrikazFavoriziranihFragment;
import com.foi.air1712.instad.fragmenti.PrikazPostavkeFragment;
import com.foi.air1712.instad.fragmenti.PrikazSvihFragment;
import com.foi.air1712.instad.loaders.DbDataLoader;
import com.foi.air1712.instad.loaders.WsDataLoader;
import com.foi.air1712.webservice.AirWebServiceCaller;
import com.google.firebase.auth.FirebaseAuth;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        BottomNavViewHelper.disableShiftMode(bottomNavigationView);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = PrikazSvihFragment.newInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = PrikazFavoritiFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                selectedFragment = PrikazPostavkeFragment.newInstance();
                                break;
                            case R.id.action_item4:
                                selectedFragment = PrikazFavoriziranihFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, PrikazSvihFragment.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        String extra_adresa = extras.getString("extra_adresa");
        String extra_naziv = extras.getString("extra_naziv");
        String extra_hash = extras.getString("extra_hash");
        String extra_slika = extras.getString("extra_slika");
        String extra_opis = extras.getString("extra_opis");
        String extra_latitude = extras.getString("extra_latitude");
        String extra_longitude = extras.getString("extra_longitude");
        String extra_datumkraj = extras.getString("extra_datumkraj");
        String extra_datumpocetka = extras.getString("extra_datumpocetka");
        String extra_objekt = extras.getString("extra_datumpocetka");
        String extra_url = extras.getString("extra_datumpocetka");

        Dogadaji izNotifikacije = new Dogadaji();
        izNotifikacije.setAdresa(extra_adresa);
        izNotifikacije.setNaziv(extra_naziv);
        izNotifikacije.setHash(extra_hash);
        izNotifikacije.setSlika(extra_slika);
        izNotifikacije.setOpis(extra_opis);
        izNotifikacije.setLatitude(extra_latitude);
        izNotifikacije.setLongitude(extra_longitude);
        izNotifikacije.setDatum_kraj(extra_datumkraj);
        izNotifikacije.setDatum_pocetka(extra_datumpocetka);
        izNotifikacije.setObjekt(extra_objekt);
        izNotifikacije.setUrl(extra_url);

        ArrayList<Dogadaji> dogadajArrayList=new ArrayList<>();
        dogadajArrayList.add(izNotifikacije);

        Fragment detaljniDogadaj = new DetaljniPrikazFragment();
        Bundle bundle = new Bundle();
        this.getSupportFragmentManager().beginTransaction();
        bundle.putParcelableArrayList("event", dogadajArrayList);

        detaljniDogadaj.setArguments(bundle);

        this.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame_layout, detaljniDogadaj).commit();
    }
}
