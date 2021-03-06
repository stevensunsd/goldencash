package cse110.com.goldencash.AppActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//Our class
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import cse110.com.goldencash.R;
import cse110.com.goldencash.SideFunctionFacade;
import cse110.com.goldencash.SideFunctionFacadeImp;
import cse110.com.goldencash.modelAccount.Account;
import cse110.com.goldencash.modelAccount.Rule;
import cse110.com.goldencash.modelUser.User;


/**
 * Created by Xin Wen on 10/27/14.
 */
public class TellerMainActivity extends Activity{

    ListView listview;
    private TextView usernameText, nameText, lastloginText, accountNumberText,accountEmailText;


    private User user = new User();
    private cse110.com.goldencash.modelAccount.Account debit;
    private cse110.com.goldencash.modelAccount.Account credit;
    private cse110.com.goldencash.modelAccount.Account saving;

    protected ArrayAdapter<String> adapter;
    private ArrayList<Account> accountArray = new ArrayList<Account>();
    private Rule rule = new Rule();
    protected SideFunctionFacade sff = new SideFunctionFacadeImp();

    /**
     * find all required text views
     */
    private void setupTextViews(){
        usernameText = (TextView) findViewById(R.id.accounts_username);
        nameText = (TextView) findViewById(R.id.accounts_name);
        lastloginText = (TextView) findViewById(R.id.accounts_last_sign_in);
        accountNumberText = (TextView) findViewById(R.id.accounts_account_number);
        accountEmailText = (TextView)findViewById(R.id.accounts_email);
    }

    /**
     * get all the user that need to show
     */
    private void getUser(){
        setProgressBarIndeterminateVisibility(true);
        Log.d("getting user","id: "+getIntent().getStringExtra("username"));
        String userId = getIntent().getStringExtra("userId");
        String username = getIntent().getStringExtra("username");
        ParseQuery query = ParseUser.getQuery();
        query.whereEqualTo("username",username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    Log.d("got user", "id: " + parseObjects.get(0).getObjectId());
                    ParseUser foundUser = (ParseUser) parseObjects.get(0);
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
                    usernameText.setText("Username: "+foundUser.getUsername());
                    accountNumberText.setText("Account Number: "+debit.getAccountNumber());
                    nameText.setText("Name: "+foundUser.getString("firstname")+" "+foundUser.getString("lastname"));
                    lastloginText.setText("Last Sign In: "+DateFormat.getDateTimeInstance().format(foundUser.getUpdatedAt()));
                    accountEmailText.setText("Email: "+foundUser.getEmail());
                    setAdapter();
                } else {
                    //network error
                    Toast.makeText(getApplicationContext(), "Network error, please try again.",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        //user = new User(getIntent().getStringExtra("userID"));
        // account = user.getAccount();
    }

    /**
     * set up adapter array for listview
     * @return
     */
    private ArrayList<String> setAdapterarray(){
        ArrayList<String> account_list=new ArrayList<String>();
        credit.applyInterest();
        saving.applyInterest();
        debit.applyInterest();
        String stringCredit = "Credit Account\nAvailable Balance:" + credit.getAmount();
        String stringSaving = "Saving Account\nAvailable Balance:" + saving.getAmount();
        String stringDebit = "Debit Account\nAvailable Balance:" +  debit.getAmount();
        String stringSavingInterest = "\nCurrent Interest Rate: " + saving.getCurrentInterestRate()+"%";
        String stringDebitInterest = "\nCurrent Interest Rate: " + debit.getCurrentInterestRate() + "%";
        if(debit.isOpen()){
            account_list.add(stringDebit + stringDebitInterest);
            accountArray.add(debit);
        }

        if(saving.isOpen()){
            account_list.add(stringSaving+stringSavingInterest);
            accountArray.add(saving);
        }
        if(credit.isOpen()){
            account_list.add(stringCredit);
            accountArray.add(credit);
        }
        return account_list;
    }

    /**
     * set adapter to listview
     */
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
        setTitle("Welcome,Teller "+user.getUserName());
        //set view
        setContentView(R.layout.activity_accounts);
        setupTextViews();
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

    }

    /**
     * showing a selection box with option of accounts
     * @param id
     */
    protected void selectionBox(long id){
        final int index = (int)id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action");
        builder.setPositiveButton("Cancel", null);
        builder.setNeutralButton("Deposit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editDeposit(index);
                    }
                });

        // check case for Show Withdraw
        if(accountArray.get(index) != credit){
            //Credit account cannot be withdraw
            builder.setNegativeButton("Withdraw",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            editWithdraw(index);
                        }
                    });
        }

        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }

    /**
     * showing a edit dialog for deposit input on screen
     * @param id
     */
    protected void editDeposit( long id) {
        final int index = (int) id;
        final EditText input = new EditText(this);
        input.setHint("$");
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter Deposit Amount").setIcon(android.R.drawable.ic_dialog_info).setView(input).setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                if(sff.isValidInput(input)) {
                    double value = Double.parseDouble(input.getText().toString());
                    value = sff.NumberFormater(value);
                    Account account = accountArray.get(index);
                    Pair<Boolean, String> resultPair = rule.canDeposit(account, value);
                    if (resultPair.first) {
                        account.deposit(value);
                        alertMsg("Success", "You have deposited $" + value);
                    }else{
                        alertMsg("Failed",resultPair.second);
                    }
                    //refreshData(); refresh data has problem loading not using for now
                }else{
                    alertMsg("Failed", input.getText().toString()+" is not a valid number");
                }
            }
        });
        builder.show();
    }

    /**
     * showing a edit dialog for withdraw input on screen
     * @param id
     */
    protected void editWithdraw(long id) {
        final EditText input = new EditText(this);
        input.setHint("$");
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        final int index = (int)id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter Withdraw Amount").setIcon(android.R.drawable.ic_dialog_info).setView(input).setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which){
                if(sff.isValidInput(input)) {
                    double value = Double.parseDouble(input.getText().toString());
                    value = sff.NumberFormater(value);
                    Account account = accountArray.get(index);

                    Pair<Boolean, String> resultPair = rule.canWithdraw(account, value);
                    if (resultPair.first) {
                        accountArray.get(index).withdraw(value);
                        alertMsg("Success", "You have withdrawn $" + value);
                    } else {
                        alertMsg("Unable to Withdraw", resultPair.second);
                    }
                    //refreshData(); refresh data has problem loading not using for now
                }else{
                    alertMsg("Failed",input.getText().toString()+" is not a valid number");
                }
            }
        });
        builder.show();
    }

    /**
     * show an alert message on current screen
     */
    private void alertMsg(String title, String msg){
        //build dialog
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder.show();
    }
}