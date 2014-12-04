package cse110.com.goldencash.modelAccount;

import com.parse.ParseClassName;

import cse110.com.goldencash.User;

@ParseClassName("SavingAccount")
public class SavingAccount extends Account{
    private int dailyAmount;

    public SavingAccount(){

    }

    public double getAmount() {
        return getDouble("saving");
    }

    public void withdraw(double value) {
        put("saving",getAmount() - value);
        saveInBackground();
    }
    public void deposit(double value) {
        put("saving",getAmount() + value);
        saveInBackground();
    }
    public boolean isOpen() {
        return getBoolean("opensaving");
    }

    public void transfer(String To,double value) {
        User User = new User();
        put("saving",getAmount() - value);
        if (To.equals("Debit")) {
            put("debit", User.getDebitAccount().getAmount() + value);
        }
        else {
            put("credit", User.getCreditAccount().getAmount() + value);
        }
        saveInBackground();
    }

    public void transfer(DebitAccount account,double value) {
        put("saving",getAmount() - value);
        account.put("debit",account.getAmount() + value);
        saveInBackground();
        account.saveInBackground();
    }
}


