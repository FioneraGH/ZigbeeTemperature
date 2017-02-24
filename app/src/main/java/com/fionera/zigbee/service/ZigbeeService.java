package com.fionera.zigbee.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.yuantuo.MAIN_WL_SDK;
import com.yuantuo.WL_SDK;
import com.yuantuo.WL_SDK_NETWORK;
import com.fionera.zigbee.util.AppConst;

public class ZigbeeService
        extends Service {

    private Handler mHandler;
    private Boolean isInit = false;

    private WL_SDK wlsdk;
    private MAIN_WL_SDK testsdk;
    private WL_SDK_NETWORK wlsdknetwork;

    private int iDelay = 0;

    private void init() {
        wlsdk.StopSerialServer();
        int iRet = wlsdk.StartSerialServer(testsdk.m_strPort, 2, 0);//串口号，检测网关的超时秒数，打开串口后延时多少毫秒
        /*
          如果返回值为0表示是物联的USB dongle，
          如果返回值为-1表示串口打开不成功，
          如果返回值为1表示不是物联的USB dongle
         */
        String sLog;
        if (iRet == 0) {
            sLog = "This is a Wulian USB dongle";
            isInit = true;
        } else if (iRet == 1) {
            sLog = "This is not a Wulian USB dongle";
        } else {
            sLog = "Fail to open com port";
        }
        System.out.println(sLog);
        //设置网关与设备断线时间。iGatewayOfflineTime为网关超时秒数，iDeviceOfflineTime为设备超时秒数
        wlsdk.SetOfflineTime(30, 70);//30, 70
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            System.loadLibrary("wl_sdk");

            //注册广播监听
            IntentFilter filter = new IntentFilter(AppConst.DONGLE_ACTION);
            registerReceiver(broadcastReceiver, filter);
            isInit = false;
            mHandler = new Handler();
            testsdk = new MAIN_WL_SDK();
            testsdk.state = 0;
            testsdk.m_strPort = "/dev/ttyHS99";
            wlsdk = new WL_SDK();
            wlsdk.RegisterCallBack(testsdk);//注册回调函数
            wlsdknetwork = new WL_SDK_NETWORK();

            mHandler.postDelayed(runnable, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getExtras().getInt("mode")) {
                case AppConst.Action_DongleEnableJoin:
                    wlsdk.EnableJoinGateway(testsdk.m_strGWID);
                    System.out.println("EnableJoinGateway");
                    break;
                case AppConst.Action_DongleControl:
                    String DevID = intent.getExtras().getString("dev");
                    String strData = intent.getExtras().getString("data");
                    if (!TextUtils.isEmpty(DevID)) {
                        wlsdk.ControlDevice(testsdk.m_strGWID, DevID, strData);
                    }
                    break;
            }
        }
    };

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                mHandler.postDelayed(this, 1000);

                iDelay++;
                if (isInit) {
                    if (testsdk.state == 3) {//检查网关网络
                        if (iDelay >= 120) {//120
                            iDelay = 0;
                            wlsdknetwork.CheckGatewayNetwork(testsdk.m_strGWID);
                        }
                    } else {
                        if (iDelay > 0) {
                            iDelay = 0;
                            doCheck();
                        }
                    }
                } else {
                    if (iDelay >= 2) {
                        iDelay = 0;
                        init();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void doCheck() {
        try {
            switch (testsdk.state) {
                case 0: //开始建网
                    if (!testsdk.m_strGWID.equals("")) {
                        wlsdknetwork.BuildNetwork(testsdk.m_strGWID, "FF");
                        testsdk.state = 1;
                    }
                    break;
                case 1://调用后网关允许设备加网
                    wlsdk.EnableJoinGateway(testsdk.m_strGWID);
                    testsdk.state = 2;
                    break;
                case 2://检查网关网络
                    wlsdknetwork.CheckGatewayNetwork(testsdk.m_strGWID);
                    testsdk.state = 3;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        try {
            wlsdk.StopSerialServer();
            wlsdk.UnRegisterCallBack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}