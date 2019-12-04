package com.runacr.android.runacr_sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.runacr.android.runacrsdk.InitListener;
import com.runacr.android.runacrsdk.RunACR;
import com.runacr.android.runacrsdk.RecognizeListener;
import com.runacr.android.runacrsdk.RecognizeResult;
import com.runacr.android.runacrsdk.RunACRError;

public class MainActivity extends AppCompatActivity {

    private TextView statusTextView;
    private Button buttonButton;
    private TextView resultTextView;

    private int REQUEST_RECORD_AUDIO = 1;

    // Helper class for copying acr file from assets
    private AssetsHelper assetsHelper = new AssetsHelper(this);

    // file which you've got from http://www.runacr.com
    private String runACRFile = "example.runacr";
    private String runACRAPIKey = "045ce2e6853c2a1183f06fb5c0ed0850";


    private static final String RESULT_IPHONE = "iPhone 7 – Design";
    private static final String RESULT_SAMSUNG = "Samsung - Surf - The Snail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = (TextView) findViewById(R.id.status);
        buttonButton = (Button) findViewById(R.id.button);
        resultTextView = (TextView) findViewById(R.id.result);

        buttonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryRecognize();
            }
        });

        // Init RunACR after copying file
        assetsHelper.copyAssetFile(runACRFile, new AssetsHelper.OnFileIsReady() {
            @Override
            public void onFileIsReady(String filePath) {
                RunACR.init(MainActivity.this, runACRAPIKey, filePath, initListener);
            }
        });
    }

    private InitListener initListener = new InitListener() {

        @Override
        public void onSuccess() {
            statusTextView.setText("Status: Ready");
            buttonButton.setEnabled(true);
        }

        @Override
        public void onFailure(int errorCode) {
            statusTextView.setText("Status: Error (code " + errorCode + ")");
            // look for error code in RunACRError class

            buttonButton.setEnabled(false);
        }
    };

    private RecognizeListener recognizeListener = new RecognizeListener() {
        @Override
        public void onStart() {
            statusTextView.setText("Status: Recognizing...");
            buttonButton.setEnabled(false);
        }

        @Override
        public void onStop() {
            statusTextView.setText("Status: Recognizing ends");
            buttonButton.setEnabled(true);
        }

        @Override
        public void onSuccess(RecognizeResult recognizeResult) {
            String trackName = "";
            if(recognizeResult.id == 1) {
                trackName = RESULT_IPHONE;
            } else if(recognizeResult.id == 2) {
                trackName = RESULT_SAMSUNG;
            }

            resultTextView.setText(trackName + "\n" + getTime(recognizeResult.relativeTimeOffset));
        }

        @Override
        public void onFailure(final int errorCode) {
            // look for error code in RunACRError class
            //if(errorCode == RunACRError.CANNOT_RECOGNIZE) {
            //    ...
            //} else if(errorCode == RunACRError.INVALID_LICENCE_ERROR1) {
            //    ...
            //}

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RunACR.recognize(recognizeListener);
                }
            }, 100);
        }
    };

    private String getTime(double s) {
        int hours = ((int) s)/ 3600;
        int minutes = (((int) s) % 3600) / 60;
        int seconds = ((int) s) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void tryRecognize() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            requestAudioRecordPermission(REQUEST_RECORD_AUDIO);
        } else {
            doRecognize();
        }
    }

    private void doRecognize() {
        RunACR.recognize(recognizeListener);
    }

    private void requestAudioRecordPermission(int request) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doRecognize();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
