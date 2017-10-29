package com.foi.air1712.webservice;

import com.foi.air1712.webservice.responses.AirWebServiceResponse;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Nikola on 29.10.2017..
 */

public interface AirWebService {
    //@FormUrlEncoded
    @GET("dogadaji.json")
    Call<AirWebServiceResponse> getDogadaji();
    //Call<AirWebServiceResponse> getDogadaji(@Field("method") String method);
}
