package cse110.com.goldencash;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by Xin Wen on 11/17/14.
 */
public class CustomerMainActivity extends Activity {

    protected String key;
    protected ListView listview;
    protected Accounts accounts;
    protected String[] accountArray;
    protected ArrayAdapter<String> adapter;


    protected boolean openDebit;
    protected boolean openCredit;
    protected boolean openSaving;
    protected float debit;
    protected float credit;
    protected float saving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //key = retrieveKey();

        // 11. Add a spinning progress bar (and make sure it's off)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);

        setTitle("Welcome, "+getIntent().getExtras().getString("username"));
        setContentView(R.layout.activity_customermain);

        //get listview from XML
        listview = (ListView) findViewById(R.id.listView_accounts_customer);

        // ListView Item Click Listener
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               // editbox();

            }
        });

        set2();

    }
    private void set2() {
        accounts = new Accounts();
        accounts.setup(retrieveKey());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.getInBackground(retrieveKey(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    openDebit = parseObject.getBoolean("opendebit");
                    openCredit = parseObject.getBoolean("opencredit");
                    openSaving = parseObject.getBoolean("opensaving");
                    debit = (float) parseObject.getInt("debit");
                    credit = (float) parseObject.getInt("credit");
                    saving = (float) parseObject.getInt("saving");
                    setAdapter();
                } else {
                    //TODO: error alert
                }
            }
        });
    }

    private void setAdapter(){
        ArrayList<String> templist=setAdapterarray();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, templist);

        // Assign adapter to ListView
        listview.setAdapter(adapter);

    }

    private ArrayList<String> setAdapterarray(){
        ArrayList<String> account_list=new ArrayList<String>();
        String stringCredit = "Credit Account\nAvailable Balance:" + credit;
        String stringSaving = "Saving Account\nAvailable Balance:" + saving;
        String stringDebit = "Debit Account\nAvailable Balance:" + debit;
        if(openDebit){
            account_list.add(stringDebit);
        }
        if(openCredit){
            account_list.add(stringCredit);
        }
        if(openSaving){
            account_list.add(stringSaving);
        }
        return account_list;
    }


    @Override
    public void onBackPressed(){
        //prevent go back to login page with override the back button behavior
        //TODO:Show up log out dialog
    }

    private String retrieveKey(){
        SharedPreferences prefs = getSharedPreferences("myFile", Context.MODE_PRIVATE);
        return prefs.getString("key","");
    }
}
