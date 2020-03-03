package com.example.prototypenew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class ChangePhoneNum extends AppCompatActivity {
    Button back,confirm;
    EditText changePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_num);

        back=findViewById(R.id.back);
        confirm=findViewById(R.id.confirm);
        changePhone=findViewById(R.id.changePhone);

        SharedPreferences sharedPreferences = getSharedPreferences("wilson", Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString("user_email", "Not Available");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePhoneNum.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum=changePhone.getText().toString().trim();

                if (!TextUtils.isEmpty(phoneNum) && phoneNum.length()>=10 && phoneNum.length()<=11){
                    ChangePhone(email,phoneNum);
                }else{
                    Toast.makeText(ChangePhoneNum.this, "Your phone number must not more than 12 digits!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void ChangePhone(final String Email,final String phoneNum){
        class ChangePhone1 extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... Void) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email",Email);
                hashMap.put("phoneNum",phoneNum);
                Handler handler = new Handler();
                String s = handler.sendPostRequest("https://mobilehost2019.com/KhorHuanYong/php/phoneNum_change.php", hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")) {
                    Toast.makeText(ChangePhoneNum.this, "Success",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChangePhoneNum.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChangePhoneNum.this, "Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        ChangePhone1  changePhone = new ChangePhone1 ();
        changePhone.execute();
    }
}
