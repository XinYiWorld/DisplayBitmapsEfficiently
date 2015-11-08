package bitmap.trainning.google.com.displaybitmapsefficiently.util.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.google.common.io.Closer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import bitmap.trainning.google.com.displaybitmapsefficiently.util.extra.DownloadedDrawable;
import bitmap.trainning.google.com.displaybitmapsefficiently.util.task.BitmapDownloaderTask;
import bitmap.trainning.google.com.displaybitmapsefficiently.util.task.BitmapWorkerTask;

/**
 * Created by LENOVO on 2015/11/7.
 * Note:relayed on Guava lib
 */
public class ImageDownloader {


    //only download the picture
    public static Bitmap downloadBitmap(String myurl) {
        InputStream is = null;
        HttpURLConnection conn = null;
        //Closer make it easy to close the IO
        Closer closer = Closer.create();
        try {
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            if (response == 200) {
                is = closer.register(conn.getInputStream());
                final Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                closer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //download the picture and show it on ImageView
    /*why do i define the method:because download picture from by the url need sometime,we don't know
    * when to set the bitmap to imageview,only the downloader knows the time .
     * solve way1:given the imageView to downloader
     * solve way2:make a listener for user
     * Note:you can code like this,it can also get the bitmap as like as your network speed is good
     * Bitmap pic = ImageDownloader.downloadBitmap(url);
                mImageView.setImageBitmap(pic);
       }
    * */
    @Deprecated
    public static Bitmap downloadBitmap(Activity activity, String myurl, final ImageView mImageView) {
        InputStream is = null;
        HttpURLConnection conn = null;
        //Closer make it easy to close the IO
        Closer closer = Closer.create();
        try {
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            if (response == 200) {
                is = closer.register(conn.getInputStream());
                final Bitmap bitmap = BitmapFactory.decodeStream(is);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
                return bitmap;
            }
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {
            e.printStackTrace();
            conn.disconnect();
        } finally {
            try {
                closer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*asynchronous tasks
    * the method means you don't have to make a new Thread in your code,it have done for you.
    * */
    public static void display(Context context,String url, ImageView imageView) {
        Bitmap bitmapFromMemCache = LruCacheUtil.getBitmapFromMemCache(url);
        if(bitmapFromMemCache != null){
            imageView.setImageBitmap(bitmapFromMemCache);
        }else{
            if (cancelPotentialDownload(url, imageView)) {
                BitmapDownloaderTask task = new BitmapDownloaderTask(context,imageView);
                DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
                imageView.setImageDrawable(downloadedDrawable);
                task.execute(url);
            }
        }
    }

    public static void display(Context context,String url, ImageView imageView,int[] size) {
        Bitmap bitmapFromMemCache = LruCacheUtil.getBitmapFromMemCache(url);
        if(bitmapFromMemCache != null){
            imageView.setImageBitmap(bitmapFromMemCache);
        }else{
            if (cancelPotentialDownload(url, imageView)) {
                BitmapDownloaderTask task = new BitmapDownloaderTask(context,imageView,size);
                DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
                imageView.setImageDrawable(downloadedDrawable);
                task.execute(url);
            }
        }
    }

    private static boolean cancelPotentialDownload(String url, ImageView imageView) {
        BitmapDownloaderTask bitmapDownloaderTask = BitmapDownloaderTask.getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }

    //load the bitmap from local resources
    public static void display(Context context,int resId, ImageView imageView) {
        //find the bitmap from cache first before decide to get in some way else
        Bitmap bitmapFromMemCache = LruCacheUtil.getBitmapFromMemCache(String.valueOf(resId));
        if(bitmapFromMemCache != null){
            imageView.setImageBitmap(bitmapFromMemCache);
        }else{
            BitmapWorkerTask task = new BitmapWorkerTask(context,imageView);
            task.execute(resId);
        }
    }

    //load the bitmap from local resources indicating the size of which
    public static void display(Context context,int resId, ImageView imageView,int[] size) {
        //find the bitmap from cache first before decide to get in some way else
        Bitmap bitmapFromMemCache = LruCacheUtil.getBitmapFromMemCache(String.valueOf(resId));
        if(bitmapFromMemCache != null){
            imageView.setImageBitmap(bitmapFromMemCache);
        }else{
            BitmapWorkerTask task = new BitmapWorkerTask(context,imageView,size);
            task.execute(resId);
        }
    }
}