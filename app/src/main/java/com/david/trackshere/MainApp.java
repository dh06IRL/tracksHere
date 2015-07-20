package com.david.trackshere;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by davidhodge on 12/22/14.
 */
public class MainApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
//        Parse.initialize(this, "GHkAYItE7z7oE1TrwWulY1Dq5uLh0dmX3AMblbdL", "vaScSEZPIga4UaHUjNg3Bg0VhOHyQAzHTO2o3Ytg");
        Parse.initialize(this, "6oCrmczWfgdA5Ho12vLVf5uVe1PENvp5vBdnMes9", "rj7zUy1D7tLNexnbJR5p719fEYx6izxdskBFlKHP");
    }
}
