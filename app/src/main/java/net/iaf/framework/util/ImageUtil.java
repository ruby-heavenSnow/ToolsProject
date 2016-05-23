/**
 * @Description: TODO
 * @author: Administrator
 * @version: 1.0
 * @see
 */

package net.iaf.framework.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 图片处理工具类
 *
 * @author Bob
 */
public class ImageUtil {

    public static void saveBitmap2File(Bitmap mBitmap, String filePath) throws IOException {
        File f = new File(filePath);
        f.createNewFile();
        FileOutputStream fOut = null;
        fOut = new FileOutputStream(f);
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        fOut.flush();
        fOut.close();
    }

    // 将图片圆角显示的函数,返回Bitmap
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

        // Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
        // bitmap.getHeight(), Config.ARGB_8888);
        // Canvas canvas = new Canvas(output);
        //
        // // DisplayMetrics dm =
        // MyApplication.getAppContext().getResources().getDisplayMetrics();
        //
        // final int color = 0xff424242;
        // final Paint paint = new Paint();
        // // 根据原来图片大小画一个矩形
        // final Rect rect = new Rect(0, 0, bitmap.getWidth(),
        // bitmap.getHeight());
        // final RectF rectF = new RectF(rect);
        // // 圆角弧度参数,数值越大圆角越大,甚至可以画圆形
        // final float roundPx = 4 * ((bitmap.getWidth())/ 50);
        //
        // paint.setAntiAlias(true);
        // canvas.drawARGB(0, 0, 0, 0);
        // paint.setColor(color);
        // // 画出一个圆角的矩形
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        // // 取两层绘制交集,显示上层
        // paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // // 显示图片
        // canvas.drawBitmap(bitmap, rect, rect, paint);
        //
        // int width = output.getWidth();
        // int height = output.getHeight();
        // int nLength = width * height * 4;
        //
        // int[] pixels = new int[nLength];
        // output.getPixels(pixels, 0, width, 0, 0, width, height);
        // bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        // output.recycle();
        // output = null;
        // 返回Bitmap对象
        return bitmap;
    }

    /**
     * 从资源获取默认图片
     *
     * @param res
     * @param resid
     * @return
     */
    public static Bitmap getDefaultBitmap(Resources res, int resid) {
        return BitmapFactory.decodeResource(res, resid);
    }

    /**
     * 从资源获取圆角默认图片
     *
     * @param res
     * @param resid
     * @return
     */
    public static Bitmap getDefaultBitmapRoundedCorner(Resources res, int resid) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, resid);

        // Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
        // bitmap.getHeight(), Config.ARGB_8888);
        // Canvas canvas = new Canvas(output);
        //
        // // DisplayMetrics dm =
        // MyApplication.getAppContext().getResources().getDisplayMetrics();
        //
        // final int color = 0xff424242;
        // final Paint paint = new Paint();
        // // 根据原来图片大小画一个矩形
        // final Rect rect = new Rect(0, 0, bitmap.getWidth(),
        // bitmap.getHeight());
        // final RectF rectF = new RectF(rect);
        // // 圆角弧度参数,数值越大圆角越大,甚至可以画圆形
        // final float roundPx = 4 * ((bitmap.getWidth())/ 50);
        //
        // paint.setAntiAlias(true);
        // canvas.drawARGB(0, 0, 0, 0);
        // paint.setColor(color);
        // // 画出一个圆角的矩形
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        // // 取两层绘制交集,显示上层
        // paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // // 显示图片
        // canvas.drawBitmap(bitmap, rect, rect, paint);
        //
        // bitmap.recycle();
        // bitmap = null;

        return bitmap;
    }

    /**
     * drawable到Bitmap的转换方法
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap byteBuffer2Bitmap(ByteBuffer data, int maxSize) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeByteArray(data.array(), 0,
                data.limit(), o);
        if (bm != null) {
            bm.recycle();
            bm = null;
        }
        int sampleSize = 1;
        if (o.outHeight > maxSize || o.outWidth > maxSize) {
            sampleSize = (int) Math.pow(
                    2,
                    (int) Math.round(Math.log(maxSize
                            / (double) Math.max(o.outHeight, o.outWidth))
                            / Math.log(0.5)));
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = sampleSize;
        return BitmapFactory.decodeByteArray(data.array(), 0, data.limit(),
                opts);
    }

    /**
     * 从文件解析出Bitmap格式的图片
     *
     * @param path
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return
     */
    public static void decodeFile(String path, int maxWidth, int maxHeight) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap image = null;
        image = BitmapFactory.decodeFile(path, options);
        maxWidth = maxHeight = 300;
        double ratio = 1D;
        if (maxWidth > 0 && maxHeight <= 0) {
            // 限定宽度，高度不做限制
            ratio = Math.ceil(options.outWidth / maxWidth);
        } else if (maxHeight > 0 && maxWidth <= 0) {
            // 限定高度，不限制宽度
            ratio = Math.ceil(options.outHeight / maxHeight);
        } else if (maxWidth > 0 && maxHeight > 0) {
            // 高度和宽度都做了限制，这时候我们计算在这个限制内能容纳的最大的图片尺寸，不会使图片变形
            double _widthRatio = Math.ceil(options.outWidth / maxWidth);
            double _heightRatio = (double) Math.ceil(options.outHeight
                    / maxHeight);
            ratio = _widthRatio > _heightRatio ? _widthRatio : _heightRatio;
        }
        if (ratio > 1) {
            options.inSampleSize = (int) ratio;
        }
        options.inSampleSize = calcScaleRatio(maxWidth, maxHeight,
                options.outWidth, options.outHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        image = BitmapFactory.decodeFile(path, options);
        saveBitmap2File(compressImage(image), path);
//        return compressImage(image);
    }

    private static int calcScaleRatio(int maxWidth, int maxHeight,
                                      int outWidth, int outHeight) {
        // TODO Auto-generated method stub
        return (outHeight / maxHeight + outWidth / maxWidth) / 2;
    }

    private static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}
