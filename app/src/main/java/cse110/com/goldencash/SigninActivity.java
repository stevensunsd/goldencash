package cse110.com.goldencash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.parse.ParseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.ProgressDialog;import com.parse.LogInCallback;

public class SigninActivity extends Activity
        implements View.OnClickListener {

    private Button signinButton;
    private Button signupButton;
    private EditText username_field;
    private EditText password_field;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void gotoMainPage(){
        Intent intent = new Intent(SigninActivity.this, UserListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void gotoCustomerMainPage(){
        Intent intent = new Intent(this, CustomerMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("username",username_field.getText().toString());
        startActivity(intent);
    }
    private void signIn() {
        final ProgressDialog dlg = new ProgressDialog(SigninActivity.this);
        //dlg.setTitle("Please wait.");
        dlg.setMessage("Logging in.  Please wait.");
        dlg.show();
        // Call the Parse login method
        ParseUser.logInInBackground(username_field.getText().toString(), password_field.getText()
                .toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                dlg.dismiss();
                if (e != null) {
                    if(e.getCode() == 100){
                        alertMsg("Connection Failed","Please check your Internet connection");
                    }else {
                        // Show the error message for general fail
                        alertMsg("Logging in Fail", "User name and Password doesn't match");
                    }
                } else {
                    Log.d(getString(R.string.debugInfo_text),""+user.getBoolean("admin"));
                    // Start an intent for the dispatch activity
                    if(user.getBoolean("admin")){
                        gotoMainPage();
                    }else{
                        gotoCustomerMainPage();
                    }
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
}
