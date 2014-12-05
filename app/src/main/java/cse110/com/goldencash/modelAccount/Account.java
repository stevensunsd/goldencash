package cse110.com.goldencash.modelAccount;

import com.parse.ParseObject;
import com.parse.ParseClassName;

import java.util.Date;

import cse110.com.goldencash.User;

@ParseClassName("Account")
public abstract class Account extends ParseObject implements AccountInterface{
    protected String accountType;

    public Account() {

    }

    public double getAmount() {return getDouble(accountType);}

    public void withdraw(double value) {
        put(accountType,getAmount() - value);
        saveInBackground();
    }

    public void deposit(double value) {
        put(accountType,getAmount() + value);
        saveInBackground();
    }

    public boolean isOpen() {
        return getBoolean("open"+ accountType);
    }

    public void transfer(String AccountType,double value) {
        User User = new User();
        put(accountType, getAmount() - value);
        User.getAccount2(AccountType).put(AccountType, User.getAccount2(AccountType).getAmount() + value);
        saveInBackground();
    }

    public void transfer(Account account,double value) {
        put(accountType,getAmount() - value);
        account.put("Debit",account.getAmount() + value);
        saveInBackground();
        account.saveInBackground();
    }

    public void closeAccount() {
        put("open"+accountType,false);
        saveInBackground();
    }

    public String getAccountNumber() {
        return getString("accountnumber");
    }

    public String getAccounttype()  { return accountType;}

    public Date getDailyTime() {return getDate("dailytime");}
}


