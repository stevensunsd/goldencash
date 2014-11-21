package cse110.com.goldencash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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
    String saltvalue;
    boolean openDebit;
    boolean openCredit;
    boolean openSaving;

    boolean finishTag = false;

    protected ProgressDialog proDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Parse.initialize(this, getString(R.string.ApplicationID),
          //      getString(R.string.ClientKey));

        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_signup);
        // Get the message from the intent
        //Intent intent = getIntent();

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
            startLoading();
            //start checking all field by checking username
            checkAllFields();
        }
    }

    //Networking with Parse for signup
    private void processSignup() {
        // Set up a new Parse user
        final Account account = ParseObject.create(Account.class);
        account.set(openDebit,openCredit,openSaving);
        account.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    alertMsg("Account Sign Up Failed", "Error");
                } else {
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password1);
                    user.put("firstname", firstname);
                    user.put("lastname", lastname);
                    user.put("admin", false);
                    user.put("account",account);

                    // Call the Parse signup method
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            stopLoading();
                            if (e != null) {
                                alertMsg("Sign Up Failed", "Please Check Your Internet Connection");
                            } else {
                                // Start an intent for the dispatch activity
                                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

    //check if password exactly same
    private boolean checkPassword(){
        //check two passwords if equals
        return (password1.equals(password2));
    }

    //A chain of methods to validate all fields that user entered
    private void checkAllFields(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username",username);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {

                if (e == null) {
                    // object will be your User
                    Log.d(getString(R.string.debugInfo_text),"Found a existing username");
                    alertMsg("Sign Up Failed", "Username already exists, choose a new username.");
                    usernameOk = false;
                } else {
                    //username is ok to create
                    Log.d(getString(R.string.debugInfo_text),"no username used, OK to create");
                    //System.out.println(username);

                    //set username to true
                    usernameOk = true;
                    //after function call back returned, check password.
                    if(checkPassword()){
                        //password1 = passwordEncryption(password1);
                        // can change if-statement later to reflect project specifications
                        if((openDebit) || (openCredit) || (openSaving)) {
                            if((firstname.matches("[a-zA-Z]+")) &&
                                    (lastname.matches("[a-zA-Z]+"))){
                                processSignup();
                            }else {
                                // entered garbage for name fields
                                Log.d(getString(R.string.debugInfo_text),
                                        "Please only enter letters for names.");
                                alertMsg("Sign Up Failed", "Please only enter letters for names.");
                            }
                        }
                    }else{

                        Log.d(getString(R.string.debugInfo_text),"The passwords do not match.");
                        alertMsg("Sign Up Failed", "Passwords do not match.");
                    }
                }
            }
        });
    }

    //this function will be using when need to clear the text user entered in the textfields
    protected void clearAlltext() {
        ViewGroup textFields = (ViewGroup) findViewById(R.id.signup_textFields);
        for (int i = 0, count = textFields.getChildCount(); i < count; ++i) {
            View view = textFields.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
        }
    }

    protected void alertMsg(String title, String msg){
        //build dialog
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //clear msg
                        clearAlltext();
                        if(finishTag){
                            finish();
                        }
                    }
                });
        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }

    protected void startLoading() {
        proDialog = new ProgressDialog(this);
        proDialog.setMessage("Signing Up...");
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    protected void stopLoading() {
        proDialog.dismiss();
        proDialog = null;
    }
}

