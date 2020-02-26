package com.example.prototypenew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {
    private EditText inputEmail;
    private Button btnReset, btnBack;
    String email1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        inputEmail = (EditText) findViewById(R.id.email);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (Button) findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email1 = inputEmail.getText().toString().trim();
                if (!email1.isEmpty()){
                    sendEmail();

                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your email",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void sendEmail(){
        final ProgressDialog loading = ProgressDialog.show(this,"Please Wait","Contacting Server",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mobilehost2019.com/KhorHuanYong/php/forgotpassword.php",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                if (response.equalsIgnoreCase("Success")) {
                    Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
                    Toast.makeText(ForgotPassword.this, "Password has been sent to your email.", Toast.LENGTH_LONG).show();
                    startActivity(i);
                    finish();
                } else {
                    loading.dismiss();
                    Toast.makeText(ForgotPassword.this, "Invalid email", Toast.LENGTH_LONG).show();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(ForgotPassword.this, "No internet . Please check your connection",
                            Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(ForgotPassword.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("email", email1);

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
