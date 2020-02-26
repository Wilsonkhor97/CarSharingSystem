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

public class ChangePasswordActivity extends AppCompatActivity {
    Button back,confirm;
    EditText password,password1;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        back=findViewById(R.id.back);
        confirm=findViewById(R.id.confirm);
        password=findViewById(R.id.password);
        password1=findViewById(R.id.password1);

        SharedPreferences sharedPreferences = getSharedPreferences("wilson", Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString("user_email", "Not Available");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Password=password.getText().toString().trim();
                String Password2=password1.getText().toString().trim();

                if (!TextUtils.isEmpty(Password) && !TextUtils.isEmpty(Password2) && Password.equals(Password2)){
                        ChangePassword(email,Password);
                }else if(!TextUtils.isEmpty(Password) && !TextUtils.isEmpty(Password2) && Password != Password2){
                    Toast.makeText(ChangePasswordActivity.this, "Please Enter the Same Password!",
                            Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(Password) || TextUtils.isEmpty(Password2)){
                    Toast.makeText(ChangePasswordActivity.this, "Please Enter the Password!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void ChangePassword(final String Email,final String Password){
        class ChangePassword extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... Void) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email",Email);
                hashMap.put("password",Password);
                Handler handler = new Handler();
                String s = handler.sendPostRequest("https://mobilehost2019.com/KhorHuanYong/php/password_change.php", hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")) {
                    Toast.makeText(ChangePasswordActivity.this, "Success",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        ChangePassword  changePassword = new ChangePassword ();
        changePassword.execute();
    }
}
