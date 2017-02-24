package com.fionera.zigbee.util;

import android.content.Intent;
import android.util.Log;

import com.fionera.zigbee.ZigbeeApp;
import com.fionera.zigbee.data.ZigbeeData;
import com.yuantuo.DongleTAG;

/**
 * SensorData
 * Created by fionera on 17-2-24 in ZigbeeTemperature.
 */

public class SensorData {

    private static String devSensor;
    private static boolean stateSensor;
    private static boolean statePeople;

    public static void dataReceive(String order, String dev, String type, String data) {
        try {
            switch (DongleTAG.getAnimal(order)) {
                case onDeviceOnline://设备上线
                    if (type.equals("C0D1D2D3")) {//多功能传感器
                        devSensor = dev;
                        stateSensor = true;
                        return;
                    }
                    break;
                case onDeviceOffline:
                    if (dev.equals(devSensor)) {
                        stateSensor = false;
                        return;
                    }
                    break;
                case onDeviceSetupData://设备退网
                    if (data.equals("BB00")) {
                        if (dev.equals(devSensor)) {
                            stateSensor = false;
                            return;
                        }
                    }
                    break;
                case onDeviceData://设备上报数据
                    if (data.equals("")) {
                        return;
                    }
                    if (dev.equals(devSensor)) {
                        handleSensor(data);
                        return;
                    }
                    break;
            }
            Intent intent = new Intent(AppConst.DATA_CHANGE_ACTION);
            intent.putExtra("mode", AppConst.Action_DongleData);
            ZigbeeApp.instance.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("exception", "", e);
            e.printStackTrace();
        }
    }

    private static void handleSensor(String data) {
//        上报数据1：”06C0010P”，06：数据长度(16进制)，
//        C0：多功能人体设备ID(16进制) ，01：功能码，表示是报警数据，
//        0P：00正常，01报警
//        上报数据2：”0AD1040PQQQQ0AD2040PQQQQ0AD3040PQQQQ”
        try {
            byte[] bytRec = Hex.decodeHex(data.toCharArray());
            int iStart = 0;
            //长度合适
            while (iStart < bytRec.length) {
                if (bytRec[iStart] != 6 && bytRec[iStart] != 10) return;
                byte iLen = (byte) (bytRec[iStart] / 2 + 1);
                if (iLen <= bytRec.length - iStart) {
                    switch (bytRec[iStart + 1]) {
                        case (byte) 0xC0://人体检测
                            if (bytRec[iStart + 2] == 1) {//报警数据
                                if (bytRec[iStart + 3] == 0) {
                                    if (statePeople) {
                                        Log.e("tag", "----检测到人离开--->>>" + data);
                                    }
                                    statePeople = false;//无人
                                } else {
                                    Log.e("tag", "----检测到人来了--->>>" + data);
                                    statePeople = true;//有人
                                }
                            }
                            break;
                        case (byte) 0xD1://温度
                            if (bytRec[iStart + 2] == 4) {//传感器数据
                                int iData = Hex.byte2uInt(bytRec[iStart + 4]) * 256 + Hex.byte2uInt(bytRec[iStart + 5]);
                                switch (bytRec[iStart + 3]) {
                                    case 1://01(int，正数)
                                        ZigbeeData.TEMP = iData;
                                        break;
                                    case 2://02(正数，一位小数)
                                        ZigbeeData.TEMP = iData / 10;
                                        break;
                                    case 3://03(正数，两位小数)
                                        ZigbeeData.TEMP = iData / 100;
                                        break;
                                    case 4://04(负整数)
                                        ZigbeeData.TEMP = -iData;
                                        break;
                                    case 5://05(负数，一位小数)
                                        ZigbeeData.TEMP = -iData / 10;
                                        break;
                                    case 6://06(负数，两位小数)
                                        ZigbeeData.TEMP = -iData / 100;
                                        break;
                                }
                            }
                            break;
                        case (byte) 0xD2://湿度
                            if (bytRec[iStart + 2] == 4) {//传感器数据
                                int iData = Hex.byte2uInt(bytRec[iStart + 4]) * 256 + Hex.byte2uInt(bytRec[iStart + 5]);
                                ZigbeeData.WET = iData / 10;
                            }
                            break;
                        case (byte) 0xD3://光照强度
                            if (bytRec[iStart + 2] == 4) {//传感器数据
                                ZigbeeData.LIGHT = Hex.byte2uInt(bytRec[iStart + 4]) * 256 + Hex.byte2uInt(bytRec[iStart + 5]);
                            }
                            break;
                    }
                }
                iStart += iLen;
            }
        } catch (Exception e) {
            Log.e("exception", "", e);
            e.printStackTrace();
        }
    }
}
