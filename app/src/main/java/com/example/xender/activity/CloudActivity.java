package com.example.xender.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;

import com.example.xender.R;

public class CloudActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        toolbar = findViewById(R.id.appbar_send);
        toolbar.setTitle("Cloud");
        toolbar.isBackInvokedCallbackEnabled();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}