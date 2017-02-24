package com.fionera.zigbee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fionera.zigbee.service.ZigbeeService;
import com.fionera.zigbee.util.AppConst;
import com.fionera.zigbee.util.SensorData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(AppConst.MAIN_ACTION);
        registerReceiver(broadcastReceiver, filter);

        startService(new Intent(this, ZigbeeService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

        stopService(new Intent(this, ZigbeeService.class));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String strOrder;
            String strDevID, strDevType, strData;
            if (intent.getAction().equals(AppConst.MAIN_ACTION)) {
                switch (intent.getExtras().getInt("mode")) {
                    case AppConst.Action_DongleLog:
                        strData = intent.getExtras().getString("data");
                        Log.d("tag", strData);
                        break;
                    case AppConst.Action_DongleData://获取 传感器上报数据后传回的数据
                        strOrder = intent.getExtras().getString("type");
                        strDevID = intent.getExtras().getString("devid");
                        strDevType = intent.getExtras().getString("devtype");
                        strData = intent.getExtras().getString("data");

                        SensorData.dataReceive(strOrder, strDevID, strDevType, strData);
                        break;
                }
            }
        }
    };

    public void allowDeviceJoinNet(View view) {
        Intent intent = new Intent(AppConst.DONGLE_ACTION);
        intent.putExtra("mode", AppConst.Action_DongleEnableJoin);
        sendBroadcast(intent);
    }
}
