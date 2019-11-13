package com.example.scanner_qr.Animations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import com.example.scanner_qr.R;
import me.dm7.barcodescanner.core.DisplayUtils;
import me.dm7.barcodescanner.core.ViewFinderView;

public class Ani extends ViewFinderView
{
    private Rect mFramingRect;
    private static final int POINT_SIZE = 10;
    private static final long ANIMATION_DELAY = 2L;

    private final int mDefaultLaserColor;
    private final int mDefaultMaskColor;
    private final int mDefaultBorderColor;
    private final int mDefaultBorderStrokeWidth;
    private final int mDefaultBorderLineLength;

    protected Paint mLaserPaint;
    protected Paint mFinderMaskPaint;
    protected Paint mBorderPaint;
    protected int mBorderLineLength;
    protected boolean mSquareViewFinder;
    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private int scannerAlpha;

    private int cntr = 0;
    private boolean goingup = false;


    public Ani(Context context) {
        super(context);

        this.mDefaultLaserColor = this.getResources().getColor(R.color.viewfinder_laser);
        this.mDefaultMaskColor = this.getResources().getColor(R.color.viewfinder_mask);
        this.mDefaultBorderColor = this.getResources().getColor(R.color.viewfinder_border);
        this.mDefaultBorderStrokeWidth = 10;
        this.mDefaultBorderLineLength = 100;
        this.init();

    }

    private void init()
    {
        this.mLaserPaint = new Paint();
        this.mLaserPaint.setColor(this.mDefaultLaserColor);
        this.mLaserPaint.setStyle(Paint.Style.FILL);
        this.mFinderMaskPaint = new Paint();
        this.mFinderMaskPaint.setColor(this.mDefaultMaskColor);
        this.mBorderPaint = new Paint();
        this.mBorderPaint.setColor(this.mDefaultBorderColor);
        this.mBorderPaint.setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setStrokeWidth((float)this.mDefaultBorderStrokeWidth);
        this.mBorderLineLength = this.mDefaultBorderLineLength;
    }

    public void setLaserColor(int laserColor)
    {
        this.mLaserPaint.setColor(laserColor);
    }

    public void setMaskColor(int maskColor)
    {
        this.mFinderMaskPaint.setColor(maskColor);
    }

    public void setBorderColor(int borderColor)
    {
        this.mBorderPaint.setColor(borderColor);
    }

    public void setBorderStrokeWidth(int borderStrokeWidth)
    {
        this.mBorderPaint.setStrokeWidth((float)borderStrokeWidth);
    }

    public void setBorderLineLength(int borderLineLength) {
        this.mBorderLineLength = borderLineLength;
    }

    public void setSquareViewFinder(boolean set)
    {
        this.mSquareViewFinder = set;
    }

    public void setupViewFinder() {
        this.updateFramingRect();
        this.invalidate();
    }

    public Rect getFramingRect()
    {
        return this.mFramingRect;
    }

    public void onDraw(Canvas canvas) {
        if (this.getFramingRect() != null) {
            this.drawViewFinderMask(canvas);
            this.drawViewFinderBorder(canvas);
            this.drawLaser(canvas);
            this.drawTexture(canvas);
//            this.drawSome(canvas);
        }
    }

    public void drawViewFinderMask(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Rect framingRect = this.getFramingRect();
        canvas.drawRect(0.0F, 0.0F, (float)width, (float)framingRect.top, this.mFinderMaskPaint);
        canvas.drawRect(0.0F, (float)framingRect.top, (float)framingRect.left, (float)(framingRect.bottom + 1), this.mFinderMaskPaint);
        canvas.drawRect((float)(framingRect.right + 1), (float)framingRect.top, (float)width, (float)(framingRect.bottom + 1), this.mFinderMaskPaint);
        canvas.drawRect(0.0F, (float)(framingRect.bottom + 1), (float)width, (float)height, this.mFinderMaskPaint);
    }

    public void drawViewFinderBorder(Canvas canvas) {
        Rect framingRect = this.getFramingRect();
        canvas.drawLine((float)(framingRect.left - 1), (float)(framingRect.top - 1), (float)(framingRect.left - 1), (float)(framingRect.top - 1 + this.mBorderLineLength), this.mBorderPaint);
        canvas.drawLine((float)(framingRect.left - 1), (float)(framingRect.top - 1), (float)(framingRect.left - 1 + this.mBorderLineLength), (float)(framingRect.top - 1), this.mBorderPaint);
        canvas.drawLine((float)(framingRect.left - 1), (float)(framingRect.bottom + 1), (float)(framingRect.left - 1), (float)(framingRect.bottom + 1 - this.mBorderLineLength), this.mBorderPaint);
        canvas.drawLine((float)(framingRect.left - 1), (float)(framingRect.bottom + 1), (float)(framingRect.left - 1 + this.mBorderLineLength), (float)(framingRect.bottom + 1), this.mBorderPaint);
        canvas.drawLine((float)(framingRect.right + 1), (float)(framingRect.top - 1), (float)(framingRect.right + 1), (float)(framingRect.top - 1 + this.mBorderLineLength), this.mBorderPaint);
        canvas.drawLine((float)(framingRect.right + 1), (float)(framingRect.top - 1), (float)(framingRect.right + 1 - this.mBorderLineLength), (float)(framingRect.top - 1), this.mBorderPaint);
        canvas.drawLine((float)(framingRect.right + 1), (float)(framingRect.bottom + 1), (float)(framingRect.right + 1), (float)(framingRect.bottom + 1 - this.mBorderLineLength), this.mBorderPaint);
        canvas.drawLine((float)(framingRect.right + 1), (float)(framingRect.bottom + 1), (float)(framingRect.right + 1 - this.mBorderLineLength), (float)(framingRect.bottom + 1), this.mBorderPaint);
    }

    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        this.updateFramingRect();
    }


    @Override
    public void drawLaser(Canvas canvas)
    {

        mLaserPaint.setAlpha(155);
        int middle = mFramingRect.height() / 2 + mFramingRect.top;
        middle = middle + cntr;
        if ((cntr < 300) && (!goingup)) {
            canvas.drawRect(mFramingRect.left + 2,
                    middle - 5,
                    mFramingRect.right - 1,
                    middle + 10,
                    mLaserPaint);
            cntr = cntr + 4;
        }

        if ((cntr >= 300) && (!goingup)) goingup = true;

        if ((cntr > -300) && (goingup)) {
            canvas.drawRect(mFramingRect.left + 4,
                    middle - 5,
                    mFramingRect.right - 1,
                    middle + 10,
                    mLaserPaint);
            cntr = cntr - 4;
        }

        if ((cntr <= -300) && (goingup)) goingup = false;

        postInvalidateDelayed(ANIMATION_DELAY,
                mFramingRect.left - POINT_SIZE,
                mFramingRect.top - POINT_SIZE,
                mFramingRect.right + POINT_SIZE,
                mFramingRect.bottom + POINT_SIZE);

    }

    public void drawTexture (Canvas canvas) {
        int a = 200;
        int b = 220;
        int c = 400;
        int d = 440;

        Paint paint = new Paint();
        paint.setColor(Color.RED);

        c = c + 100;
        d = d + 100;

        canvas.drawRect(a,b,c,d,paint);
        invalidate();
    }

    public synchronized void updateFramingRect() {
        Point viewResolution = new Point(this.getWidth(), this.getHeight());
        int orientation = DisplayUtils.getScreenOrientation(this.getContext());
        int width;
        int height;
        if (this.mSquareViewFinder) {
            if (orientation != 1) {
                height = (int)((float)this.getHeight() * 0.625F);
                width = height;
            } else {
                width = (int)((float)this.getWidth() * 0.625F);
                height = width;
            }
        } else if (orientation != 1) {
            height = (int)((float)this.getHeight() * 0.625F);
            width = (int)(1.4F * (float)height);
        } else {
            width = (int)((float)this.getWidth() * 0.75F);
            height = (int)(0.75F * (float)width);
        }

        if (width > this.getWidth()) {
            width = this.getWidth() - 50;
        }

        if (height > this.getHeight()) {
            height = this.getHeight() - 50;
        }

        int leftOffset = (viewResolution.x - width) / 2;
        int topOffset = (viewResolution.y - height) / 2;
        this.mFramingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
    }
}
