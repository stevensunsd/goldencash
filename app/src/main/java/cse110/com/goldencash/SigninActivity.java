package cse110.com.goldencash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//Parse Imports
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


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
        Parse.initialize(this, getString(R.string.ApplicationID),
                getString(R.string.ClientKey));

        //test Parse
        /*FOR TEST ONLY
        ParseObject user = new ParseObject("User");
        user.put("username","wenxin3262");
        user.put("password","123");
        user.put("user_id",12345);
        user.saveInBackground();
        */
        setContentView(R.layout.activity_signin);

        signinButton = (Button) findViewById(R.id.signin_button);
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
        if(findViewById(R.id.signin_button).equals(view)) {
            Log.d(getString(R.string.debugInfo_text), "Username entered: " +
                    username_field.getText().toString());
            signIn();
        }else{
            gotoSignup();
        }
    }

    private void gotoSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    private void gotoMainPage(){
        Intent intent = new Intent(this, AccountsActivity.class);
        startActivity(intent);
    }
    private void signIn(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username",username_field.getText().toString());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your User
                    //Log.d(getString(R.string.debugInfo_text),"Found and returned password: "+
                    //       object.getString("password"));
                    //TODO: Save user ID and go main activity
                    if(passwordEncryption(password_field.getText().toString(),
                            object.getString("salt")).equals(
                            object.getString("password"))) {
                            // System.err.println("True\n");
                            //Save User ID and go to Main Activity
                        //Log.d(getString(R.string.debugInfo_text),object.getString("salt"));
                            storeUserKey(object);
                            gotoMainPage();
                    }else{
                        //Password Not match
                        alertMsg("Unable to Sign In",getString(R.string.ERROR_password));
                    }
                } else {
                    // something went wrong with networking
                    Log.d(getString(R.string.debugInfo_text), "Error: " + e.getMessage());

                    alertMsg("Network Error","Please try again.");
                }
            }
        });
    }

    private void alertMsg(String title, String msg){
        //build dialog
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       //clear msg
                        username_field.setText("");
                        password_field.setText("");
                    }
                });
        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }

    private String passwordEncryption(String password, String salted) {
        try {
            //System.err.println(salted);
            MessageDigest msg = MessageDigest.getInstance("MD5");
            msg.update(salted.getBytes());
            byte[] bytes = msg.digest(password.getBytes());
            StringBuilder build = new StringBuilder();
            for (byte aByte : bytes) {
                build.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            //System.err.println(build.toString());
            return build.toString();
        }
        catch(NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return ""; // TODO: changed to a generic error msg
        }
    }

    private void storeUserKey(ParseObject user){
        user.getParseObject("account").fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e == null){
                    String key = object.getObjectId();
                    //saving to sharedpreference
                    Log.d(getString(R.string.debugInfo_text),"Key: "+key);
                    getPreferences(MODE_PRIVATE).edit().putString("key",key).commit();
                }else{
                    //TODO:Signin error msg
                }

            }
        });

        //to retrieve
        //"your_variable" = getPreferences(MODE_PRIVATE).getString("Name of variable",default value);
    }
}
