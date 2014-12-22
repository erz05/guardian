package com.erz.weathermap;

import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.erz.weathermap.data.Countries;
import com.erz.weathermap.data.Country;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.Vector;

public class MainActivity extends FragmentActivity
        implements GoogleMap.OnMyLocationChangeListener, OnBackgroundTaskListener<Countries> {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    static final CameraPosition SYDNEY =
            new CameraPosition.Builder().target(new LatLng(-33.87365, 151.20689))
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();
    Marker myMarker, startMarker, endMarker;
    private Polyline myLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMapIfNeeded();

        TextView search = (TextView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
            }
        });

        //FetchStringTask task = new FetchStringTask(this, "https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyBGxJk3vM6McDk1NyCK_oMiKTNC6I3QBWM");
        //task.run();
        FetchCountriesTask task = new FetchCountriesTask(this, this);
        task.run();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(this);
//        mMap.addPolyline(new PolylineOptions()
//                .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
//                .width(5)
//                .color(Color.RED));
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    public void onMyLocationChange(Location location) {
        if(mMap != null && location != null){

            double lat = location.getLatitude();
            double lng = location.getLongitude();

            if(myMarker == null){
                myMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng)).title("It's Me!")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(lat, lng))
                        .zoom(0)
                        //.bearing(0)
                        //.tilt(25)
                        .build()));
            }else {
                myMarker.setPosition(new LatLng(lat, lng));
            }
        }
    }

    private void onSearch(){
        AddressTextView end = (AddressTextView) findViewById(R.id.end);
        AddressTextView start = (AddressTextView) findViewById(R.id.start);
        Address endAddress = end.getAddress();
        Address startAddress = start.getAddress();
        if(startAddress != null && endAddress != null){
            if(myLine == null) {
                myLine = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(startAddress.getLatitude(), startAddress.getLongitude()),
                                new LatLng(endAddress.getLatitude(), endAddress.getLongitude()))
                        .width(5)
                        .color(Color.RED));
            }else {
                List<LatLng> points = myLine.getPoints();
                points.clear();
                points.add(new LatLng(startAddress.getLatitude(), startAddress.getLongitude()));
                points.add(new LatLng(endAddress.getLatitude(), endAddress.getLongitude()));
                myLine.setPoints(points);
            }

            if(startMarker == null){
                startMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(startAddress.getLatitude(), startAddress.getLongitude())).title("Start"));
            }else {
                startMarker.setPosition(new LatLng(startAddress.getLatitude(), startAddress.getLongitude()));
            }

            if(endMarker == null){
                endMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(endAddress.getLatitude(), endAddress.getLongitude())).title("Finish")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }else {
                endMarker.setPosition(new LatLng(endAddress.getLatitude(), endAddress.getLongitude()));
            }
        }
    }

    @Override
    public void onBegin() {

    }

    @Override
    public void onProgress(String message) {

    }

    @Override
    public void onComplete(Countries result) {
        Log.v("DELETE_THIS", "result is null? "+(result == null));
        if(result != null && result.size() > 0){
            Country tmp = null;
            for(Country country: result){
                if(country.id.equals("Egypt")){
                    tmp = country;
                    break;
                }
            }

            if(tmp != null && tmp.coordinates != null){
                Polygon polygon = mMap.addPolygon(new PolygonOptions()
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE));

                Vector<LatLng> points = new Vector<LatLng>();
                for (double[] d: tmp.coordinates){
                    points.add(new LatLng(d[0],d[1]));
                }

                polygon.setPoints(points);
            }
        }
    }

    @Override
    public void onError(String message) {

    }
}
