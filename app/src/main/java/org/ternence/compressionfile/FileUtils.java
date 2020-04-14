package org.ternence.compressionfile;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileUtils {

    /**
     * Return the file by path.
     *
     * @param filePath The path of file.
     * @return the file
     */
    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    public static int fileSizeInDir(final String dirPath, final boolean isExcludeDir, final boolean isRecursive) {
        return fileSizeInDir(new File(dirPath), isExcludeDir, isRecursive);
    }

    public static int fileSizeInDir(final File dirPath, final boolean isExcludeDir, final boolean isRecursive) {
        int fileSize = 0;
        List<File> files = listFilesInDir(dirPath, isRecursive);
        if (files.size() == 0) {
            return 0;
        }

        if (!isExcludeDir) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    fileSize ++;
                }
            }
        } else {
            return files.size();
        }
        return fileSize;
    }

    /**
     * Return the files in directory.
     * <p>Doesn't traverse subdirectories</p>
     *
     * @param dirPath The path of directory.
     * @return the files in directory
     */
    public static List<File> listFilesInDir(final String dirPath) {
        return listFilesInDir(dirPath, null);
    }

    /**
     * Return the files in directory.
     * <p>Doesn't traverse subdirectories</p>
     *
     * @param dir The directory.
     * @return the files in directory
     */
    public static List<File> listFilesInDir(final File dir) {
        return listFilesInDir(dir, null);
    }

    /**
     * Return the files in directory.
     * <p>Doesn't traverse subdirectories</p>
     *
     * @param dirPath    The path of directory.
     * @param comparator The comparator to determine the order of the list.
     * @return the files in directory
     */
    public static List<File> listFilesInDir(final String dirPath, Comparator<File> comparator) {
        return listFilesInDir(getFileByPath(dirPath), false);
    }

    /**
     * Return the files in directory.
     * <p>Doesn't traverse subdirectories</p>
     *
     * @param dir        The directory.
     * @param comparator The comparator to determine the order of the list.
     * @return the files in directory
     */
    public static List<File> listFilesInDir(final File dir, Comparator<File> comparator) {
        return listFilesInDir(dir, false, comparator);
    }

    /**
     * Return the files in directory.
     *
     * @param dirPath     The path of directory.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @return the files in directory
     */
    public static List<File> listFilesInDir(final String dirPath, final boolean isRecursive) {
        return listFilesInDir(getFileByPath(dirPath), isRecursive);
    }

    /**
     * Return the files in directory.
     *
     * @param dir         The directory.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @return the files in directory
     */
    public static List<File> listFilesInDir(final File dir, final boolean isRecursive) {
        return listFilesInDir(dir, isRecursive, null);
    }

    /**
     * Return the files in directory.
     *
     * @param dirPath     The path of directory.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @param comparator  The comparator to determine the order of the list.
     * @return the files in directory
     */
    public static List<File> listFilesInDir(final String dirPath,
                                            final boolean isRecursive,
                                            final Comparator<File> comparator) {
        return listFilesInDir(getFileByPath(dirPath), isRecursive, comparator);
    }

    /**
     * Return the files in directory.
     *
     * @param dir         The directory.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @param comparator  The comparator to determine the order of the list.
     * @return the files in directory
     */
    public static List<File> listFilesInDir(final File dir,
                                            final boolean isRecursive,
                                            final Comparator<File> comparator) {
        return listFilesInDirWithFilter(dir, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        }, isRecursive, comparator);
    }

    /**
     * Return the files that satisfy the filter in directory.
     * <p>Doesn't traverse subdirectories</p>
     *
     * @param dirPath The path of directory.
     * @param filter  The filter.
     * @return the files that satisfy the filter in directory
     */
    public static List<File> listFilesInDirWithFilter(final String dirPath,
                                                      final FileFilter filter) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter);
    }

    /**
     * Return the files that satisfy the filter in directory.
     * <p>Doesn't traverse subdirectories</p>
     *
     * @param dir    The directory.
     * @param filter The filter.
     * @return the files that satisfy the filter in directory
     */
    public static List<File> listFilesInDirWithFilter(final File dir,
                                                      final FileFilter filter) {
        return listFilesInDirWithFilter(dir, filter, false, null);
    }

    /**
     * Return the files that satisfy the filter in directory.
     * <p>Doesn't traverse subdirectories</p>
     *
     * @param dirPath    The path of directory.
     * @param filter     The filter.
     * @param comparator The comparator to determine the order of the list.
     * @return the files that satisfy the filter in directory
     */
    public static List<File> listFilesInDirWithFilter(final String dirPath,
                                                      final FileFilter filter,
                                                      final Comparator<File> comparator) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, comparator);
    }

    /**
     * Return the files that satisfy the filter in directory.
     * <p>Doesn't traverse subdirectories</p>
     *
     * @param dir        The directory.
     * @param filter     The filter.
     * @param comparator The comparator to determine the order of the list.
     * @return the files that satisfy the filter in directory
     */
    public static List<File> listFilesInDirWithFilter(final File dir,
                                                      final FileFilter filter,
                                                      final Comparator<File> comparator) {
        return listFilesInDirWithFilter(dir, filter, false, comparator);
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * @param dirPath     The path of directory.
     * @param filter      The filter.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @return the files that satisfy the filter in directory
     */
    public static List<File> listFilesInDirWithFilter(final String dirPath,
                                                      final FileFilter filter,
                                                      final boolean isRecursive) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive);
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * @param dir         The directory.
     * @param filter      The filter.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @return the files that satisfy the filter in directory
     */
    public static List<File> listFilesInDirWithFilter(final File dir,
                                                      final FileFilter filter,
                                                      final boolean isRecursive) {
        return listFilesInDirWithFilter(dir, filter, isRecursive, null);
    }


    /**
     * Return the files that satisfy the filter in directory.
     *
     * @param dirPath     The path of directory.
     * @param filter      The filter.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @param comparator  The comparator to determine the order of the list.
     * @return the files that satisfy the filter in directory
     */
    public static List<File> listFilesInDirWithFilter(final String dirPath,
                                                      final FileFilter filter,
                                                      final boolean isRecursive,
                                                      final Comparator<File> comparator) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive, comparator);
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * @param dir         The directory.
     * @param filter      The filter.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @param comparator  The comparator to determine the order of the list.
     * @return the files that satisfy the filter in directory
     */
    public static List<File> listFilesInDirWithFilter(final File dir,
                                                      final FileFilter filter,
                                                      final boolean isRecursive,
                                                      final Comparator<File> comparator) {
        List<File> files = listFilesInDirWithFilterInner(dir, filter, isRecursive);
        if (comparator != null) {
            Collections.sort(files, comparator);
        }
        return files;
    }

    private static List<File> listFilesInDirWithFilterInner(final File dir,
                                                            final FileFilter filter,
                                                            final boolean isRecursive) {
        List<File> list = new ArrayList<>();
        if (!isDir(dir)) {
            return list;
        }
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (isRecursive && file.isDirectory()) {
                    list.add(file);
                    list.addAll(listFilesInDirWithFilterInner(file, filter, true));
                }
                if (filter.accept(file)) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * Return whether it is a directory.
     *
     * @param dirPath The path of directory.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDir(final String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    /**
     * Return whether it is a directory.
     *
     * @param file The file.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDir(final File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static long sizeOfDirectory0(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return 0L;
        } else {
            long size = 0L;
            File[] var4 = files;
            int var5 = files.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                File file = var4[var6];

                try {
                    if (!isSymlink(file)) {
                        size += sizeOf0(file);
                        if (size < 0L) {
                            break;
                        }
                    }
                } catch (IOException var9) {
                }
            }

            return size;
        }
    }

    private static long sizeOf0(File file) {
        return file.isDirectory() ? sizeOfDirectory0(file) : file.length();
    }

    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        } else {
            return Files.isSymbolicLink(file.toPath());
        }
    }

}
