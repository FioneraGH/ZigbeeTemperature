package com.yuantuo;

public enum DongleTAG {
    onGatewayOnline,
    onGatewayOffline,
    onGatewaySetupData,
    onDeviceOnline,
    onDeviceOffline,
    onDeviceData,
    onDeviceSetupData,
    onDeviceAlarmData,
    onReportDeviceOnline,
    onLogMessage;

    public static DongleTAG getAnimal(String tagStr) {
        return valueOf(tagStr);
    }
}