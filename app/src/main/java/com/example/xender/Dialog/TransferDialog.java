package com.example.xender.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.xender.R;

public class TransferDialog extends Dialog {
    ProgressBar pb;
    private int progress = 0;
    private int max = 100;
    private int step = 5;
    public TransferDialog(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        setContentView(R.layout.fragment_transfer_dialog);
        setCancelable(false);

        pb = findViewById(R.id.progressBar);
        pb.setMax(max);
        pb.setProgress(0);

        Button cancel = findViewById(R.id.cancelTransfer);
        cancel.setOnClickListener(v-> {
            dismiss();
        });

        Button pause = findViewById(R.id.pauseTransfer);
        pause.setOnClickListener(v-> {
            TextView info = findViewById(R.id.progressInfo);
            info.setVisibility(View.VISIBLE);
        });

        runProgress();
    }

    private void runProgress() {
        Thread thread = new Thread(() -> {
            while (progress < max) {
                progress += step;
                pb.incrementProgressBy(step);
                TextView info = findViewById(R.id.progressInfo);

                if (progress >= max) {
                    progress = max;
                }
                info.setText("Progress: " + (int)((progress * 100)/max) + "%");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }


}
