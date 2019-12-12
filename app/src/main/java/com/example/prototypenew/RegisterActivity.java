package com.example.prototypenew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    Button reg,login;
    SharedPreferences preferences;
    EditText email,password,name,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        name=(EditText)findViewById(R.id.name);
        phone=(EditText)findViewById(R.id.phone);

        reg = findViewById(R.id.register);
        login = findViewById(R.id.login);

        preferences = getSharedPreferences("user_details", MODE_PRIVATE);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email = email.getText().toString().trim();
                final String Name = name.getText().toString().trim();
                final String Password = password.getText().toString().trim();
                final String Phone = phone.getText().toString().trim();
                if (TextUtils.isEmpty(Email)){
                    Toast.makeText(getApplicationContext(), "Please enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(Name)){
                    Toast.makeText(getApplicationContext(), "Please enter your name!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(password.length() <6){
                    Toast.makeText(getApplicationContext(), "Please enter your password!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(phone.length() <10){
                    Toast.makeText(getApplicationContext(), "Please enter your phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    final ProgressDialog loading = ProgressDialog.show(RegisterActivity.this, "Please Wait", "Contacting Server", false, false);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            "https://mobilehost2019.com/KhorHuanYong/php/register_user.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            loading.dismiss();

                            if (response.equalsIgnoreCase("Success")) {

                                Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_LONG)
                                        .show();
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                            else if(response.equalsIgnoreCase("Exist")){

                                Toast.makeText(RegisterActivity.this, "Email already exist", Toast.LENGTH_LONG)
                                        .show();
                            }else{

                                Toast.makeText(RegisterActivity.this, "Cannot Register", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(RegisterActivity.this,"No internet . Please check your connection",
                                        Toast.LENGTH_LONG).show();
                            }
                            else{

                                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name",Name);
                            params.put("phone",Phone);
                            params.put("email",Email);
                            params.put("password",Password);
                            return params;
                        }

                    };

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                }}
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

}


