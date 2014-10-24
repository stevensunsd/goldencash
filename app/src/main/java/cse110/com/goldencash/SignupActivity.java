package cse110.com.goldencash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by jeremywen on 10/21/14.
 */
public class SignupActivity extends Activity implements View.OnClickListener {

    Button cancelButton;

    boolean usernameOk;
    String username;
    EditText password1;
    EditText password2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "1XwyXTQlIQDuwcjETTTmvEaysvJVZLsSasuxibY3",
                "hQIEN0MfhYKiBPCHsPZ6djei7myOjcRpX56Cd4Xc");
        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_signup);
        // Get the message from the intent
        Intent intent = getIntent();

        cancelButton = (Button) findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);

        username = ((EditText)
                findViewById(R.id.signup_username)).getText().toString().toLowerCase();

        password1 = (EditText) findViewById(R.id.signup_password);

        password2 = (EditText) findViewById(R.id.signup_password2);

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

    private boolean CheckPassword(){

        //check two passwords if equals
        return (password1.getText().equals(password2.getText()));
    }

    private void CheckUsername(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username",username);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {


                if (e == null) {
                    // object will be your User
                    Log.d(getString(R.string.debugInfo_text),"Found a exist username");
                    // print out error massage username exist

                    usernameOk = false;
                } else {
                    //username is ok to create
                    Log.d(getString(R.string.debugInfo_text),"no username used OK to create");

                    //set username to true
                    usernameOk = true;
                }
            }
        });
    }

    private void clearAlltext() {
        ViewGroup textFields = (ViewGroup) findViewById(R.id.signup_textFields);
        for (int i = 0, count = textFields.getChildCount(); i < count; ++i) {
            View view = textFields.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
        }
    }
    //TODO: check all text field correctness, before "Parsing"

}

