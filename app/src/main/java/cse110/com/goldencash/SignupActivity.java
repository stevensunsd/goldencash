package cse110.com.goldencash;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by jeremywen on 10/21/14.
 */
public class SignupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_signup);

        // Enable the "Up" button for more navigation options
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
