package com.cosi153a.todopro;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.io.*;
import java.util.*;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.Geocoder;
import android.widget.EditText;
import android.location.Address;
import android.view.View;

public class MapActivity extends Activity {

    private GoogleMap googleMap;
    private LatLng latlng;
    private MarkerOptions markerOptions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            googleMap.setMyLocationEnabled(true);


        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Would you like traffic enabled?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        googleMap.setTrafficEnabled(true);
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        googleMap.setTrafficEnabled(false);
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
    public void search_loc(View view) {

        googleMap.clear();

        EditText etLocation = (EditText) findViewById(R.id.search);
        String location = etLocation.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> addressList = null;

        try {
            addressList = gc.getFromLocationName(location, 1);

        } catch (IOException e) {
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }

        for (int i = 0; i < addressList.size(); i++) {
            Address address = (Address) addressList.get(i);
            latlng = new LatLng(address.getLatitude(), address.getLongitude());

            markerOptions = new MarkerOptions();
            markerOptions.position(latlng);
            markerOptions.title(location);

            googleMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));

        }

    }

    public void finishBtn(View view) {
        finish();
    }

}
