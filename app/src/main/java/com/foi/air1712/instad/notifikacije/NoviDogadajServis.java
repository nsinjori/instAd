package com.foi.air1712.instad.notifikacije;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.foi.air1712.database.Dogadaji;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darko on 19.1.2018..
 */

public class NoviDogadajServis extends Service{
    private static List<String> trenutni = new ArrayList<>();
    private DatabaseReference ref;
    private ValueEventListener vel;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("noviDogadaji");
        vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        Dogadaji dogadaj = postSnapshot.getValue(Dogadaji.class);
                        if(!trenutni.contains(dogadaj.getHash())){
                            notificiraj(dogadaj);
                            }
                        trenutni.add(dogadaj.getHash());
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
        Toast.makeText(getBaseContext(),"Događaj: "+dogadaj.getNaziv(), Toast.LENGTH_SHORT).show();
    }
}
