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

/**
 * This class provides a way to perform Bluetooth LE advertise operations
 *
 * @author ternence.c@gmail.com
 */
public class BleUtils {

    private static final String TAG = BleUtils.class.getSimpleName();

    /**
     * The device is capable of communicating with other devices via Bluetooth Low Energy radio.
     *
     * @return true if the device supports Bluetooth Low Energy radio
     */
    public static boolean isSupportBle(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * Return true if Bluetooth is currently enabled and ready for use.
     *
     * @return true if the local adapter is turned on
     */
    private static boolean isOpen(BluetoothAdapter adapter) {
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

    /**
     * Start Advertise
     *
     * @param advertiseSettings Advertise settings includes advertise mode {@link AdvertiseSettings.Builder#setAdvertiseMode(int)},
     *                          TX power level {@link AdvertiseSettings.Builder#setTxPowerLevel(int)},
     *                          timeout {@link AdvertiseSettings.Builder#setTimeout(int)},
     *                          connect status {@link AdvertiseSettings.Builder#setConnectable(boolean)
     * @param advertiseData     Advertise data packet container for Bluetooth LE advertising.
     * @param advertiseCallback Bluetooth LE advertising callbacks, used to deliver advertising operation status.
     */
    public static void startAdvertise(BluetoothAdapter adapter,
                                      AdvertiseSettings advertiseSettings,
                                      AdvertiseData advertiseData,
                                      AdvertiseCallback advertiseCallback) throws IllegalStateException {
        if (isOpen(adapter)) {
            BluetoothLeAdvertiser advertiser = adapter.getBluetoothLeAdvertiser();
            if (null != advertiser) {
                advertiser.startAdvertising(advertiseSettings, advertiseData, advertiseCallback);
            }
        }
    }

    /**
     * Start Advertise
     *
     * @param advertiseSettings Advertise settings includes advertise mode {@link AdvertiseSettings.Builder#setAdvertiseMode(int)},
     *                          TX power level {@link AdvertiseSettings.Builder#setTxPowerLevel(int)},
     *                          timeout {@link AdvertiseSettings.Builder#setTimeout(int)},
     *                          connect status {@link AdvertiseSettings.Builder#setConnectable(boolean)
     * @param advertiseData     Advertise data packet container for Bluetooth LE advertising.
     * @param scanResponseData  Scan response associated with the advertisement data.
     * @param advertiseCallback Bluetooth LE advertising callbacks, used to deliver advertising operation status.
     */
    public static void startAdvertise(BluetoothAdapter adapter,
                                      AdvertiseSettings advertiseSettings,
                                      AdvertiseData advertiseData,
                                      AdvertiseData scanResponseData,
                                      AdvertiseCallback advertiseCallback) throws IllegalStateException {
        if (isOpen(adapter)) {
            BluetoothLeAdvertiser advertiser = adapter.getBluetoothLeAdvertiser();
            if (null != advertiser) {
                advertiser.startAdvertising(advertiseSettings, advertiseData, scanResponseData, advertiseCallback);
            }
        }
    }

    /**
     * Stop Bluetooth LE advertising.
     *
     * @param advertiseCallback {@link AdvertiseCallback} identifies the advertising instance to stop.
     */
    public static void stopAdvertise(BluetoothAdapter adapter, AdvertiseCallback advertiseCallback) throws IllegalStateException {
        if (isOpen(adapter)) {
            BluetoothLeAdvertiser bluetoothLeAdvertiser = adapter.getBluetoothLeAdvertiser();
            if (null != bluetoothLeAdvertiser) {
                bluetoothLeAdvertiser.stopAdvertising(advertiseCallback);
            }
        }
    }
}
