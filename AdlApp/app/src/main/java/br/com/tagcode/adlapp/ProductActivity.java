package br.com.tagcode.adlapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;


import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Adler Pagliarini on 16/01/2016.
 */

// other zoom, see after: http://developer.android.com/intl/pt-br/training/animation/zoom.html

public class ProductActivity extends Activity implements View.OnClickListener {

    TextView title;
    TextView description;
    TextView price;
    TouchImageView image;
    Button bSharePhoto;
    Button bShareText;
    Button bBuy;
    Product product;
    ProductLocalStore productLocalStore;

    //Facebook
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private LoginManager loginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        callbackManager = CallbackManager.Factory.create();


        //recieved like array, other way to transfer data
        /*ArrayList<String> i = new ArrayList<>(intent.getStringArrayListExtra("key"));
        title.setText(i.get(0));*/

        //recived like object
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");

        //find layout
        title = (TextView) findViewById(R.id.tvName);
        description = (TextView) findViewById(R.id.tvLongDescription);
        price = (TextView) findViewById(R.id.tvSalePrice);
        image = (TouchImageView) findViewById(R.id.ivLargeImage);
        new DownloadImageTask((TouchImageView) findViewById(R.id.ivLargeImage)).execute(product.largeFrontImage);
        bSharePhoto = (Button) findViewById(R.id.bSharePhoto);
        bShareText = (Button) findViewById(R.id.bShareText);
        bBuy = (Button) findViewById(R.id.bBuy);

        //set values
        title.setText(product.name);
        description.setText(product.longDescription);
        price.setText("$" + product.salePrice);

        //buttons click
        bBuy.setOnClickListener(this);
        bSharePhoto.setOnClickListener(this);
        bShareText.setOnClickListener(this);
        //bShareText.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bSharePhoto:
                List<String> permissionNeeds = Arrays.asList("publish_actions");
                //this loginManager helps you eliminate adding a LoginButton to your UI
                loginManager = LoginManager.getInstance();

                loginManager.logInWithPublishPermissions(this, permissionNeeds);

                loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        DownloadBitmapTask downloadBitmapTask = new DownloadBitmapTask(ProductActivity.this);
                        downloadBitmapTask.getBitmapDataInBackground(product.largeFrontImage, new GetDownloadBitmapTask() {
                            @Override
                            public void done(Bitmap bitmap) {
                                if (bitmap != null) {
                                    sharePhotoToFacebook(bitmap);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                    }
                });
                break;
            case R.id.bShareText:
                shareTextToFacebook();
                break;
            case R.id.bBuy:

                productLocalStore = new ProductLocalStore(ProductActivity.this);
                if(productLocalStore.getProductSelected()){
                    productLocalStore.clearProductData();
                    productLocalStore.setProductSelected(false);
                }
                productLocalStore.storeProductData(product);
                productLocalStore.setProductSelected(true);
                startActivity(new Intent(ProductActivity.this, Logged.class));
                break;
        }

    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void sharePhotoToFacebook(Bitmap bitmap){

        //BitmapFactory.decodeResource(getResources(), product.images); | BitmapFactory.decodeResource(getResources(), R.drawable.top);
        //List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        //bitmaps.add(bitmap);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .setUserGenerated(true)
                .setCaption(product.name + ", " + product.longDescription + " - $" + product.salePrice)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
        toast("Sent with success.");
    }

    private void shareTextToFacebook(){
       ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                .putString("og:type", "books.book")
                .putString("og:title", product.name)
                .putString("og:description", product.longDescription + "  -  " + product.salePrice)
                //.putPhoto("og:image", photo) //doesn't work
                .putString("books:isbn", "0-553-57340-3")
                .build();
       ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                .setActionType("books.reads")
                .putObject("book", object)
                .build();

        ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                .setPreviewPropertyName("book")
                .setAction(action)
                .build();

        ShareDialog.show(this, content);
        toast("Sent with success.");


        //other way to send a post, but without a bitmap
        /*shareDialog = new ShareDialog(this);
        if (ShareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(product.titles)
                    .setContentDescription(product.descriptions)
                    .build();

            shareDialog.show(linkContent);

        }*/
    }

    private void toast(String t){
        Toast toast = Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT);
        toast.show();
    }

}




