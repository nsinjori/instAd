package com.foi.air1712.instad.notifikacije;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.instad.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Darko on 28.8.2018..
 */

public class AlertOnlyNotification implements INotifikacija{
    private static int id=0101;
    Context context;
    AlertOnlyNotification(Context c){
        context = c;
    }
    @Override
    public void notificiraj(Dogadaji dogadaj) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                        .setContentTitle("Novi događaj!")
                        .setContentText(dogadaj.getNaziv() + " - "+ dogadaj.getObjekt())
                        .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, mBuilder.build());
        id++;
    }
}
