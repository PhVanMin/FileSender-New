package com.example.xender.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class StorageUtil {

    public static final int FILTER_BY_DOCUMENT = 0;
    public static final int FILTER_BY_AUDIO = 1;
    public static final int FILTER_BY_VIDEO = 2;

    private static final String[] documentPatterns = {"pdf", "txt", ".docs", ".docx", ".ppt", ".xlsx"};
    private static final String[] audioPatterns = {".mp3", ".m4a"};
    private static final String[] videoPatterns = {".mp4"};

    public static ArrayList<File> files = new ArrayList<>();

    public static String[] getPattern(int type) {
        if (type == FILTER_BY_DOCUMENT) return documentPatterns;

        if (type == FILTER_BY_AUDIO) return audioPatterns;

        if (type == FILTER_BY_VIDEO) return videoPatterns;

        return null;
    }

    public static long getByteAvailable() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable;
        bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        long megAvailable = bytesAvailable / (1024 * 1024);
        Log.e("", "Available MB : " + megAvailable);
        return bytesAvailable;
    }

    public static long getByteMemorySize() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();

        return totalBlocks * blockSize;
    }

    public static void getAllDir(File dir, int filter) {
        File[] listFile = dir.listFiles();
        if (listFile == null) return;

        for (File file : listFile) {
            if (file.getName().startsWith("."))
                continue;

            if (file.isDirectory()) {
                getAllDir(file, filter);
            } else {
                for (String pattern : getPattern(filter)) {
                    if (file.getName().endsWith(pattern)) {
                        Log.d("File test", file.getName());
                        files.add(file);
                        break;
                    }
                }
            }
        }

    }
}
