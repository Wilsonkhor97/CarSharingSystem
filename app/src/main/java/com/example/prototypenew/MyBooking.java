package com.example.prototypenew;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyBooking extends AppCompatActivity {
    SharedPreferences sharedPreferencesB;
    Button back;
    ListView carList;
    Spinner sploc;
    TextView text;
    ArrayList<Car> dataModelArrayList;
    private ListAdapter listAdapter;
    @TargetApi(Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);

        sharedPreferencesB=MyBooking.this.getSharedPreferences("car_details", Context.MODE_PRIVATE);
        back=findViewById(R.id.back);
        carList=findViewById(R.id.car_list);
        text=findViewById(R.id.text);
        sploc = MyBooking.this.findViewById(R.id.carType);

        retrieveJSON();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyBooking.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyBooking.this, MyCarBooking.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("CarDetails",dataModelArrayList.get(position));
                startActivity(intent);


            }
        });
    }
    private void retrieveJSON() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mobilehost2019.com/KhorHuanYong/php/myBooking.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr", ">>" + response);

                        try {

                            JSONObject obj = new JSONObject(response);

                            dataModelArrayList = new ArrayList<>();
                            JSONArray dataArray  = obj.getJSONArray("Car");

                            for (int i = 0; i < dataArray.length(); i++) {

                                Car playerModel = new Car();
                                JSONObject dataobj = dataArray.getJSONObject(i);

                                playerModel.setUrl(dataobj.getString("url"));
                                playerModel.setPlateNum(dataobj.getString("plateNum"));
                                playerModel.setTypeOfCar(dataobj.getString("typeOfCar"));
                                playerModel.setNameOfCar(dataobj.getString("nameOfCar"));
                                playerModel.setsDate(dataobj.getString("sdate"));
                                playerModel.seteDate(dataobj.getString("edate"));
                                playerModel.setsTime(dataobj.getString("stime"));
                                playerModel.seteTime(dataobj.getString("etime"));
                                playerModel.setPrice(dataobj.getString("price"));
                                playerModel.setLatitude(dataobj.getString("latitude"));
                                playerModel.setLongitude(dataobj.getString("longitude"));
                                playerModel.setPhoneNum(dataobj.getString("phoneNum"));

                                dataModelArrayList.add(playerModel);

                                setupListview();

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
                SharedPreferences sharedPreferences = getSharedPreferences("wilson", Context.MODE_PRIVATE);
                final String email = sharedPreferences.getString("user_email", "Not Available");
                params.put("email", email);
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
    private void setupListview(){
        listAdapter = new ListAdapter(this,dataModelArrayList);
        carList.setAdapter(listAdapter);
    }
}
