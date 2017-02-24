package com.fionera.zigbee;

import android.app.Application;
import android.content.Context;

/**
 * ZigbeeApp
 * Created by fionera on 17-2-24 in ZigbeeTemperature.
 */

public class ZigbeeApp
        extends Application {

    public static ZigbeeApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }
}
