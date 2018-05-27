package com.sahni.rahul.ieee_niec.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.sahni.rahul.ieee_niec.R;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.widget.Toast;

public class ScanQrCodeActivity extends AppCompatActivity {

    private static final String TAG = ScanQrCodeActivity.class.getSimpleName();
    private CameraSource.Builder mCameraBuilder;
    private boolean isSurfaceCreated = false;
    private CameraSource mCameraSource;
    private SurfaceView mSurfaceView;
    private SurfaceCallBack mSurfaceCallBack;
    private BarcodeProcessorDetector mProcessorDetector;
    private BarcodeDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView scanLine = findViewById(R.id.scan_line_image_view);
        mSurfaceView = findViewById(R.id.surfaceView);
        Animation translate = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, -0.5f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.5f);
        translate.setDuration(1000);
        translate.setRepeatCount(Animation.INFINITE);
        translate.setRepeatMode(Animation.REVERSE);
        translate.setInterpolator(new LinearInterpolator());
        scanLine.setAnimation(translate);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        if(mDetector == null) {
            mDetector = new BarcodeDetector.Builder(this)
                    .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                    .build();
        }
        mDetector.setProcessor(getBarcodeProcessorDetector());
        mCameraBuilder = getCameraSourceBuilder(mDetector);
        mCameraSource = mCameraBuilder.build();
        mSurfaceView.getHolder().addCallback(addSurfaceCallBack());
    }

    private CameraSource.Builder getCameraSourceBuilder(BarcodeDetector detector) {
        if (mCameraBuilder == null) {
            mCameraBuilder = new CameraSource.Builder(this, detector)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setAutoFocusEnabled(true);
        }
        return mCameraBuilder;
    }

    @SuppressLint("MissingPermission")
    private void startCamera(CameraSource cameraSource, SurfaceView surfaceView) {
        if (isSurfaceCreated) {
            try {
                cameraSource.start(surfaceView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SurfaceCallBack implements SurfaceHolder.Callback{
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            isSurfaceCreated = true;
            startCamera(mCameraSource, mSurfaceView);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if(mCameraSource != null) {
                mCameraSource.release();
            }
        }
    }
    private SurfaceCallBack addSurfaceCallBack(){
        if(mSurfaceCallBack == null){
            mSurfaceCallBack = new SurfaceCallBack();
        }
        return mSurfaceCallBack;
    }

    private class BarcodeProcessorDetector implements Detector.Processor<Barcode> {
        @Override
        public void release() {

        }

        @Override
        public void receiveDetections(Detector.Detections<Barcode> detections) {
            SparseArray<Barcode> barcodes = detections.getDetectedItems();
            if (barcodes.size() > 0) {
                String data = barcodes.valueAt(0).displayValue;
                if (data.startsWith("https:") || data.startsWith("http:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                        finish();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ScanQrCodeActivity.this, "URL not found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
                mDetector.release();
            }

        }
    }

    private BarcodeProcessorDetector getBarcodeProcessorDetector(){
        if(mProcessorDetector == null){
            mProcessorDetector = new BarcodeProcessorDetector();
        }
        return mProcessorDetector;
    }

}
