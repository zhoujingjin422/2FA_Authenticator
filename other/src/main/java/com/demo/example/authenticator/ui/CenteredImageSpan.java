package com.demo.example.authenticator.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import java.lang.ref.WeakReference;


public class CenteredImageSpan extends ImageSpan {
    private int extraHorSpace;
    private int extraSpace;
    private int initialDescent;
    private WeakReference<Drawable> mDrawableRef;

    public CenteredImageSpan(Drawable drawable) {
        this(drawable, 0);
    }

    public CenteredImageSpan(Drawable drawable, int i) {
        this(drawable, i, 0);
    }

    public CenteredImageSpan(Drawable drawable, int i, int i2) {
        super(drawable, i);
        this.initialDescent = 0;
        this.extraSpace = 0;
        this.extraHorSpace = 0;
        this.extraHorSpace = i2;
    }

    public Rect getBounds() {
        return getDrawable().getBounds();
    }

    @Override 
    public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
        Drawable cachedDrawable = getCachedDrawable();
        int intrinsicHeight = cachedDrawable.getIntrinsicHeight();
        canvas.save();
        canvas.translate(f, (float) (((i5 - cachedDrawable.getBounds().bottom) / 2) + (((intrinsicHeight - paint.getFontMetricsInt().descent) + paint.getFontMetricsInt().ascent) / 2) + 24));
        cachedDrawable.draw(canvas);
        canvas.restore();
    }

    @Override 
    public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
        Rect bounds = getCachedDrawable().getBounds();
        if (fontMetricsInt != null) {
            int i3 = bounds.bottom;
            int i4 = fontMetricsInt.descent;
            int i5 = i3 - (i4 - fontMetricsInt.ascent);
            if (i5 >= 0) {
                this.initialDescent = i4;
                this.extraSpace = i5;
            }
            int i6 = (this.extraSpace / 2) + this.initialDescent;
            fontMetricsInt.descent = i6;
            fontMetricsInt.bottom = i6;
            int i7 = (-bounds.bottom) + i6;
            fontMetricsInt.ascent = i7;
            fontMetricsInt.top = i7;
        }
        return bounds.right + this.extraHorSpace;
    }

    private Drawable getCachedDrawable() {
        WeakReference<Drawable> weakReference = this.mDrawableRef;
        Drawable drawable = weakReference != null ? weakReference.get() : null;
        if (drawable != null) {
            return drawable;
        }
        Drawable drawable2 = getDrawable();
        this.mDrawableRef = new WeakReference<>(drawable2);
        return drawable2;
    }
}
