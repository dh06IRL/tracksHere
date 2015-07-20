package com.david.trackshere.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.david.trackshere.R;
import com.david.trackshere.cards.TrackConfigCard;
import com.david.trackshere.cards.WeatherCard;
import com.david.trackshere.models.current.CurrentResponse;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.views.GridViewWithHeaderAndFooter;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;

/**
 * Created by davidhodge on 12/28/14.
 */
public class TracksInfoActivity extends BaseActivity {

    Context mContext;
    @InjectView(R.id.card_list)
    GridViewWithHeaderAndFooter grid;
    @InjectView(R.id.loading)
    ProgressBar loading;
    @InjectView(R.id.float_map)
    FloatingActionButton floatMap;

    ArrayList<Card> cardArrayList;
    CardArrayAdapter cardArrayAdapter;
    SharedPreferences sharedPreferences;

    String searchString;
    String trackName;
    String lat;
    String lon;
    String requestUrl;

    Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_list);
        ButterKnife.inject(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mGson = new Gson();

        Intent intent = getIntent();
        searchString = intent.getStringExtra("search");
        trackName = intent.getStringExtra("name");
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");

        try {
            getSupportActionBar().setTitle(trackName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }catch (NullPointerException e){

        }

        floatMap.setVisibility(View.GONE);

        loading.setVisibility(View.VISIBLE);
        retrieveTracks();
    }

    public void retrieveTracks(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(searchString);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                try {
                    if (e == null) {
                        Log.d("spots", "parse response " + parseObjects.size());
                        Collections.sort(parseObjects, new Comparator<ParseObject>() {
                            @Override
                            public int compare(ParseObject s1, ParseObject s2) {
                                return s1.getString("name").compareTo(s2.getString("name"));
                            }
                        });

                        cardArrayList = new ArrayList<Card>();
                        for(int i = 0; i < parseObjects.size(); i++){
                            TrackConfigCard trackConfigCard = new TrackConfigCard(mContext, parseObjects.get(i), lat, lon);
                            if (Build.VERSION.SDK_INT < 21) {
                                trackConfigCard.setShadow(false);
                                trackConfigCard.changeBackgroundResource(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
                                trackConfigCard.setBackgroundResource(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
                            }
                            cardArrayList.add(trackConfigCard);
                        }

                        cardArrayAdapter = new CardArrayAdapter(mContext, cardArrayList);
                        grid.setAdapter(cardArrayAdapter);
                        loading.setVisibility(View.GONE);

                        getWeather();

                    } else {
                        Log.e("spots", "parse error " + e.toString());
                    }
                }catch (Exception e1){
                    Log.e("spots", e1.toString());
                }
            }
        });
    }

    public void getWeather(){
        if (sharedPreferences.getBoolean("mes", false) == true) {
            requestUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric";
        } else {
            requestUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=imperial";
        }
        Log.d("weather", requestUrl);

        Ion.with(mContext)
                .load(requestUrl)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String jsonContent) {
                        if (e != null) {
                            Log.e("weather", e.toString());
                        } else {

                            JsonReader reader = new JsonReader(new StringReader(jsonContent));
                            reader.setLenient(true);
                            CurrentResponse currentResponse = mGson.fromJson(reader, CurrentResponse.class);
                            WeatherCard currentWeatherCard = new WeatherCard(mContext, currentResponse);
                            if (Build.VERSION.SDK_INT < 21) {
                                currentWeatherCard.setShadow(false);
                                currentWeatherCard.changeBackgroundResource(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
                                currentWeatherCard.setBackgroundResource(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
                            }
                            cardArrayList.add(currentWeatherCard);
                            cardArrayAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

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
        }else if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
