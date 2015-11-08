package bitmap.trainning.google.com.displaybitmapsefficiently.util.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import bitmap.trainning.google.com.displaybitmapsefficiently.util.core.BitmapScaleUtils;
import bitmap.trainning.google.com.displaybitmapsefficiently.util.core.LruCacheUtil;

/*
task for download bitmap from local resources
 */
public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
    private Context mContext;
    private final WeakReference<ImageView> imageViewReference;
    private int data = 0;
    private int bitmapWidth = 0;
    private int bitmapHeight = 0;

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }

    public BitmapWorkerTask(Context context ,ImageView imageView) {
        mContext = context;
        // Use a WeakReference to ensure the ImageView can be garbage collected
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

    public BitmapWorkerTask(Context context ,ImageView imageView,int[] size) {
        mContext = context;
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        bitmapWidth = size[0];
        bitmapHeight = size[1];
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
        data = params[0];
        Bitmap bitmap = BitmapScaleUtils.decodeSampledBitmapFromResource(mContext.getResources(), data, bitmapWidth, bitmapHeight);
        //save the loaded bitmap to cache
        if(bitmap != null){
            LruCacheUtil.addBitmapToMemoryCache(String.valueOf(data),bitmap);
        }
        return bitmap;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}