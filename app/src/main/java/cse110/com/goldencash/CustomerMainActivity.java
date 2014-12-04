package cse110.com.goldencash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by Xin Wen on 11/17/14.
 */
public class CustomerMainActivity extends Activity {

    protected String key;
    protected ListView listview;
    protected String[] accountArray;
    protected ArrayAdapter<String> adapter;


    protected boolean openDebit;
    protected boolean openCredit;
    protected boolean openSaving;
    protected float debit;
    protected float credit;
    protected float saving;

    protected User user = new User();

    protected boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //key = retrieveKey();

        // 11. Add a spinning progress bar (and make sure it's off)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);

        setTitle("Welcome, "+user.getFirstName());
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

        setup();
        setAdapter();
    }
    private void setup(){
        Account account = user.getAccount();
        openDebit = account.isOpenDebit();
        openCredit = account.isOpenCredit();
        openSaving = account.isOpenSaving();
        debit = (float) account.getDebit();
        credit = (float) account.getCredit();
        saving = (float) account.getSaving();
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
        customerLogOut();
    }

    private String retrieveKey(){
//        SharedPreferences prefs = getSharedPreferences("myFile", Context.MODE_PRIVATE);
//        return prefs.getString("key","");
        return user.getAccount().getObjectId();
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
        if (id == R.id.action_transaction) {
            Log.d(getString(R.string.debugInfo_text), "clicked, now going to Transaction");
            gotoTransactionPage();
            return true;
        }else if (id == R.id.action_logout){
            customerLogOut();
        }else if (id == R.id.action_close_account){
            closeAccount();
        }else if (id == R.id.action_interests){
            gotoInterestPage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void customerLogOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log Out?");
        builder.setPositiveButton("Log Out",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        user.logOut();
                        gotoSigninActtivity();
                    }
                });
        builder.setNegativeButton("Cancel",null);
        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }

    private void closeAccount(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ArrayList list = new ArrayList();
        if(openDebit){
            list.add("Debit");
        }
        if(openCredit){list.add("Credit");}
        if(openSaving){
            list.add("Saving");
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //check close account
                Account account = user.getAccount();
                switch (i) {
                    case 0:
                        if(account.isOpenDebit()) {
                            account.setOpenDebit(false);
                            break;
                        }
                        else if(account.isOpenCredit()) {
                            flag = true;
                            account.setOpenCredit(false); break;
                        }
                        else {
                            account.setOpenSaving(false); break;
                        }
                    case 1:
                        if(account.isOpenCredit()&&!flag) {
                            account.setOpenCredit(false); break;
                        }
                        else {
                            account.setOpenSaving(false); break;
                        }
                    case 2:account.setOpenSaving(false);break;
                    default: // error
                }
                refreshData();
            }
        });
        builder.setTitle("Choose an account").setIcon(android.R.drawable.ic_dialog_info);
        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }
    private void gotoSigninActtivity(){
        Intent intent = new Intent(this,SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void gotoTransactionPage(){
        Intent intent = new Intent(this,TransactionActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void gotoInterestPage(){
        Intent intent = new Intent(this, InterestActivity.class);
        //intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void refreshData() {
        Intent intent = new Intent(CustomerMainActivity.this, CustomerMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
