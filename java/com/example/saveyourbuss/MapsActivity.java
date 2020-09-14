package com.example.saveyourbuss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static android.location.LocationManager.GPS_PROVIDER;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button goBtn,loadAllBusBtn;
    DBHelper helper;
    double lat, lan;
    double passlat, passlan;
    String nameshow, feedback;

    EditText searchEditText;
    GoogleMap getmMapap;
    Context context;
    LocationListener locationListener;
    LocationManager locationManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleServiceAvailable()) {
            Toast.makeText(getApplicationContext(), "Perfect", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_maps);
            mapload();
        }
        helper = new DBHelper(this);

        searchEditText = findViewById(R.id.searchEditText);
        goBtn = findViewById(R.id.goBtn);
        loadAllBusBtn = findViewById(R.id.loadAllBusBtn);
        loadAllBusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchValues();
            }
        });
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    search();
            }
        });

    }

    // loading map
    private void mapload() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    // check api availability
    public boolean googleServiceAvailable() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(isAvailable)) {
            Toast.makeText(getApplicationContext(), "Error is" + googleApiAvailability.getErrorString(isAvailable), Toast.LENGTH_LONG).show();

        } else
            Toast.makeText(getApplicationContext(), "Sorry Map Can't Load", Toast.LENGTH_LONG).show();
        return false;
    }

    // getMapLocation
    private void getMapLocation(double lat, double lan) {

        LatLng location = new LatLng(lat, lan);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
    }

    // load all the saved location of busses
    public void fetchValues(){
        Cursor cursor;
        cursor = helper.getData();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    lat = cursor.getDouble(cursor.getColumnIndex(helper.LAT));
                    lan = cursor.getDouble(cursor.getColumnIndex(helper.LAN));
                    nameshow = cursor.getString(cursor.getColumnIndex(helper.NAME));
                    feedback = cursor.getString(cursor.getColumnIndex(helper.FEED));

                    LatLng location = new LatLng(lat, lan);
                    mMap.addMarker(new MarkerOptions().position(location).title(nameshow).snippet(feedback).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconn)));


                } while (cursor.moveToNext());
            }
        } else {
            Toast.makeText(this, "Not found", Toast.LENGTH_LONG).show();
        }
    }



    // search Location on Map
    public void search() {

        String locationname = searchEditText.getText().toString();
        if (locationname.isEmpty()){
            searchEditText.setError("Input required");
        }else {
            //Geocoder is used to get string and find lat and lan
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocationName(locationname, 1);
                Address address = addresses.get(0);
                String locName = address.getLocality();
                Toast.makeText(getApplicationContext(), "Location Name is:" + locName, Toast.LENGTH_LONG).show();
                lat = address.getLatitude();
                lan = address.getLongitude();
                getMapLocation(lat, lan);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getMapLocation( 55.006763, -7.318268);

        locationManager = (LocationManager) getSystemService(context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lan = location.getLongitude();
                getMapLocation(lat, lan);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }
        locationManager.requestLocationUpdates(GPS_PROVIDER, 100000, 0, locationListener);
        //mMap.setMyLocationEnabled(true);

        Toast.makeText(MapsActivity.this,"RUNA",Toast.LENGTH_LONG).show();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                Toast.makeText(MapsActivity.this,"RUNB",Toast.LENGTH_LONG).show();
                passlat = latLng.latitude;
                passlan = latLng.longitude;
                MarkerOptions options = new MarkerOptions()
                        .title("location")
                        .position(latLng);
                mMap.addMarker(options);

                Intent intent = new Intent(MapsActivity.this, SaveDataActivity.class);
                intent.putExtra("LAT", passlat);
                intent.putExtra("LAN", passlan);
                startActivity(intent);
            }
        });


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                View v = layoutInflater.inflate(R.layout.dialog, null);

                TextView ett1, ett2;
                ett1 = v.findViewById(R.id.busNumber);
                ett2 = v.findViewById(R.id.aboutBus);
                helper = new DBHelper(getApplicationContext());


                Cursor cursor;
                cursor = helper.getData();
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            lat = cursor.getDouble(cursor.getColumnIndex(helper.LAT));
                            lan = cursor.getDouble(cursor.getColumnIndex(helper.LAN));
                            nameshow = cursor.getString(cursor.getColumnIndex(helper.NAME));
                            feedback = cursor.getString(cursor.getColumnIndex(helper.FEED));

                            if (lat == marker.getPosition().latitude && lan == marker.getPosition().longitude) {
                                ett1.setText("Number:" + nameshow);
                                ett2.setText("About:" + feedback);

                            } else {
                                
                            }


                        } while (cursor.moveToNext());


                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                }


                return v;
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    finish();
                    startActivity(getIntent());
                } else {
                    finish();
                    Toast.makeText(MapsActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
