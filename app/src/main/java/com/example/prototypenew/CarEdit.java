package com.example.prototypenew;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.prototypenew.ui.dashboard.DashboardFragment;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CarEdit extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener {

    Button back,edit,cusDetails,delete;
    Spinner sp;
    List<String> list;
    String locat;
    ArrayAdapter<String> adp;
    EditText carName,date,date1,time,time1,price,phone;
    String PlateNum,CarType,CarName, Date, Date1,Time,Time1,Price,Phone,Photo;
    TextView plateNum;
    ImageView photo;
    Car car;
    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_edit);
        car=(Car)getIntent().getSerializableExtra("CarDetails");

//        requestStoragePermission();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initializing googleapi client
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();

        back=findViewById(R.id.Back);
        edit=findViewById(R.id.edit);
        sp=findViewById(R.id.carType);
        sp.setOnItemSelectedListener(this);
        plateNum=findViewById(R.id.plateNum);
        carName=findViewById(R.id.carName);
        date=findViewById(R.id.date);
        date1=findViewById(R.id.date1);
        time=findViewById(R.id.time);
        time1=findViewById(R.id.time1);
        price=findViewById(R.id.price);
        phone=findViewById(R.id.phone);
        photo=findViewById(R.id.photo);
        cusDetails=findViewById(R.id.cusDetails);
        delete=findViewById(R.id.Delete);

        Photo=car.getUrl();
        PlateNum=car.getPlateNum();
        CarType=car.getTypeOfCar();
        CarName=car.getNameOfCar();
        Date=car.getsDate();
        Date1=car.geteDate();
        Time=car.getsTime();
        Time1=car.geteTime();
        Price=car.getPrice();
        Phone=car.getPhoneNum();

        photo.setImageURI(Uri.parse(Photo));
        plateNum.setText(PlateNum);
        carName.setText(CarName);
        date.setText(Date);
        date1.setText(Date1);
        time.setText(Time);
        time1.setText(Time1);
        price.setText(Price);
        phone.setText(Phone);

        list = new ArrayList<>();
        list.add("Sedan");
        list.add("MPV");
        list.add("SUV");
        list.add("Hatchback");
        list.add("4*4");

        adp = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, list);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);


        RequestOptions options = new RequestOptions().centerCrop().dontAnimate().placeholder(R.drawable.camera).error(R.drawable.camera);
        Glide
                .with(CarEdit.this)
                .load(Photo).apply(options).listener(new RequestListener<Drawable>() {

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                photo.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                photo.setVisibility(View.VISIBLE);
                return false;
            }
        })
                .into(photo);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog loading = ProgressDialog.show(CarEdit.this, "Please Wait", "Contacting Server", false, false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        "https://mobilehost2019.com/KhorHuanYong/php/deleteCarDetails.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();

                        if (response.equalsIgnoreCase("Success")) {

                            Toast.makeText(CarEdit.this, "Successfully deleted...", Toast.LENGTH_LONG)
                                    .show();
                            Intent i = new Intent(CarEdit.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        }else{

                            Toast.makeText(CarEdit.this, "Error...", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(CarEdit.this,"No internet . Please check your connection",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{

                            Toast.makeText(CarEdit.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        String platenum=plateNum.getText().toString().trim();

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("name", platenum);
                        return params;
                    }

                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarEdit.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMultipart();
            }
        });

        cusDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarEdit.this, CusShow.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("CarDetails",car);
                startActivity(intent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//                    if (checkSelfPermission(Manifest.permission.CAMERA)==
//                            PackageManager.PERMISSION_DENIED ||
//                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
//                                    PackageManager.PERMISSION_DENIED){
//
//                        String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//                        requestPermissions(permission,PERMISSION_CODE);
//                    }
//                    else{
//                        showFileChooser();
//                    }
//                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        locat = parent.getSelectedItem().toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + locat, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(arg0.getContext(), "Please Select Type of Vehicles", Toast.LENGTH_LONG).show();

    }

    public void setDate(View v) {
        final Calendar myCalendar=Calendar.getInstance();
        int year=myCalendar.get(Calendar.YEAR);
        int month=myCalendar.get(Calendar.MONTH);
        int dt=myCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerDialog = new DatePickerDialog(CarEdit.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yEar, int mOnth, int dat) {
                myCalendar.set(Calendar.YEAR, yEar);
                myCalendar.set(Calendar.MONTH, mOnth);
                myCalendar.set(Calendar.DAY_OF_MONTH, dat);
                String myFormat = "dd-MM-yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                date.setText(sdf.format(myCalendar.getTime()));

            }
        }, year, month, dt);
        datepickerDialog.show();
    }
    public void setDate1(View v) {
        final Calendar myCalendar1=Calendar.getInstance();
        int year1=myCalendar1.get(Calendar.YEAR);
        int month1=myCalendar1.get(Calendar.MONTH);
        int dt1=myCalendar1.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerDialog1 = new DatePickerDialog(CarEdit.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yEar, int mOnth, int dat) {
                myCalendar1.set(Calendar.YEAR, yEar);
                myCalendar1.set(Calendar.MONTH, mOnth);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dat);
                String myFormat = "dd-MM-yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                date1.setText(sdf.format(myCalendar1.getTime()));


            }
        }, year1, month1, dt1);
        datepickerDialog1.show();
    }


    public void setTime(View v) {
        final Calendar d=Calendar.getInstance();
        int hour=d.get(Calendar.HOUR_OF_DAY);
        int mInute=d.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CarEdit.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                d.set(Calendar.HOUR_OF_DAY,hour);
                d.set(Calendar.MINUTE,minute);
                String myFormat = "HH:mm"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                time.setText(sdf.format(d.getTime()));
            }
        }, hour, mInute, false);
        timePickerDialog.show();
    }
    public void setTime1(View v) {
        final Calendar d=Calendar.getInstance();
        int hour=d.get(Calendar.HOUR_OF_DAY);
        int mInute=d.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog1 = new TimePickerDialog(CarEdit.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                d.set(Calendar.HOUR_OF_DAY,hour);
                d.set(Calendar.MINUTE,minute);
                String myFormat = "HH:mm"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                time1.setText(sdf.format(d.getTime()));
            }
        }, hour, mInute, false);
        timePickerDialog1.show();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.prototypenew/http/host/path")
        );
        AppIndex.AppIndexApi.start(googleApiClient, viewAction);
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.prototypenew/http/host/path")
        );
        AppIndex.AppIndexApi.end(googleApiClient, viewAction);
    }

    //Getting current location
    private void getCurrentLocation() {
        mMap.clear();
        //Creating a location object
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //moving the map to location
            moveMap();
        }
    }

    //Function to move the map
    private void moveMap() {
        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }


    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }


    public void onConnectionSuspended(int i) {

    }


    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mMap.clear();
        //Adding a new marker to the current pressed position
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));

        latitude = latLng.latitude;
        longitude = latLng.longitude;
    }


    public void onMarkerDragStart(Marker marker) {

    }


    public void onMarkerDrag(Marker marker) {

    }


    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void uploadMultipart() {
        //getting name for the image
//        String platenum=plateNum.getText().toString().trim();
//        String CarName=carName.getText().toString().trim();
//        String CarType=sp.getSelectedItem().toString();
//        String Date= date.getText().toString().trim();
//        String Date1= date1.getText().toString().trim();
//        String Time= time.getText().toString().trim();
//        String Time1= time1.getText().toString().trim();
//        String Price=price.getText().toString().trim();
//        String Phone = phone.getText().toString().trim();
//        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//        double latitude1 = location.getLatitude();
//        double longitude1 = location.getLongitude();

        SharedPreferences sharedPreferences = getSharedPreferences("wilson", Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString("user_email", "Not Available");
        String Email=email.trim();

            final ProgressDialog loading = ProgressDialog.show(CarEdit.this, "Please Wait", "Contacting Server", false, false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    "https://mobilehost2019.com/KhorHuanYong/php/update.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    loading.dismiss();

                    if (response.equalsIgnoreCase("Success")) {

                        Toast.makeText(CarEdit.this, "Data updated successfully...", Toast.LENGTH_LONG)
                                .show();
                        Intent i = new Intent(CarEdit.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }else{

                        Toast.makeText(CarEdit.this, "Data cannot update...", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(CarEdit.this,"No internet . Please check your connection",
                                Toast.LENGTH_LONG).show();
                    }
                    else{

                        Toast.makeText(CarEdit.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    String platenum=plateNum.getText().toString().trim();
                    String CarName=carName.getText().toString().trim();
                    String CarType=sp.getSelectedItem().toString();
                    String Date= date.getText().toString().trim();
                    String Date1= date1.getText().toString().trim();
                    String Time= time.getText().toString().trim();
                    String Time1= time1.getText().toString().trim();
                    String Price=price.getText().toString().trim();
                    String Phone = phone.getText().toString().trim();
                    Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    double latitude1 = location.getLatitude();
                    double longitude1 = location.getLongitude();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", platenum);
                    params.put("typeOfCar", CarType);
                    params.put("nameOfCar", CarName);
                    params.put("sDate", Date);
                    params.put("eDate", Date1);
                    params.put("sTime", Time);
                    params.put("eTime", Time1);
                    params.put("price", Price);
                    params.put("latitude", String.valueOf(latitude1));
                    params.put("longitude", String.valueOf(longitude1));
                    params.put("phoneNum", Phone);
                    params.put("ownerEmail", email);
                    return params;
                }

            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);


//        if (bitmap==null){
//            Toast.makeText(CarEdit.this.getApplicationContext(), "Please upload your latest car picture", Toast.LENGTH_LONG).show();
//        } else {
//            //getting the actual path of the image
//            String path = getPath(filePath);
//
//            //Uploading code
//            try {
//                String uploadId = UUID.randomUUID().toString();
//
//                //Creating a multi part request
//                new MultipartUploadRequest(this, uploadId, "https://mobilehost2019.com/KhorHuanYong/php/update.php")
//                        .addFileToUpload(path, "image") //Adding file
//                        .addParameter("name", platenum)
//                        .addParameter("typeOfCar", CarType)
//                        .addParameter("nameOfCar", CarName)
//                        .addParameter("sDate", Date)
//                        .addParameter("eDate", Date1)
//                        .addParameter("sTime", Time)
//                        .addParameter("eTime", Time1)
//                        .addParameter("price", Price)
//                        .addParameter("latitude", String.valueOf(latitude1))
//                        .addParameter("longitude", String.valueOf(longitude1))
//                        .addParameter("phoneNum", Phone)
//                        .addParameter("ownerEmail", email)//Adding text parameter to the request
//                        .setNotificationConfig(new UploadNotificationConfig())
//                        .setMaxRetries(2)
//                        .startUpload(); //Starting the upload
//                Toast.makeText(this,"Data updated successfully...",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(CarEdit.this, MainActivity.class);
//                startActivity(intent);
//
//
//            } catch (Exception exc) {
//                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
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

                // File file= new File(filePath.getPath());
                //file.getName();

                bitmap = MediaStore.Images.Media.getBitmap(CarEdit.this.getApplicationContext().getContentResolver(), filePath);
                //  bitmap = new Compressor(getActivity().getApplicationContext()).compressToBitmap(file);
                photo.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = CarEdit.this.getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = CarEdit.this.getApplicationContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(CarEdit.this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(CarEdit.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(CarEdit.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
}