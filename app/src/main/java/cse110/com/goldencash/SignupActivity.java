package cse110.com.goldencash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Xin Wen on 10/21/14.
 */
public class SignupActivity extends Activity implements View.OnClickListener {

    Button cancelButton;
    Button confirmButton;
    boolean usernameOk;
    String username;
    String password1;
    String password2;
    String firstname;
    String lastname;
    boolean openDebit;
    boolean openCredit;
    boolean openSaving;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, getString(R.string.ApplicationID),
                getString(R.string.ClientKey));

        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_signup);
        // Get the message from the intent
        Intent intent = getIntent();

        confirmButton = (Button) findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(this);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        username = ((EditText)
                findViewById(R.id.signup_username)).getText().toString().toLowerCase();

        password1 = ((EditText) findViewById(R.id.signup_password)).getText().toString();

        password2 = ((EditText) findViewById(R.id.signup_password2)).getText().toString();

        firstname = ((EditText)findViewById(R.id.signup_firstname)).getText().toString();
        lastname = ((EditText)findViewById(R.id.signup_lastname)).getText().toString();

        openDebit = ((CheckBox)findViewById(R.id.check_debit)).isChecked();
        openCredit = ((CheckBox)findViewById(R.id.check_credit)).isChecked();
        openSaving  = ((CheckBox)findViewById(R.id.check_saving)).isChecked();

        if(findViewById(R.id.button_cancel).equals(view)) {
            finish();
        }else{
            //start checking all field by checking username
            checkUsername();
        }
    }


    private void processSignup() {

            Log.d(getString(R.string.debugInfo_text),"signing up for: "+username);
            ParseObject user = new ParseObject("User");
            user.put("username",username);
            user.put("password",password1);
            user.put("firstname",firstname);
            user.put("lastname",lastname);
            user.put("debit",openDebit);
            user.put("credit",openCredit);
            user.put("saving",openSaving);
            user.saveInBackground();

    }

    private boolean checkPassword(){

        //check two passwords if equals
        return (password1.equals(password2));
    }

    private void checkUsername(){

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
                    Log.d(getString(R.string.debugInfo_text),"no username used, OK to create");

                    //set username to true
                    usernameOk = true;
                    //after function call back returned, check password.
                    if(checkPassword()){
                        // can change if-statement later to reflect project specifications
                        if((openDebit) || (openCredit) || (openSaving)) {
                            if((firstname.matches("[a-zA-Z]+")) &&
                                    (lastname.matches("[a-zA-Z]+"))){
                                processSignup();
                            }else {
                                // entered garbage for name fields
                                Log.d(getString(R.string.debugInfo_text),
                                        "Please only enter letters for names.");
                            }
                        }
                    }else{
                        //password not match
                        Log.d(getString(R.string.debugInfo_text),"The passwords do not match.");
                    }
                }
            }
        });
    }

    //this function will be using when need to clear the text user entered in the textfields
    private void clearAlltext() {
        ViewGroup textFields = (ViewGroup) findViewById(R.id.signup_textFields);
        for (int i = 0, count = textFields.getChildCount(); i < count; ++i) {
            View view = textFields.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
        }
    }
}

