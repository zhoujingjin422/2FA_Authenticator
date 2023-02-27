package com.demo.example.authenticator;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.demo.example.authenticator.ui.CameraView;
import com.demo.example.authenticator.util.CameraFocusFix;

import com.demo.example.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;


public class BarcodeActivity extends BaseActivity {
    public static final String EXTRA_STR_QRCODE = "extra_qr";
    public static final int RESULT_MANUAL = 1;
    public static final String TAG = BarcodeActivity.class.getSimpleName();
    protected CameraView mCameraView;
    protected TextView mMessageView;

    @Override
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (canHideStatusBar()) {
            getWindow().setFlags(512, 512);
        }
        setContentView(R.layout.activity_camera);
        this.mCameraView = (CameraView) findViewById(R.id.camera_surfaceview);
        this.mMessageView = (TextView) findViewById(R.id.tv_camera_error);
        BarcodeDetector build = new BarcodeDetector.Builder(this).setBarcodeFormats(256).build();
        final CameraSource build2 = new CameraSource.Builder(this, build).setAutoFocusEnabled(true).setRequestedFps(30.0f).build();
        this.mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() { 
            @Override 
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            }

            @Override 
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ContextCompat.checkSelfPermission(BarcodeActivity.this, "android.permission.CAMERA") == 0) {
                        build2.start(BarcodeActivity.this.mCameraView.getHolder());
                        CameraFocusFix.cameraFocus(build2, "continuous-picture");
                        BarcodeActivity.this.mCameraView.setCameraSource(build2);
                    }
                } catch (IOException unused) {
                    BarcodeActivity.this.mMessageView.setText(R.string.error_camera_open);
                }
            }

            @Override 
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                build2.stop();
            }
        });
        build.setProcessor(new Detector.Processor<Barcode>() { 
            @Override 
            public void release() {
            }

            @Override 
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> detectedItems = detections.getDetectedItems();
                if (detectedItems.size() != 0) {
                    BarcodeActivity.this.mMessageView.post(new Runnable() { 
                        @Override 
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra(BarcodeActivity.EXTRA_STR_QRCODE, ((Barcode) detectedItems.valueAt(0)).displayValue);
                            BarcodeActivity.this.setResult(-1, intent);
                            BarcodeActivity.this.finish();
                        }
                    });
                }
            }

            public void lambda$receiveDetections$0$BarcodeActivity$2(SparseArray sparseArray) {
                Intent intent = new Intent();
                intent.putExtra(BarcodeActivity.EXTRA_STR_QRCODE, ((Barcode) sparseArray.valueAt(0)).displayValue);
                BarcodeActivity.this.setResult(-1, intent);
                BarcodeActivity.this.finish();
            }
        });
        revealThisActivity(R.id.root_camera, bundle, new Runnable() { 
            @Override 
            public final void run() {
                BarcodeActivity.this.lambda$onCreate$1$BarcodeActivity();
            }
        });
    }

    public void lambda$onCreate$1$BarcodeActivity() {
        this.mCameraView.setVisibility(View.VISIBLE);
    }

    public void lambda$onBackPressed$2$BarcodeActivity() {
        super.onBackPressed();
    }

    @Override
    
    public void onBackPressed() {
        this.mCameraView.setVisibility(View.VISIBLE);
        this.mCameraView.post(new Runnable() { 
            @Override 
            public final void run() {
                BarcodeActivity.this.lambda$onBackPressed$2$BarcodeActivity();
            }
        });
    }
}
