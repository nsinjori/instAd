package com.foi.air1712.instad.notifikacije;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.instad.MainActivity;
import com.foi.air1712.instad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Darko on 19.1.2018..
 */

public class NoviDogadajServis extends Service{
    private static List<String> trenutni = new ArrayList<>();
    private DatabaseReference ref;
    private ValueEventListener vel;
    private int id = 001;
    private boolean firstTimeRun;
    private FirebaseAuth firebaseAuth;
    private List<String> favoriti;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        favoriti = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference fav = database.getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("favs");
        fav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                favoriti.clear();
                if(snapshot!=null) {
                    Map<String, String> mapData = (Map) snapshot.getValue();
                    if(mapData!=null) {
                        for (Map.Entry<String, String> entry : mapData.entrySet()) {
                            String fav = entry.getKey().toString();
                            if (!favoriti.contains(fav))
                                favoriti.add(fav);
                        }
                    }
                }else{
                    System.out.println("Nema favorita!");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
        ref = database.getReference("noviDogadaji");
        vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!firstTimeRun) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Dogadaji dogadaj = postSnapshot.getValue(Dogadaji.class);
                        if (!trenutni.contains(dogadaj.getHash())&&favoriti.contains(dogadaj.getObjekt())) {
                            notificiraj(dogadaj);
                        }
                        trenutni.add(dogadaj.getHash());
                    }
                } else {
                    firstTimeRun = false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        };
        ref.addValueEventListener(vel);
        return START_STICKY;
    }

    private void notificiraj(Dogadaji dogadaj) {

        Intent noviIntent = new Intent(getBaseContext(), MainActivity.class);

        Bundle extras = new Bundle();
        extras.putString("extra_adresa",dogadaj.getAdresa());
        extras.putString("extra_naziv",dogadaj.getNaziv());
        extras.putString("extra_hash",dogadaj.getHash());
        extras.putString("extra_slika",dogadaj.getSlika());
        extras.putString("extra_opis",dogadaj.getOpis());
        extras.putString("extra_latitude",dogadaj.getLatitude());
        extras.putString("extra_longitude",dogadaj.getLongitude());
        extras.putString("extra_datumkraj",dogadaj.getDatum_kraj());
        extras.putString("extra_datumpocetka",dogadaj.getDatum_pocetka());
        extras.putString("extra_objekt",dogadaj.getObjekt());
        extras.putString("extra_url",dogadaj.getUrl());
        //extras.putParcelableArrayList("event22", dogadajArrayList);
        noviIntent.putExtras(extras);

        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, noviIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getBaseContext())
                        .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                        .setContentTitle("Novi događaj")
                        .setContentText(dogadaj.getNaziv())
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, mBuilder.build());
        id++;
    }

    @Override
    public void onCreate() {
        firstTimeRun = true;
    }
    @Override
    public void onDestroy() {
        ref.removeEventListener(vel);
        firstTimeRun=true;
    }
}
