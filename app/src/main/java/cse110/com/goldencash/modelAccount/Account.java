package cse110.com.goldencash.modelAccount;

import com.parse.ParseObject;
import com.parse.ParseClassName;

import java.text.SimpleDateFormat;
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
        addLog("Withdraw", value);
        saveInBackground();
    }

    public void deposit(double value) {
        put(accountType,getAmount() + value);
        addLog("Deposit",value);
        saveInBackground();
    }

    public boolean isOpen() {
        return getBoolean("open"+ accountType);
    }

    public void transfer(String AccountType,double value) {
        User User = new User();
        put(accountType, getAmount() - value);
        User.getAccount2(AccountType).put(AccountType, User.getAccount2(AccountType).getAmount() + value);
        addLog(AccountType,value);
        saveInBackground();
    }

    public void transfer(Account account,double value) {
        put(accountType,getAmount() - value);
        account.put("Debit",account.getAmount() + value);
        addLog(account,value);
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

    public String getLog() {return getString("Log");}

    public void addLog(String choose,double value) {
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat df =new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String currentTimeString = df.format(currentTime);
        String newLogFrom;
        String newLogTo = "";
        User user = new User();
        if(choose.equals("Debit")||choose.equals("Saving")||choose.equals("Credit")) {
            newLogFrom = currentTimeString + " " + "Customer Transfer From " + accountType + " To " + choose + " Account" + " $" + value + '\n';
            newLogTo = currentTimeString + " " + "Customer Transfer From " + accountType + " To " + choose + " Account" + " $" + value + '\n';
        }
        else {
            newLogFrom = currentTimeString + " " + "Teller " + choose + " " + accountType + " Account" + " $" + value + '\n';
        }

        put("Log",getLog()+ newLogFrom );
        user.getAccount2(choose).put("Log",user.getAccount2(choose).getLog() + newLogTo);
        saveInBackground();
        user.getAccount2(choose).saveInBackground();
    }

    public void addLog(Account account,double value) {
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat df =new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String currentTimeString = df.format(currentTime);
        String newLogFrom;
        String newLogTo;
        newLogFrom = currentTimeString + " " + "Customer Transfer From " + accountType + " To " + "Account Number: " + account.getAccountNumber() + " $" + value + '\n';
        newLogTo = currentTimeString + " " + "Customer Transfer From " + "Account Number: " + getAccountNumber() + " $" + value + '\n';
        put("Log",getLog()+ newLogFrom );
        account.put("Log",account.getLog() + newLogTo);
        saveInBackground();
        account.saveInBackground();
    }

    public Date getDailyTime() {return getDate("dailytime");}
}


