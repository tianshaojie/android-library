package cn.skyui.library.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class GlideCircleTransform extends BitmapTransformation {

    private static final String ID = "cn.skyui.library.glide.GlideCircleTransform";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private int mBorderColor;
    private int mBorderWidth;

    public GlideCircleTransform() {
    }

    public GlideCircleTransform(int borderColor, int borderWidth) {
        mBorderColor = borderColor;
        mBorderWidth = borderWidth;

        Paint mBorderPaint = new Paint();
        mBorderPaint.setDither(true);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }


    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap circle = TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight);
        return addBorderToCircularBitmap(circle, mBorderWidth, mBorderColor);
    }

    // Custom method to add a border around circular bitmap
    protected Bitmap addBorderToCircularBitmap(Bitmap srcBitmap, int borderWidth, int borderColor){
        // Calculate the circular bitmap width with border
        int dstBitmapWidth = srcBitmap.getWidth()+borderWidth*2;

        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(dstBitmap);
        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);

        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);

        /*
            public void drawCircle (float cx, float cy, float radius, Paint paint)
                Draw the specified circle using the specified paint. If radius is <= 0, then nothing
                will be drawn. The circle will be filled or framed based on the Style in the paint.

            Parameters
                cx : The x-coordinate of the center of the cirle to be drawn
                cy : The y-coordinate of the center of the cirle to be drawn
                radius : The radius of the cirle to be drawn
                paint : The paint used to draw the circle
        */
        // Draw the circular border around circular bitmap
        canvas.drawCircle(
                canvas.getWidth() / 2, // cx
                canvas.getWidth() / 2, // cy
                canvas.getWidth()/2 - borderWidth / 2, // Radius
                paint // Paint
        );

        // Free the native object associated with this bitmap.
        srcBitmap.recycle();

        // Return the bordered circular bitmap
        return dstBitmap;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GlideCircleTransform) {
            GlideCircleTransform other = (GlideCircleTransform) o;
            return mBorderColor == other.mBorderColor && mBorderWidth == other.mBorderWidth;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(),
                Util.hashCode(mBorderColor + mBorderWidth));
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        byte[] radiusData = ByteBuffer.allocate(4).putInt(mBorderColor + mBorderWidth).array();
        messageDigest.update(radiusData);
    }
}
