package cse110.com.goldencash;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

/**
 * Created by Xin Wen on 10/27/14.
 */
public class AccountsActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getString(R.string.debugInfo_text),"Got Key: "+ retrieveKey());
    }

    private String retrieveKey(){
        return getPreferences(MODE_PRIVATE).getString("key","");
    }
}
