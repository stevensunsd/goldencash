package cse110.com.goldencash.AppActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;

import cse110.com.goldencash.R;
import cse110.com.goldencash.modelAccount.Account;
import cse110.com.goldencash.modelUser.User;

/**
 * Created by Xin Wen on 11/17/14.
 */
public class CustomerMainActivity extends Activity {

    protected String key;
    protected ListView listview;
    protected ArrayAdapter<String> adapter;
    protected TextView accountNumberText;

    protected User user = new User();
    private cse110.com.goldencash.modelAccount.Account debit = user.getAccount2("Debit");
    private cse110.com.goldencash.modelAccount.Account credit = user.getAccount2("Credit");
    private cse110.com.goldencash.modelAccount.Account saving = user.getAccount2("Saving");

    private ArrayList<Account> accountArray = new ArrayList<Account>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add a spinning progress bar (and make sure it's off)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);

        setTitle("Welcome, "+user.getFirstName());
        setContentView(R.layout.activity_customermain);

        accountNumberText = (TextView) findViewById(R.id.customer_main_account_number);
        accountNumberText.setText("Account Number: "+debit.getAccountNumber());
        //get listview from XML
        listview = (ListView) findViewById(R.id.listView_accounts_customer);

        // ListView Item Click Listener
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("CustomerMain","item clicked at: "+position+id);
                gotoStatementsPage(position);

            }
        });
        setAdapter();
    }

    /**
     * this method set correct adapter to listview
     */
    private void setAdapter(){
        ArrayList<String> templist=setAdapterarray();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, templist);

        // Assign adapter to ListView
        listview.setAdapter(adapter);

    }

    /**
     * setup the array for the adapter
     * @return
     */
    private ArrayList<String> setAdapterarray(){
        ArrayList<String> account_list=new ArrayList<String>();
        credit.applyInterest();
        saving.applyInterest();
        debit.applyInterest();
        String stringCredit = "Credit Account\nAvailable Balance:" + credit.getAmount();
        String stringSaving = "Saving Account\nAvailable Balance:" + saving.getAmount();
        String stringDebit = "Debit Account\nAvailable Balance:" + debit.getAmount();
        String stringSavingInterest = "\nCurrent Interest Rate: " + saving.getCurrentInterestRate()+"%";
        String stringDebitInterest = "\nCurrent Interest Rate: " + debit.getCurrentInterestRate() + "%";
        if(debit.isOpen()){
            account_list.add(stringDebit+stringDebitInterest);
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


    @Override
    public void onBackPressed(){
        //prevent go back to login page with override the back button behavior
        customerLogOut();
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
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * log out customer
     */
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

    /**
     * close the account that is selected
     */
    private void closeAccount(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayList list = new ArrayList();
        if(debit.isOpen()){
            list.add("Debit");
        }
        if(saving.isOpen()){
            list.add("Saving");
        }
        if(credit.isOpen()){
            list.add("Credit");
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //check close account
                Account account = accountArray.get(i);
                account.closeAccount();
                if(list.size()==1) {
                    ParseUser.getCurrentUser().put("Disable", true);
                    ParseUser.getCurrentUser().saveInBackground();
                    alertMsg("Warning", "You have closed all your account, Your account is disabled.");
                }
                else {
                    refreshData();
                }
            }
        });
        builder.setTitle("Choose an account").setIcon(android.R.drawable.ic_dialog_info);
        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }

    /**
     * go to sign in activity
     */
    private void gotoSigninActtivity(){
        Intent intent = new Intent(this,SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * go to transaction activity
     */
    private void gotoTransactionPage(){
        Intent intent = new Intent(this,TransactionActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * go to sign in activity
     */
    private void gotoSigninPage(){
        Intent intent = new Intent(this,SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * go to statement activity
     * @param i
     */
    private void gotoStatementsPage(int i){
        Intent intent = new Intent(this, StatementsActivity.class);
        Log.d("CustomerMain","go Statement with: "+accountArray.get(i).getAccounttype());
        intent.putExtra("account", accountArray.get(i).getAccounttype());
        startActivity(intent);
    }

    /**
     * refresh current display
     */
    private void refreshData() {
        Intent intent = new Intent(CustomerMainActivity.this, CustomerMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * showing an alert message on current screen
     * @param title
     * @param msg
     */
    private void alertMsg(String title, String msg){
        //build dialog
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        gotoSigninPage();
                    }
                });
        builder.show();
    }


}
