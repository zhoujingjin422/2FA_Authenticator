package com.demo.example.authenticator.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;


public class CameraView extends SurfaceView {
    private Size mPreviewSize;

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CameraView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CameraView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setCameraSource(CameraSource cameraSource) {
        this.mPreviewSize = cameraSource.getPreviewSize();
        requestLayout();
    }

    @Override 
    public void onMeasure(int i, int i2) {
        int resolveSize = SurfaceView.resolveSize(getSuggestedMinimumWidth(), i);
        int resolveSize2 = SurfaceView.resolveSize(getSuggestedMinimumHeight(), i2);
        Size size = this.mPreviewSize;
        if (size != null) {
            float width = ((float) size.getWidth()) / ((float) this.mPreviewSize.getHeight());
            float f = (float) resolveSize;
            float f2 = (float) resolveSize2;
            if (width < f / f2) {
                int i3 = (int) (f / width);
                setY(getY() - (((float) (i3 - resolveSize2)) * 0.5f));
                resolveSize2 = i3;
            } else {
                int i4 = (int) (f2 / width);
                setX(getX() - (((float) (i4 - resolveSize)) * 0.5f));
                resolveSize = i4;
            }
        }
        setMeasuredDimension(resolveSize, resolveSize2);
    }
}
