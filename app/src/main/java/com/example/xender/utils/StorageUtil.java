package com.example.xender.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class StorageUtil {

    public static final int FILTER_BY_DOCUMENT = 0;
    public static final int FILTER_BY_AUDIO = 1 ;
    public static final int FILTER_BY_VIDEO = 2 ;

    private static String documentPatterns[] ={".pdf",".txt",".docs",".docx",".ppt",".xlsx",".xml"};
    private static String audioPatterns[] ={".mp3"};
    private static String videoPatterns[] ={".mp4"};

    public static String[] getPattern(int type) {
        if(type == FILTER_BY_DOCUMENT) return documentPatterns;

        if(type==FILTER_BY_AUDIO) return audioPatterns;

        if(type==FILTER_BY_VIDEO) return videoPatterns;

        return null;
    }

    public static long getByteAvailable(){
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable;
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        }
        else {
            bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
        }
        long megAvailable = bytesAvailable / (1024 * 1024);
        Log.e("","Available MB : "+megAvailable);
        return bytesAvailable;

    } ;
    public static long getByteMemorySize() {

        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();

           return (long)totalBlocks*blockSize;

    }

    public static void getAllDir(File dir, int filter){
            File listFile[]=dir.listFiles();

            if(listFile!=null){
                for (File file: listFile) {

                    if(file.isDirectory() &&
                            !file.getAbsolutePath().contains("Android") &&
                    !file.getAbsolutePath().contains("Lyrics")
                    ){
                        getAllDir(file,filter);
                    } else if(!file.isDirectory())
                    {
                        for (String pattern : getPattern(filter) )
                        {
                            if (file.getName().contains(pattern)) {
                                Log.d("File test", file.getAbsolutePath());
                                break;
                            }
                        }

                    }
                }
            }
            else {
                Log.d("File test","NULL");
            }
    }
}
