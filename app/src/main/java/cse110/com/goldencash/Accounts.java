package cse110.com.goldencash;


import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.content.DialogInterface;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Yang on 10/31/2014.
 */
public class Accounts extends Activity {
    protected String objId;
    private boolean openDebit;
    private boolean openCredit;
    private boolean openSaving;
    private float debitAmount;
    private float creditAmount;
    private float savingAmount;


    private boolean flag;

    public void setup(String id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    objId = parseObject.getObjectId();
                    openDebit = parseObject.getBoolean("opendebit");
                    if (openDebit) {
                        debitAmount = (float) parseObject.getInt("debit");
                    }
                    openCredit = parseObject.getBoolean("opencredit");
                    if (openCredit) {
                        creditAmount = (float) parseObject.getInt("credit");
                    }
                    openSaving = parseObject.getBoolean("opensaving");
                    if (openSaving) {
                        savingAmount = (float) parseObject.getInt("saving");
                    }
                } else {
                    //TODO: error alert
                }
            }
        });
    }

    // Getter methods
    public float getDebitAmount() {
        if (openDebit) {
            return debitAmount;
        } else {
            return 0;
        }
    }

    public float getCreditAmount() {
        if (openCredit) {
            return creditAmount;
        } else {
            return 0;
        }
    }

    public float getSavingAmount() {
        if (openSaving) {
            return savingAmount;
        } else {
            return 0;
        }
    }

    public String getObjectId() {
        return objId;
    }

    public boolean getOpenDebit() {
        return openDebit;
    }

    public boolean getOpenCredit() {
        return openCredit;
    }

    public boolean getOpenSaving() {
        return openSaving;
    }



    // methods updating balance, return true for success and false for fail
    public boolean updateCreditAmount(final float amount){
        //Parse update the value
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.getInBackground(objId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null) {
                    if(openCredit) {
                        parseObject.put("credit", amount);
                        parseObject.saveInBackground();
                        flag = true;
                    }
                    else {
                        //err msg
                        flag = false;
                    }
                }
            }
        });
        return flag;
    }

    public boolean updateDebitAmount(final float amount) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.getInBackground(objId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    if (openDebit) {
                        parseObject.put("debit", amount);
                        parseObject.saveInBackground();
                        flag = true;
                    } else {
                        //err msg
                        flag = false;
                    }
                }
            }
        });
        return flag;
    }

    public boolean updateSavingAmount(final float amount) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.getInBackground(objId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null) {
                    if(openSaving) {
                        parseObject.put("saving", amount);
                        parseObject.saveInBackground();
                        flag = true;
                    }
                    else {
                        //err msg
                        flag = false;
                    }
                }
            }
        });
        return flag;
    }

    public boolean  checkwithdraw(final float withdrawAmount, final float currentAmount, String AccountType)
    {
        if( withdrawAmount > currentAmount)
        {
            String msg = "The Withdraw Amount: " + withdrawAmount + "is less than the amount; "+ currentAmount + "in your" + AccountType + "account";
            alertMsg("Withdraw Error", msg);
            return false;
        }
        else  return true;

    }
    protected void alertMsg(String title, String msg){
        //build dialog
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }
}