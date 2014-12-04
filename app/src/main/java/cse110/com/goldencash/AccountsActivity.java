package cse110.com.goldencash;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

//Our class
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by Xin Wen on 10/27/14.
 */
public class AccountsActivity extends Activity{

    ListView listview;

    private User user = new User();
    private cse110.com.goldencash.modelAccount.Account debit = user.getDebitAccount();
    private cse110.com.goldencash.modelAccount.Account credit = user.getCreditAccount();
    private cse110.com.goldencash.modelAccount.Account saving = user.getSavingAccount();

    protected ArrayAdapter<String> adapter;

    protected boolean flag = false;
/*
    private void getUser(){
        Log.d("getting user","id: "+getIntent().getStringExtra("userID"));
        String userId = getIntent().getStringExtra("userID");
        ParseQuery query = ParseUser.getQuery();
        query.getInBackground(userId,new GetCallback() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    Log.d("got user","id: "+parseObject.getObjectId());
                    account =  (Account) parseObject.getParseObject("account");
                    try {
                        account.fetchIfNeeded();
                    } catch (ParseException exc) {
                        exc.printStackTrace();
                    }
                    setAdapter();
                }else{
                    //network error
                }
            }
        });
        //user = new User(getIntent().getStringExtra("userID"));
       // account = user.getAccount();
    }
*/
    private void refreshData() {
        Intent intent = new Intent(AccountsActivity.this, AccountsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private ArrayList<String> setAdapterarray(){
        ArrayList<String> account_list=new ArrayList<String>();
        String stringCredit = "Credit Account\nAvailable Balance:" + credit.getAmount();
        String stringSaving = "Saving Account\nAvailable Balance:" + saving.getAmount();
        String stringDebit = "Debit Account\nAvailable Balance:" + debit.getAmount();
        if(debit.isOpen()){
            account_list.add(stringDebit);
        }
        if(credit.isOpen()){
            account_list.add(stringCredit);
        }
        if(saving.isOpen()){
            account_list.add(stringSaving);
        }
        return account_list;
    }

    private void setAdapter(){
        ArrayList<String> data = setAdapterarray();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, data);
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
                // add id to give choose about which account you want to change
                editbox(id);
            }
        });
        //getUser();
        setAdapter();
    }

    protected void selectionBox(long id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Withdraw",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.setNegativeButton("Deposit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }

    protected void editbox(long id) {
        final EditText input = new EditText(this);
        final int choose = (int)id;
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter The Amount You Want to Change").setIcon(android.R.drawable.ic_dialog_info).setView(input).setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                switch (choose) {
                    case 0:
                        if(debit.isOpen()) {
                            // add + -, and call debit.withdraw or debit.deposit
                            //account.setDebit(Double.parseDouble(input.getText().toString()));
                            break;
                        }
                        else if(credit.isOpen()) {
                            flag = true;
                            //account.setCredit(Double.parseDouble(input.getText().toString())); break;
                        }
                        else {
                            //account.setSaving(Double.parseDouble(input.getText().toString())); break;
                        }
                    case 1:
                        if(credit.isOpen()&&!flag) {
                            //account.setCredit(Double.parseDouble(input.getText().toString())); break;
                        }
                        else {
                            //account.setSaving(Double.parseDouble(input.getText().toString())); break;
                        }
                    case 2://account.setSaving(Double.parseDouble(input.getText().toString())); break;
                    default: // error
                }
                finish();
                //refreshData(); refresh data has problem loading not using for now
            }

        });
        builder.show();
    }

}