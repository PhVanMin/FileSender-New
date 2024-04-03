package com.example.xender.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xender.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCloudActivity extends AppCompatActivity {
    ImageView qr_code;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcloud);

        qr_code=findViewById(R.id.Qr_code_cloud);
        textView=findViewById(R.id.URI_text);
        String uri = getIntent().getStringExtra("QRCODE");
        generateQRCode(uri);
        textView.setText(uri);
    }
    public void generateQRCode(String qrcode){
        try {
            Log.d("QR Cloud", "generateQRCode: "+qrcode);
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix =multiFormatWriter.encode(qrcode, BarcodeFormat.QR_CODE,250,250);
            BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr_code.setImageBitmap(bitmap);

        } catch(Exception e){

        }
    }

}