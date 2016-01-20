package br.com.tagcode.adlapp;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
/**
 * Created by Adler Pagliarini on 19/01/2016.
 */


public class WebViewActivity extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.bestbuy.com/site/olspage.jsp?id=pcmcat152200050035&type=category&cmp=RMX&ky=2h6sdP8RSwZRaaxzSKZw5fYedVySzLGE4&qvsids=4423310");

    }

}
