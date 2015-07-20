package com.david.trackshere.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.david.trackshere.R;
import com.david.trackshere.adapter.TracksAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * Created by davidhodge on 12/26/14.
 */
public class TrackListActivity extends BaseActivity {

    Context mContext;
    @InjectView(R.id.card_list)
    GridViewWithHeaderAndFooter grid;
    @InjectView(R.id.loading)
    ProgressBar loading;
    @InjectView(R.id.float_map)
    FloatingActionButton floatMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_list);
        ButterKnife.inject(this);

        floatMap.setVisibility(View.VISIBLE);
        floatMap.attachToListView(grid);
        floatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent test = new Intent(getApplicationContext(), MapViewActivity.class);
                startActivity(test);
            }
        });

        loading.setVisibility(View.VISIBLE);
        retrieveTracks();
    }

    public void retrieveTracks(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("tracks");
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

//                        for(int i = 0; i < parseObjects.size(); i++){
//                            if(parseObjects.get(i).getString("display").contentEquals("User Submitted")){
//                                parseObjects.add(0, parseObjects.get(i));
//                                parseObjects.remove(i + 1);
//                            }
//                        }

                        TracksAdapter tracksAdapter = new TracksAdapter(mContext, parseObjects);

//                        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(catsAdapter);
//                        swingBottomInAnimationAdapter.setInitialDelayMillis(300);
//                        swingBottomInAnimationAdapter.setAbsListView(staggeredGridView);
                        grid.setAdapter(tracksAdapter);
                        loading.setVisibility(View.GONE);

                    } else {
                        Log.e("spots", "parse error " + e.toString());
                    }
                }catch (Exception e1){
                    Log.e("spots", e1.toString());
                }
            }
        });
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
            Intent test = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(test);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
