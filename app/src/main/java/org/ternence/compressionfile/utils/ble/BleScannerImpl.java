package org.ternence.compressionfile.utils.ble;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;

import java.util.List;

public class BleScannerImpl {

    private final ScanCallback mScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    }

}
