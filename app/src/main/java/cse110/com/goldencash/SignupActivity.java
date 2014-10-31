package cse110.com.goldencash;

import android.app.Activity;
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
            checkAllFields();
        }
    }

    //Networking with Parse for signup
    private void processSignup() {

            Log.d(getString(R.string.debugInfo_text),"signing up for: "+username);
            ParseObject user = new ParseObject("User");
            ParseObject account = new ParseObject("Account");
            user.put("username",username);
            user.put("password",password1);
            user.put("firstname",firstname);
            user.put("lastname",lastname);
            user.put("salt", saltvalue);

            account.put("opendebit",openDebit);
            account.put("opencredit",openCredit);
            account.put("opensaving",openSaving);

            account.put("debit", 100);
            account.put("credit", 100);
            account.put("saving", 100);

            user.put("account",account);
            user.saveInBackground();
    }

    //check if password exactly same
    private boolean checkPassword(){

        //check two passwords if equals
        return (password1.equals(password2));
    }

    private String passwordEncryption(String password) {
        try {
            SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            rand.nextBytes(salt);
            saltvalue = salt.toString();
            MessageDigest msg = MessageDigest.getInstance("MD5");
            msg.update(saltvalue.getBytes());
            byte[] bytes = msg.digest(password.getBytes());
            StringBuilder build = new StringBuilder();
            for (byte aByte : bytes) {
                build.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            // System.err.println(build.toString());
            return build.toString();
        }
        catch(NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            //alertMsg("Fatal Error", "Program failed to generate password. Please try again later.");
            return "";
        }
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
                        password1 = passwordEncryption(password1);
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
    private void clearAlltext() {
        ViewGroup textFields = (ViewGroup) findViewById(R.id.signup_textFields);
        for (int i = 0, count = textFields.getChildCount(); i < count; ++i) {
            View view = textFields.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
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
                        clearAlltext();

                    }
                });
        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }

}

