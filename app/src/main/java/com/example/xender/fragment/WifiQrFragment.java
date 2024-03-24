package com.example.xender.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.xender.R;
import com.example.xender.activity.QRActivity;
import com.example.xender.activity.SendActivity;
import com.example.xender.wifi.WifiDirectBroadcastReceiver;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WifiQrFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WifiQrFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String address;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView qr_code;
    QRActivity activity;
    public WifiQrFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WifiQRFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WifiQrFragment newInstance(String param1, String param2) {
        WifiQrFragment fragment = new WifiQrFragment();
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
        return inflater.inflate(R.layout.fragment_wifi_qr, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        qr_code= getActivity().findViewById(R.id.Qr_code);
//        QRActivity activity = (QRActivity) getActivity();

    }

    public void generateQRCode(String qrcode){
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix =multiFormatWriter.encode(qrcode, BarcodeFormat.QR_CODE,250,250);
            BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr_code.setImageBitmap(bitmap);

        } catch(Exception e){

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        QRActivity qrActivity = (QRActivity) getActivity();
        qrActivity.wifiQrFragment= this;
    }

}