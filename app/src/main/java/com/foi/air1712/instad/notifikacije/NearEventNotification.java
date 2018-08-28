package com.foi.air1712.instad.notifikacije;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.instad.MainActivity;
import com.foi.air1712.instad.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Darko on 28.8.2018..
 */

public class NearEventNotification implements INotifikacija{
    private static int id = 2001;
    Context context;
    NearEventNotification(Context c){
        context = c;
    }
    @Override
    public void notificiraj(Dogadaji dogadaj) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                        .setContentTitle("Događaj u blizini")
                        .setContentText(dogadaj.getNaziv())
                        .setContentIntent(getPIntent(dogadaj))
                        .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, mBuilder.build());
        id++;
    }
    private PendingIntent getPIntent(Dogadaji dogadaj){
        Intent noviIntent = new Intent(context, MainActivity.class);

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
        PendingIntent pendingIntent = PendingIntent.getActivity(context, uniqueInt, noviIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
