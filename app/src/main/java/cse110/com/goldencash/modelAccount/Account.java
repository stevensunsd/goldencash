package cse110.com.goldencash.modelAccount;

import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseClassName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import cse110.com.goldencash.User;
import cse110.com.goldencash.AccountRule;
@ParseClassName("Account")
public abstract class Account extends ParseObject {
    protected String accountType;
    private AccountRule rule;
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

    public void transfer(String AccountType,double value) {
        put(accountType, getAmount() - value);
        user.getAccount2(AccountType).put(AccountType, user.getAccount2(AccountType).getAmount() + value);

        //check rule then update Time
        if(rule.isAmountCorsstheLine(user.getAccount2(accountType),-value)) updateTime();
        if(rule.isAmountCorsstheLine(user.getAccount2(AccountType),value))
        user.getAccount2(AccountType).updateTime();

        addLog(AccountType,value);
        saveInBackground();
    }

    public void transfer(Account account,double value) {
        put(accountType,getAmount() - value);
        account.put("Debit", account.getAmount() + value);

        //check rule then update Time
        if(rule.isAmountCorsstheLine(user.getAccount2(accountType),-value)) updateTime();
        if(rule.isAmountCorsstheLine(account, value))
        account.updateTime();

        addLog(account,value);
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

    public void addLog(String choose,double value) {
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat df =new SimpleDateFormat("d MMM yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String currentTimeString = df.format(currentTime);
        String newLogFrom;
        String newLogTo;

        User user = new User();

        if(choose.equals("Debit")||choose.equals("Saving")||choose.equals("Credit")) {
            newLogFrom = currentTimeString + " - $" + value + " Transfer To " + choose + " Account" + '\n';
            newLogTo = currentTimeString + " + $" + value + " Transfer From " + accountType + " Account" + '\n';
            user.getAccount2(choose).put("Log",user.getAccount2(choose).getLog() + newLogTo);
            user.getAccount2(choose).saveInBackground();
        }
        else {
            if (choose == "Withdraw")
                newLogFrom = currentTimeString + " - $" + value + " Teller Withdraw" +'\n';
            else
                newLogFrom = currentTimeString + " + $" + value + " Teller Deposit" + '\n';
        }
        put("Log",getLog()+ newLogFrom );
        saveInBackground();
    }

    public void addLog(Account account,double value) {
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat df =new SimpleDateFormat("d MMM yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String currentTimeString = df.format(currentTime);
        String newLogFrom;
        String newLogTo;

        //format AccountNumber
        String s = account.getAccountNumber();
        String s1,s2,s3;
        s1 = s.substring(0,3);
        s2 = s.substring(3,5);
        s3 = s.substring(5,8);
        s = s1 + "-" + s2 + "-" + s3;

        newLogFrom = currentTimeString + " - $" + value + " Transfer To Account Number: " + s + '\n';
        newLogTo = currentTimeString + " + $" + value + " Account Number: " + s + " Transfer Into Debit Account" + '\n';
        put("Log",getLog()+ newLogFrom );
        account.put("Log",account.getLog() + newLogTo);
        saveInBackground();
        account.saveInBackground();
    }

    public abstract double getMonthInterest();

    public void calculateAmountafterInterest() {
        double interest = getMonthInterest();
        put(accountType,getAmount() + interest);
        // add log
        saveInBackground();
    }

    public boolean isOver30days() {
        Date currentTime = new Date(System.currentTimeMillis());
        Date updateTime = getupdateTime();
        long days= (updateTime.getTime() - currentTime.getTime()) / (1000*60*60*24);
        return days>=30?true:false;
    }

    public void updateTime() { put("UpdateTime",System.currentTimeMillis());}

    public Date getupdateTime(){ return getDate("UpdateTime"); }

    public Date getDailyTime() {return getDate("dailytime");}

}


