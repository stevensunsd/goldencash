package cse110.com.goldencash.AppActivity;

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

import cse110.com.goldencash.R;
import cse110.com.goldencash.SideFunctionFacade;
import cse110.com.goldencash.SideFunctionFacadeImp;
import cse110.com.goldencash.modelUser.User;


/**
 *  Title: class SigninActivity
 *  Description: User need to Signin for their account for the beginning
 */
public class SigninActivity extends Activity implements View.OnClickListener {

    // initialize variable using to store input data
    private Button signinButton;
    private Button signupButton;
    private EditText username_field;
    private EditText password_field;
    private Button forgotButton;
    private int wrongInfo_counter = 0;
    private String username;
    protected SideFunctionFacade sff = new SideFunctionFacadeImp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);
        // connect UI to the code
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
        // if user click signin Button
        if(findViewById(R.id.signin_button).equals(view)) {
            checkInput();
        // if user click signup Button
        }else if(findViewById(R.id.signup_button).equals(view)){
            gotoSignup();
        // if user click resetPassword Button
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
            alertMsg("Logging in Fail", "Your account has been blocked, Please contact Customer Service.");
        }else {
            if(username==null||!username.equals(username_field.getText().toString())) {
                username = username_field.getText().toString();
                wrongInfo_counter = 0;
            }
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
                            if (username.equals(username_field.getText().toString()))
                                wrongInfo_counter++;
                            if (wrongInfo_counter == 3)
                                alertMsg("Logging in Fail", "Your account has been blocked, Please contact Customer Service.\"");
                            alertMsg("Sign in failed", "Invalid username or password.");
                        }
                    } else {
                        Log.d(getString(R.string.debugInfo_text), "" + user.getBoolean("admin"));
                        // Start an intent for the dispatch activity
                        if (user.getBoolean("admin")) {
                            gotoMainPage();
                        } else {
                            if (!user.getBoolean("Disable")) {
                                gotoCustomerMainPage();
                            } else {
                                alertMsg("Unable to Sign in", "You don't have any active account, Please contact Customer Service.");
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
        if(sff.isEmpty(username_field.getText().toString())){
            alertMsg("Log in failed", "Please Enter Username");
        }
        else if (sff.isEmpty(password_field.getText().toString())){
            alertMsg("Log in failed", "Please Enter Password");
        }
        else {
            signIn();
        }
    }


}
