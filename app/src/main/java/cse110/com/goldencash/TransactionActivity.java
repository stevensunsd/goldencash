package cse110.com.goldencash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

import cse110.com.goldencash.modelAccount.Account;

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
    private cse110.com.goldencash.modelAccount.Account debit = user.getAccount2("Debit");
    private cse110.com.goldencash.modelAccount.Account credit = user.getAccount2("Credit");
    private cse110.com.goldencash.modelAccount.Account saving = user.getAccount2("Saving");

    private Account sourceAccount = debit;
    private AccountRule rule = new AccountRule();
    private String email;

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

    private ArrayAdapter createAdapter(ArrayList list){
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void editbox() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("$");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Amount").setIcon(android.R.drawable.ic_dialog_info).setView(input).setNegativeButton("Cancel",null);
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
                startLoading();
                String amount = input.getText().toString();
                Log.d("amount",amount);
                if(transactionMode){
                    makeTransactionToOther(targetAccount,(double)Integer.parseInt(amount));
                }else {
                    makeTransaction(Integer.parseInt(amount), spinnerFrom.getSelectedItem().toString(),
                            spinnerTo.getSelectedItem().toString());
                }
                stopLoading();
                }

        });
        builder.show();
    }
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
    private void makeTransaction(int amount,String from, String to){
        if(rule.canTransfer(user.getAccount2(from),amount)) {
            if (from.equals("Debit")) {
                if (to.equals("Saving"))
                    debit.transferIn(saving, amount);
                else
                    debit.transferIn(credit, amount);
            } else {
                if (to.equals("Debit"))
                    saving.transferIn(debit, amount);
                else
                    debit.transferIn(credit, amount);
            }
            alertMsg("Successful", "Transaction Success");
        }else{
            alertMsg("Failed","Insufficient fund in the "+sourceAccount.getAccounttype()+ " account.");
        }
    }

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

    private void gotoCustomerMainPage(){
        Intent intent = new Intent(this,CustomerMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

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

    private void checkEmailEntered(String email){
        Log.d("transaction","Target email: "+email+" Owner Email: "+user.getEmail());
        if(email.isEmpty() || email.equals( user.getEmail())){
            alertMsg("Invalid Email Address","Please enter an correct email address");
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
                            editbox();
                        } else {
                            alertMsg("User Not Found",
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
