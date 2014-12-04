package cse110.com.goldencash.modelAccount;

import com.parse.ParseClassName;

import cse110.com.goldencash.User;

@ParseClassName("DebitAccount")
public class DebitAccount extends Account {
    private int dailyAmount;

    public DebitAccount(){

    }

    public double getAmount() {
        return getDouble("debit");
    }

    public void withdraw(double value) {
        put("debit",getAmount() - value);
        saveInBackground();
    }
    public void deposit(double value) {
        put("debit",getAmount() + value);
        saveInBackground();
    }
    public boolean isOpen() {
        return getBoolean("opendebit");
    }

    public void transfer(String To,double value) {
        User User = new User();
        put("debit",getAmount() - value);
        if (To.equals("Saving")) {
            put("saving", User.getSavingAccount().getAmount() + value);
        }
        else {
            put("credit", User.getCreditAccount().getAmount() + value);
        }
        saveInBackground();
    }

    public void transfer(DebitAccount account,double value) {
        put("debit",getAmount() - value);
        account.put("debit",account.getAmount() + value);
        saveInBackground();
        account.saveInBackground();
    }
}
