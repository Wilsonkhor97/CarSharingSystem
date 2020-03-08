package com.example.prototypenew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;

import static android.view.View.Z;

public class RegisterActivity extends AppCompatActivity {
    Button reg,login,captureBTN;
    SharedPreferences preferences;
    EditText email,password,name,phone,icNo;
    ImageView photo;
    Car car;
    private Bitmap bitmap;
    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int PERMISSION_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        name=(EditText)findViewById(R.id.name);
        phone=(EditText)findViewById(R.id.phoneNum);
        icNo=(EditText)findViewById(R.id.ic);
        photo=findViewById(R.id.photo);
        captureBTN=findViewById(R.id.capture_image_btn);

        reg = findViewById(R.id.register);
        login = findViewById(R.id.login);

        preferences = getSharedPreferences("user_details", MODE_PRIVATE);

        captureBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA)==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                    PackageManager.PERMISSION_DENIED){

                        String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

                        requestPermissions(permission,PERMISSION_CODE);
                    }
                    else{
                        showFileChooser();
                    }
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email = email.getText().toString().trim();
                final String Name = name.getText().toString().trim();
                final String Password = password.getText().toString().trim();
                final String IcNo = icNo.getText().toString().trim();
                final String Phone= phone.getText().toString().trim();

                if (TextUtils.isEmpty(Email) || !Email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
                    Toast.makeText(getApplicationContext(), "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(Password.length() <6){
                    Toast.makeText(getApplicationContext(), "Your password must more than 5 digits!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(Name)){
                    Toast.makeText(getApplicationContext(), "Please enter your name!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(IcNo.length() !=12){
                    Toast.makeText(getApplicationContext(), "Please enter your IC No correctly!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(Phone.length()!=10 && Phone.length()!=11){
                    Toast.makeText(getApplicationContext(), "Please enter your phone number correctly!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(bitmap == null){
                    Toast.makeText(getApplicationContext(), "Please upload your lisence photo!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
//                    final ProgressDialog loading = ProgressDialog.show(RegisterActivity.this, "Please Wait", "Contacting Server", false, false);
//
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
//                            "https://mobilehost2019.com/KhorHuanYong/php/uploadCusInfo.php", new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            loading.dismiss();
//
//                            if (response.equalsIgnoreCase("Success")) {
//
//                                Toast.makeText(RegisterActivity.this, "Please check your email for verification.", Toast.LENGTH_LONG)
//                                        .show();
//                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
//                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(i);
//                                finish();
//                            }
//                            else if(response.equalsIgnoreCase("Exist")){
//
//                                Toast.makeText(RegisterActivity.this, "Email already exist", Toast.LENGTH_LONG)
//                                        .show();
//                            }else{
//
//                                Toast.makeText(RegisterActivity.this, "Cannot Register", Toast.LENGTH_LONG)
//                                        .show();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            loading.dismiss();
//                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                Toast.makeText(RegisterActivity.this,"No internet . Please check your connection",
//                                        Toast.LENGTH_LONG).show();
//                            }
//                            else{
//
//                                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//
//                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                            30000,
//                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                    requestQueue.add(stringRequest);
                    uploadMultipart();

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

    public void uploadMultipart() {
        //getting name for the image
        final String Email = email.getText().toString().trim();
        final String Password = password.getText().toString().trim();
        final String Name = name.getText().toString().trim();
        final String IcNo = icNo.getText().toString().trim();
        final String Phone= phone.getText().toString().trim();

        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, "https://mobilehost2019.com/KhorHuanYong/php/uploadCusInfo.php")
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("email", Email)
                    .addParameter("password", Password)
                    .addParameter("name", Name)
                    .addParameter("icNo", IcNo)
                    .addParameter("phoneNum", Phone)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
                Toast.makeText(this, "Please enter your email and password to login.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);


        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

//                File file= new File(filePath.getPath());
//                file.getName();

                bitmap = MediaStore.Images.Media.getBitmap(RegisterActivity.this.getApplicationContext().getContentResolver(), filePath);
                //  bitmap = new Compressor(getActivity().getApplicationContext()).compressToBitmap(file);
                photo.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = RegisterActivity.this.getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = RegisterActivity.this.getApplicationContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}


