package com.foi.air1712.webservice;

import com.foi.air1712.core.AirWebServiceHandler;
import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.database.Lokacije;
import com.foi.air1712.webservice.responses.AirWebServiceResponse;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Nikola on 29.10.2017..
 */

public class AirWebServiceCaller {

    AirWebServiceHandler mAirWebServiceHandler;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private ArrayList<Dogadaji> DohvaceniDogadaji = new ArrayList<Dogadaji>();
    private ArrayList<Lokacije> DohvaceneLokacije = new ArrayList<Lokacije>();

    // retrofit object
    Retrofit retrofit;
    // base URL of the web service
    private final String baseUrl = "https://instad-f1929.firebaseio.com/";
    //private final String baseUrl = "http://instad.servebeer.com/InstAd/";


    // constructor
    //public AirWebServiceCaller(){
    //public AirWebServiceCaller(){
    public AirWebServiceCaller(AirWebServiceHandler airWebServiceHandler){
        this.mAirWebServiceHandler = airWebServiceHandler;

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
                                //handleDogadaji(response);
                                dajDogadaje();
                                //dajLokacije();
                            }
                            else if(entityType == Lokacije.class){
                                System.out.println("************** dohvacene lokacije...");
                                //handleDogadaji(response);
                                dajLokacije();
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

    //tu ideju metode za dogdaje..

    private void handleDogadaji(Response<AirWebServiceResponse> response) {
        Gson gson = new Gson();
        System.out.println("*************************");
        System.out.println("hmm");
        System.out.println("*************************");
        /*
        Dogadaji[] storeDogadaji2 = gson.fromJson(response.body().getItems().toString(), Dogadaji[].class);
        */
        Dogadaji[] storeDogadaji = response.body().getItems();
        System.out.println("Ovolko ih je: " + storeDogadaji.length);
        //System.out.println("Ovolko ih je: " + storeDogadaji[0].getObjekt());
        if(mAirWebServiceHandler != null){
            mAirWebServiceHandler.onDataArrived(Arrays.asList(storeDogadaji), true, 1316217);

        }
    }







    private void dajDogadaje(){
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("dogadaji");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Imamo " + snapshot.getChildrenCount() + " dogadaja dohvacenih");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Dogadaji dogadaj = postSnapshot.getValue(Dogadaji.class);
                    System.out.println(dogadaj.getHash() + " - " + dogadaj.getNaziv());
                    System.out.println(dogadaj);
                    System.out.println("kaj se tu desava");

                    /** proba za datume da ih ne uzima ak su stari
                    String dtStart = dogadaj.getDatum_pocetka();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    try {
                        date = format.parse(dtStart);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date danas = new Date();

                    if(!date.before(danas)){
                        DohvaceniDogadaji2.add(dogadaj);
                    }
                    /** kraj provjere datum **/

                    DohvaceniDogadaji.add(dogadaj);



                }
                System.out.println("nekaj ih ima");
                System.out.println(DohvaceniDogadaji.size());
                //initializeAdapter();

                if(mAirWebServiceHandler != null){
                    mAirWebServiceHandler.onDataArrived(DohvaceniDogadaji, true, 1316217);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

        });



    }


    private void dajLokacije(){
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("lokacije");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Imamo " + snapshot.getChildrenCount() + " lokacija dohvacenih");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Lokacije lokacije = postSnapshot.getValue(Lokacije.class);
                    System.out.println(lokacije.getNaziv() + " - " + lokacije.getLatitude());
                    System.out.println(lokacije);
                    System.out.println("kaj se tu desava");
                    DohvaceneLokacije.add(lokacije);
                }
                System.out.println("nekaj ih ima");
                System.out.println(DohvaceneLokacije.size());
                //initializeAdapter();

                if(mAirWebServiceHandler != null){
                    mAirWebServiceHandler.onDataArrived(DohvaceneLokacije, true, 1316217);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

        });



    }


}
