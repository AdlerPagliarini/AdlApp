package br.com.tagcode.adlapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Logged extends Activity implements View.OnClickListener {

    Button bLogout, bViewProduct, bPay;
    TextView etName, etAge, etUserName;
    UserLocalStore userLocalStore;
    ProductLocalStore productLocalStore;
    ProductRelationUserLocalStore productRelationUserLocalStore;

    TextView title;
    TextView description;
    TextView price;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        etName = (TextView) findViewById(R.id.etName);
        etUserName = (TextView) findViewById(R.id.etUserName);
        etAge = (TextView) findViewById(R.id.etAge);

        bLogout = (Button) findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);

        bViewProduct = (Button) findViewById(R.id.bViewProduct);
        bViewProduct.setOnClickListener(this);

        bPay = (Button) findViewById(R.id.bPay);
        bPay.setOnClickListener(this);
        bPay.setVisibility(View.INVISIBLE);

        userLocalStore = new UserLocalStore(this);
        productLocalStore = new ProductLocalStore(this);
        productRelationUserLocalStore = new ProductRelationUserLocalStore(this);

        //product
        //product = (Product) intent.getSerializableExtra("product");

        title = (TextView) findViewById(R.id.tvTitle);
        description = (TextView) findViewById(R.id.tvDescription);
        price = (TextView) findViewById(R.id.tvPrice);
        image = (ImageView) findViewById(R.id.ivIcon);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate() == true) {
            displayUserDetails();
            if (checkRelation()) {
                if (checkProductSelected()) {
                    displayProductDetails();
                    title.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);
                    image.setVisibility(View.VISIBLE);
                }
            } else {
                productLocalStore.clearProductData();
                productLocalStore.setProductSelected(false);
                title.setVisibility(View.INVISIBLE);
                description.setVisibility(View.INVISIBLE);
                price.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
            }
        } else {
            startActivity(new Intent(Logged.this, Login.class));
        }
    }

    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();

        etName.setText(user.getName());
        etUserName.setText(user.getUserName());
        etAge.setText(user.getAge() + "");
    }

    private boolean checkProductSelected() {
        return productLocalStore.getProductSelected();
    }

    private void displayProductDetails() {
        Product product = productLocalStore.getProduct();
        title.setText(product.name);
        description.setText(product.manuFacturer);
        price.setText("$" + product.salePrice);
        new DownloadImageTask((ImageView) findViewById(R.id.ivIcon)).execute(product.largeImage);
        bPay.setVisibility(View.VISIBLE);
    }

    private boolean checkRelation() {
        User user = userLocalStore.getLoggedInUser();
        Product product = productLocalStore.getProduct();
        ProductRelationUser relation = productRelationUserLocalStore.getProductRelationUser();

        if (productRelationUserLocalStore.getProductRelationUserLocal()) {
                productRelationUserLocalStore.clearProductRelationUserLocal();
                productRelationUserLocalStore.setProductRelationUserLocal(false);
                if (checkProductSelected()) {
                        createRelation(user, product);
                        return true;

                }else{

                    return false;

                }
        } else {

            if (!checkProductSelected()) {
                return false;
            }else {
                createRelation(user, product);
                return true;
            }

        }
    }

    private void createRelation(User user, Product product){
        ProductRelationUser relation = new ProductRelationUser(product, user);
        productRelationUserLocalStore.storeProductRelationUserLocalData(relation);
        productRelationUserLocalStore.setProductRelationUserLocal(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(Logged.this, Login.class));
                break;
            case R.id.bViewProduct:
                startActivity(new Intent(Logged.this, MainActivity.class));
                break;
            case R.id.bPay:
                Product product = productLocalStore.getProduct();
                Uri uri = Uri.parse(product.addToCartUrl);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browserIntent);
                break;
        }
    }
}
