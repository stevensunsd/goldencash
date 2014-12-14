package cse110.com.goldencash;

// App.java
import com.parse.Parse;
import com.parse.ParseObject;
import android.app.Application;

import cse110.com.goldencash.modelUser.User;

/**
 *  Title: class App
 *  Description: main starter class for the whole Application
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // register the ParseSubclasses
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(cse110.com.goldencash.modelAccount.CreditAccount.class);
        ParseObject.registerSubclass(cse110.com.goldencash.modelAccount.DebitAccount.class);
        ParseObject.registerSubclass(cse110.com.goldencash.modelAccount.SavingAccount.class);
        // Initialize Parse Database service
        Parse.initialize(this, getString(R.string.ApplicationID),
                getString(R.string.ClientKey));

    }
}