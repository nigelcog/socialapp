package com.creativecub.socialapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.creativecub.socialapp.R;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Yogesh on 26-04-2015.
 */
public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    Context context;
    public ImageLoader(ImageView imageView,Context context)
    {
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap b = null;
        try {
            b = loadFiletoBitmap(params[0]);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(b!=null)
            return b;

        return downloadBitmap(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
                else
                {
                    Log.e("err","kya yaar");
                }
            }
        }
    }
    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = compress(BitmapFactory.decodeStream(inputStream));
                saveToFile(bitmap,url);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
    private Bitmap compress(Bitmap b)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTempStorage = new byte[24*1024];
        options.inJustDecodeBounds = false;
        options.inSampleSize=32;
        return ThumbnailUtils.extractThumbnail(b, 50, 50);
    }
    private void saveToFile(Bitmap b,String url) throws UnsupportedEncodingException {
        String filename = URLEncoder.encode(url,"UTF-8");
        try {
            FileOutputStream out = new FileOutputStream(context.getFilesDir().getAbsolutePath()+"/"+ filename);
            b.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch(Exception e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private Bitmap loadFiletoBitmap(String filename) throws UnsupportedEncodingException {
        filename = URLEncoder.encode(filename,"UTF-8");
        try {
            File f = new File(context.getFilesDir().getAbsolutePath()+"/"+ filename);
            if (!f.exists()) { return null; }

            Bitmap tmp = BitmapFactory.decodeFile(context.getFilesDir().getAbsolutePath()+"/"+ filename);
            return tmp;
        } catch (Exception e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
