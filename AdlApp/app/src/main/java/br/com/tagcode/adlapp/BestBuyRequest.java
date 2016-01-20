package br.com.tagcode.adlapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Adler Pagliarini on 14/01/2016.
 */
public class BestBuyRequest {
    ProgressDialog progressDialog;
    public static  final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://api.bestbuy.com/v1/products(longDescription=iPhone*|sku=7619002)?show=sku,name,manufacturer,longDescription,largeImage,largeFrontImage,salePrice,addToCartUrl&pageSize=100&page=5&apiKey=mnhm4sysa3yhyxfhuqp72met&format=json"; //"http://api.bestbuy.com/v1/products(longDescription=iPhone*|sku=7619002)?show=sku,name,manufacturer,longDescription,largeImage,largeFrontImage,salePrice&pageSize=100&page=5&apiKey=mnhm4sysa3yhyxfhuqp72met&format=json";

    public BestBuyRequest(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please Wait...");
    }

    public void fetchBestBuyDataInBackground(String url, GetBestBuyCallBack bestBuyCallback){
        progressDialog.show();
        new fetchBestBuyDataAsyncTask(url, bestBuyCallback).execute();
    }

    public class fetchBestBuyDataAsyncTask extends AsyncTask<Void, Void, JSONObject>{
        String url;
        GetBestBuyCallBack bestBuyCallBack;

        public fetchBestBuyDataAsyncTask(String url, GetBestBuyCallBack bestBuyCallBack){
            this.url = url;
            this.bestBuyCallBack = bestBuyCallBack;
        }


        @Override
        protected JSONObject doInBackground(Void... params) {

            JSONObject jObject = null;

            //Will be used if we want to read some data from server
            BufferedReader reader = null;

            try {
                //Converting address String to URL
                URL url = new URL(this.url);
                //Opening the connection (Not setting or using CONNECTION_TIMEOUT)
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();

                InputStream stream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder sb = new StringBuilder();

                String line;
                while((line = reader.readLine()) != null) { //Read till there is something available
                    sb.append(line + "\n");
                }
                line = sb.toString();
                jObject = new JSONObject(line);

                if(jObject.length() == 0){
                    jObject = null;
                }
                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check",line);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();     //Closing the
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jObject;
        }
        @Override
        //when AsyncTask finished
        protected void onPostExecute(JSONObject jObject) {
            progressDialog.dismiss();
            bestBuyCallBack.done(jObject);
            super.onPostExecute(jObject);
        }
    }
}
