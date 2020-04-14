package org.ternence.compressionfile.utils;

import android.os.storage.StorageVolume;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class FilesUtils {

    private static final String TAG = FilesUtils.class.getSimpleName();
    private static final String META_DATA_PATH = "META-INF/com/android/metadata";
    public static final int FLAG_FOR_WRITE = 1 << 8;

    public static File[] getExternalDirs() {
        /*Begin modified by OnePlus Willis for OOS-1986*/
        //final int userId = UserHandle.myUserId();
        final int userId = myUserId();
        //final StorageVolume[] volumes = StorageManager.getVolumeList(userId, StorageManager.FLAG_FOR_WRITE);
        final StorageVolume[] volumes =
                getVolumeList(userId, getFlagForWrite());
        final File[] files = new File[volumes.length];
        for (int i = 0; i < volumes.length; i++) {
            //files[i] = volumes[i].getPathFile();
            files[i] = getPathFile(volumes[i]);
        }
        /*End modified by OnePlus Willis for OOS-1986*/
        return files;
    }

    public static int getFlagForWrite(){
        try {
            Class<?> storageManager;
            storageManager = Class.forName("android.os.storage.StorageManager");

            int flag = ReflectUtils.readValue(storageManager, "FLAG_FOR_WRITE", FLAG_FOR_WRITE);
            Log.d(TAG, "FLAG_FOR_WRITE:" + flag);
            return flag;
        } catch (Exception e) {
            Log.e(TAG, "getFlagForWrite() failed:" + e.getMessage());
        }

        return FLAG_FOR_WRITE;
    }

    public static File getPathFile(StorageVolume volume) {
        try {
            Class<?> storageVolumeClass;
            storageVolumeClass = Class.forName("android.os.storage.StorageVolume");
            Method getPathFile = storageVolumeClass.getMethod("getPathFile");
            File pathFile = (File)getPathFile.invoke(volume);
            Log.d(TAG, "getPathFile:" + pathFile);

            return pathFile;
        } catch (Exception e) {
            Log.e(TAG, "getPathFile failed:" + e.getMessage());
        }

        return null;
    }

    public static StorageVolume[] getVolumeList(int userId, int flags) {
        try {
            Class<?> storageManager;
            storageManager = Class.forName("android.os.storage.StorageManager");

            Method getVolumeList = storageManager.getMethod("getVolumeList", int.class, int.class);

            StorageVolume[] sv = (StorageVolume[])getVolumeList.invoke(null, userId, flags);

            Log.d(TAG, "getVolumeList2:" + sv);

            return sv;
        } catch (Exception e) {
            Log.e(TAG, "getVolumeList failed:" + e.getMessage());
        }
        return new StorageVolume[0];
    }

    public static int myUserId() {
        try {
            Class<?> userHandleClass;
            userHandleClass = Class.forName("android.os.UserHandle");
            Method myUserId = userHandleClass.getMethod("myUserId");
            int id = (int)myUserId.invoke(null);
            Log.d(TAG, "myUserId:" + id);

            return id;
        } catch (Exception e) {
            Log.e(TAG, "myUserId failed:" + e.getMessage());
        }

        return 0;
    }

    public static long getPackageDataPath(String path) {
        long size = -1;
        File file = new File(path);
        if (file.isHidden() || !file.isFile() || !file.canRead()) {
            Log.i(TAG, "path: " + path);
            return -1;
        }

        boolean isZipSuffix = path.endsWith(".zip");
        Log.i(TAG, "Scanning package, isZipSuffix: " + isZipSuffix);
        if (!isZipSuffix) {
            return -1;
        }
        ZipFile zis = null;
        try {
            zis = new ZipFile(file);
            ZipEntry zipEntry = zis.getEntry(META_DATA_PATH);
            if (zipEntry == null) {
                Log.i(TAG, "not a upgrade package");
                return -1;
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
