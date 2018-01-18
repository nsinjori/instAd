package com.foi.air1712.instad.fragmenti;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.database.MyItem;
import com.foi.air1712.instad.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Nikola on 18.1.2018..
 */

public class PrikazMapaFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ClusterManager.OnClusterClickListener<MyItem>, ClusterManager.OnClusterInfoWindowClickListener<MyItem>,
        ClusterManager.OnClusterItemClickListener<MyItem>, ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>{

    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;


    //za dohvacanje dogadaja//
    List<Dogadaji> sviDogadaji = null;
    private ClusterManager<MyItem> mClusterManager;
    private MyItem clickedClusterItem;

    //////

    private com.google.android.gms.maps.MapFragment mapFragment;
    private GoogleMap map = null;
    private float zoomStatus;
    private Context context;

    protected GoogleApiClient mGoogleApiClient;

    public static PrikazMapaFragment newInstance() {
        PrikazMapaFragment fragment = new PrikazMapaFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.map_fragment, container, false);
        context = rootView.getContext();

        sviDogadaji = Dogadaji.dajSveAzuriraneDogadaje();
        System.out.println("122131 dohvaceni - " + sviDogadaji.size());

        mapFragment = new com.google.android.gms.maps.MapFragment();
        mapFragment.getMapAsync(this);
        getActivity().getFragmentManager().beginTransaction().add(R.id.framemapa, mapFragment).commit();

        //provjera da li je gps omogućen
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(context, "GPS je omogućen", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Dohvaćanje lokacije...", Toast.LENGTH_SHORT).show();
        }else{
            GPSupozorenje();
        }
        //kraj provjere

        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        zoomStatus = map.getCameraPosition().zoom;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
        }

        pokreniCluster(map);
        map.setInfoWindowAdapter(new detaljniInfoWindow());


    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Pozicija = " + location.getLatitude()+", "+ location.getLongitude());

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //marker za trenutnu lokaciju
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Trenutna lokacija");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = map.addMarker(markerOptions);

        //micanje kamere
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(9));

        //kraj updatea za lokacije
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();

        try {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("koordinate:  " + bounds);

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {

    }

    @Override
    public boolean onClusterItemClick(MyItem myItem) {
        clickedClusterItem = myItem;
        System.out.println(myItem.getTitle());
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {
        ArrayList<Dogadaji> dogadajTag = new ArrayList<Dogadaji>();
        dogadajTag.add(clickedClusterItem.getDogadaj());

        AppCompatActivity activity = (AppCompatActivity) getContext();
        Fragment detaljiDogadaja = new DetaljniPrikazFragment();
        Bundle bundle = new Bundle();
        activity.getSupportFragmentManager().beginTransaction();
        bundle.putParcelableArrayList("event", dogadajTag);
        detaljiDogadaja.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame_layout, detaljiDogadaja).commit();


    }

    class detaljniInfoWindow implements GoogleMap.InfoWindowAdapter {

        private final View infowindowView;

        detaljniInfoWindow() {

            infowindowView = getActivity().getLayoutInflater().inflate(R.layout.map_detalji_infowindow, null);
            infowindowView.setLayoutParams(new RelativeLayout.LayoutParams(900, RelativeLayout.LayoutParams.WRAP_CONTENT));


        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView tvTitle = ((TextView) infowindowView
                    .findViewById(R.id.title));
            TextView tvSnippet = ((TextView) infowindowView
                    .findViewById(R.id.snippet));

            ImageView slika = ((ImageView) infowindowView.findViewById(R.id.slika));

            tvTitle.setText(clickedClusterItem.getTitle());
            tvSnippet.setText(clickedClusterItem.getObjektDogadaja() + ", " +clickedClusterItem.getSnippet()+ " kuna");

            if (slika != null) {
                Picasso.with(context).load(clickedClusterItem.getUrlSlike()).into(slika, new MarkerCallback(marker));
            }

            return infowindowView;
        }
    }

    class MarkerInfo extends DefaultClusterRenderer<MyItem> implements GoogleMap.OnCameraIdleListener{

        public MarkerInfo(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            markerOptions.snippet(item.getSnippet());
            markerOptions.title(item.getTitle());

            super.onBeforeClusterItemRendered(item, markerOptions);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            //setMinClusterSize(1);
            /**return cluster.getSize() > 10; // if markers <=10 then not clustering*/
            //return true;
            System.out.println("ZOOMSTATUS:" + zoomStatus + "novi:");
            if (zoomStatus > 18) {
                return cluster.getSize() > 10; //if markers <=10 then not clustering
            } else {
                return cluster.getSize() > 1; //if markers <=1 then not clustering
                //return true;
            }
        }

        @Override
        public void onCameraIdle() {
            zoomStatus = map.getCameraPosition().zoom;
            System.out.println("kamera se mice ++: " + zoomStatus);

        }
    }

    public class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }

        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error");
        }
    }
    
    /** aaaaaaaaaa **/
    private void pokreniCluster(GoogleMap map) {

        mClusterManager = new ClusterManager<MyItem>(context, map);
        //mClusterManager.setRenderer(new PersonRenderer());
        mClusterManager.setRenderer(new MarkerInfo(context, map, mClusterManager));
        map.setOnCameraIdleListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);
        map.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        map.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        map.setOnInfoWindowClickListener(mClusterManager);

        popuniCluster();
        mClusterManager.cluster();
    }

    private void popuniCluster() {

        ArrayList<LatLng> spremljeneKoordinate = new ArrayList<>();
        spremljeneKoordinate.add(new LatLng(1.0,1.0));

        for (final Dogadaji dogadaj : sviDogadaji) {
            String dogadajLat = dogadaj.getLatitude();
            String dogadajLon = dogadaj.getLongitude();
            LatLng izracunato;
            LatLng dogadajKoordinate;

            if(dogadajLat.contentEquals("nema") || dogadajLon.contentEquals("nema")){continue;}
            else{
                do{
                    double dogadajLatitude = Double.parseDouble(dogadajLat);
                    double dogadajLongitude = Double.parseDouble(dogadajLon);
                    dogadajKoordinate = new LatLng(dogadajLatitude, dogadajLongitude);

                    izracunato = izracunajOffset(dogadajLatitude, dogadajLongitude);
                    spremljeneKoordinate.add(izracunato);
                }while(!spremljeneKoordinate.contains(izracunato));

                mClusterManager.addItem(new MyItem(izracunato, dogadaj));

            }

        }

    }


    private LatLng izracunajOffset(double lat, double lon){
        //gotov algoritam//
        //Earth’s radius, sphere
        double R = 6378137;

        Random rand = new Random();
        double n = rand.nextInt(50);

        Random rando = new Random();
        double n1 = rando.nextInt(50);

        //offsets in meters
        double dn = n;
        double de = n1;

        //Coordinate offsets in radians
        double dLat = dn/R;
        double dLon = de/(R*Math.cos(Math.PI*lat/180));

        //OffsetPosition, decimal degrees
        double noviLat = lat + dLat * 180/Math.PI;
        double noviLon = lon + dLon * 180/Math.PI;

        LatLng vrati = new LatLng(noviLat, noviLon);
        return vrati;
    }

    //upozorenej da je iskljucen gps
    private void GPSupozorenje(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("GPS nije omogućen. Želite li ga omogućiti?")
                .setCancelable(false)
                .setPositiveButton("Omogući GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Odustani",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    //kraj alerta
}
