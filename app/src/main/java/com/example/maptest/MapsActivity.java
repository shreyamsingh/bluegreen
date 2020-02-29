package com.example.maptest;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat, longi;
    String[] d;
    File imgfile;
    public static final String EXTRA_MESSAGE = "com.example.blueblue.MESSAGE";
    ArrayList<String> api = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();

        try {
            d = (intent.getStringExtra(MainActivity.EXTRA_MESSAGE)).split(" ");
            for (int i = 0; i < d.length/2; i++) {
                lat = Double.parseDouble(d[i]);
                longi = Double.parseDouble(d[i+1]);
                api.add(MainActivity.getPath());
            }
        }
        catch(Exception e){
            lat = 0.0;
            longi = 0.0;
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng sydney = null;
        for (int i = 0; i < d.length; i+=2) {
            sydney = new LatLng(Double.parseDouble(d[i]), Double.parseDouble(d[i+1]));
            mMap.addMarker(new MarkerOptions().position(sydney).title("" + i/2));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker m) {
                    send(findViewById(android.R.id.content).getRootView(), m.getTitle());
                    return true;
                }
            });
        }
        try {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
        catch (Exception e) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void gobacktoscreen(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void send(View v, String s) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.putExtra(EXTRA_MESSAGE, s);
            System.out.println(Integer.parseInt(s));
            intent.setDataAndType(MainActivity.getURI().get(Integer.parseInt(s)), "image/*");
            startActivity(intent);
        }
        catch (Exception e) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}