package com.foi.air1712.instad.loaders;

import com.foi.air1712.core.AirWebServiceHandler;
import com.foi.air1712.core.DataLoadedListener;
import com.foi.air1712.core.DataLoader;
import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.webservice.AirWebServiceCaller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikola on 29.10.2017..
 */

public class WsDataLoader extends DataLoader {
    private boolean dogadajiArrived = false;

    @Override
    public void loadData(DataLoadedListener dataLoadedListener) {
        super.loadData(dataLoadedListener);

        AirWebServiceCaller dogadajiWs = new AirWebServiceCaller(dogadajiHandler);
        dogadajiWs.getAll("getAll", Dogadaji.class);


    }

    //TODO: As an exercise, change the architecture so that you have only one AirWebServiceHandler

    AirWebServiceHandler dogadajiHandler = new AirWebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok, long timestamp) {
            if(ok){
                List<Dogadaji> dogadaji = (List<Dogadaji>) result;
                for(Dogadaji dogadajulisti : dogadaji){
                    dogadajulisti.save();
                }
                dogadajiArrived = true;
                checkDataArrival();
            }
        }
    };


    private void checkDataArrival(){
        if(dogadajiArrived){
            mDataLoadedListener.onDataLoaded((ArrayList<Dogadaji>) Dogadaji.getAll());
        }
    }
}

