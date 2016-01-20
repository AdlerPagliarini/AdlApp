package br.com.tagcode.adlapp;

/**
 * Created by Adler Pagliarini on 18/01/2016.
 */
public class ProductRelationUser {
    Product product;
    User user;

    public ProductRelationUser(Product product, User user) {
        this.product = product;
        this.user = user;
    }
}
