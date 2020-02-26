package com.example.prototypenew;

import android.content.Intent;
import android.widget.AdapterView;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

public class CarShow extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener
        {

    ImageView photo;
    TextView plateNum, carType, carName, date, date1,time,time1,price,phone;
    String PlateNum,CarType,CarName, Date, Date1,Time,Time1,Price,Phone,Photo;
    Button back,rent;
    Car car;
    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_show);
        car=(Car)getIntent().getSerializableExtra("CarDetails");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initializing googleapi client
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();

        plateNum = findViewById(R.id.plateNum);
        carType = findViewById(R.id.carType);
        carName = findViewById(R.id.carName);
        date = findViewById(R.id.date);
        date1 = findViewById(R.id.date1);
        time = findViewById(R.id.time);
        time1 = findViewById(R.id.time1);
        price = findViewById(R.id.price);
        phone = findViewById(R.id.phone);
        back=findViewById(R.id.Back);
        rent=findViewById(R.id.rent);
        photo=findViewById(R.id.photo);

        latitude=Double.valueOf(car.getLatitude());
        longitude=Double.valueOf(car.getLongitude());
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
        carType.setText(CarType);
        carName.setText(CarName);
//        date.setText(Date);
//        date1.setText(Date1);
        time.setText(Time);
        time1.setText(Time1);
        price.setText(Price);
        phone.setText(Phone);

        RequestOptions options = new RequestOptions().centerCrop().dontAnimate().placeholder(R.drawable.camera).error(R.drawable.camera);
        Glide
                .with(CarShow.this)
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

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarShow.this, payment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("CarDetails",car);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarShow.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

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
                LatLng latLng = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(new MarkerOptions()
                        .position(latLng) //setting position
                        .draggable(true) //Making the marker draggable
                        .title("Car Location")); //Adding a title
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                mMap.setOnMarkerDragListener(this);
                mMap.setOnMapLongClickListener(this);
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
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

            }
}