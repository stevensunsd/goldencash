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
import com.parse.ParseException;
import com.parse.ParseUser;

import android.app.ProgressDialog;import com.parse.LogInCallback;

public class SigninActivity extends Activity implements View.OnClickListener {

    private Button signinButton;
    private Button signupButton;
    private EditText username_field;
    private EditText password_field;
    private Button forgotButton;
    private int wrongInfo_counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        signinButton = (Button) findViewById(R.id.signin_button);
        signinButton.setOnClickListener(this);
        signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(this);
        forgotButton = (Button) findViewById(R.id.button_reset_password);
        forgotButton.setOnClickListener(this);

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
        if (id == R.id.action_transaction) {
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
        //Log.d(getString(R.string.debugInfo_text), "Username entered: " +  username_field.getText().toString());
            checkInput();
        }else if(findViewById(R.id.signup_button).equals(view)){
            gotoSignup();
        }else{
            gotoResetPassword();
        }
    }

    private void gotoResetPassword(){
        Intent intent = new Intent(this,ResetPasswordActivity.class);
        startActivity(intent);
    }

    private void gotoSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    private void gotoMainPage(){
        Intent intent = new Intent(this, UserListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void gotoCustomerMainPage(){
        Intent intent = new Intent(this, CustomerMainActivity.class);
        //Intent intent = new Intent(this, AccountsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("username",username_field.getText().toString());
        startActivity(intent);
    }
    private void signIn() {
        if(wrongInfo_counter >= 3){
            alertMsg("Logging in Fail", "Your account has been blocked, please contact customer service.");
        }else {
            final ProgressDialog dlg = new ProgressDialog(SigninActivity.this);
            dlg.setMessage("Logging in.  Please wait.");
            dlg.show();
            // Call the Parse login method
            User.logInInBackground(username_field.getText().toString(), password_field.getText()
                    .toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    dlg.dismiss();
                    if (e != null) {
                        if (e.getCode() == 100) {
                            alertMsg("Connection Failed", "Please check your Internet connection");
                        } else {
                            // Show the error message for general fail
                            ++wrongInfo_counter;
                            if (wrongInfo_counter < 3) {
                                alertMsg("Sign in failed", "Invalid username or password.");
                            } else {
                                alertMsg("Log in failed", "Your account has been blocked, please contact customer service.");
                            }
                        }
                    } else {
                        Log.d(getString(R.string.debugInfo_text), "" + user.getBoolean("admin"));
                        // Start an intent for the dispatch activity
                        if (user.getBoolean("admin")) {
                            gotoMainPage();
                        } else {
                            if (user.getParseObject("Creditaccount") != null ||
                                    user.getParseObject("Debitaccount") != null ||
                                    user.getParseObject("Savingaccount") != null) {
                                gotoCustomerMainPage();
                            } else {
                                alertMsg("Unable to Sign in", "You don't have any active account, please contact customer service.");
                            }
                        }
                    }
                }
            });
        }
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

    private void checkInput(){
        if(isEmpty(username_field.getText().toString())){
            alertMsg("Log in failed", "Please Enter Username");
        }
        else if (isEmpty(password_field.getText().toString())){
            alertMsg("Log in failed", "Please Enter Password");
        }
        else {
            signIn();
        }
    }

    private boolean isEmpty(String s) {
        return s.trim().length() > 0 ? false : true;
    }
}
