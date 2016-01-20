package br.com.tagcode.adlapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Adler Pagliarini on 14/01/2016.
 */
public class ProductRelationUserLocalStore {

    public static final String SP_NAME = "RelationDetails";

    //allow to save data on the phone
    SharedPreferences ProductRelationUserLocalDatabase;

    public ProductRelationUserLocalStore(Context context){
        ProductRelationUserLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    //save user
    public void storeProductRelationUserLocalData(ProductRelationUser productRelationUser){
        SharedPreferences.Editor spEditor = ProductRelationUserLocalDatabase.edit();
        spEditor.putString("sku", productRelationUser.product.sku);
        spEditor.putString("user", productRelationUser.user.userName);
        spEditor.commit();
    }

    //return user
    public ProductRelationUser getProductRelationUser(){
        String sku = ProductRelationUserLocalDatabase.getString("sku", "");
        String user = ProductRelationUserLocalDatabase.getString("user", "");
        Product p = new Product(sku);
        User u = new User(user);
        ProductRelationUser storeProductRelationUser = new ProductRelationUser(p, u);
        return storeProductRelationUser;
    }

    public void setProductRelationUserLocal(boolean relation){
        SharedPreferences.Editor spEditor = ProductRelationUserLocalDatabase.edit();
        spEditor.putBoolean("RelationSelected", relation);
        spEditor.commit();
    }

    public boolean getProductRelationUserLocal(){
        if(ProductRelationUserLocalDatabase.getBoolean("RelationSelected", false) == true){
            return true;
        }else{
            return  false;
        }
    }

    public void clearProductRelationUserLocal(){
        SharedPreferences.Editor spEditor = ProductRelationUserLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
