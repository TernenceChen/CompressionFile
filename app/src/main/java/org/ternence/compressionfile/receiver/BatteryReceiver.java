package org.ternence.compressionfile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author ternence
 */
public class BatteryReceiver extends BroadcastReceiver {

    private static final String TAG = "BatteryReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "BatteryReceiver onReceiver");
    }

}
