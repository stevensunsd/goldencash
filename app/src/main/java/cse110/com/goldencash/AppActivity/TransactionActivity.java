package cse110.com.goldencash.AppActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import cse110.com.goldencash.R;
import cse110.com.goldencash.SideFunctionFacade;
import cse110.com.goldencash.SideFunctionFacadeImp;
import cse110.com.goldencash.modelAccount.Account;
import cse110.com.goldencash.modelAccount.Rule;
import cse110.com.goldencash.modelUser.User;

/**
 * Created by Xin Wen on 11/20/14.
 */
public class TransactionActivity extends Activity{

    protected Spinner spinnerFrom, spinnerTo;
    protected Button button;
    protected Button transferButton;
    protected ProgressDialog proDialog;

    //false for within same account transfer
    private boolean transactionMode = false;
    protected User user = new User();
    private cse110.com.goldencash.modelAccount.Account targetAccount;
    private cse110.com.goldencash.modelAccount.Account debit = user.getAccount("Debit");
    private cse110.com.goldencash.modelAccount.Account credit = user.getAccount("Credit");
    private cse110.com.goldencash.modelAccount.Account saving = user.getAccount("Saving");

    private Account sourceAccount = debit;
    private Rule rule = new Rule();
    private String email;
    protected SideFunctionFacade sff = new SideFunctionFacadeImp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 11. Add a spinning progress bar (and make sure it's off)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);

        //set title
        setTitle("New Transaction");

        setContentView(R.layout.activity_transaction);

        addItemsOnSpinners();

        addListenerOnButton();

        addListenerOnSpinnerItemSelection();


    }
    /**
     * add correct accounts to spinners.
     */
    private void addItemsOnSpinners(){
        spinnerFrom = (Spinner) findViewById(R.id.spinner_transaction_from);
        spinnerTo = (Spinner) findViewById(R.id.spinner_transaction_to);

        //set Spinner From
        ArrayList<String> list = new ArrayList<String>();
        list.add("Debit");
        list.add("Saving");
        //list.add("Credit");

        spinnerFrom.setAdapter(createAdapter(list));
        //spinnerTo.setAdapter(adapter);
    }

    /**
     * add onclicklistener to buttons
     */
    private void addListenerOnButton(){
        button = (Button)findViewById(R.id.button_confirm_transaction);
        transferButton = (Button)findViewById(R.id.button_transfer_to_account);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getString(R.string.debugInfo_text),""+spinnerFrom.getSelectedItem()+spinnerTo.getSelectedItem());
                editbox();
            }
        });
        transferButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(getString(R.string.debugInfo_text),"transfer button clicked");
                showTransferAccountDialog();
            }
        });

    }

    /**
     * add click listener to spinner
     */
    private void addListenerOnSpinnerItemSelection(){

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0){
                    ArrayList<String> list = new ArrayList<String>();
                    list.add("Credit");
                    list.add("Saving");
                    spinnerTo.setAdapter(createAdapter(list));
                }else{
                    ArrayList<String> list = new ArrayList<String>();
                    list.add("Credit");
                    list.add("Debit");
                    spinnerTo.setAdapter(createAdapter(list));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        //spinnerTo.setOnItemSelectedListener(this);
    }

    /**
     * setup adapter for listview.
     * @param list
     * @return
     */
    private ArrayAdapter createAdapter(ArrayList list){
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    /**
     * show an edit amount dialog on current screen.
     * content will be differ from what kind of account selected.
     */
    private void editbox() {
        final EditText input = new EditText(this);
        input.setHint("$");
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Amount").setIcon(android.R.drawable.ic_dialog_info).setView(input).setNegativeButton("Cancel",null);

        //checking transaction boolean value to choose what content to show.
        if(transactionMode) {
            String[] options = {"From Checking Account", "From Saving Account"};
            int selected = 0; // or whatever you want
            builder.setSingleChoiceItems(options, selected, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if (item >= 0) {
                        sourceAccount = saving;
                    } else {
                        sourceAccount = debit;
                    }
                }
            });
        }
        builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                if (sff.isValidInput(input)) {
                    startLoading();
                    Double amount = Double.parseDouble(input.getText().toString());
                    amount = sff.NumberFormater(amount);
                    if (transactionMode) {
                        makeTransactionToOther(targetAccount, amount);
                    } else {
                        makeTransaction(amount, spinnerFrom.getSelectedItem().toString(),
                                spinnerTo.getSelectedItem().toString());
                    }
                    stopLoading();
                } else {
                    alertMsg("Failed", input.getText().toString() + " is not a valid number");
                }
            }
        });
        builder.show();
    }

    /**
     * make transaction to other user
     * @param account
     * @param value
     */
    private void makeTransactionToOther(Account account,double value){
        if(rule.canTransferToAnother(sourceAccount,targetAccount,value)) {
            sourceAccount.transferOut(account, value);
            alertMsg("Successful",
                    "You have transferred $" + value +
                            " from " + sourceAccount.getAccounttype() + " account to " + email);
        }else{
            alertMsg("Failed","Insufficient fund in the "+sourceAccount.getAccounttype()+ " account.");
        }
    }

    /**
     * make transaction to other account that current user have
     * @param amount
     * @param from
     * @param to
     */
    private void makeTransaction(double amount,String from, String to){
        Pair<Boolean, String> resultPair = rule.canDeposit(user.getAccount(to),amount);
        if(resultPair.first) {
            Pair<Boolean,String > result = rule.canTransfer(user.getAccount(from), amount);
            if (result.first) {
                if (from.equals("Debit")) {
                    if (to.equals("Saving"))
                        debit.transferIn(saving, amount);
                    else
                        debit.transferIn(credit, amount);
                } else {
                    if (to.equals("Debit"))
                        saving.transferIn(debit, amount);
                    else
                        saving.transferIn(credit, amount);
                }
                alertMsg("Successful", "Transaction Success");
            } else {
                alertMsg("Failed", result.second);
            }
        }else{
            alertMsg("Failed", resultPair.second );
        }
    }

    /**
     * Helper methods to show loading HUD
     */
    protected void startLoading() {
        proDialog = new ProgressDialog(this);
        proDialog.setMessage("Transferring...Please Wait");
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    protected void stopLoading() {
        proDialog.dismiss();
        proDialog = null;
    }


    /**
     * show an error alert dialog on current activity
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
                        gotoCustomerMainPage();
                    }
                });
        builder.show();
    }

    /**
     * Helper method to show transfer account dialog, see below
     * @param title
     * @param msg
     */
    private void alertMsg2(String title, String msg){
        //build dialog
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showTransferAccountDialog();
                    }
                });
        builder.show();
    }

    /**
     * go to customer main activity, ends current one.
     */
    private void gotoCustomerMainPage(){
        Intent intent = new Intent(this,CustomerMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * showing a dialog that user be able to enter an email address.
     */
    private void showTransferAccountDialog(){
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter an email address").setIcon(android.R.drawable.ic_dialog_info).setView(input).setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setProgressBarIndeterminateVisibility(true);
                transactionMode = true;
                email = input.getText().toString();
                checkEmailEntered(email);
            }

        });
        builder.show();
    }

    /**
     * Helper method that validates user email entered from database.
     * @param email
     */
    private void checkEmailEntered(final String email){
        Log.d("transaction","Target email: "+email+" Owner Email: "+user.getEmail());
        if(email.isEmpty() || email.equals( user.getEmail())){
            setProgressBarIndeterminateVisibility(false);
            alertMsg2("Invalid Email Address","Please enter an correct email address");
        }else {
            //find target account in database
            ParseQuery query = ParseUser.getQuery();
            query.whereEqualTo("email", email);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    setProgressBarIndeterminateVisibility(false);
                    if (e == null) {
                        if (!parseObjects.isEmpty()) {
                            Log.d("Got target email", "");
                            ParseObject po = parseObjects.get(0);
                            ParseObject account = po.getParseObject("Debitaccount");
                            try {
                                account.fetch();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            targetAccount = (Account) account;
                            if(!rule.canTransferToThis(targetAccount)){
                                alertMsg2("Unable to Transfer", "User " + email + " doesn't have a active Debit account");
                            }else {
                                editbox();
                            }
                        } else {
                            alertMsg2("User Not Found",
                                    "There is no user associate to the email you entered.");
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Network error, please try again.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
