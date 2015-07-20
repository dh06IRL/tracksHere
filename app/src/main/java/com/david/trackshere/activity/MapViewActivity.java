package com.david.trackshere.activity;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.david.trackshere.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by davidhodge on 1/3/15.
 */
public class MapViewActivity extends BaseActivity {

    MapView mapView;
    GoogleMap mMap;
    Context mContext;
    Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_gmap);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        MapsInitializer.initialize(this);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
//        mapView.onResume();
        setUpMapIfNeeded();
    }

    public void retrieveTracks(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("tracks");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                try {
                    if (e == null) {
                        Log.d("spots", "parse response " + parseObjects.size());

                        for(int i = 0; i < parseObjects.size(); i++){
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(
                                            Double.parseDouble(parseObjects.get(i).getString("lat")),
                                            Double.parseDouble(parseObjects.get(i).getString("lon"))))
                                    .title(parseObjects.get(i).getString("name")));
                        }

                    } else {
                        Log.e("spots", "parse error " + e.toString());
                    }
                }catch (Exception e1){
                    Log.e("spots", e1.toString());
                }
            }
        });
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((MapView) findViewById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        retrieveTracks();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
