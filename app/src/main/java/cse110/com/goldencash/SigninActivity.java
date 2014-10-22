package cse110.com.goldencash;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import static android.widget.AdapterView.OnItemClickListener;


public class SigninActivity extends Activity
        implements View.OnClickListener {

    Button signinButton;
    EditText username_field;
    EditText password_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signinButton = (Button) findViewById(R.id.sigin_button);
        signinButton.setOnClickListener(this);

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
        Log.d("DebugInfo" , username_field.getText().toString());
    }


}
