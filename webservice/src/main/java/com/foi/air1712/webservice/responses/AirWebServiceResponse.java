package com.foi.air1712.webservice.responses;

import com.foi.air1712.database.Dogadaji;

/**
 * Created by Nikola on 29.10.2017..
 */

public class AirWebServiceResponse {
    private Dogadaji[] items;

    public Dogadaji[] getItems ()
    {
        return items;
    }

    public void setItems (Dogadaji[] items)
    {
        this.items = items;
    }
}
