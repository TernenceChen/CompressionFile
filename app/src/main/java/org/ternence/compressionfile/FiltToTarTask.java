package org.ternence.compressionfile;

import java.util.concurrent.ExecutorService;

public class FiltToTarTask {

    private ExecutorService mArchiveFilesExecutor;
    private static final int MAX_BATCH_FILES = 20;
    private static final int MAX_CHECK_THREADS = 6;

//    public void archiveFilesWithExecutors(File[] files, HashMap<String, File> destFileMap) {
//        final List<File> fileArrayList = Arrays.asList(files);
//        int batch = fileArrayList.size() / MAX_BATCH_FILES;
//        int slice = batch > MAX_CHECK_THREADS ? MAX_CHECK_THREADS : batch;
//        List<ArrayList<File>> sliceList = new ArrayList<>(slice);
//        for (int sli = 0; sli < slice; sli++) {
//            sliceList.add(new ArrayList<>());
//        }
//        int sli = 0;
//        for (File file : fileArrayList) {
//            if (sli >= slice) {
//                sli = 0;
//            }
//            sliceList.get(sli).add(file);
//            sli++;
//        }
//
//        final int nThreads = sliceList.size();
//        LogUtils.d(TAG, "ArchiveFilesWithExecutors -> nThreads = " + nThreads);
//        List<Future> futures = new ArrayList<>(nThreads);
//        mArchiveFilesExecutor = Executors.newFixedThreadPool(nThreads);
//        for (int i = 0; i < nThreads; i++) {
//            Future future = mArchiveFilesExecutor.submit(new ArchiveFilesTask(sliceList.get(i), destFileMap));
//            futures.add(future);
//        }
//        mArchiveFilesExecutor.shutdown();
//
//        ArrayList<File> result = new ArrayList<>();
//        try {
//            for (Future future : futures) {
//                ArrayList<File> list = (ArrayList<File>) future.get();
//                LogUtils.d(TAG, "archiveFilesWithExecutors -> result list " + list);
//                if (list != null) {
//                    result.addAll(list);
//                }
//            }
//        } catch (Exception e) {
//            LogUtils.e(TAG, "checkMd5WithExecutors -> e = " + e);
//        }
//
//    }
//
//    private static class ArchiveFilesTask implements Callable<ArrayList<File>> {
//        private final List<File> archiveFiles;
//        private final ArrayList<File> invalidList;
//        private final HashMap<String, File> correctionMap;
//
//        ArchiveFilesTask(List<File> list, HashMap<String, File> map) {
//            archiveFiles = list;
//            correctionMap = map;
//            invalidList = new ArrayList<>();
//        }
//
//        @Override
//        public ArrayList<File> call() {
//            for (File file : archiveFiles) {
//                invalidList.add(file);
//            }
//            return invalidList;
//        }
//    }


}
