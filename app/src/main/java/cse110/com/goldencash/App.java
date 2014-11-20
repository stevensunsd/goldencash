package cse110.com.goldencash;

// App.java
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Account.class);
        Parse.initialize(this, getString(R.string.ApplicationID),
                getString(R.string.ClientKey));

    }
}