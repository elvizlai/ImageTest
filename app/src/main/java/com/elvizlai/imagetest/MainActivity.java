package com.elvizlai.imagetest;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

public class MainActivity extends Activity {
    final public String dirName = "ElvizLai_Pic";
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        createSDDir(dirName);
        createPrivateDir(dirName);
        createPublicDir(dirName);
        createInternalDir(dirName);
        createInternalCacheDir(dirName);

        savePic(R.raw.mylove, "mylove");

        mImageView = (ImageView) findViewById(R.id.image);
        mImageView.setImageBitmap(loadImage(getResources(), R.raw.mylove, 640, 400));
    }

    private Bitmap loadImage(Resources res, int redID, int height, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, redID, options);

        options.inSampleSize = calImageSize(options, height, width);
        options.inJustDecodeBounds = false;

        WeakReference<Bitmap> weakReference = new WeakReference<Bitmap>(BitmapFactory.decodeResource(res, redID, options));

        return weakReference.get();
    }

    private void savePic(int redId, String picName) {

        InputStream is = getResources().openRawResource(redId);

        File file = new File(createSDDir(dirName).getAbsolutePath() + File.separator + picName + ".jpeg");

        try {
            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[is.available()];//TODO uncheck
            is.read(data);
            os.write(data);
            is.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int calImageSize(BitmapFactory.Options options, int reqHeight, int reqWidth) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private File createSDDir(String fileName) {

        File file = new File(Environment.getExternalStorageDirectory(), fileName);

        if (!file.mkdir()) {
            Log.e(fileName, "mkdir failed");
        }

        return file;
    }

    private File createPrivateDir(String name) {
        File file = new File(getBaseContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), name + "_private");

        if (!file.mkdir()) {
            Log.e(name, "mkdir failed");
        }
        return file;
    }

    private File createPublicDir(String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName + "_public");
        if (!file.mkdirs()) {
            Log.e(fileName, "mkdir failed");
        }
        return file;
    }

    private File createInternalDir(String fileName) {
        File file = new File(getBaseContext().getFilesDir(), fileName);

        if (file.mkdir()) {
            Log.e(fileName, "mkdir failed");
        }

        return file;
    }

    private File createInternalCacheDir(String fileName) {
        File file = new File(getBaseContext().getCacheDir(), fileName);
        if (file.mkdir()) {
            Log.e(fileName, "mkdir failed");
        }

        return file;
    }


    private boolean isStorageStateOK() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
