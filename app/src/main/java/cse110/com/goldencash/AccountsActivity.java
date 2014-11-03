package cse110.com.goldencash;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//Our class
import cse110.com.goldencash.Accounts;

/**
 * Created by Xin Wen on 10/27/14.
 */
public class AccountsActivity extends Activity{

    ListView listview;
    protected boolean openDebit;
    protected boolean openCredit;
    protected boolean openSaving;
    protected float debit;
    protected float credit;
    protected float saving;

    protected Accounts accounts;

    protected ProgressDialog proDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getString(R.string.debugInfo_text), "Got Key: " + retrieveKey());
        accounts = new Accounts(retrieveKey());
        credit = accounts.getCreditAmount();

        setContentView(R.layout.activity_accounts);

        String[] accounts = new String[] { "Credit", "Debit", "Saving"};
        //get listview from XML
        listview = (ListView) findViewById(R.id.account_list);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, accounts);

        // Assign adapter to ListView
        listview.setAdapter(adapter);
/*
        // ListView Item Click Listener
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listview.getItemAtPosition(position);

                // Show Alert
                //Toast.makeText(getApplicationContext(),
                  //      "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                    //    .show();

            }
        });
        */
    }

    private String retrieveKey(){
        return getPreferences(MODE_PRIVATE).getString("key","");
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
