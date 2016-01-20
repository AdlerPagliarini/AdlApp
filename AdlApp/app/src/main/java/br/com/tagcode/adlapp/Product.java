package br.com.tagcode.adlapp;

import java.io.Serializable;
/**
 * Created by Adler Pagliarini on 15/01/2016.
 */
public class Product implements Serializable{

    String sku;
    String name;
    String manuFacturer;
    String longDescription;
    String largeImage;
    String largeFrontImage;
    String salePrice;
    String addToCartUrl;

    public Product(String sku, String name, String manuFacturer, String longDescription, String largeImage, String largeFrontImage, String salePrice, String addToCartUrl) {
        this.sku = sku;
        this.name = name;
        this.manuFacturer = manuFacturer;
        this.longDescription = longDescription;
        this.largeImage = largeImage;
        this.largeFrontImage = largeFrontImage;
        this.salePrice = salePrice;
        this.addToCartUrl = addToCartUrl;
    }
    public Product(String sku) {
        this.sku = sku;
        this.name = "";
        this.manuFacturer = "";
        this.longDescription = "";
        this.largeImage = "";
        this.largeFrontImage = "";
        this.salePrice = "";
        this.addToCartUrl = "";
    }
}
