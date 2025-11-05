package com.xlocker.support.preference.colorpicker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Simple checkboard pattern used for representing the alpha channel.
 */
public class AlphaPatternDrawable extends Drawable {

    private final Paint bitmapPaint = new Paint();
    private final Paint evenPaint = new Paint();
    private final Paint oddPaint = new Paint();
    private final int squareSize;

    private int numColumns;
    private int numRows;
    private Bitmap patternBitmap;

    public AlphaPatternDrawable(int squareSize) {
        this.squareSize = squareSize;
        this.evenPaint.setColor(-1);
        this.oddPaint.setColor(-3421237);
    }

    private void generatePatternBitmap() {
        if (getBounds().width() <= 0 || getBounds().height() <= 0) {
            return;
        }
        patternBitmap = Bitmap.createBitmap(getBounds().width(), getBounds().height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(patternBitmap);
        Rect rect = new Rect();
        boolean shouldUseEvenColor = true;
        for (int row = 0; row <= numRows; row++) {
            boolean useEvenColorInRow = shouldUseEvenColor;
            for (int column = 0; column <= numColumns; column++) {
                rect.top = squareSize * row;
                rect.left = squareSize * column;
                rect.bottom = rect.top + squareSize;
                rect.right = rect.left + squareSize;
                canvas.drawRect(rect, useEvenColorInRow ? evenPaint : oddPaint);
                useEvenColorInRow = !useEvenColorInRow;
            }
            shouldUseEvenColor = !shouldUseEvenColor;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (patternBitmap != null) {
            canvas.drawBitmap(patternBitmap, null, getBounds(), bitmapPaint);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        numColumns = (int) Math.ceil(bounds.width() / (float) squareSize);
        numRows = (int) Math.ceil(bounds.height() / (float) squareSize);
        generatePatternBitmap();
    }

    @Override
    public void setAlpha(int alpha) {
        throw new UnsupportedOperationException("Alpha is not supported by this drawable.");
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        throw new UnsupportedOperationException("ColorFilter is not supported by this drawable.");
    }
}
