package org.ternence.compressionfile.utils.ble;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class BleUtils {

    private static final String TAG = BleUtils.class.getSimpleName();

    /**
     * Return true if Bluetooth is currently enabled and ready for use.
     *
     * @return true if the local adapter is turned on
     */
    public static boolean isOpen(BluetoothAdapter adapter) {
        if (null != adapter) {
            return adapter.isEnabled();
        }
        return false;
    }

    /**
     * Start the remote device discovery process.
     */
    public static void searchDevices(BluetoothAdapter adapter) {
        if (null != adapter) {
            adapter.startDiscovery();
        }
    }

    /**
     * Cancel the current device discovery process.
     */
    public static void cancelDiscovery(BluetoothAdapter adapter) {
        if (null != adapter) {
            adapter.cancelDiscovery();
        }
    }

    /**
     * Register bluetooth receiver
     *
     * @param receiver Bluetooth broadcast receiver
     */
    public static void registerBluetoothReceiver(BroadcastReceiver receiver, Activity activity) {
        if (null == receiver || null == activity) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        activity.registerReceiver(receiver, intentFilter);
    }

    /**
     * Unregister bluetooth receiver
     *
     * @param receiver Bluetooth broadcast receiver
     */
    public static void unRegisterBluetoothReceiver(BroadcastReceiver receiver, Activity activity) {
        if (null == receiver || null == activity) {
            return;
        }
        activity.unregisterReceiver(receiver);
    }

    /**
     * Turn on the local Bluetooth adapter&mdash; do not use without explicit
     * user action to turn on Bluetooth.
     */
    public void enableBlueTooth(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter bluetoothAdapter = null;
            if (bluetoothManager != null) {
                bluetoothAdapter = bluetoothManager.getAdapter();
            }
            if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
        }
    }

    /**
     * Turn off the local Bluetooth adapter&mdash;do not use without explicit
     * user action to turn off Bluetooth.
     */
    public void disableBlueTooth(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter bluetoothAdapter = null;
            if (bluetoothManager != null) {
                bluetoothAdapter = bluetoothManager.getAdapter();
            }
            if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }
    }

    public static void startAdvertise(BluetoothLeAdvertiser advertiser,
                                      AdvertiseSettings advertiseSettings,
                                      AdvertiseData advertiseData,
                                      AdvertiseCallback advertiseCallback) {
        if (null != advertiser) {
            advertiser.startAdvertising();
        }
    }


}
