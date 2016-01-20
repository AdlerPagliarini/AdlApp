package br.com.tagcode.adlapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Adler Pagliarini on 14/01/2016.
 */
public class ProductLocalStore {

    public static final String SP_NAME = "ProductDetails";

    //allow to save data on the phone
    SharedPreferences ProductLocalDatabase;

    public ProductLocalStore(Context context){
        ProductLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    //save user
    public void storeProductData(Product product){
        SharedPreferences.Editor spEditor = ProductLocalDatabase.edit();

        spEditor.putString("sku", product.sku);
        spEditor.putString("name", product.name);
        spEditor.putString("manufacturer", product.manuFacturer);
        spEditor.putString("longDescription", product.longDescription);
        spEditor.putString("largeImage", product.largeImage);
        spEditor.putString("largeFrontImage", product.largeFrontImage);
        spEditor.putString("salePrice", product.salePrice);
        spEditor.putString("addToCartUrl", product.addToCartUrl);

        spEditor.commit();
    }

    //return user
    public Product getProduct(){

        String sku = ProductLocalDatabase.getString("sku", "");
        String name = ProductLocalDatabase.getString("name", "");
        String manuFacturer = ProductLocalDatabase.getString("manufacturer", "");
        String longDescription = ProductLocalDatabase.getString("longDescription", "");
        String largeImage = ProductLocalDatabase.getString("largeImage", "");
        String largeFrontImage = ProductLocalDatabase.getString("largeFrontImage", "");
        String salePrice = ProductLocalDatabase.getString("salePrice", "");
        String addToCartUrl = ProductLocalDatabase.getString("addToCartUrl", "");

        Product storeProduct = new Product(sku,name,manuFacturer,longDescription,largeImage,largeFrontImage,salePrice, addToCartUrl);

        return storeProduct;
    }

    public void setProductSelected(boolean selected){
        SharedPreferences.Editor spEditor = ProductLocalDatabase.edit();
        spEditor.putBoolean("productSelected", selected);
        spEditor.commit();
    }

    public boolean getProductSelected(){
        if(ProductLocalDatabase.getBoolean("productSelected", false) == true){
            return true;
        }else{
            return  false;
        }
    }

    public void clearProductData(){
        SharedPreferences.Editor spEditor = ProductLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
