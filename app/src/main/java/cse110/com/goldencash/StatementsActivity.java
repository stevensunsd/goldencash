package cse110.com.goldencash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cse110.com.goldencash.modelAccount.Account;

/**
 * Created by Xin Wen on 11/30/2014.
 */
public class StatementsActivity extends Activity {
    private User user = new User();
    private TextView statmentTV;
    private Button print_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statements);
        setTitle("Statements");
        print_button = (Button)findViewById(R.id.button_print_statement);
        print_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
            }
        });
        getStatementOnScreen();
    }

    private void getStatementOnScreen(){
        statmentTV = (TextView) findViewById(R.id.statement_text);
        statmentTV.setText(user.getAccount2("Debit").getLog());
    }
}
