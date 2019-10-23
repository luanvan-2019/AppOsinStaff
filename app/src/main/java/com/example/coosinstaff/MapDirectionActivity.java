package com.example.coosinstaff;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.coosinstaff.model.directionmap.FetchURL;
import com.example.coosinstaff.model.directionmap.TaskLoadedCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MapDirectionActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private static final String API_KEY = BuildConfig.ApiKey;

    GoogleMap map;
    Button btnGetDirection;
    MarkerOptions place1,place2;
    String orderType;
    Integer idOrder;
    Connection connect;
    Float latitudeOrder,longitudeOrder;
    Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_direction);

        btnGetDirection = findViewById(R.id.btn_direction_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

        idOrder = getIntent().getIntExtra("idOrder",0);
        orderType = getIntent().getStringExtra("orderType");
        Log.d("BBB",idOrder+"");
        Log.d("BBB",orderType);

        if (orderType.trim().equals("Dùng lẻ")){
            try {
                com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                connect =conStr.CONN();
                // Connect to database
                if (connect == null){Toast.makeText(getApplicationContext(),"Không có kết nối mạng!",Toast.LENGTH_LONG).show();}
                else {
                    // Change below query according to your own database.
                    String query = "select * from ORDER_SINGLE where ID= '" + idOrder  + "'";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        latitudeOrder = rs.getFloat("LATITUDE");
                        longitudeOrder = rs.getFloat("LONGITUDE");
                    }
                }
                connect.close();
            }
            catch (Exception ex){
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        place1 = new MarkerOptions().position(new LatLng(27.658143,85.3199503)).title("Vị trí của tôi");
        place2 = new MarkerOptions().position(new LatLng(27.667491,85.3208583)).title("Vị trí làm việc");

        PushDownAnim.setPushDownAnimTo(btnGetDirection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getUrl(place1.getPosition(), place2.getPosition(),"driving");
                new FetchURL(MapDirectionActivity.this).execute(url,"driving");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(place1);
        map.addMarker(place2);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.ApiKey);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }
}
