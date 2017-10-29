package com.foi.air1712.webservice;

import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.webservice.responses.AirWebServiceResponse;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.util.Arrays;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Nikola on 29.10.2017..
 */

public class AirWebServiceCaller {

    // retrofit object
    Retrofit retrofit;
    // base URL of the web service
    private final String baseUrl = "http://instad.servebeer.com/InstAd/";
    //private final String baseUrl = "http://cortex.foi.hr/mtl/courses/air/";

    // constructor
    //public AirWebServiceCaller(){
    public AirWebServiceCaller(){

        //To verify what's sending over the network, use Interceptors
        OkHttpClient client = new OkHttpClient();

        // for debuggint use HttpInterceptor
        //client.interceptors().add(new HttpInterceptor());

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    // get all records from a web service
    public void getAll(String method, final Type entityType){
        System.out.println("Usel sam");

        AirWebService serviceCaller = retrofit.create(AirWebService.class);
        Call<AirWebServiceResponse> call = serviceCaller.getDogadaji();



        System.out.println("presel call");

        System.out.println("################################");
        System.out.println(call);
        System.out.println("################################");

        if(call != null){
            System.out.println("call nije null ipak");
            call.enqueue(new Callback<AirWebServiceResponse>() {
                @Override
                public void onResponse(Response<AirWebServiceResponse> response, Retrofit retrofit) {
                    try {
                        if(response.isSuccess()){

                            System.out.println(response.body());

                            if(entityType == Dogadaji.class){
                                System.out.println("************** dohvaceni dogadaji...");
                            } else
                            {
                                System.out.println("Unrecognized class");
                            }
                        }else{
                            System.out.println("ali zato response nije dobar");
                            System.out.println(response.body());
                            System.out.println(entityType);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            });
        }else {
            System.out.println("call je null ?????");
        }
    }


}
