package bitmap.trainning.google.com.displaybitmapsefficiently.util.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by LENOVO on 2015/11/6.
 * come from "http://developer.android.com/intl/zh-cn/training/displaying-bitmaps/manage-memory.html","http://android-developers.blogspot.com/"
 * bug1:the bitmap shows normally on virtual device,but wrongly on real device
 */

public class BitmapScaleUtils {


    //calculate the scale percentage
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;    //the inSampleSize is more larger,the more small the bitmap will be.
    }

    //decode bitmap from resources
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    //防止图片的像素过大，对图片进行一定的压缩。
    public static Bitmap decodeSampledBitmapFromSelf(Bitmap srcBitmap, int reqWidth, int reqHeight){
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = srcBitmap.getWidth();
        options.outHeight = srcBitmap.getHeight();
        int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //make a copy of the Bitmap
        Bitmap.Config config = srcBitmap.getConfig();
        Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), config);
        Matrix matrix = new Matrix();

        //scale the copybitmap,if possible ,make it smaller to avoid the oom
        matrix.setScale(1.0f/inSampleSize,1.0f/inSampleSize);
        new Canvas(copyBitmap).drawBitmap(srcBitmap, matrix, new Paint());
        return copyBitmap;
    }
}
