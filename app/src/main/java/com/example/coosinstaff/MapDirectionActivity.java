package com.example.coosinstaff;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.coosinstaff.model.GPSRoutine;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapDirectionActivity extends AppCompatActivity implements
        OnMapReadyCallback, View.OnClickListener {

    FusedLocationProviderClient fusedLocationProviderClient;
    Location mLocation;
    GPSRoutine gpsRoutine;

    SupportMapFragment mapFragment;
    Marker oldMarker, newMarker;
    CameraUpdate cameraUpdate;
    MarkerOptions markerOptions;
    LatLng ll;
    GoogleMap gMap;

    String[] permission = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    final int RC_PERMISSIONS = 100;
    FloatingActionButton fab;
    Button btnWates,btnNganjuk;
    TextView txtKhoangCach,txtThoiGian;

    ArrayList<Marker> arrayMarker = new ArrayList<>();
    ArrayList<HashMap<String,String>> arrayLocation = new ArrayList<>();
    ArrayList<HashMap<String,Double>> arraySteps = new ArrayList<>();

    String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?"+
            "key=AIzaSyDb8vrfFWCuSNigtxvfM-zC-4MvNQlGIFQ"+
            "&query=";
    //10.770641
    //106.6646647
    String url2 = "https://maps.googleapis.com/maps/api/directions/json?"+
            "key=AIzaSyDb8vrfFWCuSNigtxvfM-zC-4MvNQlGIFQ&";

    String MODE_DRIVING = "driving";
    String urlQuery = "";

    int firstTimeShow = 0;
    Connection connect;
    Integer idOrder;
    String orderType;
    Float latitudeOrder,longitudeOrder;
    RelativeLayout imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_direction);

        fab = findViewById(R.id.floatingActionButton);
        btnWates = findViewById(R.id.btnDirection);
        txtKhoangCach = findViewById(R.id.txt_khoangcach);
        txtThoiGian = findViewById(R.id.txt_thoigian);
        imgBack = findViewById(R.id.ic_back);

        fab.setOnClickListener(this);
        btnWates.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        idOrder = getIntent().getIntExtra("idOrder",0);
        orderType = getIntent().getStringExtra("orderType");

        if (orderType.trim().equals("Dùng lẻ")){
            try
            {
                com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                connect =conStr.CONN();        // Connect to database
                if (connect == null)
                {
                    Toast.makeText(getApplicationContext(),"Không có kết nối mạng !",Toast.LENGTH_LONG).show();
                }
                else
                {
                    // Change below query according to your own database.
                    String query = "select * from ORDER_SINGLE where ID="+idOrder+"";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
                        latitudeOrder=rs.getFloat("LATITUDE");
                        longitudeOrder=rs.getFloat("LONGITUDE");
                    }
                }
                connect.close();
            }
            catch (Exception ex)
            {

            }
        }else if (orderType.trim().equals("Định kỳ")){
            try
            {
                com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                connect =conStr.CONN();        // Connect to database
                if (connect == null)
                {
                    Toast.makeText(getApplicationContext(),"Không có kết nối mạng !",Toast.LENGTH_LONG).show();
                }
                else
                {
                    // Change below query according to your own database.
                    String query = "select * from ORDER_MULTI where ID="+idOrder+"";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
                        latitudeOrder=rs.getFloat("LATITUDE");
                        longitudeOrder=rs.getFloat("LONGITUDE");
                    }
                }
                connect.close();
            }
            catch (Exception ex)
            {

            }
        }
        else if (orderType.trim().equals("Tổng vệ sinh")){
            try
            {
                com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                connect =conStr.CONN();        // Connect to database
                if (connect == null)
                {
                    Toast.makeText(getApplicationContext(),"Không có kết nối mạng !",Toast.LENGTH_LONG).show();
                }
                else
                {
                    // Change below query according to your own database.
                    String query = "select * from ORDER_OVERVIEW where ID="+idOrder+"";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
                        latitudeOrder=rs.getFloat("LATITUDE");
                        longitudeOrder=rs.getFloat("LONGITUDE");
                    }
                }
                connect.close();
            }
            catch (Exception ex)
            {

            }
        }else {
            try
            {
                com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                connect =conStr.CONN();        // Connect to database
                if (connect == null)
                {
                    Toast.makeText(getApplicationContext(),"Không có kết nối mạng !",Toast.LENGTH_LONG).show();
                }
                else
                {
                    // Change below query according to your own database.
                    String query = "select * from ORDER_COOK where ID="+idOrder+"";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
                        latitudeOrder=rs.getFloat("LATITUDE");
                        longitudeOrder=rs.getFloat("LONGITUDE");
                    }
                }
                connect.close();
            }
            catch (Exception ex)
            {

            }
        }
    }

    public void getThisDeviceLocation(){
        if (Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(MapDirectionActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MapDirectionActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)!=
                            PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MapDirectionActivity.this,permission,RC_PERMISSIONS);
            }
        }
        gpsRoutine = new GPSRoutine(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                MapDirectionActivity.this);
        fusedLocationProviderClient.requestLocationUpdates(
                gpsRoutine.getmLocationRequest(),
                locationCallback, Looper.myLooper());
    }

    LocationCallback locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            mLocation = locationResult.getLastLocation();
            ll = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());

            if (oldMarker != null) oldMarker.remove();
            newMarker = gMap.addMarker(new MarkerOptions().position(ll));
            newMarker.setIcon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_AZURE));
            newMarker.setTitle("Vị trí của tôi");

            oldMarker = newMarker;

            if (firstTimeShow == 0){
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll,16);
                gMap.animateCamera(cameraUpdate);
                firstTimeShow= 1;
            }
        }
    };



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.floatingActionButton:
                firstTimeShow = 0;
                getThisDeviceLocation();
                break;
            case R.id.btnDirection:
                //vi tri ca lam truyen vao
                String latlng = latitudeOrder+","+longitudeOrder;
                cariLokasi(latlng);
                break;
            case R.id.ic_back:
                finish();
                break;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        if (gMap!=null){
            getThisDeviceLocation();
        }
        String latlng = latitudeOrder+","+longitudeOrder;
        cariLokasi(latlng);
    }

    private void cariLokasi(String lokasi) {
        gMap.clear();

        getThisDeviceLocation();
        urlQuery = url+lokasi;
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(urlQuery,
                null,jsonObjectListener,jsonOjectErrorListener);
        Volley.newRequestQueue(MapDirectionActivity.this).add(jsonObjectRequest);
    }

    Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                String lat="",lng="",namaTmp="",alamat="",placeId="";

                JSONArray places = response.getJSONArray("results");
                arrayLocation.clear();
                for (int i= 0; i<places.length();i++){
                    JSONObject info = places.getJSONObject(i);
                    namaTmp= info.getString("name");
                    alamat = info.getString("formatted_address");
                    placeId = info.getString("place_id");
                    JSONObject geometri = info.getJSONObject("geometry");
                    JSONObject location = geometri.getJSONObject("location");
                    lat = location.getString("lat");
                    lng = location.getString("lng");
                    HashMap<String,String> hm = new HashMap<>();
                    hm.put("lat",lat);
                    hm.put("lng",lng);
                    hm.put("nama_tmp",namaTmp);
                    hm.put("alamat",alamat);
                    hm.put("place_id",placeId);
                    arrayLocation.add(hm);
                }
                addMarkerToMap(places.length());
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    Response.Listener<JSONObject> jsonObjectDirectionListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                double startLat = 0, startLng = 0, stopLat = 0, stopLng = 0,gDistance = 0;
                arraySteps.clear();

                JSONObject routes = response.getJSONArray("routes").getJSONObject(0);
                JSONObject legs = routes.getJSONArray("legs").getJSONObject(0);
                JSONArray steps = legs.getJSONArray("steps");

                JSONObject distance = legs.getJSONObject("distance");
                String dis = distance.getString("text");
                JSONObject duration =legs.getJSONObject("duration");
                String dur = duration.getString("text");
                txtKhoangCach.setText(dis);
                txtThoiGian.setText(dur);

                for (int i = 0; i<steps.length();i++){
                    JSONObject step = steps.getJSONObject(i);
                    JSONObject startLocation = step.getJSONObject("start_location");
                    startLat = startLocation.getDouble("lat");
                    startLng = startLocation.getDouble("lng");
                    JSONObject endLocation = step.getJSONObject("end_location");
                    stopLat = endLocation.getDouble("lat");
                    stopLng = endLocation.getDouble("lng");

                    HashMap<String,Double> hm = new HashMap<>();
                    hm.put("start_lat",startLat);
                    hm.put("stop_lat",stopLat);
                    hm.put("start_lng",startLng);
                    hm.put("stop_lng",stopLng);
                    arraySteps.add(hm);
                }
                gambarPollyline(arraySteps, Color.RED,10f);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener jsonOjectErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getBaseContext(),"Error : "+error.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

    public void addMarkerToMap(int length){
        LocationManager mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                mLocation = mLocationManager.getLastKnownLocation(provider);
            }
            if (mLocation == null) {
                continue;
            }
            if (bestLocation == null || mLocation.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = mLocation;
            }
        }
        mLocation = bestLocation;
        markerOptions = new MarkerOptions();
        for(int i=0; i<length;i++){
            ll = new LatLng(
                    Double.valueOf(arrayLocation.get(i).get("lat")),
                    Double.valueOf(arrayLocation.get(i).get("lng"))
            );
            markerOptions.position(ll);
            markerOptions.title(arrayLocation.get(i).get("nama_tmp"));
            markerOptions.snippet(arrayLocation.get(i).get("alamat"));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_RED));
            newMarker = gMap.addMarker(markerOptions);
            newMarker.setTag(i);

            arrayMarker.add(newMarker);

            urlQuery = url2+"origin="
                    +String.valueOf(mLocation.getLatitude())+","
                    +String.valueOf(mLocation.getLongitude())+"&"+
                    "mode="+MODE_DRIVING+"&"+
                    "destination=place_id:"+arrayLocation.get(i).get("place_id");

            JsonObjectRequest jsonObjectRequest = new
                    JsonObjectRequest(urlQuery,
                    null,jsonObjectDirectionListener,jsonOjectErrorListener);
            Volley.newRequestQueue(MapDirectionActivity.this).add(jsonObjectRequest);
        }

        ll = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll,12);
        gMap.animateCamera(cameraUpdate);
    }

    public void gambarPollyline(ArrayList<HashMap<String,Double>> arraySteps, int warna, float lebar){
        ArrayList<LatLng> arrayLatLng = new ArrayList<>();
        for (int i=0;i<arraySteps.size();i++){
            arrayLatLng.add(new LatLng(arraySteps.get(i).get("start_lat"),
                    arraySteps.get(i).get("start_lng")));
            arrayLatLng.add(new LatLng(arraySteps.get(i).get("stop_lat"),
                    arraySteps.get(i).get("stop_lng")));
        }

        PolylineOptions polylineOptions = new PolylineOptions()
                .clickable(true).addAll(arrayLatLng).color(warna).width(lebar);
        gMap.addPolyline(polylineOptions);
    }

}