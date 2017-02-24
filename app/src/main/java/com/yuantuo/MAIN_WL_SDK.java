package com.yuantuo;

import com.fionera.zigbee.util.AppRunning;

public class MAIN_WL_SDK
        implements WL_SDK_CALLBACK {
    public String m_strGWID = ""; // 网关ID
    public String m_strPort = "";
    public int state = 0;

    // 回调函数。strGWID：网关ID
    public void onGatewayOnline(String strGWID) {
//        System.out.println("onGatewayOnline:");
//        System.out.println(strGWID);
        m_strGWID = strGWID;
        AppRunning.sendData(String.valueOf(DongleTAG.onGatewayOnline), "", "", "");
    }

    public void onGatewayOffline(String strGWID) {
//        System.out.println("onGatewayOffline");
//        System.out.println(strGWID);
        AppRunning.sendData(String.valueOf(DongleTAG.onGatewayOffline), "", "", "");
    }

    public void onGatewaySetupData(String strGWID, String strData) {
//        System.out.println("onGatewaySetupData");
//        System.out.println(strGWID);
//        System.out.println(strData);
        AppRunning.sendData(String.valueOf(DongleTAG.onGatewaySetupData), "", "", strData);
    }

    public void onDeviceOnline(String strGWID, String strDevID, String strDevType, String strData) {
//        System.out.println("onDeviceOnline");
//        System.out.println(strGWID);
//        System.out.println(strDevID);
//        System.out.println(strDevType);
//        System.out.println(strData);
        AppRunning.sendData(String.valueOf(DongleTAG.onDeviceOnline), strDevID, strDevType,
                strData);
    }

    public void onDeviceOffline(String strGWID, String strDevID) {
//        System.out.println("onDeviceOffline");
//        System.out.println(strGWID);
//        System.out.println(strDevID);
        AppRunning.sendData(String.valueOf(DongleTAG.onDeviceOffline), strDevID, "", "");
    }

    /*
     获取设备数据
     * @param strGWID 网关ID
     * @param strDevID 设备ID
     * @param strData 设备数据
     */
    public void onDeviceData(String strGWID, String strDevID, String strData) {
//        System.out.println("onDeviceData");
//        System.out.println(strGWID);
//        System.out.println(strDevID);
//        System.out.println(strData);
//        strDevID = FFD7FA04004B1200;s
//        trData=06C00101（C0人体探测）:01有人来了，00，正常，
//        0416210000（气味探测）：0066=6*16+6=102
//        0AD104020101(D1温度)：02，一位正小数0101=257=25.7℃
//        0AD204020148(D2湿度)：0148=328=32.8%
//        0AD304020056(D3光强)：0056=96=9.6
        AppRunning.sendData(String.valueOf(DongleTAG.onDeviceData), strDevID, "", strData);
    }

    public void onDeviceSetupData(String strGWID, String strDevID, String strData) {
//        System.out.println("onDeviceSetupData");
//        System.out.println(strGWID);
//        System.out.println(strDevID);
//        System.out.println(strData);
        AppRunning.sendData(String.valueOf(DongleTAG.onDeviceSetupData), strDevID, "", strData);
    }

    public void onDeviceAlarmData(String strGWID, String strDevID, String strData) {
//        System.out.println("onDeviceAlarmData");
//        System.out.println(strGWID);
//        System.out.println(strDevID);
//        System.out.println(strData);
        AppRunning.sendData(String.valueOf(DongleTAG.onDeviceAlarmData), strDevID, "", strData);

    }

    public void onReportDeviceOnline(String strGWID, String strDevID, String strData) {
//        System.out.println("onReportDeviceOnline");
//        System.out.println(strGWID);
//        System.out.println(strDevID);
//        System.out.println(strData);
        AppRunning.sendData(String.valueOf(DongleTAG.onReportDeviceOnline), strDevID, "", strData);
    }

    public void onLogMessage(String strMsg) {
//		System.out.println("onLogMessage");
//		System.out.println(strMsg);
        AppRunning.sendData(String.valueOf(DongleTAG.onLogMessage), "", "", "");
    }
}