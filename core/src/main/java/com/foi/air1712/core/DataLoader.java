package com.foi.air1712.core;

import com.foi.air1712.database.Dogadaji;

import java.util.ArrayList;

/**
 * Created by Nikola on 29.10.2017..
 */

public abstract class DataLoader {

    public ArrayList<Dogadaji> dogadaji;

    protected DataLoadedListener mDataLoadedListener;

    public void loadData(DataLoadedListener dataLoadedListener){
        this.mDataLoadedListener = dataLoadedListener;
    }

    public boolean dataLoaded(){
        if(dogadaji == null){
            return false;
        }
        else{
            return true;
        }
    }

}
