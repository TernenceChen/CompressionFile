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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String COMPRESS_SRC_FILE;
    private static final String COMPRESS_DEST_FILE;
    private static final String ROOT_PATH;
    private Collection<File> srcFiles = new ArrayList<>();
    private File mSrcFile;
    private File mDestFile;

    private ExecutorService mArchiveFilesExecutor;
    private static final int MAX_BATCH_FILES = 5;
    private static final int MAX_CHECK_THREADS = 6;

    static {
        ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
        COMPRESS_SRC_FILE = ROOT_PATH + File.separator + "aa" + File.separator;
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
                    Log.i(TAG, "SrcFile size: " + srcFiles.size());

                }
            });
        }
    }

    private List<File> loadFiles(String dirPath) {
        Log.i(TAG, "LoadFiles: " + dirPath);
        return FileUtils.listFilesInDir(dirPath, false);
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
                Log.i(TAG, "COMPRESS_SRC_FILE: " + COMPRESS_SRC_FILE);
                doTar(new File(COMPRESS_SRC_FILE), ROOT_PATH);
//                ZipUtils.zipFiles(srcFiles, mDestFile);
            } catch (IOException e) {
                Log.e(TAG, "ZipUtils zipFiles has a exception: " + e);
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "TarUtils archive has a exception: " + e);
            }
        }
    }

    private class DecompressFileRunnable implements Runnable {

        @Override
        public void run() {
            try {
//                ZipUtils.unzipFile(mDestFile, mSrcFile);
                Log.i(TAG, "Dest File: " + (ROOT_PATH + File.separator + "111.tar"));
                TarUtils.dearchive(new File(ROOT_PATH + File.separator+ "aa" + File.separator + "111.tar"), new File(ROOT_PATH + File.separator + "aaa"));
            } catch (IOException e) {
                Log.e(TAG, "ZipUtils unZipFiles has a exception: " + e);
                e.printStackTrace();
            } catch (Exception e) {
                Log.e(TAG, "TarUtils deArchive has a exception: " + e);
                e.printStackTrace();
            }
        }
    }

    private void doTar(File rootFile, String rootPath) throws Exception{
        Log.i(TAG, "COMPRESS_SRC_FILE: " + rootPath);
        File[] files = rootFile.listFiles();
        long doTarStartTime = System.currentTimeMillis();
        Log.i(TAG, "DoTar start Time: " + doTarStartTime);
        archiveFilesWithExecutors(files);
        Log.i(TAG, "DoTar End Time: " + (System.currentTimeMillis() - doTarStartTime));
//        for (File file : files) {
//            long startTime = System.currentTimeMillis();
//            long filesize = FileUtils.sizeOfDirectory0(file);
//            Log.i(TAG, "File name: " + file.getPath() + "  File size: " + filesize + (filesize < 500 * 1024 * 1024));
////            if (filesize < 500 * 1024 * 1024) {
////            File destFile = new File(ROOT_PATH + File.separator + "aa" + rootPath);
//            String folderPath = file.getAbsolutePath().replace(ROOT_PATH, "");
//            Log.i(TAG, "Folder Path: " + folderPath);
//            File destFile = new File(ROOT_PATH + File.separator + "aaa");
//            if (!destFile.exists()) {
//                destFile.mkdirs();
//            }
//            Log.i(TAG, "destFile Path: " + (ROOT_PATH + folderPath + ".tar"));
//            TarUtils.archive(file, ROOT_PATH + File.separator + "aaa" + File.separator + file.getName() + ".tar");
////            } else {
////                String folderPath = file.getAbsolutePath().replace(ROOT_PATH, "");
////                Log.i(TAG, "Do Tar File path: " + folderPath);
////                doTar(file, folderPath);
////            }
//            Log.i(TAG, "Speed Time: " + (System.currentTimeMillis() - startTime));
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v(TAG,"onRequestPermissionsResult requestCode ： " + requestCode
                + " Permission: " + permissions[0] + " was " + grantResults[0]
                + " Permission: " + permissions[1] + " was " + grantResults[1]
        );
    }

    public void archiveFilesWithExecutors(File[] files) {
        final List<File> fileArrayList = Arrays.asList(files);
        Log.i(TAG, "Fils size: " + fileArrayList.size());
        int batch = fileArrayList.size() / MAX_BATCH_FILES;
        Log.i(TAG, "File batch: " + batch);
        int slice = batch > MAX_CHECK_THREADS ? MAX_CHECK_THREADS : batch;
        Log.i(TAG, "File sLice: " + slice);
        List<ArrayList<File>> sliceList = new ArrayList<>(slice);
        for (int sli = 0; sli < slice; sli++) {
            sliceList.add(new ArrayList<File>());
        }
        int sli = 0;
        for (File file : fileArrayList) {
            if (sli >= slice) {
                sli = 0;
            }
            sliceList.get(sli).add(file);
            sli++;
        }
        Log.i(TAG, "File sli: " + sli);

        final int nThreads = sliceList.size();
        Log.d(TAG, "ArchiveFilesWithExecutors -> nThreads = " + nThreads);
        List<Future> futures = new ArrayList<>(nThreads);
        mArchiveFilesExecutor = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Future future = mArchiveFilesExecutor.submit(new ArchiveFilesTask(sliceList.get(i)));
            futures.add(future);
        }
        mArchiveFilesExecutor.shutdown();

        ArrayList<File> result = new ArrayList<>();
        try {
            for (Future future : futures) {
                ArrayList<File> list = (ArrayList<File>) future.get();
                Log.d(TAG, "archiveFilesWithExecutors -> result list " + list);
                if (list != null) {
                    result.addAll(list);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "checkMd5WithExecutors -> e = " + e);
        }

    }

    private static class ArchiveFilesTask implements Callable<ArrayList<File>> {
        private final List<File> archiveFiles;
        private final ArrayList<File> invalidList;

        ArchiveFilesTask(List<File> list) {
            archiveFiles = list;
            invalidList = new ArrayList<>();
        }

        @Override
        public ArrayList<File> call() {
            for (File file : archiveFiles) {
                long startTime = System.currentTimeMillis();
                long filesize = FileUtils.sizeOfDirectory0(file);
                Log.i(TAG, "File name: " + file.getPath() + "  File size: " + filesize + (filesize < 500 * 1024 * 1024));
                String folderPath = file.getAbsolutePath().replace(ROOT_PATH, "");
                Log.i(TAG, "Folder Path: " + folderPath);
                File destFile = new File(ROOT_PATH + File.separator + "aaa");
                if (!destFile.exists()) {
                    destFile.mkdirs();
                }
                Log.i(TAG, "destFile Path: " + (ROOT_PATH + folderPath + ".tar"));
                try {
                    TarUtils.archive(file, ROOT_PATH + File.separator + "aaa" + File.separator + file.getName() + ".tar");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "Speed Time: " + (System.currentTimeMillis() - startTime));
                invalidList.add(file);
            }
            return invalidList;
        }
    }
}
