package cse110.com.goldencash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//Our class
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

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
    protected String[] accountArray;

    protected ProgressDialog proDialog;


    private void set() {
        accounts = new Accounts();
        accounts.setup(retrieveKey());
        openDebit=accounts.getOpenDebit();

        openCredit=accounts.getOpenCredit();
        openSaving=accounts.getOpenSaving();
        credit = accounts.getCreditAmount();
        debit = accounts.getDebitAmount();
        saving = accounts.getSavingAmount();
        String creditAccount = "Credit Account\nAvailable Balance:" + openDebit;
        accountArray = new String[] { creditAccount, "Debit", "Saving"};
    }


    private void set2() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.getInBackground(retrieveKey(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    openDebit = parseObject.getBoolean("opendebit");
                    System.out.println("1"+openDebit);
                    openCredit= parseObject.getBoolean("opencredit");
                    openSaving= parseObject.getBoolean("opensaving");
                }
                else {
                    //TODO: error alert
                }
            }
        });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        /* ArrayList<String> account_list=new ArrayList<String>();
        if(openDebit){
            account_list.add("Debit");
        }else if(openCredit){
            account_list.add("Credit");s
        }else if(openSaving){
            account_list.add("Saving");
        }
        */
        //get listview from XML
        listview = (ListView) findViewById(R.id.listView);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        set2();
        System.out.println("Fuck"+openDebit);
        accountArray = new String[] { "Credit Account\nAvailable Balance:" + openDebit, "Debit", "Saving"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, accountArray);

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
        SharedPreferences prefs = getSharedPreferences("myFile", Context.MODE_PRIVATE);
        return prefs.getString("key","");
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