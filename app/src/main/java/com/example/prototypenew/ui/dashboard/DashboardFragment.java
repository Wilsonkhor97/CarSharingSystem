package com.example.prototypenew.ui.dashboard;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototypenew.Car;
import com.example.prototypenew.CarEdit;
import com.example.prototypenew.ListAdapter;
import com.example.prototypenew.MainActivity;
import com.example.prototypenew.MyBooking;
import com.example.prototypenew.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {

    ListView carList;
    Button booking;
    ArrayList<Car> dataModelArrayList;
    private ListAdapter listAdapter;
    @TargetApi(Build.VERSION_CODES.O)

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        carList=getActivity().findViewById(R.id.car_list1);
        booking=getActivity().findViewById(R.id.booking);
        retrieveJSON();

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyBooking.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CarEdit.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("CarDetails",dataModelArrayList.get(position));
                startActivity(intent);


            }
        });

    }

    private void retrieveJSON() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mobilehost2019.com/KhorHuanYong/php/findOwner_car.php",
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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("wilson", Context.MODE_PRIVATE);
                final String email = sharedPreferences.getString("user_email", "Not Available");

                Map<String, String> params = new HashMap<String, String>();
                params.put("ownerEmail",email);
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
    private void setupListview(){
        listAdapter = new ListAdapter(getActivity(),dataModelArrayList);
        carList.setAdapter(listAdapter);
    }
}