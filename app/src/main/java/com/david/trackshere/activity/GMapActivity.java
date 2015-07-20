package com.david.trackshere.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.FeatureCollection;
import com.cocoahero.android.geojson.GeoJSON;
import com.cocoahero.android.geojson.LineString;
import com.cocoahero.android.geojson.Point;
import com.david.trackshere.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by davidhodge on 12/22/14.
 */
public class GMapActivity extends ActionBarActivity implements SensorEventListener{

    MapView mapView;
    GoogleMap mMap;
    Context mContext;
    Location mLocation;
    float[] mRotationMatrix = new float[16];
    float mDeclination;
    String dataUrl;
    String name;
    String lat;
    String lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_gmap);

        Intent intent = getIntent();
        dataUrl = intent.getStringExtra("data");
        name = intent.getStringExtra("name");
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        MapsInitializer.initialize(this);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
//        mapView.onResume();
        setUpMapIfNeeded();
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
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mLocation = location;

                GeomagneticField field = new GeomagneticField(
                        (float)location.getLatitude(),
                        (float)location.getLongitude(),
                        (float)location.getAltitude(),
                        System.currentTimeMillis()
                );

                // getDeclination returns degrees
                mDeclination = field.getDeclination();
            }
        });
//
//        if (mMap.getMyLocation() != null) {
//            LatLng myLocation = new LatLng(mMap.getMyLocation().getLatitude(),
//                    mMap.getMyLocation().getLongitude());
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
//                    13));
//        }else{
            LatLng myLocation = new LatLng(Double.parseDouble(lat),
                    Double.parseDouble(lon));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                    18));
//        }

//        // Create new TileOverlayOptions instance.
//        TileOverlayOptions opts = new TileOverlayOptions();
//
//        // Find your MapBox online map ID.
//        String myMapID = "dhodge.ki9fgm3g";
//
//        // Create an instance of MapBoxOnlineTileProvider.
//        MapBoxOnlineTileProvider provider = new MapBoxOnlineTileProvider(myMapID);
//
//        // Set the tile provider on the TileOverlayOptions.
//        opts.tileProvider(provider);
//
//        // Add the tile overlay to the map.
//        TileOverlay overlay = mMap.addTileOverlay(opts);

        IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setColor(mContext.getResources().getColor(android.R.color.transparent));
        iconFactory.setTextAppearance(mContext, android.R.style.TextAppearance_Material_Widget_EditText);
        addIcon(iconFactory, "Default", new LatLng(36.888, -76.998));

        getData();
    }

    private void addIcon(IconGenerator iconFactory, String text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        mMap.addMarker(markerOptions);
    }


    private void updateCamera(float bearing) {
        CameraPosition oldPos = mMap.getCameraPosition();

        CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(
                    mRotationMatrix, event.values);
            float[] orientation = new float[3];
            SensorManager.getOrientation(mRotationMatrix, orientation);
            float bearing = (float) Math.toDegrees(orientation[0]) + mDeclination;
            updateCamera(bearing);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void getData() {
        Ion.with(mContext)
                .load(dataUrl)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Toast.makeText(mContext, "Error loading", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Log.d("response", result);

                        try {
//                            GeoJSONObject geoJSON = GeoJSON.parse(result);
                            FeatureCollection featureCollection = (FeatureCollection) GeoJSON.parse(result);
                            Log.d("response", featureCollection.getFeatures().toString());

//                            for(int i = 0; i < featureCollection.getFeatures().size(); i++){
                            for (Feature f : featureCollection.getFeatures()) {
                                // Parse Into UI Objections
                                int j;
                                if (f.getGeometry() instanceof Point) {
                                    Log.d("response", f.getGeometry().toString());
                                    JSONArray coordinates = (JSONArray) f.getGeometry().toJSON().get("coordinates");
                                    double lon = (Double) coordinates.get(0);
                                    double lat = (Double) coordinates.get(1);

                                    IconGenerator iconFactory = new IconGenerator(mContext);
                                    iconFactory.setColor(mContext.getResources().getColor(android.R.color.transparent));
                                    iconFactory.setTextAppearance(mContext, android.R.style.TextAppearance_Material_Widget_EditText);
                                    addIcon(iconFactory, f.getProperties().optString("title"), new LatLng(lat, lon));
                                    //TODO ADD POINT TO MAP
                                } else if (f.getGeometry() instanceof LineString) {
                                    //TODO ADD LINE TO MAP
                                    Log.d("response", f.getGeometry().toString());
                                    PolylineOptions options = new PolylineOptions()
                                            .width(5)
                                            .color(Color.parseColor(f.getProperties().getString("stroke")))
                                            .geodesic(true);
                                    JSONArray points = (JSONArray) f.getGeometry().toJSON().get("coordinates");
                                    JSONArray coordinates;
                                    for (j = 0; j < points.length(); j++) {
                                        coordinates = (JSONArray) points.get(j);
                                        double lon = (Double) coordinates.get(0);
                                        double lat = (Double) coordinates.get(1);
                                        options.add(new LatLng(lat, lon));
                                    }
                                    Polyline polyline = mMap.addPolyline(options);
                                } else {
                                    Log.d("response", f.getGeometry().toString());
                                }
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Log.e("error", e1.toString());
                        }

//                        GeoResponse geoResponse = new Gson().fromJson(result, GeoResponse.class);
//                        for(int i = 0; i < geoResponse.getFeatures().size(); i++){
//                            if(TextUtils.equals(geoResponse.getFeatures().get(i).getGeometry().getType(), "Point")){
//                                IconGenerator iconFactory = new IconGenerator(mContext);
//                                iconFactory.setColor(mContext.getResources().getColor(android.R.color.transparent));
//                                iconFactory.setTextAppearance(mContext, android.R.style.TextAppearance_Material_Widget_EditText);
//                                addIcon(iconFactory, geoResponse.getFeatures().get(i).getProperties().getTitle(), new LatLng(geoResponse.getFeatures().get(i).getGeometry().getCoordinates().get(0), geoResponse.getFeatures().get(i).getGeometry().getCoordinates().get(0)));
//                                //TODO ADD POINT TO MAP
//                            }else if(TextUtils.equals(geoResponse.getFeatures().get(i).getGeometry().getType(), "LineString")){
//                                //TODO ADD LINE TO MAP
//                            }
//                        }

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
