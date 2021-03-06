package com.foi.air1712.instad.notifikacije;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.foi.air1712.database.Dogadaji;
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

    private boolean firstTimeRun;
    private FirebaseAuth firebaseAuth;
    private List<String> favoriti;
    private INotifikacija notifikacija;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notifikacija = new ClickableNotification(getBaseContext());
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
                            notifikacija.notificiraj(dogadaj);
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
