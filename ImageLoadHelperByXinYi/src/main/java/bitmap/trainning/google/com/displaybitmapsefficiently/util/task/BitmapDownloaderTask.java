package bitmap.trainning.google.com.displaybitmapsefficiently.util.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import bitmap.trainning.google.com.displaybitmapsefficiently.util.core.BitmapScaleUtils;
import bitmap.trainning.google.com.displaybitmapsefficiently.util.core.ImageDownloader;
import bitmap.trainning.google.com.displaybitmapsefficiently.util.core.LruCacheUtil;
import bitmap.trainning.google.com.displaybitmapsefficiently.util.extra.DownloadedDrawable;

/**
 * task for download bitmap from internet
 */
public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private Context mContext;
    private int bitmapWidth = 0;
    private int bitmapHeight = 0;

    public  String url;
    private final WeakReference<ImageView> imageViewReference;


    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }


    public BitmapDownloaderTask(Context context ,ImageView imageView) {
        mContext = context;
        imageViewReference = new WeakReference<ImageView>(imageView);

        if(imageView.getWidth() != 0 && imageView.getHeight() != 0)
        {
            bitmapWidth = imageView.getWidth();
            bitmapHeight = imageView.getHeight() ;
        }else{
            //if the developer didn't indicate the width and height,then base on the size of window
            WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wManager.getDefaultDisplay();
            bitmapWidth = display.getWidth();
            bitmapHeight = display.getHeight();
        }
    }

    public BitmapDownloaderTask(Context context ,ImageView imageView,int[] size) {
        mContext = context;
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        bitmapWidth = size[0];
        bitmapHeight = size[1];
    }


    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        Bitmap bitmap = ImageDownloader.downloadBitmap(params[0]);
        //save the loaded bitmap to cache
        if(bitmap != null){
            //scale the original picture to avoid the oom
            bitmap = BitmapScaleUtils.decodeSampledBitmapFromSelf(bitmap, bitmapWidth, bitmapHeight);
            LruCacheUtil.addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
        }
        return bitmap;
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
            // Change bitmap only if this process is still associated with it
            if (this == bitmapDownloaderTask) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }
}
