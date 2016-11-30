package edu.orangecoastcollege.cs273.caffeinefinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class CaffeineListActivity extends AppCompatActivity implements OnMapReadyCallback{

    private DBHelper db;
    private List<Location> allLocationsList;
    private ListView locationsListView;
    private LocationListAdapter locationListAdapter;

    //new mwmbwe for google maps
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caffeine_list);

        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        db.importLocationsFromCSV("locations.csv");

        allLocationsList = db.getAllCaffeineLocations();

        locationsListView = (ListView) findViewById(R.id.locationsListView);
        locationListAdapter = new LocationListAdapter(this, R.layout.location_list_item, allLocationsList);
        locationsListView.setAdapter(locationListAdapter);


        SupportMapFragment caffMapFrag = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.caffeineMapFrag);

        caffMapFrag.getMapAsync(CaffeineListActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //use lat and longitude from each location to create a marker from google maps

        mMap = googleMap;
        for(Location loco: allLocationsList)
        {
            LatLng objLat = new LatLng(loco.getLatitude(), loco.getLongitude());
            mMap.addMarker(new MarkerOptions().position(objLat).title(loco.getName()));
        }

        //change cam to show waypoints
        LatLng currentPos = new LatLng(33.671028, -117.911305);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentPos).zoom(14.0f).build();
        //zoom has 14 zomm levels

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }
}
