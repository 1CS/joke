package com.lcs.joke.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class BigImageView extends ImageView {
    private float lastY;
    private int moveCount = 0;
    private int maxHeight = 0;
    private int bmWidth, bmHeight;

    public BigImageView(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public BigImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    public BigImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        Matrix matrix = getImageMatrix();
        float scaleFactor = getWidth() / (float) getDrawable().getIntrinsicWidth();
        int picHeight = (int) (bmHeight * scaleFactor);
        if (picHeight > getHeight()) {
            scaleFactor = scaleFactor * 2 / 3;
            float px = (bmWidth * scaleFactor - getWidth()) / 2 / (scaleFactor - 1);
            picHeight = (int) (bmHeight * scaleFactor);
            matrix.setScale(scaleFactor, scaleFactor, px, 0);
        } else {
            float py = (bmHeight * scaleFactor - getHeight()) / 2 / (scaleFactor - 1);
            matrix.setScale(scaleFactor, scaleFactor, 0, py);
        }
        maxHeight = picHeight - getHeight();
        setImageMatrix(matrix);
        return super.setFrame(l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (maxHeight < 0) {
            performClick();
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (moveCount < 4) moveCount++;
                scrollY(event.getY());
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (moveCount < 2) {
                    scrollTo(0, 0);
                    performClick();
                }
                moveCount = 0;
                break;
        }
        return true;
    }

    private void scrollY(float eventY) {
        int moveUpY = (int) (lastY - eventY);
        if (moveUpY < 0) {
            if (-moveUpY > getScrollY()) {
                moveUpY = -getScrollY();
            }
        }
        if (moveUpY > maxHeight - getScrollY()) {
            moveUpY = maxHeight - getScrollY();
        }
        scrollBy(0, moveUpY);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        bmWidth = bm.getWidth();
        bmHeight = bm.getHeight();
    }
}
