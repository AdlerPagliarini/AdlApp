package br.com.tagcode.adlapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Adler Pagliarini on 19/01/2016.
 */
public class DownloadBitmapTask {

    ProgressDialog progressDialog;

    public DownloadBitmapTask(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing photo");
        progressDialog.setMessage("Please Wait...");
    }

    public void getBitmapDataInBackground(String url, GetDownloadBitmapTask getDownloadBitmapTask) {
        //progressDialog.show();
        new DownloadBitmap(url, getDownloadBitmapTask).execute();
    }

    public class DownloadBitmap extends AsyncTask<Void, Void, Bitmap> {

        String url;
        GetDownloadBitmapTask getDownloadBitmapTask;

        public DownloadBitmap(String url, GetDownloadBitmapTask getDownloadBitmapTask) {
            this.url = url;
            this.getDownloadBitmapTask = getDownloadBitmapTask;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            String urldisplay = url;
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //progressDialog.dismiss();
            getDownloadBitmapTask.done(result);
            super.onPostExecute(result);
        }
    }
}