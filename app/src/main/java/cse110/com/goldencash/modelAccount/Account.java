package cse110.com.goldencash.modelAccount;

import com.parse.ParseObject;
import com.parse.ParseClassName;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import cse110.com.goldencash.modelUser.User;

@ParseClassName("Account")
public abstract class Account extends ParseObject {
    protected String accountType;
    private Rule rule = new Rule();
    private User user = new User();
    public Account() {

    }

    public double getAmount() {return getDouble(accountType);}

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

    public void deposit(double value) {
        put(accountType,getAmount() + value);
        //check rule then update time stamp for interest
        if(rule.isAmountCorsstheLine(this,value))
            updateTime();
        addLog("Deposit",value);
        saveInBackground();
    }

    public boolean isOpen() {
        return getBoolean("open");
    }

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

    public void closeAccount() {
        put("open", false);
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

        newLogFrom = currentTimeString() + " - $" + value + " Transfer To Account Number: " + account.getAccountNumber() + '\n';
        newLogTo = currentTimeString() + " + $" + value + " Account Number: " + account.getAccountNumber() + " Transfer Into Debit Account" + '\n';
        put("Log",getLog()+ newLogFrom );
        account.put("Log",account.getLog() + newLogTo);
        saveInBackground();
        account.saveInBackground();
    }

    public abstract double getMonthInterest();
    public abstract int getMonthInterestRate();
    public abstract int getCurrentInterestRate();

    public void applyInterest() {
        if(isOver30days()) {
            calculateInterest();
            put("UpdateTime",System.currentTimeMillis());
            saveInBackground();
        }
    }

    public void calculateInterest() {
        double interest = getMonthInterest();
        interest = NumberFormater(interest);
        put(accountType,getAmount() + interest);
        addInterestLog(interest);
        saveInBackground();
    }

    public Date getupdateTime(){ return getDate("UpdateTime"); }

    public Date getDailyTime() {return getDate("DailyTime");}

    private void updateTime() {
        put("UpdateTime",new Date(System.currentTimeMillis()));
        saveInBackground();
    }

    private void updateDailyTime(){
        put("DailyTime", new Date(System.currentTimeMillis()));
        saveInBackground();
    }

    private void updateDailyAmount(double amount){
        put("DailyAmount",amount);
    }

    public void resetDailyAmount(){
        put("DailyAmount",0.0);
    }

    public double getDailyAmount(){
        return getDouble("DailyAmount");
    }

    public boolean isOver30days() {
        Date currentTime = new Date(System.currentTimeMillis());
        Date updateTime = getupdateTime();
        long days= (currentTime.getTime() - updateTime.getTime()) / (1000*60*60*24);
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
            newLogFrom = currentTimeString() + " + $" + value + " Monthly Interest based on your Interest Rate " + getMonthInterestRate() + "%" +'\n';
        else if(value<0)
            newLogFrom = currentTimeString() + " - $" + Math.abs(value) + " Penalty For Balance Below $100 over 30 days" + '\n';
        else newLogFrom = currentTimeString() + " No Interest or Penalty Apply" + '\n';
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

    private double NumberFormater(double value) {
        BigDecimal number = new BigDecimal(value);
        return number.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}


