package cse110.com.goldencash;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

//Our class
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by Xin Wen on 10/27/14.
 */
public class AccountsActivity extends Activity{

    ListView listview;
    protected String objectID;
    protected boolean openDebit;
    protected boolean openCredit;
    protected boolean openSaving;
    protected float debit;
    protected float credit;
    protected float saving;
    protected float number;


    protected Accounts accounts;
    protected String[] accountArray;

    protected ProgressDialog proDialog;
    protected ArrayAdapter<String> adapter;

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
        accounts = new Accounts();
        accounts.setup(retrieveKey());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.getInBackground(retrieveKey(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    objectID = parseObject.getObjectId();
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

    private void refreshData() {



        Intent intent = new Intent(this, AccountsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //accounts = new Accounts();

        //accounts.setup(retrieveKey());
       /*
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

                    adapter.clear();
                    adapter.addAll(setAdapterarray());
                    adapter.notifyDataSetChanged();
                    listview.invalidate();
                    Log.d(getString(R.string.debugInfo_text),"Done refresh.");
                } else {
                    //TODO: error alert
                }
            }
        });
        */


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

    private void setAdapter(){
        ArrayList<String> templist=setAdapterarray();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, templist);

        // Assign adapter to ListView
        listview.setAdapter(adapter);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 11. Add a spinning progress bar (and make sure it's off)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);
        //set title
        setTitle(getIntent().getExtras().getString("username"));
        //set view
        setContentView(R.layout.activity_accounts);

        //Intent intent = getIntent();
        //String id = intent.getExtras().getString("userID");

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
        listview = (ListView) findViewById(R.id.listView_accounts);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data



        // Assign adapter to ListView
       // listview.setAdapter(adapter);

        // ListView Item Click Listener
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                editbox();

            }
        });
        set2();

    }

    /*
    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }
    */

    protected void editbox() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Money").setIcon(android.R.drawable.ic_dialog_info).setView(input).setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                String update=input.getText().toString();
                number = (float)Integer.parseInt(update);
                Log.d(getString(R.string.debugInfo_text),"new value"+number);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Debit");
                query.getInBackground(objectID, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            object.put("debit", number);
                            object.saveEventually();
                        } else {

                        }
                    }
                });
                refreshData();
            }

        });
        builder.show();


    }

    private String retrieveKey(){
//        SharedPreferences prefs = getSharedPreferences("myFile", Context.MODE_PRIVATE);
//        return prefs.getString("key","");
        Intent intent = getIntent();
        return intent.getExtras().getString("userID");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Log.d(getString(R.string.debugInfo_text),"clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}