package com.foi.air1712.webservice;

import com.foi.air1712.webservice.responses.AirWebServiceResponse;

import retrofit.Call;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;

/**
 * Created by Nikola on 29.10.2017..
 */

public interface AirWebService {
    //@FormUrlEncoded
    //@GET("dogadaji.json")
    @GET("dogadaji.json?print=pretty")
    //@GET(".json?print=pretty")
    Call<AirWebServiceResponse> getDogadaji();
    //Call<AirWebServiceResponse> getDogadaji(@Field("method") String method);

    @GET("lokacije.json?print=pretty")
        //@GET(".json?print=pretty")
    Call<AirWebServiceResponse> getLokacije();
    //Call<AirWebServiceResponse> getLokacije(@Field("method") String method);
}
