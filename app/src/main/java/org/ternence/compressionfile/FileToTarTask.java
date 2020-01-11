package org.ternence.compressionfile;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileToTarTask {

    private static final String TAG = "FileToTarTask";

    private ExecutorService mArchiveFilesExecutor;
    private static final int MAX_BATCH_FILES = 20;
    private static final int MAX_CHECK_THREADS = 6;

    public void archiveFilesWithExecutors(File[] files, HashMap<String, File> destFileMap) {
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
            Future future = mArchiveFilesExecutor.submit(new ArchiveFilesTask(sliceList.get(i), destFileMap));
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
        private final HashMap<String, File> correctionMap;

        ArchiveFilesTask(List<File> list, HashMap<String, File> map) {
            archiveFiles = list;
            correctionMap = map;
            invalidList = new ArrayList<>();
        }

        @Override
        public ArrayList<File> call() {
            for (File file : archiveFiles) {
                invalidList.add(file);
            }
            return invalidList;
        }
    }


}
