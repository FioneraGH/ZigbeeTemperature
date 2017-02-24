package com.fionera.zigbee.util;

import android.content.Intent;

import com.fionera.zigbee.ZigbeeApp;

/**
 * AppRunning
 * Created by fionera on 17-2-24 in ZigbeeTemperature.
 */

public class AppRunning {


    public  static void sendData(String type, String strDevID, String strDevType, String strData){
        Intent intent = new Intent(AppConst.MAIN_ACTION);
        intent.putExtra("mode", AppConst.Action_DongleData);
        intent.putExtra("type", type);
        intent.putExtra("devid", strDevID);
        intent.putExtra("devtype", strDevType);
        intent.putExtra("data", strData);
        ZigbeeApp.instance.sendBroadcast(intent);
    }
}
