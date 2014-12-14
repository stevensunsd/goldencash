package cse110.com.goldencash.modelAccount;

import com.parse.ParseObject;
import com.parse.ParseClassName;


import java.util.Date;


import cse110.com.goldencash.modelUser.User;

@ParseClassName("Account")

/**
 *  Title: abstract class Account
 *  Description: User has an Account to operate and use as an adapter for database
 */
public abstract class Account extends ParseObject {
    protected String accountType;
    private Rule rule = new Rule();
    private User user = new User();
    protected SideFunctionFacade sff = new SideFunctionFacadeImp();

    /**
     *  Empty Account Constructor
     */
    public Account() {

    }

    /**
     *  Getter for Account,access data Amount
     */
    public double getAmount() {return getDouble(accountType);}

    /**
     *  Getter for Account,access data Open
     */
    public boolean isOpen() {
        return getBoolean("open");
    }

    /**
     *  Getter for Account,access data AccountNumber
     */
    public String getAccountNumber() {
        return getString("accountnumber");
    }

    /**
     *  Getter for Account,access data AccountType
     */
    public String getAccounttype()  { return accountType;}

    /**
     *  Getter for Account,access data Log
     */
    public String getLog() {return getString("Log");}

    /**
     *  Getter for Account,access data UpdateTime
     */
    public Date getupdateTime(){ return getDate("UpdateTime"); }

    /**
     *  Getter for Account,access data DailyTime
     */
    public Date getDailyTime() {return getDate("DailyTime");}

    /**
     *  Getter for Account,access data DailyAmount
     */
    public double getDailyAmount(){
        return getDouble("DailyAmount");
    }

    /**
     *  Calculate how much interest should given based on month interest rate
     */
    public abstract double getMonthInterest();

    /**
     *  Calculate how much interest rate should given based on amount in account
     */
    public abstract int getMonthInterestRate();

    /**
     *  Calculate how much current interest rate should be
     */
    public abstract int getCurrentInterestRate();

    /**
     *  Setter for Account,access data UpdateTime
     */
    private void updateTime() {
        put("UpdateTime",new Date(System.currentTimeMillis()));
        saveInBackground();
    }

    /**
     *  Setter for Account,access data DailyTime
     */
    private void updateDailyTime(){
        put("DailyTime", new Date(System.currentTimeMillis()));
        saveInBackground();
    }

    /**
     *  Setter for Account,access data DailyAmount
     */
    private void updateDailyAmount(double amount){
        put("DailyAmount",amount);
    }

    /**
     *  Setter for Account,reset data DailyAmount to zero
     */
    public void resetDailyAmount(){
        put("DailyAmount",0.0);
    }

    /**
     *  Account operation withdraw
     *  1. update Amount in account
     *  2. Use Rule to check if the amount cross the Interest change rage
     *  3. Update InterestChangeTime and Update DailyTime,DailyAmount
     *  4. add Log
     */
    public void withdraw(double value) {
        put(accountType,getAmount() - value);
        //check rule then update Time
        if(rule.isAmountCorsstheLine(user.getAccount2(accountType), -value))
            updateTime();
        addLog("Withdraw", value);
        updateDailyTime();
        updateDailyAmount(value);
        saveInBackground();
    }

    /**
     *  Account operation deposit
     *  1. update Amount in account
     *  2. Use Rule to check if the amount cross the Interest change rage
     *  3. Update InterestChangeTime
     *  4. add Log
     */
    public void deposit(double value) {
        put(accountType,getAmount() + value);
        //check rule then update time stamp for interest
        if(rule.isAmountCorsstheLine(this,value))
            updateTime();
        addLog("Deposit",value);
        saveInBackground();
    }

    /**
     *  Account operation transferIn
     *  1. update Amount in account and inner account
     *  2. Use Rule to check if the amount cross the Interest change rage
     *  3. Update InterestChangeTime for both account
     *  4. add Log for both account
     */
    public void transferIn(Account account,double value) {
        put(accountType, getAmount() - value);
        account.put(account.accountType, account.getAmount() + value);

        //check rule then update Time
        if(rule.isAmountCorsstheLine(this,-value))
            updateTime();

        if(rule.isAmountCorsstheLine(account, value))
            account.updateTime();

        addTransferInLog(account,value);
        saveInBackground();
    }

    /**
     *  Account operation transferOut
     *  1. update Amount in account and other user account
     *  2. Use Rule to check if the amount cross the Interest change rage
     *  3. Update InterestChangeTime for both account
     *  4. add Log for both account
     */
    public void transferOut(Account account,double value) {
        put(accountType,getAmount() - value);
        account.put("Debit", account.getAmount() + value);

        //check rule then update Time
        if(rule.isAmountCorsstheLine(this,-value))
            updateTime();

        if(rule.isAmountCorsstheLine(account, value))
            account.updateTime();

        addTransferOutLog(account,value);
        saveInBackground();
        account.saveInBackground();
    }

    /**
     *  Account operation closeAccount
     *  set account open to false
     */
    public void closeAccount() {
        put("open", false);
        saveInBackground();
    }

    /**
     *  Helper Method for Add Log when apply Interest
     */
    private void addInterestLog(double value) {
        put("Log",getLog()+ sff.stringFormater(value, getMonthInterestRate()));
        saveInBackground();
    }

    /**
     *  Helper Method for Add Log when withdraw or deposit
     */
    private void addLog(String choose,double value) {
        put("Log",getLog()+ sff.stringFormater(choose, value) );
        saveInBackground();
    }

    /**
     *  Helper Method for Add Log when transfer inner account
     */
    private void addTransferInLog(Account account,double value) {
        String newLogFrom;
        String newLogTo;

        newLogFrom = sff.currentTimeString() + " - $" + value + " Transfer To " + account.accountType + " Account" + '\n';
        newLogTo = sff.currentTimeString() + " + $" + value + " Transfer From " + accountType + " Account" + '\n';

        put("Log",getLog()+ newLogFrom );
        saveInBackground();
        account.put("Log",account.getLog() + newLogTo);
        account.saveInBackground();
    }

    /**
     *  Helper Method for Add Log when transfer to other user account
     */
    private void addTransferOutLog(Account account,double value) {
        String newLogFrom;
        String newLogTo;

        newLogFrom = sff.currentTimeString() + " - $" + value + " Transfer To Account Number: " + account.getAccountNumber() + '\n';
        newLogTo = sff.currentTimeString() + " + $" + value + " Account Number: " + account.getAccountNumber() + " Transfer Into Debit Account" + '\n';
        put("Log",getLog()+ newLogFrom );
        account.put("Log",account.getLog() + newLogTo);
        saveInBackground();
        account.saveInBackground();
    }

    /**
     *  Account operation applyInterest
     *  1. check if the amount in the account is unchanged for 30days
     *  2. call calculateInterest
     *  3. update the UpdateTime
     */
    public void applyInterest() {
        if(sff.isOver30days(getupdateTime())) {
            calculateInterest();
            updateTime();
            saveInBackground();
        }
    }

    /**
     *  Helper Method for ApplyInterest to Account
     *  1. call getMonthInterest to figure out how much interest should give
     *  2. Add Log for giving interest to Account
     */
    private void calculateInterest() {
        double interest = getMonthInterest();
        interest = sff.NumberFormater(interest);
        put(accountType,getAmount() + interest);
        addInterestLog(interest);
        saveInBackground();
    }
}


