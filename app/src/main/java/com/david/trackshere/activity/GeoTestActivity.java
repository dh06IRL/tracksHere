package com.david.trackshere.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.david.trackshere.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

/**
 * Created by davidhodge on 2/4/15.
 */
public class GeoTestActivity extends BaseActivity implements GeoQueryEventListener,  GoogleMap.OnCameraChangeListener {

    MapView mapView;
    GoogleMap mMap;
    Context mContext;
//    Location mLocation;
    FloatingActionButton floatAdd;
//    Location location;

    GeoFire geoFire;
    GeoQuery geoQuery;
    String GEO_FIRE_REF = "https://zeen.firebaseio.com/";
    Map<String,Marker> markers;
    Circle searchCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_gmap);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        MapsInitializer.initialize(this);

        mapView = (MapView) findViewById(R.id.map);
        floatAdd = (FloatingActionButton) findViewById(R.id.float_add);
        mapView.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        geoFire = new GeoFire(new Firebase(GEO_FIRE_REF));
        markers = new HashMap<String, Marker>();

//        mapView.onResume();
        setUpMapIfNeeded();

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject route = new ParseObject("cop");
                ParseGeoPoint parseGeoPoint = new ParseGeoPoint();
                route.put("name", "Police");
                route.put("time", new Date());
                route.put("location", parseGeoPoint);
                route.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    public void getGeoPoints(Location location){
        ParseGeoPoint userLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("cop");
        query.whereWithinMiles("location", userLocation, 2);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    Log.d("spots", "parse response " + parseObjects.size());

                    for(int i = 0; i < parseObjects.size(); i++){
                        ParseGeoPoint point = parseObjects.get(i).getParseGeoPoint("location");
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(point.getLatitude(), point.getLongitude()))
                                .title(parseObjects.get(i).getString("name")));
                    }
                }else{
                    Log.e("spots", "parse error " + e.toString());
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

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                ParseObject route = new ParseObject("cop");
                ParseGeoPoint parseGeoPoint = new ParseGeoPoint(latLng.latitude, latLng.longitude);
                route.put("name", "Police");
                route.put("location", parseGeoPoint);
                route.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();
                        Location location2 = new Location("");
                        location2.setLatitude(latLng.latitude);
                        location2.setLongitude(latLng.longitude);
                        getGeoPoints(location2);
                    }
                });
            }
        });

        SmartLocation.with(mContext)
                .location()
                .config(LocationParams.NAVIGATION)
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location loc) {
                        GeoLocation INITIAL_CENTER = new GeoLocation(loc.getLatitude(), loc.getLongitude());
                        geoQuery = geoFire.queryAtLocation(INITIAL_CENTER, 1);
                        geoQuery.addGeoQueryEventListener(GeoTestActivity.this);
                        getGeoPoints(loc);

                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(loc.getLatitude(), loc.getLongitude()))
                                .zoom(mMap.getCameraPosition().zoom)
                                .bearing(mMap.getCameraPosition().bearing)
                                .tilt(mMap.getCameraPosition().tilt)
                                .build()));
                    }
                });
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
    protected void onStop() {
        super.onStop();
        // remove all event listeners to stop updating in the background
        geoQuery.removeAllListeners();
        for (Marker marker: markers.values()) {
            marker.remove();
        }
        markers.clear();
        SmartLocation.with(mContext).location().stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // add an event listener to start updating locations again
//        geoQuery.addGeoQueryEventListener(this);
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
        geoFire.removeLocation("firebase-hq");
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        geoFire.removeLocation("firebase-hq");
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)));
        this.markers.put(key, marker);
    }

    @Override
    public void onKeyExited(String key) {
        Marker marker = this.markers.get(key);
        if (marker != null) {
            marker.remove();
            this.markers.remove(key);
        }
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        Marker marker = this.markers.get(key);
        if (marker != null) {
            animateMarkerTo(marker, location.latitude, location.longitude);
        }
    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(FirebaseError error) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        // Update the search criteria for this geoQuery and the circle on the map
        LatLng center = cameraPosition.target;
        double radius = zoomLevelToRadius(cameraPosition.zoom);
        searchCircle.setCenter(center);
        searchCircle.setRadius(radius);
        geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
        // radius in km
        geoQuery.setRadius(radius/1000);

        geoFire.setLocation("firebase-hq", new GeoLocation(center.latitude, center.longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, FirebaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });
    }

    private double zoomLevelToRadius(double zoomLevel) {
        // Approximation to fit circle into view
        return 16384000/Math.pow(2, zoomLevel);
    }

    private void animateMarkerTo(final Marker marker, final double lat, final double lng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long DURATION_MS = 3000;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final LatLng startPosition = marker.getPosition();
        handler.post(new Runnable() {
            @Override
            public void run() {
                float elapsed = SystemClock.uptimeMillis() - start;
                float t = elapsed/DURATION_MS;
                float v = interpolator.getInterpolation(t);

                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                marker.setPosition(new LatLng(currentLat, currentLng));

                // if animation is not finished yet, repeat
                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
}
