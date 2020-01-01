package org.ternence.compressionfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.ternence.compressionfile.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String COMPRESS_SRC_FILE;
    private static final String COMPRESS_DEST_FILE;
    private static final String ROOT_PATH;
    private Collection<File> srcFiles = new ArrayList<>();
    private File mSrcFile;
    private File mDestFile;

    static {
        ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
        COMPRESS_SRC_FILE = ROOT_PATH + File.separator + "11" + File.separator;
        COMPRESS_DEST_FILE = ROOT_PATH + File.separator + "111" + File.separator + "copy.zip";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        mSrcFile = ZipUtils.getFileByPath(COMPRESS_SRC_FILE);
        mDestFile = ZipUtils.getFileByPath(COMPRESS_DEST_FILE);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setCompressFile(new CompressFile());
        if (isStoragePermissionGranted()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "LoadFiles");
                    List<File> files = loadFiles(COMPRESS_SRC_FILE);
                    if (files == null) {
                        Log.i(TAG, "LoadFiles is null");
                        return;
                    }
                    for (File file : files) {
                        Log.i(TAG, "file name: " + file.getAbsolutePath());
                        srcFiles.add(file);
                    }

                }
            });
        }
    }

    private List<File> loadFiles(String dirPath) {
        Log.i(TAG, "LoadFiles: " + dirPath);
        return FileUtils.listFilesInDir(dirPath, true);
    }

    public class CompressFile {

        public void doCompressFiles() {
            CompressFileRunnable runnable = new CompressFileRunnable();
            Thread thread = new Thread(runnable);
            thread.start();
            Toast.makeText(MainActivity.this, "doCompressFiles", Toast.LENGTH_SHORT).show();
        }

        public void doDecompressFiles() {
            DecompressFileRunnable runnable = new DecompressFileRunnable();
            Thread thread = new Thread(runnable);
            thread.start();
            Toast.makeText(MainActivity.this, "doDecompressFiles", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isStoragePermissionGranted() {
        final Context context = getApplicationContext();
        int readPermissionCheck = ContextCompat
                .checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermissionCheck = ContextCompat
                .checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (readPermissionCheck == PackageManager.PERMISSION_GRANTED
                && writePermissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "granted permission");
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            return false;
        }
    }

    private class CompressFileRunnable implements Runnable {

        @Override
        public void run() {
            try {
                ZipUtils.zipFiles(srcFiles, mDestFile);
            } catch (IOException e) {
                Log.e(TAG, "ZipUtils zipFiles has a exception: " + e);
                e.printStackTrace();
            }
        }
    }

    private class DecompressFileRunnable implements Runnable {

        @Override
        public void run() {
            try {
                ZipUtils.unzipFile(mDestFile, mSrcFile);
            } catch (IOException e) {
                Log.e(TAG, "ZipUtils unZipFiles has a exception: " + e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v(TAG,"onRequestPermissionsResult requestCode ： " + requestCode
                + " Permission: " + permissions[0] + " was " + grantResults[0]
                + " Permission: " + permissions[1] + " was " + grantResults[1]
        );
    }
}
