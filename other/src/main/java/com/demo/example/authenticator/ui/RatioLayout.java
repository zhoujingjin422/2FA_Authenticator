package com.demo.example.authenticator.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.demo.example.R;


public class RatioLayout extends FrameLayout {
    private float ratio;

    public RatioLayout(Context context) {
        this(context, null, 0);
    }

    public RatioLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RatioLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.ratio = 1.0f;
    }

    public void setRatio(float f) {
        this.ratio = f;
        requestLayout();
    }

    @Override 
    public void onMeasure(int i, int i2) {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.max_viewfinder);
        int min = Math.min(dimensionPixelSize, MeasureSpec.getSize(i));
        super.onMeasure(MeasureSpec.makeMeasureSpec(min, 1073741824), MeasureSpec.makeMeasureSpec(Math.min(dimensionPixelSize, (int) (((float) min) / this.ratio)), 1073741824));
    }
}
