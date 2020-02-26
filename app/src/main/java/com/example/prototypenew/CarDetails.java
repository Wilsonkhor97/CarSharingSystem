package com.example.prototypenew;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.HashMap;

public class CarDetails extends AppCompatActivity {
    Button back;
    ListView carList;
    ArrayList<HashMap<String, String>> carlist;
    Dialog myDialogWindow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_car);

        back=findViewById(R.id.back);
        carList=findViewById(R.id.car_list);

        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                loadWindow(position);
            }
        });
    }
    private void loadWindow( int p){
        myDialogWindow = new Dialog(CarDetails.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        myDialogWindow.setContentView(R.layout.car_show);

        Button back = myDialogWindow.findViewById(R.id.back);
        TextView typeOfCar, nameOfCar, date, time, price,phoneNum;
        typeOfCar = myDialogWindow.findViewById(R.id.carType);
        nameOfCar = myDialogWindow.findViewById(R.id.carName);
        date = myDialogWindow.findViewById(R.id.date);
        time = myDialogWindow.findViewById(R.id.time);
        price = myDialogWindow.findViewById(R.id.price);
        phoneNum = myDialogWindow.findViewById(R.id.phone);
        typeOfCar.setText(carlist.get(p).get("typeOfCar"));
        nameOfCar.setText(carlist.get(p).get("nameOfCar"));
        date.setText(carlist.get(p).get("date"));
        time.setText(carlist.get(p).get("time"));
        price.setText(carlist.get(p).get("price"));
        phoneNum.setText(carlist.get(p).get("phoneNum"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogWindow.dismiss();
            }
        });
        myDialogWindow.show();
    }

}
