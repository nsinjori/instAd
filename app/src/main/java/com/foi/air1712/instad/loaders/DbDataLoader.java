package com.foi.air1712.instad.loaders;

import com.foi.air1712.core.DataLoadedListener;
import com.foi.air1712.core.DataLoader;
import com.foi.air1712.database.Dogadaji;

import java.util.ArrayList;

/**
 * Created by Nikola on 29.10.2017..
 */

public class DbDataLoader extends DataLoader {
    @Override
    public void loadData(DataLoadedListener dataLoadedListener) {
        super.loadData(dataLoadedListener);
        try{
            dogadaji = (ArrayList<Dogadaji>) Dogadaji.getAll();

            mDataLoadedListener.onDataLoaded(dogadaji);

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
