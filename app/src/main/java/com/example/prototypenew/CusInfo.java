package com.example.prototypenew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.EasyPermissions;

public class CusInfo extends AppCompatActivity {

    Button back,payment,captureBTN;
    EditText name,email,icNo,phoneNum;
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
        setContentView(R.layout.activity_cus_info);

        back=findViewById(R.id.Back);
        payment=findViewById(R.id.payment);
        name=findViewById(R.id.name);
        email=findViewById(R.id.userEmail);
        icNo=findViewById(R.id.icNo);
        phoneNum=findViewById(R.id.phoneNum);
        photo=findViewById(R.id.photo);
        captureBTN=findViewById(R.id.capture_image_btn);
        car=(Car)getIntent().getSerializableExtra("CarDetails");

        requestStoragePermission();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CusInfo.this, FindCarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

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

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CusInfo.this, payment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("CarDetails",car);
                startActivity(intent);
            }
        });

//        payment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final ProgressDialog loading = ProgressDialog.show(CusInfo.this, "Please Wait", "Contacting Server", false, false);
//
//                StringRequest stringRequest = new StringRequest(Request.Method.POST,
//                        "https://mobilehost2019.com/KhorHuanYong/php/paymentDetails.php", new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        loading.dismiss();
//
//                        if (response.equalsIgnoreCase("Success")) {
//
//                            Toast.makeText(CusInfo.this, "Payment successfully made...", Toast.LENGTH_LONG)
//                                    .show();
//
//                            String name1=name.getText().toString().trim();
//                            String icNo1=icNo.getText().toString().trim();
//                            String email1= email.getText().toString().trim();
//                            String phoneNum1= phoneNum.getText().toString().trim();
//
//                            if(name1.isEmpty() || icNo1.isEmpty() || email1.isEmpty() || phoneNum1.isEmpty()) {
//                                Toast.makeText(CusInfo.this.getApplicationContext(), "Please fill in all the details", Toast.LENGTH_LONG).show();
//                            } else if (bitmap==null){
//                                Toast.makeText(CusInfo.this.getApplicationContext(), "Please upload your license picture", Toast.LENGTH_LONG).show();
//                            } else {
//                                uploadMultipart();
//                            }
//                        }else{
//
//                            Toast.makeText(CusInfo.this, "Error...", Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        loading.dismiss();
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(CusInfo.this,"No internet . Please check your connection",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                        else{
//
//                            Toast.makeText(CusInfo.this, error.toString(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }){
//                    @Override
//                    protected Map<String, String> getParams() {
//                        SharedPreferences sharedPreferences = CusInfo.this.getSharedPreferences("wilson", Context.MODE_PRIVATE);
//                        final String email = sharedPreferences.getString("user_email", "Not Available");
//
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("userEmail", email);
//                        params.put("name", car.getPlateNum());
//                        return params;
//                    }
//
//                };
//
//                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        30000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                requestQueue.add(stringRequest);
//
//            }
//        });
    }
    private void Upload(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://mobilehost2019.com/KhorHuanYong/php/payment_update.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("Success")) {
                            uploadMultipart();
                        } else {
                            Toast.makeText(CusInfo.this, "Pending...", Toast.LENGTH_SHORT).show();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(CusInfo.this, "No internet . Please check your connection",
                            Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(CusInfo.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void uploadMultipart() {
        //getting name for the image
        String name1=name.getText().toString().trim();
        String icNo1=icNo.getText().toString().trim();
        String email1= email.getText().toString().trim();
        String phoneNum1= phoneNum.getText().toString().trim();

            //getting the actual path of the image
            String path = getPath(filePath);

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, "https://mobilehost2019.com/KhorHuanYong/php/uploadCusInfo.php")
                        .addFileToUpload(path, "image") //Adding file
                        .addParameter("plateNum", car.getPlateNum())
                        .addParameter("name", name1)
                        .addParameter("icNo", icNo1)
                        .addParameter("phoneNum", phoneNum1)
                        .addParameter("email", email1)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
//                Toast.makeText(this, "Data uploaded successfully...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CusInfo.this, MainActivity.class);
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

                bitmap = MediaStore.Images.Media.getBitmap(CusInfo.this.getApplicationContext().getContentResolver(), filePath);
                //  bitmap = new Compressor(getActivity().getApplicationContext()).compressToBitmap(file);
                photo.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = CusInfo.this.getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = CusInfo.this.getApplicationContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(CusInfo.this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(CusInfo.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(CusInfo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
