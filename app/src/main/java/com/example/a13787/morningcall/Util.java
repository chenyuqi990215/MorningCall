package com.example.a13787.morningcall;

import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.util.Enumeration;

/**
 * Created by 13787 on 2019/3/2.
 */

public class Util {
    public static final String ACRA_REPORT_ADDRESS = "http://developer.miivii.com:5984/acra-bugreport/_design/acra-storage/_update/report";

    public static final String ACRA_REPORT_LOGIN = "bugreport";

    public static final String ACRA_REPORT_PASSWORD = "miiviibugreport";

    // instead by your own machine IP address which you will connect to.
    public static final String ROS_MASTER_URI = "http://192.168.31.213:11311/";

    public static String getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                if(!intf.getDisplayName().equals("eth0")) continue;
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex)
        {
            Log.e("WifiPreference", ex.toString());
        }
        return "Error";
    }
}
