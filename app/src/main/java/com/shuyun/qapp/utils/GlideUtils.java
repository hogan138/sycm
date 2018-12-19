package com.shuyun.qapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.shuyun.qapp.R;

/**
 * https://blog.csdn.net/my_rabbit/article/details/65937832
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/6/26 11:11
 * Glide的加载工具类
 */
public class GlideUtils {

    /**
     * 加载网络图片
     *
     * @param mContext
     * @param path
     * @param imageview
     */
    public static void LoadImage(Context mContext, String path,
                                 ImageView imageview) {
        Glide.with(mContext).load(path).centerCrop().placeholder(R.mipmap.zw01)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);
    }

    /**
     * 加载带尺寸的图片
     *
     * @param mContext
     * @param path
     * @param Width
     * @param Height
     * @param imageview
     */
    public static void LoadImageWithSize(Context mContext, String path,
                                         int Width, int Height, ImageView imageview) {
        Glide.with(mContext).load(path).override(Width, Height)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);
    }

    /**
     * 加载本地图片
     *
     * @param mContext
     * @param path
     * @param imageview
     */
    public static void LoadImageWithLocation(Context mContext, Integer path,
                                             ImageView imageview) {
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageview);
    }

    /**
     * 圆形加载
     *
     * @param mContext
     * @param path
     * @param imageview
     */
    public static void LoadCircleImage(Context mContext, String path,
                                       ImageView imageview) {
        Glide.with(mContext).load(path).centerCrop()
                .transform(new GlideCircleTransform(mContext, 0, ContextCompat.getColor(mContext, R.color.white)))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);

    }

    static class GlideCircleTransform extends BitmapTransformation {

        private Paint mBorderPaint;
        private float mBorderWidth;

        public GlideCircleTransform(Context context) {
            super(context);
        }

        public GlideCircleTransform(Context context, int borderWidth, int borderColor) {
            super(context);
            mBorderWidth = Resources.getSystem().getDisplayMetrics().density * borderWidth;

            mBorderPaint = new Paint();
            mBorderPaint.setDither(true);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }


        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderWidth / 2));
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            if (mBorderPaint != null) {
                float borderRadius = r - mBorderWidth / 2;
                canvas.drawCircle(r, r, borderRadius, mBorderPaint);
            }
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }
}
