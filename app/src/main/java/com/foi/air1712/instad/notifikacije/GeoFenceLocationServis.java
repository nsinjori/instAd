package com.foi.air1712.instad.notifikacije;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.instad.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darko on 20.1.2018..
 */

public class GeoFenceLocationServis extends IntentService {
    private static final String IDENTIFIER = "LocationAlertServis";

    public GeoFenceLocationServis() {
        super(IDENTIFIER);
    }
    INotifikacija notifikacija;

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(IDENTIFIER, "" + getErrorString(geofencingEvent.getErrorCode()));
            return;
        }
        Log.i(IDENTIFIER, geofencingEvent.toString());
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            getGeofenceInfo(triggeringGeofences);
        }
    }
    private void getGeofenceInfo(List<Geofence> triggeringGeofences) {
        String string ="";
        for (Geofence geofence : triggeringGeofences) {
            string = geofence.getRequestId();
            notifikacija = new NearEventNotification(getBaseContext());
            notifikacija.notificiraj(Dogadaji.getDogadajByHash(string));
        }
    }
    private String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "Geofence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "geofence too many_geofences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "geofence too many pending_intents";
            default:
                return "geofence error";
        }
    }
    /*
    @Override
    public void notificiraj(Dogadaji dogadaj) {

    }
    */
}
