package com.example.prototypenew;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CusShow extends AppCompatActivity {
    Button back;
    TextView name,icNo,phoneNum,userEmail,test;
    ImageView photo;
    Car car;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_show);

        back=findViewById(R.id.Back);
        car=(Car)getIntent().getSerializableExtra("CarDetails");



        retrieveJSON();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CusShow.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void retrieveJSON() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mobilehost2019.com/KhorHuanYong/php/find_customer.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr", ">>" + response);

                        try {

                            JSONObject obj = new JSONObject(response);

                            name=findViewById(R.id.name);
                            photo=findViewById(R.id.photo1);
                            icNo=findViewById(R.id.icNo);
                            phoneNum=findViewById(R.id.phoneNum);
                            userEmail=findViewById(R.id.userEmail);

                            JSONArray dataArray  = obj.getJSONArray("Customer");

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);

                                photo.setImageURI(Uri.parse(dataobj.getString("url")));
//                                test=findViewById(R.id.test);
//                                test.setText(dataobj.getString("url"));
                                name.setText(dataobj.getString("name"));
                                icNo.setText(dataobj.getString("icNo"));
                                phoneNum.setText(dataobj.getString("phoneNum"));
                                userEmail.setText(dataobj.getString("userEmail"));

                                RequestOptions options = new RequestOptions().centerCrop().dontAnimate().placeholder(R.drawable.camera).error(R.drawable.camera);
                                Glide
                                        .with(CusShow.this)
                                        .load(dataobj.getString("url")).apply(options).listener(new RequestListener<Drawable>() {

                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        photo.setVisibility(View.VISIBLE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        photo.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                })
                                        .into(photo);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //Adding parameters to request
                params.put("plateNum", car.getPlateNum());
                //returning parameter
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
