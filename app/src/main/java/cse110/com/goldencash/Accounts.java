package cse110.com.goldencash;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Yang on 10/31/2014.
 */
public class Accounts {
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
                    debitAmount = (double) parseObject.getInt("debit");
                    creditAmount = (double) parseObject.getInt("credit");
                    savingAmount = (double) parseObject.getInt("saving");
                } else {
                    //TODO: error alert
                }
            }
        });
    }

    // Getter methods
    public double getDebitAmount() {
        return debitAmount;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public double getSavingAmount() {
        return savingAmount;
    }
}