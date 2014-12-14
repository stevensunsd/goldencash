package cse110.com.goldencash.AppActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import cse110.com.goldencash.R;

/**
 * Created by Xin Wen on 12/4/2014.
 */
public class ResetPasswordActivity extends Activity implements View.OnClickListener {
    private Button confirmButton;
    private Button cancelButton;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        confirmButton = (Button) findViewById(R.id.button_rp_confirm);
        confirmButton.setOnClickListener(this);
        cancelButton = (Button) findViewById(R.id.button_rp_cancel);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(findViewById(R.id.button_rp_confirm).equals(view)) {
            //Log.d(getString(R.string.debugInfo_text), "Username entered: " +  username_field.getText().toString());
            email = ((EditText) findViewById(R.id.rp_email)).getText().toString();
            checkInput();
        }else{
            finish();
        }
    }

    /**
     * check input and process it
     */
    void checkInput(){
        if(isEmpty(email)) {
            alertMsg("Failed", "Please Enter an Email Address");
        }
        else {
            processResetPassword();
        }
    }

    /**
     * reset password
     */
    private void processResetPassword(){
        ParseUser.requestPasswordResetInBackground(email,
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                            alertMsg("Success"," An email was successfully sent with reset instructions.");
                        } else {
                            // Something went wrong. Look at the ParseException to see what's up.
                            alertMsg("Sorry","We cannot process your request, please contact customer service");
                        }
                    }
                });
    }

    /**
     * check empty string
     * @param s
     * @return
     */
    private boolean isEmpty(String s) {
        return s.trim().length() > 0 ? false : true;
    }

    /**
     * showing an alert message on screen
     * @param title
     * @param msg
     */
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

                            finish();
                    }

                });
        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }

    //this function will be using when need to clear the text user entered in the textfields
    protected void clearAlltext() {
        ViewGroup textFields = (ViewGroup) findViewById(R.id.rp_viewgroup);
        for (int i = 0, count = textFields.getChildCount(); i < count; ++i) {
            View view = textFields.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
        }
    }
}
