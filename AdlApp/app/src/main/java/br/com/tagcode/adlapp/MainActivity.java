package br.com.tagcode.adlapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.os.Handler;

/**
 * Created by Adler Pagliarini on 15/01/2016.
 */
public class MainActivity extends Activity implements  View.OnClickListener{

    ListView list;
    ListViewAdapter listViewAdapter;
    boolean flag_loading = false;
    boolean flag_loadingMore = false;
    private static int MAX_SHOW_ITEMS = 7;
    int maxPageToLoad = 1;
    int page = 1;
    ArrayList<String> querys = new ArrayList<String>();
    int swap = 0;

    Button bFirst, bPrev, bNext, bLast, bSearch;
    EditText etSearch;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSearch:
                String t = etSearch.getText().toString();
                if(! t.equals("") && ! t.equals(null)){
                    querys.add("name=" + t + "*|longDescription=" + t + "*");
                    page = 1;
                    swap = querys.size() - 1;
                    loadData(page, swap);
                    etSearch.setText("");
                }else{
                    toast("Write something to search...");
                }
                break;
            case R.id.bFirst:
                page = 1;
                loadData(page, swap);
                toast("page " + page + "/" + maxPageToLoad);
                break;
            case R.id.bPrevius:
                page--;
                if(!(page >= 1)){
                    page = 1;
                }
                loadData(page, swap);
                toast("page " + page + "/" + maxPageToLoad);
                break;
            case R.id.bNext:
                page++;
                if(!(page <= maxPageToLoad)){
                    page = maxPageToLoad;
                }
                loadData(page, swap);
                toast("page " + page + "/" + maxPageToLoad);
                break;
            case R.id.bLast:
                page = maxPageToLoad;
                loadData(page, swap);
                toast("page " + page + "/" + maxPageToLoad);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        querys.add("longDescription=iPhone*|sku=7619002");
        querys.add("manufacturer=Apple&name=iPhone*");
        //querys.add("manufacturer=Samsung&name=Galaxy*");

        list = (ListView) findViewById(R.id.lvListView);
        bFirst = (Button) findViewById(R.id.bFirst);
        bPrev = (Button) findViewById(R.id.bPrevius);
        bNext = (Button) findViewById(R.id.bNext);
        bLast = (Button) findViewById(R.id.bLast);
        bSearch = (Button) findViewById(R.id.bSearch);
        etSearch = (EditText) findViewById(R.id.etSearch);

        bFirst.setOnClickListener(this);
        bPrev.setOnClickListener(this);
        bNext.setOnClickListener(this);
        bLast.setOnClickListener(this);
        bSearch.setOnClickListener(this);

        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);

        loadData(page, swap);

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                //to be able to scroll listview
                if (firstVisibleItem == 0) {
                    swipeView.setEnabled(true);
                } else swipeView.setEnabled(false);


                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && totalItemCount != listViewAdapter.getTotalItems()) {
                    if (flag_loading == false) {
                        flag_loading = true;
                        showMore();
                    } else {
                        flag_loading = false;
                    }
                }/*else if(totalItemCount != 0 && totalItemCount == listViewAdapter.getTotalItems()){
                    if (flag_loadingMore == false) {
                        if(maxPageToLoad > page){
                            flag_loadingMore = true;
                            page++;
                            toast("loading page " + page + "/" + maxPageToLoad);
                            loadData(page);
                        }
                    }
                }*/
            }
        });

        //pull to refresh
        swipeView.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_dark);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                swipeView.setRefreshing(true);
                Log.d("Swipe", "Refreshing");
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        swipeView.setRefreshing(false);
                        page = 1;
                        swap++;
                        if(!(swap < querys.size())){
                            //reload first query again
                            swap = 0;
                        }
                        loadData(page, swap);
                    }
                }, 3000);
            }
        });
    }

    private void loadData(int page, final int swap){

        String url = "http://api.bestbuy.com/v1/products("+ querys.get(swap) +")?show=sku,name,manufacturer,longDescription,largeImage,largeFrontImage,salePrice,addToCartUrl,categoryPath.id,categoryPath.name&pageSize=30&page=" + page + "&apiKey=mnhm4sysa3yhyxfhuqp72met&format=json";
        BestBuyRequest bestBuyRequest = new BestBuyRequest(this);
        bestBuyRequest.fetchBestBuyDataInBackground(url, new GetBestBuyCallBack() {
            @Override
            public void done(JSONObject jObject) {
                if (jObject != null) {
                    ArrayList<Product> productsArray = loadArrayWithJson(jObject);
                    listViewAdapter = new ListViewAdapter(MainActivity.this, productsArray);
                    list.setAdapter(listViewAdapter);
                }
                //flag_loadingMore = false;
            }
        });

    }
    private ArrayList<Product> loadArrayWithJson(JSONObject jObject) {

        ArrayList<Product> productsArray = new ArrayList<Product>();
        String[] sku;
        String[] name;
        String[] manuFacturer;
        String[] longDescription;
        String[] largeImage;
        String[] largeFrontImage;
        String[] salePrice;
        String[] addToCartUrl;

        try{
            maxPageToLoad = (Integer) jObject.get("totalPages");
            JSONArray products = (JSONArray) jObject.get("products");
            sku = new String[products.length()];
            name = new String[products.length()];
            manuFacturer = new String[products.length()];
            longDescription = new String[products.length()];
            largeImage = new String[products.length()];
            largeFrontImage = new String[products.length()];
            salePrice = new String[products.length()];
            addToCartUrl = new String[products.length()];

            for (int i = 0; i < products.length(); i++)
            {
                try {
                        JSONObject t = (JSONObject) products.getJSONObject(i);
                        sku[i] = Integer.toString(t.getInt("sku"));
                        name[i] = t.getString("name");
                        manuFacturer[i] = t.getString("manufacturer");
                        longDescription[i] = t.getString("longDescription");
                        largeImage[i] = t.getString("largeImage");
                        largeFrontImage[i] = t.getString("largeFrontImage");
                        salePrice[i] = Double.toString(t.getDouble("salePrice"));
                        addToCartUrl[i] = t.getString("addToCartUrl");

                    productsArray.add(new Product(sku[i], name[i], manuFacturer[i], longDescription[i], largeImage[i], largeFrontImage[i], salePrice[i], addToCartUrl[i]));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }finally {
            if(productsArray == null){
                return null;
            }else{
                return productsArray;
            }
        }

    }

    private void showMore(){
        listViewAdapter.populateList(MAX_SHOW_ITEMS);
        listViewAdapter.notifyDataSetChanged();
        /*int lastPos = listViewAdapter.getCount();
        list.setAdapter(listViewAdapter);
        list.setSelection(lastPos);//list.setSelectionFromTop(lastPos, 0);*/
    }

    private void toast(String t){
        Toast toast = Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT);
        toast.show();
    }


}

