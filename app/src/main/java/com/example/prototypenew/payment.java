package com.example.prototypenew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class payment extends AppCompatActivity {
    String orderid,email,price,date,plateNum,name;
    WebView simpleWebView;
    boolean loadingFinished = true;
    boolean redirect = false;
    Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        car=(Car)getIntent().getSerializableExtra("CarDetails");

        SharedPreferences sharedPreferences = getSharedPreferences("wilson", Context.MODE_PRIVATE);
        final String email1 = sharedPreferences.getString("user_email", "Not Available");
        final String name1 = sharedPreferences.getString("user_name", "Not Available");

        final int min = 1;
        final int max = 100000;
        final int random = new Random().nextInt((max - min) + 1) + min;

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat tf = new SimpleDateFormat("HHmm");
        String date1=df1.format(new Date());
        String time=tf.format(new Date());

        email = email1;
        date=df.format(new Date());
        price=car.getPrice();
        orderid = date1+time+random;
        name=name1;
        plateNum=car.getPlateNum();

        simpleWebView = (WebView) findViewById(R.id.wbview1);
        simpleWebView.setWebViewClient(new MyWebViewClient());

        String url = "https://mobilehost2019.com/KhorHuanYong/php/payment.php?" +
                "email="+email+"&name="+name+"&amount="+price+"&orderid="+orderid+"&plateNum="+plateNum;
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(url); // load the url on the web view
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("HANIS","shouldOverrideUrlLoading");

            if (!loadingFinished) {
                redirect = true;
            }
            loadingFinished = false;
            view.loadUrl(url); // load the url
            return true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            Log.e("HANIS","onPageStarted");
            loadingFinished = false;

            //SHOW LOADING IF IT ISNT ALREADY VISIBLE
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e("HANIS","onPageFinished");
            if(!redirect){
                loadingFinished = true;
            }

            if(loadingFinished && !redirect){
                //HIDE LOADING IT HAS FINISHED
//                Toast.makeText(payment.this, "Loading completed...", Toast.LENGTH_SHORT).show();
            } else{
                redirect = false;
//                Toast.makeText(payment.this, "Redirecting..", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mnu_back:
                loadPayment();
                Intent intent = new Intent(payment.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadPayment() {
        String url = "https://mobilehost2019.com/KhorHuanYong/php/payment.php?" +
                "email="+email+"&name="+name+"&amount="+price+"&orderid="+orderid+"&plateNum="+plateNum;

        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(url); // load the url on the web view
    }
}
