package com.example.xender.adapter;

import static com.example.xender.adapter.FileAdapter.DOCX_IMAGE;
import static com.example.xender.adapter.FileAdapter.DOC_IMAGE;
import static com.example.xender.adapter.FileAdapter.MUSIC_IMAGE;
import static com.example.xender.adapter.FileAdapter.PDF_IMAGE;
import static com.example.xender.adapter.FileAdapter.PPT_IMAGE;
import static com.example.xender.adapter.FileAdapter.TXT_IMAGE;
import static com.example.xender.adapter.FileAdapter.VIDEO_IMAGE;
import static com.example.xender.adapter.FileAdapter.XLSX_IMAGE;
import static com.example.xender.adapter.FileAdapter.XML_IMAGE;

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
import com.example.xender.model.FileCloud;
import com.example.xender.model.FileSend;

import org.apache.commons.io.FilenameUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSendAdapter extends ArrayAdapter<FileSend> {
    Context context;
    List<FileSend> fileSendList;
    public static Map<String,Integer> extension = new HashMap<>();

    public FileSendAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        fileSendList = objects;
        extension.put("pdf",PDF_IMAGE);
        extension.put("txt",TXT_IMAGE);
        extension.put("docs",DOC_IMAGE);
        extension.put("docx",DOCX_IMAGE);
        extension.put("ppt",PPT_IMAGE);
        extension.put("pptx",PPT_IMAGE);
        extension.put("xlsx",XLSX_IMAGE);
        extension.put("xml",XML_IMAGE);
        extension.put("mp3",MUSIC_IMAGE);
        extension.put("mp4",VIDEO_IMAGE);
    }
    @Override
    public int getCount() {
        return fileSendList.size();
    }
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row ;
        row = inflater.inflate(R.layout.file, null);
        TextView path = (TextView) row.findViewById(R.id.path_textView);
        TextView size = (TextView) row.findViewById(R.id.size_textView);
        FileSend current = fileSendList.get(position);

        ImageView imageView = (android.widget.ImageView) row.findViewById(R.id.file_imgView);

        Log.d("adapter ",  (FilenameUtils.getExtension(current.getFileName())));
        int index = extension.get(FilenameUtils.getExtension(current.getFileName()));
        Log.d("adapter ", String.valueOf(index));
        // imageView.setImageResource(IMAGE_FILE[index]);
        path.setText(current.getFileName());
        size.setText(current.getTime().toString());

        return row;
    }
}
