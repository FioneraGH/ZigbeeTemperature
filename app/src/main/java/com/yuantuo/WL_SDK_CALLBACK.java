package com.yuantuo;

public interface WL_SDK_CALLBACK {
    void onGatewayOnline(String strGWID);

    void onGatewayOffline(String strGWID);

    void onGatewaySetupData(String strGWID, String strData);

    void onDeviceOnline(String strGWID, String strDevID, String strDevType, String strData);

    void onDeviceOffline(String strGWID, String strDevID);

    void onDeviceData(String strGWID, String strDevID, String strData);

    void onDeviceSetupData(String strGWID, String strDevID, String strData);

    void onDeviceAlarmData(String strGWID, String strDevID, String strData);

    void onReportDeviceOnline(String strGWID, String strDevID, String strData);

    void onLogMessage(String strMsg);
}