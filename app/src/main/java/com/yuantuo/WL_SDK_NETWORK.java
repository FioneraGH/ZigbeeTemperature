package com.yuantuo;

public class WL_SDK_NETWORK {
    public native boolean BuildNetwork(String strGWID, String strCmd);

    public native boolean GatewayQuitNetwork(String strGWID);

    public native boolean CheckGatewayNetwork(String strGWID);

    public native boolean DeviceQuitNetwork(String strGWID, String strDevID);
}