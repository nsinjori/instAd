package com.foi.air1712.instad.notifikacije;

import android.app.Activity;
import android.content.Context;

public interface IModulNotifikacija
{
    void start(Context context);
    void stop(Context context);
    boolean getIsRunning(Activity activity);
}
