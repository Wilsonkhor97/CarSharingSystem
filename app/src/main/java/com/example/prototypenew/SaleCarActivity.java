package com.example.prototypenew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleCarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button back,publish;
    Spinner sp;
    SharedPreferences sharedPreferences;
    List<String> list;
    String locat;
    ArrayAdapter<String> adp;
    TextView carName,date,time,price,location,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_car);

        back=findViewById(R.id.Back);
        publish=findViewById(R.id.publish);
        sp=findViewById(R.id.carType);
        sp.setOnItemSelectedListener(this);
        carName=findViewById(R.id.carName);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        price=findViewById(R.id.price);
        location=findViewById(R.id.location);
        phone=findViewById(R.id.phone);

        sharedPreferences=getSharedPreferences("user_details", Context.MODE_PRIVATE);

        list = new ArrayList<>();
        list.add("Sedan");
        list.add("MPV");
        list.add("SUV");
        list.add("Hatchback");
        list.add("4*4");

        adp = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, list);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaleCarActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CarName=carName.getText().toString().trim();
                String CarType=sp.getSelectedItem().toString();
                String Date= date.getText().toString().trim();
                String Time= time.getText().toString().trim();
                String Price=price.getText().toString().trim();
                String Location=location.getText().toString().trim();
                String Phone = phone.getText().toString().trim();
                rentCar(CarName,CarType,Price,Date,Phone,Time,Location);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        locat = parent.getSelectedItem().toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + locat, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(arg0.getContext(), "Please Select Type of Vehicles", Toast.LENGTH_LONG).show();

    }
    private void rentCar(final String CarName,final String CarType,final String Price,final String Date,final String Phone,final String Time,final String Location){

        if (TextUtils.isEmpty(CarName)){
            Toast.makeText(getApplicationContext(), "Please Enter Car Name!", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(Date)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Date!", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(Time)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Time!", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(Price)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Price!", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(Location)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Location!", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(Phone)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Phone Number!", Toast.LENGTH_SHORT).show();
            return;
        }
        //Getting values from edit texts
        final ProgressDialog loading = ProgressDialog.show(this,"Please Wait","Contacting Server",false,false);
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mobilehost2019.com/KhorHuanYong/php/submitDetails.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.equalsIgnoreCase("Success")){

                            Intent i = new Intent(SaleCarActivity.this, MainActivity.class);
                            Toast.makeText(SaleCarActivity.this, "Add Successful", Toast.LENGTH_LONG).show();
                            startActivity(i);
                            finish();
                        }else{
                            //If the server response is not success
                            //Displaying an error message on toast
                            loading.dismiss();
                            Toast.makeText(SaleCarActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        loading.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(SaleCarActivity.this,"No internet . Please check your connection",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{

                            Toast.makeText(SaleCarActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }){


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> hashMap = new HashMap<String, String>();
                //Adding parameters to request
                hashMap.put("nameOfCar",CarName);
                hashMap.put("typeOfCar",CarType);
                hashMap.put("date",Date);
                hashMap.put("time",Time);
                hashMap.put("price",Price);
                hashMap.put("location",Location);
                hashMap.put("phoneNum",Phone);

                //returning parameter
                return hashMap;
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
