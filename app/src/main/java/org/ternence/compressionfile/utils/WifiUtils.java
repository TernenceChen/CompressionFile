package org.ternence.compressionfile.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WifiUtils {

    public static boolean isSupport5GBand(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            return wifiManager.is5GHzBandSupported();
        } else {
            return false;
        }
    }

}
