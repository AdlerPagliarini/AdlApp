package br.com.tagcode.adlapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Adler Pagliarini on 15/01/2016.
 */
public class ListViewAdapter extends BaseAdapter implements View.OnClickListener {

    ArrayList<Product> listShow; //items that is showed
    Context context;

    //count total of items
    private int totalItems = 0;
    private static int MAX_SHOW_ITEMS = 7;

    private boolean canLoad = false;

    private Bitmap bitmap = null;

    ArrayList<Product> productsArray; //all items
    MemoryCache memoryCache;
    Product temp;

    public ListViewAdapter(Context c, ArrayList<Product> productsArray){
        context = c;
        listShow = new ArrayList<Product>();
        Resources res = c.getResources();
        this.productsArray = productsArray;
        totalItems = this.productsArray.size();
        populateList(MAX_SHOW_ITEMS);
        memoryCache = new MemoryCache();
        memoryCache.clear();
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage("Incorrect product details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
    public void populateList(int load){
        //start point
        int i = getCount();

        //if user sent to load more than max value
        if(load > totalItems){
            load = totalItems;
        }else{
            //load more items
            load = i + load;
            if(load > totalItems){
                load = totalItems;
            }
        }
        Toast toast = Toast.makeText(context, "loading..." + load + " of " + totalItems, Toast.LENGTH_SHORT);
        toast.show();
        for(i = i; i < load; i++){
            Product p = productsArray.get(i);
            listShow.add(new Product(p.sku,p.name,p.manuFacturer,p.longDescription,p.largeImage,p.largeFrontImage,p.salePrice, p.addToCartUrl));
        }
        canLoad = true;
    }

    public int getTotalItems() {
        return totalItems;
    }

    private void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public boolean isCanLoad() {
        return canLoad;
    }


    @Override
    public int getCount() {
        return listShow.size();
    }

    @Override
    public Object getItem(int i) {
        return listShow.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_item,viewGroup,false);
        //TextView tvSku = (TextView) row.findViewById(R.id.tvSku);
        TextView tvName = (TextView) row.findViewById(R.id.tvName);
        TextView tvManuFacturer = (TextView) row.findViewById(R.id.tvManuFacturer);
        //TextView tvLongDescription = (TextView) row.findViewById(R.id.tvLongDescription);
        final ImageView tvLargeImage = (ImageView) row.findViewById(R.id.ivLargeImage);
        //TextView tvLargeFrontImage = (TextView) row.findViewById(R.id.tvLargeFrontImage);
        TextView tvSalePrice = (TextView) row.findViewById(R.id.tvSalePrice);



        temp = listShow.get(i);

        tvName.setText(temp.name);
        tvManuFacturer.setText(temp.manuFacturer);

        bitmap = memoryCache.get(temp.largeImage);
        if(bitmap != null){
            tvLargeImage.setImageBitmap(bitmap);
            //Toast toast = Toast.makeText(context, "Loading From Cache", Toast.LENGTH_SHORT);
            //toast.show();
        }else {
            //Toast toast = Toast.makeText(context, "Loading From URL", Toast.LENGTH_SHORT);
            //toast.show();
            DownloadBitmapTask downloadBitmapTask = new DownloadBitmapTask(context);
            downloadBitmapTask.getBitmapDataInBackground(temp.largeImage, new GetDownloadBitmapTask() {
                @Override
                public void done(Bitmap bitmapReturned) {
                    if (bitmapReturned != null) {
                        bitmap = bitmapReturned;
                        memoryCache.put(temp.largeImage, bitmap);
                        tvLargeImage.setImageBitmap(bitmap);
                    }
                }
            });
        }
        //new DownloadImageTask((ImageView) row.findViewById(R.id.ivLargeImage)).execute(temp.largeImage);
        tvSalePrice.setText("$" + temp.salePrice);

        tvLargeImage.setOnClickListener(this);
        tvLargeImage.setTag(temp);

        return row;
    }
    @Override
    public void onClick(View view) {
        Product product = (Product) view.getTag();
        Intent intent = new Intent(context,ProductActivity.class);

        //transfer like array
        /*ArrayList<String> l = new ArrayList<>();
        l.add(product.titles);
        l.add(product.descriptions);
        l.add(product.prices);
        intent.putStringArrayListExtra("key", l);*/

        //transfer like object
        intent.putExtra("product", product);
        context.startActivity(intent);
    }

}
