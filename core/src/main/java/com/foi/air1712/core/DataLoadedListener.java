package com.foi.air1712.core;

import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.database.Lokacije;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Nikola on 29.10.2017..
 */

public interface DataLoadedListener {
    void onDataLoaded(ArrayList<Dogadaji> dogadaji, ArrayList<Lokacije> lokacije);
}
