package cse110.com.goldencash.AppActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cse110.com.goldencash.R;
import cse110.com.goldencash.modelUser.User;

/**
 * Created by Xin Wen on 11/30/2014.
 */
public class StatementsActivity extends Activity {
    private User user = new User();
    private TextView statmentTV;
    private Button print_button;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statements);
        Log.d("Statement: ","account is: "+getIntent().getExtras().getString("account"));
        account =  getIntent().getExtras().getString("account");
        setTitle(account+" Account Statement");
        getStatementOnScreen();
        print_button = (Button)findViewById(R.id.button_print_statement);
        print_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, statmentTV.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
            }
        });
    }

    /**
     * setup views for showing statement
     */
    private void getStatementOnScreen(){
        statmentTV = (TextView) findViewById(R.id.statement_text);
        statmentTV.setText(user.getAccount2(account).getLog());
    }
}
