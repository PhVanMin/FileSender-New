package com.example.xender.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xender.Loader.ImagesLoader;
import com.example.xender.R;
import com.example.xender.activity.ChooseActivity;
import com.example.xender.activity.SendActivity;
import com.example.xender.adapter.GalleryAdapter;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    GalleryAdapter galleryAdapter;
    List<String> images;
    TextView gallery_number;

    private static final int MY_READ_PERMISSION_CODE = 101;


    public PhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoFragment newInstance(String param1, String param2) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Log.d("ActivitySend", "PhotoFragment ");
        super.onActivityCreated(savedInstanceState);

        gallery_number = getActivity().findViewById(R.id.gallery_number);
        recyclerView = getActivity().findViewById(R.id.recylerview_gallery_images);

        //check permission

        if( (android.os.Build.VERSION.SDK_INT) <= 32)
        {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

            ) {

                ActivityCompat.requestPermissions(getActivity(), new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE}, ChooseActivity.READ_IMAGES_PERMISSION);
            }else {
                loadImages();
            }
        }
        else {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]
                        {Manifest.permission.READ_MEDIA_IMAGES}, ChooseActivity.READ_IMAGES_PERMISSION);
            } else loadImages();
        }



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ChooseActivity sendActivity = (ChooseActivity) getActivity();
        sendActivity.photoFragment = this;
    }

    public void loadImages() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        images = ImagesLoader.listOfImages(getActivity());
        galleryAdapter = new GalleryAdapter(getActivity(), images, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                // do sth
                Log.d("File exist", path);
                Toast.makeText(getActivity(), path, Toast.LENGTH_SHORT).show();
                File imageFile = new File(path);
                if (imageFile.exists()) {
                    Toast.makeText(getActivity(), "File found! " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), SendActivity.class);
                    intent.putExtra("File", imageFile.getAbsolutePath());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "File not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(galleryAdapter);
        String number = String.format(Locale.ENGLISH, "Photos (%d)", images.size());
        gallery_number.setText(number);
    }

}