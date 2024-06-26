package com.example.xender.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.xender.R;
import com.example.xender.model.Contact;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FileAdapter extends ArrayAdapter<File> {
    private Context context;
    public static int IMAGE_FILE[] ={
            R.drawable.pdf_svgrepo_com,
            R.drawable.txt_svgrepo_com,
            R.drawable.doc_svgrepo_com,
            R.drawable.docx_file_format_svgrepo_com,
            R.drawable.ppt_svgrepo_com,
            R.drawable.xlsx_file_format_svgrepo_com,
            R.drawable.xml_svgrepo_com,
            R.drawable.music_file_5_svgrepo_com,
            R.drawable.video_file_1_svgrepo_com,
            R.drawable.video_file_1_svgrepo_com,
    } ;
    public static int PDF_IMAGE = 0;
    public static int TXT_IMAGE = 1;
    public static int DOC_IMAGE = 2;
    public static int DOCX_IMAGE = 3;
    public static int PPT_IMAGE = 4;
    public static int XLSX_IMAGE = 5;
    public static int XML_IMAGE = 6;
    public static int MUSIC_IMAGE = 7;
    public static int VIDEO_IMAGE = 8;
    public static int IMAGE_IMAGE = 9;

    public ArrayList<File> files;
    public static Map<String,Integer> extension = new HashMap<String,Integer>() {{
        put("pdf",PDF_IMAGE);
        put("txt",TXT_IMAGE);
        put("docs",DOC_IMAGE);
        put("docx",DOCX_IMAGE);
        put("ppt",PPT_IMAGE);
        put("pptx",PPT_IMAGE);
        put("xlsx",XLSX_IMAGE);
        put("xml",XML_IMAGE);
        put("mp3",MUSIC_IMAGE);
        put("m4a",MUSIC_IMAGE);
        put("mp4",VIDEO_IMAGE);
        put("png",IMAGE_IMAGE);
        put("jpg",IMAGE_IMAGE);
        put("jpeg",IMAGE_IMAGE);
    }};


    public FileAdapter(@NonNull Context context, int resource, @NonNull ArrayList<File> objects) {
        super(context, resource, objects);
        this.context = context ;
        this.files= objects;
        Log.d("adapter 11", "111");
        extension.put("pdf",PDF_IMAGE);
        extension.put("txt",TXT_IMAGE);
        extension.put("docs",DOC_IMAGE);
        extension.put("docx",DOCX_IMAGE);
        extension.put("ppt",PPT_IMAGE);
        extension.put("pptx",PPT_IMAGE);
        extension.put("xlsx",XLSX_IMAGE);
        extension.put("xml",XML_IMAGE);
        extension.put("mp3",MUSIC_IMAGE);
        extension.put("m4a",MUSIC_IMAGE);
        extension.put("mp4",VIDEO_IMAGE);
        extension.put("png",IMAGE_IMAGE);
        extension.put("jpg",IMAGE_IMAGE);
        extension.put("jpeg",IMAGE_IMAGE);
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.file, parent, false);
        }

        TextView path = row.findViewById(R.id.path_textView);
        TextView size = row.findViewById(R.id.size_textView);
        File current = files.get(position);
        ImageView imageView = row.findViewById(R.id.file_imgView);

        int index = extension.get(FilenameUtils.getExtension(current.getAbsolutePath()));
        imageView.setImageResource(IMAGE_FILE[index]);
        path.setText(current.getName());
        size.setText(readableFileSize(current.length()));

        return row;
    }
    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
