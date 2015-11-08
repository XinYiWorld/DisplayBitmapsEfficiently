package bitmap.trainning.google.com.displaybitmapsefficiently.util.extra;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.lang.ref.WeakReference;

import bitmap.trainning.google.com.displaybitmapsefficiently.util.task.BitmapDownloaderTask;

/**
 * Created by LENOVO on 2015/11/7.
 */
public class DownloadedDrawable extends ColorDrawable {
    private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

    public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
        super(Color.RED);
        bitmapDownloaderTaskReference =
                new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
    }

    public BitmapDownloaderTask getBitmapDownloaderTask() {
        return bitmapDownloaderTaskReference.get();
    }
}
