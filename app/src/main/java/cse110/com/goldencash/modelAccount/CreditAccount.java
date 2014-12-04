package cse110.com.goldencash.modelAccount;
import com.parse.ParseClassName;

import cse110.com.goldencash.User;

@ParseClassName("CreditAccount")
public class CreditAccount extends Account {

    public CreditAccount(){

    }

    public double getAmount() {
        return getDouble("credit");
    }

    public void withdraw(double value) {
        put("credit",getAmount() - value);
        saveInBackground();
    }
    public void deposit(double value) {
        put("credit",getAmount() + value);
        saveInBackground();
    }

    public boolean isOpen() {
        return getBoolean("opencredit");
    }

    public void transfer(String To,double value) {
        User User = new User();
        put("credit",getAmount() - value);
        if (To.equals("Saving")) {
            put("saving", User.getSavingAccount().getAmount() + value);
        }
        else {
            put("debit", User.getDebitAccount().getAmount() + value);
        }
        saveInBackground();
    }

    public void transfer(DebitAccount account,double value) {
        put("credit",getAmount() - value);
        account.put("debit",account.getAmount() + value);
        saveInBackground();
        account.saveInBackground();
    }
}
