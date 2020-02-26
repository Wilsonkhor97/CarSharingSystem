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

import com.example.prototypenew.ui.home.HomeFragment;

import java.util.HashMap;

public class ChangeName extends AppCompatActivity {
    Button back,confirm;
    EditText changeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        back=findViewById(R.id.back);
        confirm=findViewById(R.id.confirm);
        changeName=findViewById(R.id.changeName);


        SharedPreferences sharedPreferences = getSharedPreferences("wilson", Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString("user_email", "Not Available");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeName.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=changeName.getText().toString().trim();

                if (!TextUtils.isEmpty(name)){
                    ChangeName(email,name);
                }else{
                    Toast.makeText(ChangeName.this, "Please Enter Your Name!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void ChangeName(final String Email,final String Name){
        class ChangeName1 extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... Void) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email",Email);
                hashMap.put("name",Name);
                Handler handler = new Handler();
                String s = handler.sendPostRequest("https://mobilehost2019.com/KhorHuanYong/php/name_change.php", hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")) {
                    Toast.makeText(ChangeName.this, "Success",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChangeName.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChangeName.this, "Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        ChangeName1  changeName = new ChangeName1 ();
        changeName.execute();
    }

}
