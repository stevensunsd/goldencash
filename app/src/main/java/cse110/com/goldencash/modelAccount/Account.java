package cse110.com.goldencash.modelAccount;

import android.app.ProgressDialog;
import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseClassName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import cse110.com.goldencash.TransactionActivity;
import cse110.com.goldencash.User;
import cse110.com.goldencash.AccountRule;
@ParseClassName("Account")
public abstract class Account extends ParseObject {
    protected String accountType;
    private AccountRule rule = new AccountRule();
    private User user = new User();
    public Account() {

    }

    public double getAmount() {return getDouble(accountType);}

    public void withdraw(double value) {
        put(accountType,getAmount() - value);
        //check rule then update Time
        if(rule.isAmountCorsstheLine(user.getAccount2(accountType), -value)) updateTime();
        addLog("Withdraw", value);
        saveInBackground();
    }

    public void deposit(double value) {
        put(accountType,getAmount() + value);
        //check rule then update time stamp for interest
        if(rule.isAmountCorsstheLine(user.getAccount2(accountType),value)) updateTime();
        addLog("Deposit",value);
        saveInBackground();
    }

    public boolean isOpen() {
        return getBoolean("open"+ accountType);
    }

    public void transferIn(Account account,double value) {
        put(accountType, getAmount() - value);
        account.put(account.accountType, account.getAmount() + value);

        //check rule then update Time
        if(rule.isAmountCorsstheLine(user.getAccount2(account.accountType),-value)) updateTime();
        if(rule.isAmountCorsstheLine(user.getAccount2(account.accountType), value))
        account.updateTime();

        addTransferInLog(account,value);
        saveInBackground();
    }

    public void transferOut(Account account,double value) {
        put(accountType,getAmount() - value);
        account.put("Debit", account.getAmount() + value);

        //check rule then update Time
        if(rule.isAmountCorsstheLine(user.getAccount2(accountType),-value)) updateTime();
        if(rule.isAmountCorsstheLine(account, value))
        account.updateTime();
        addTransferOutLog(account,value);
        saveInBackground();
        account.saveInBackground();
    }

    public void closeAccount() {
        put("open" + accountType, false);
        saveInBackground();
    }

    public String getAccountNumber() {
        return getString("accountnumber");
    }

    public String getAccounttype()  { return accountType;}

    public String getLog() {return getString("Log");}

    public void addInterestLog(double value) {
        put("Log",getLog()+ stringFormater(value));
        saveInBackground();
    }

    public void addLog(String choose,double value) {
        put("Log",getLog()+ stringFormater(choose,value) );
        saveInBackground();
    }

    public void addTransferInLog(Account account,double value) {
        String newLogFrom;
        String newLogTo;

        newLogFrom = currentTimeString() + " - $" + value + " Transfer To " + account.accountType + " Account" + '\n';
        newLogTo = currentTimeString() + " + $" + value + " Transfer From " + accountType + " Account" + '\n';

        put("Log",getLog()+ newLogFrom );
        saveInBackground();
        account.put("Log",account.getLog() + newLogTo);
        account.saveInBackground();
    }



    public void addTransferOutLog(Account account,double value) {
        String newLogFrom;
        String newLogTo;

        //format AccountNumber
        String s = account.getAccountNumber();
        String s1,s2,s3;
        s1 = s.substring(0,3);
        s2 = s.substring(3,5);
        s3 = s.substring(5,8);
        s = s1 + "-" + s2 + "-" + s3;

        newLogFrom = currentTimeString() + " - $" + value + " Transfer To Account Number: " + s + '\n';
        newLogTo = currentTimeString() + " + $" + value + " Account Number: " + s + " Transfer Into Debit Account" + '\n';
        put("Log",getLog()+ newLogFrom );
        account.put("Log",account.getLog() + newLogTo);
        saveInBackground();
        account.saveInBackground();
    }

    public abstract double getMonthInterest();
    public abstract int getInterestRate();

    public void calculateAmountafterInterest() {
        double interest = getMonthInterest();
        put(accountType,getAmount() + interest);
        addInterestLog(interest);
        saveInBackground();
    }

    public Date getupdateTime(){ return getDate("UpdateTime"); }

    public Date getDailyTime() {return getDate("dailytime");}


    private void updateTime() { put("UpdateTime",new Date(System.currentTimeMillis())); saveInBackground();}

    protected boolean isOver30days() {
        Date currentTime = new Date(System.currentTimeMillis());
        Date updateTime = getupdateTime();
        long days= (updateTime.getTime() - currentTime.getTime()) / (1000*60*60*24);
        return days>=30?true:false;
    }

    private String currentTimeString() {
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat df =new SimpleDateFormat("d MMM yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return df.format(currentTime);
    }

    private String stringFormater(double value){
        String newLogFrom;
        if(value>0)
            newLogFrom = currentTimeString() + " + $" + value + " Monthly Interest based on current Interest Rate " + getInt("InterestRate") + "%" +'\n';
        else
            newLogFrom = currentTimeString() + " - $" + value + " Penalty For Balance Below $100 over 30 days" + '\n';
        return newLogFrom;
    }

    private String stringFormater(String choose,double value){
        String newLogFrom;
        if (choose == "Withdraw")
            newLogFrom = currentTimeString() + " - $" + value + " Teller Withdraw" +'\n';
        else
            newLogFrom = currentTimeString() + " + $" + value + " Teller Deposit" + '\n';
        return newLogFrom;
    }
}


