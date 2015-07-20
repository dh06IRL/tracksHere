package com.david.trackshere.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.david.trackshere.R;


public class MainActivity extends ActionBarActivity {

//    private MapView mv;
    private String currentMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        mv = (MapView) findViewById(R.id.mapview);
//        mv.setMinZoomLevel(mv.getTileProvider().getMinimumZoomLevel());
//        mv.setMaxZoomLevel(mv.getTileProvider().getMaximumZoomLevel());
//        mv.setCenter(mv.getTileProvider().getCenterCoordinate());
//        mv.setZoom(0);
////        currentMap = getString(R.string.streetMapId);
//
//        mv.setUserLocationEnabled(true);
//
//        mv.loadFromGeoJSONURL("https://a.tiles.mapbox.com/v4/dhodge.ki9fgm3g/features.json?access_token=pk.eyJ1IjoiZGhvZGdlIiwiYSI6Ikdvd29admMifQ.gJoIa-YeVroiIdHE-ScNQA");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent test = new Intent(getApplicationContext(), GMapActivity.class);
            startActivity(test);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
