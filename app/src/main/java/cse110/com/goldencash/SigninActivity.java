package cse110.com.goldencash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

//Parse Imports
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class SigninActivity extends Activity
        implements View.OnClickListener {

    Button signinButton;
    Button signupButton;
    EditText username_field;
    EditText password_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init Parse with these specific key
        Parse.initialize(this, "1XwyXTQlIQDuwcjETTTmvEaysvJVZLsSasuxibY3",
                "hQIEN0MfhYKiBPCHsPZ6djei7myOjcRpX56Cd4Xc");

        //test Parse
        /*
        ParseObject user = new ParseObject("User");
        user.put("username","wenxin3262");
        user.put("password","123");
        user.put("user_id",12345);
        user.saveInBackground();
        */
        setContentView(R.layout.activity_signin);

        signinButton = (Button) findViewById(R.id.sigin_button);
        signinButton.setOnClickListener(this);
        signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(this);

        username_field = (EditText) findViewById(R.id.usernameField);
        password_field = (EditText) findViewById(R.id.passwordField);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        //SignIn Button Clicked, Should have gone to a pop-up cover the main screen,
        //then check the username and password with the database, if correct, jump
        //to main screen, or false should return to the sign in screen.
        if(findViewById(R.id.sigin_button).equals(view)) {
            Log.d(getString(R.string.debugInfo_text), "Username entered: " + username_field.getText().toString());
            signIn();
        }else{
            signUp();
        }
    }

    private void signUp() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    private void signIn(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username",username_field.getText().toString());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your User
                    Log.d(getString(R.string.debugInfo_text),"Found and returned password: "+
                            object.getString("password"));
                    //TODO: use method check if password entered correctly
                } else {
                    // something went wrong
                    Log.d(getString(R.string.debugInfo_text),"Error: " + e.getMessage());
                    //TODO: username not found, use Toast post error to screen
                }
            }
        });
    }

}
