package cse110.com.goldencash;


import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Yang on 10/31/2014.
 */
public class Accounts {
    protected String objId;
    private boolean openDebit;
    private boolean openCredit;
    private boolean openSaving;
    private float debitAmount;
    private float creditAmount;
    private float savingAmount;

    Accounts (String id) {
        setup(id);
    };

    private void setup(String id) {
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

    public boolean getOpenDebit() {
        return openDebit;
    }

    public boolean getOpenCredit() {
        return openCredit;
    }

    public boolean getOpenSaving() {
        return openSaving;
    }

    public void updateCreditAmount(float amount){
        //Parse update the value
    }


}