package com.creativecub.socialapp.miscellaneous;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by Yogesh on 27-01-2015.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "X3MG7zxSoE9kvUY136A6ugSZ38SnERX37yuNWhBu", "wClNnW8zPs9UylKxWHzIbLSeBGwPFRtPVWT4xKSF");
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}
