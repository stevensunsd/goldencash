package cse110.com.goldencash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by jeremywen on 10/21/14.
 */
public class SignupActivity extends Activity implements View.OnClickListener {

    Button cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_signup);
        // Get the message from the intent
        Intent intent = getIntent();
        // Enable the "Up" button for more navigation options
        getActionBar().setDisplayHomeAsUpEnabled(true);

        cancelButton = (Button) findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(findViewById(R.id.button_cancel).equals(view)) {
            finish();
        }else{
            signup();
        }
    }

    private void signup() {
        //create Parse user object
    }
}

//TODO: check all text field correctness, before "Parsing"
