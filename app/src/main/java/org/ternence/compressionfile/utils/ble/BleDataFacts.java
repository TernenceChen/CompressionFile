package org.ternence.compressionfile.utils.ble;

/**
 * @author ternence.c@gmail.com
 */
public class BleDataFacts {

    private final int mVendor;
    private final int mBleFlag;
    private final String mDeviceType;
    private final String mDeviceId;
    private final String mDeviceName;
    private int mDeviceVersion = 0x01;

    public static class Builder {
        private  int mVendor = 0;
        private  int mBleFlag = 0x01;
        private  String mDeviceType = "";
        private  String mDeviceId = "";
        private  String mDeviceName = "";

        public Builder setVendor(int val) {
            mVendor = val;
            return this;
        }

        public Builder setBleFlag(int val) {
            mBleFlag = val;
            return this;
        }

        public Builder setDeviceType(String deviceType) {
            mDeviceType = deviceType;
            return this;
        }

        public Builder setDeviceId(String deviceId) {
            mDeviceId = deviceId;
            return this;
        }

        public Builder setDeviceName(String deviceName) {
            mDeviceName = deviceName;
            return this;
        }

        public BleDataFacts build() {
            return new BleDataFacts(this);
        }
    }

    private BleDataFacts(Builder builder) {
        mVendor = builder.mVendor;
        mBleFlag = builder.mBleFlag;
        mDeviceType = builder.mDeviceType;
        mDeviceId = builder.mDeviceId;
        mDeviceName = builder.mDeviceName;
    }

}
