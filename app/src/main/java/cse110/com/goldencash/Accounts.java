package cse110.com.goldencash;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Yang on 10/31/2014.
 */
public class Accounts {
    private boolean openDebit;
    private boolean openCredit;
    private boolean openSaving;
    private double debitAmount;
    private double creditAmount;
    private double savingAmount;

    public void Accounts (String id) {
        setup(id);
    };

    private void setup(String id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    openDebit = parseObject.getBoolean("opendebit");
                    if (openDebit) {
                        debitAmount = (double) parseObject.getInt("debit");
                    }
                    openCredit = parseObject.getBoolean("opencredit");
                    if (openCredit) {
                        creditAmount = (double) parseObject.getInt("credit");
                    }
                    if (openSaving) {
                        savingAmount = (double) parseObject.getInt("saving");
                    }
                } else {
                    //TODO: error alert
                }
            }
        });
    }

    // Getter methods
    public double getDebitAmount() {
        if (openDebit) {
            return debitAmount;
        } else {
            return 0;
        }
    }

    public double getCreditAmount() {
        if (openCredit) {
            return creditAmount;
        } else {
            return 0;
        }
    }

    public double getSavingAmount() {
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
}