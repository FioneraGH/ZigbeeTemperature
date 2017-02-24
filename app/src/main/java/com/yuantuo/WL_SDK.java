package com.yuantuo;

public class WL_SDK {
    public native boolean RegisterCallBack(WL_SDK_CALLBACK obj);

    public native void UnRegisterCallBack();

    public native void EnableLogMsg(boolean bEnable);

    public native boolean StartTCPServer(int iPort);

    public native void StopTCPServer();

    public native int StartSerialServer(String strPort, int iTimeout, int iDelay);

    public native void StopSerialServer();

    public native boolean EnableJoinGateway(String strGWID);

    public native boolean DisableJoinGateway(String strGWID);

    public native boolean RefreshGateway(String strGWID);

    public native boolean RequestOnlineDevice(String strGWID);

    public native boolean SetOfflineTime(int iGatewayOfflineTime, int iDeviceOfflineTime);

    public native boolean ControlDevice(String strGWID, String strDevID, String strCmd);

    public native boolean FindDevice(String strGWID, String strDevID, int itimes);

    public native boolean GetDeviceSignal(String strGWID, String strDevID);

    public native boolean RefreshDevice(String strGWID, String strDevID);
}