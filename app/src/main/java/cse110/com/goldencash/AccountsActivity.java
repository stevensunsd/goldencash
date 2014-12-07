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
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import cse110.com.goldencash.modelAccount.Account;

/**
 * Created by Xin Wen on 10/27/14.
 */
public class AccountsActivity extends Activity{

    ListView listview;

    private User user = new User();
    private cse110.com.goldencash.modelAccount.Account debit;
    private cse110.com.goldencash.modelAccount.Account credit;
    private cse110.com.goldencash.modelAccount.Account saving;

    protected ArrayAdapter<String> adapter;

    protected boolean flag = false;
    protected boolean [] flagArray = new boolean[] {false,false,false};

    private void getUser(){
        Log.d("getting user","id: "+getIntent().getStringExtra("username"));
        String userId = getIntent().getStringExtra("userId");
        String username = getIntent().getStringExtra("username");
        ParseQuery query = ParseUser.getQuery();
        query.whereEqualTo("username",username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    Log.d("got user", "id: " + parseObjects.get(0).getObjectId());
                    debit = (Account) parseObjects.get(0).getParseObject("Debitaccount");
                    credit = (Account) parseObjects.get(0).getParseObject("Creditaccount");
                    saving = (Account) parseObjects.get(0).getParseObject("Savingaccount");
                    try {
                        debit.fetch();
                        credit.fetch();
                        saving.fetch();
                    } catch (ParseException exc) {
                        exc.printStackTrace();
                    }
                    setAdapter();
                } else {
                    //network error
                    finish();
                }
            }
        });
        //user = new User(getIntent().getStringExtra("userID"));
        // account = user.getAccount();
    }


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
        String stringSavingInterest = "\nCurrent Interest Rate: " + saving.getInterestRate()+"%";
        String stringDebitInterest = "\nCurrent Interest Rate: " + debit.getInterestRate() + "%";
        if(debit.isOpen()){
            account_list.add(stringDebit+stringDebitInterest);
            flagArray[0] = true;
        }

        if(saving.isOpen()){
            account_list.add(stringSaving+stringSavingInterest);
            flagArray[1] = true;
        }
        if(credit.isOpen()){
            account_list.add(stringCredit);
            flagArray[2] = true;
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
        setProgressBarIndeterminateVisibility(true);
        //set title
        setTitle("Welcome,Teller "+user.getUserName());
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
                selectionBox(id);
                //editbox(id);
            }
        });
        getUser();
        //setAdapter();
        setProgressBarIndeterminateVisibility(false);
    }

    protected void selectionBox(long id){
        final long index = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action");
        builder.setPositiveButton("Cancel", null);
        builder.setNeutralButton("Deposit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editDeposit(index);
                    }
                });

        // convert flagArray to string
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < flagArray.length; i++) {
            if (flagArray[i]) str.append("1"); else str.append("0");
        }
        String s = str.toString();

        // check case for Show Withdraw
        if(flagArray[2]!=true){
            //Credit account cannot be withdraw
            builder.setNegativeButton("Withdraw",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            editWithdraw(index);
                        }
                    });
        }
        else if (s.equals("001")){
            // not show
        }
        else if (s.equals("011")||s.equals("101")){
            if(id!=1) {
                //Credit account cannot be withdraw
                builder.setNegativeButton("Withdraw",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                editWithdraw(index);
                            }
                        });
            }
        }
        else {
            if(id!=2) {
                //Credit account cannot be withdraw
                builder.setNegativeButton("Withdraw",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                editWithdraw(index);
                            }
                        });
            }
        }

        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }

    protected void editDeposit(long id) {
        final EditText input = new EditText(this);
        final int choose = (int)id;
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter Deposit Amount").setIcon(android.R.drawable.ic_dialog_info).setView(input).setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                double value = Double.parseDouble(input.getText().toString());
                switch (choose) {
                    case 0:
                        if(debit.isOpen()) {
                            // add + -, and call debit.withdraw or debit.deposit
                            //account.setDebit(Double.parseDouble(input.getText().toString()));
                            debit.deposit(value);
                            break;
                        }
                        else if(saving.isOpen()) {
                            flag = true;
                            //account.setCredit(Double.parseDouble(input.getText().toString())); break;
                            saving.deposit(value);
                        }
                        else {
                            //credit
                            //account.setSaving(Double.parseDouble(input.getText().toString())); break;
                            credit.deposit(value);
                        }
                    case 1:
                        if(saving.isOpen()&&!flag) {
                            //account.setCredit(Double.parseDouble(input.getText().toString())); break;
                            saving.deposit(value);
                        }
                        else {
                            //credit
                            //account.setSaving(Double.parseDouble(input.getText().toString())); break;
                            credit.deposit(value);
                        }
                    case 2: credit.deposit(value);
                        //credit
                        //account.setSaving(Double.parseDouble(input.getText().toString())); break;
                    default: // error
                }
                finish();
                //refreshData(); refresh data has problem loading not using for now
            }

        });
        builder.show();
    }
    protected void editWithdraw(long id) {
        final EditText input = new EditText(this);
        final int choose = (int)id;
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter Withdraw Amount").setIcon(android.R.drawable.ic_dialog_info).setView(input).setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which){
                double value = Double.parseDouble(input.getText().toString());
                switch (choose) {
                    case 0:
                        if(debit.isOpen()) {
                            // add + -, and call debit.withdraw or debit.deposit
                            //account.setDebit(Double.parseDouble(input.getText().toString()));
                            debit.withdraw(value);
                            break;
                        }
                        else if(saving.isOpen()) {
                            flag = true;
                            //account.setCredit(Double.parseDouble(input.getText().toString())); break;
                            saving.withdraw(value);
                        }
                        else {
                            //credit
                            //account.setSaving(Double.parseDouble(input.getText().toString())); break;
                            credit.withdraw(value);
                        }
                    case 1:
                        if(saving.isOpen()&&!flag) {
                            //account.setCredit(Double.parseDouble(input.getText().toString())); break;
                            saving.withdraw(value);
                        }
                        else {
                            //credit
                            //account.setSaving(Double.parseDouble(input.getText().toString())); break;
                        }
                    case 2: //credit
                        //account.setSaving(Double.parseDouble(input.getText().toString())); break;
                    default: // error
                }
                finish();
                //refreshData(); refresh data has problem loading not using for now
            }
        });
        builder.show();
    }
}