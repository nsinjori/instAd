package com.foi.air1712.instad.notifikacije;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;


public class ModulNotifikacija implements IModulNotifikacija
{
    @Override
    public void start(Context context)
    {
        context.startService(new Intent(context,NoviDogadajServis.class));
    }

    @Override
    public void stop(Context context)
    {
        context.stopService(new Intent(context,NoviDogadajServis.class));
    }

    @Override
    public boolean getIsRunning(Activity activity)
    {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ((NoviDogadajServis.class).getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
