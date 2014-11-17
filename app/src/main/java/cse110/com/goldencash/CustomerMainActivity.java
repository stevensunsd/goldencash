package cse110.com.goldencash;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Xin Wen on 11/17/14.
 */
public class CustomerMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed(){
        //prevent go back to login page with override the back button behavior
        //TODO:Show up log out dialog
    }
}
