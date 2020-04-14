package org.ternence.compressionfile.utils.ble;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;


/**
 * @author ternence.c@gmail.com
 */
public class BluetoothAdvertiseCallback extends AdvertiseCallback {

    @Override
    public void onStartSuccess(AdvertiseSettings settingsInEffect) {
        super.onStartSuccess(settingsInEffect);
    }

    @Override
    public void onStartFailure(int errorCode) {
        super.onStartFailure(errorCode);
    }
}
