package com.demo.example.authenticator.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.demo.example.R;


public class ProgressTextView extends AppCompatTextView {
    private int mColorBg;
    private int mColorFg;
    private int mColorWarning;
    private Paint mPaintBg;
    private Paint mPaintText;
    private float mProgress;
    private Shader mShader;
    private final Rect mTextBounds = new Rect();

    public ProgressTextView(Context context) {
        super(context);
        init();
    }

    public ProgressTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ProgressTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        Paint paint = new Paint();
        this.mPaintBg = paint;
        paint.setColor(this.mColorBg);
        this.mPaintBg.setStyle(Paint.Style.FILL);
        this.mPaintText = getPaint();
        this.mColorBg = ContextCompat.getColor(getContext(), R.color.colorDefaultAnim);
        this.mColorFg = ContextCompat.getColor(getContext(), R.color.colorTextBlue);
        this.mColorWarning = ContextCompat.getColor(getContext(), R.color.colorPink);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setProgress(isInEditMode() ? 50.0f : 0.0f);
    }

    public void setProgress(float f) {
        this.mProgress = f;
        this.mShader = null;
        invalidate();
    }

    public void setmColorBg(int i) {
        this.mColorBg = i;
    }

    @Override 
    public void onDraw(Canvas canvas) {
        if (this.mShader == null) {
            Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(createBitmap);
            this.mPaintBg.setColor(this.mColorBg);
            canvas2.drawRect(0.0f, 0.0f, (float) createBitmap.getWidth(), (float) createBitmap.getHeight(), this.mPaintBg);
            this.mPaintBg.setColor(this.mProgress < 25.0f ? this.mColorWarning : this.mColorFg);
            this.mPaintText.getTextBounds(getText().toString(), 0, getText().length(), this.mTextBounds);
            canvas2.drawRect((float) getPaddingLeft(), 0.0f, ((this.mProgress * ((float) Math.min(this.mTextBounds.width(), createBitmap.getWidth()))) / 100.0f) + ((float) getPaddingLeft()), (float) createBitmap.getHeight(), this.mPaintBg);
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            BitmapShader bitmapShader = new BitmapShader(createBitmap, tileMode, tileMode);
            this.mShader = bitmapShader;
            this.mPaintText.setShader(bitmapShader);
        }
        canvas.drawText(getText().toString(), (float) getPaddingLeft(), ((float) (getHeight() + this.mTextBounds.height())) * 0.5f, this.mPaintText);
    }
}
