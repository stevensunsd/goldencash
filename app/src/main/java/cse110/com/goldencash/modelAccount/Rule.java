package cse110.com.goldencash.modelAccount;

/**
 * Created by Yang on 12/3/2014.
 */

import android.util.Pair;

import java.util.Calendar;
import java.util.Date;

/**
 *  Title: class Rule
 *  Description: Rule Object using for check condition during transaction
 */
public class Rule {
    private boolean checkSufficientFund(double balance, double charge) {
        return balance - charge >= 0;
    }

    private boolean isDailyTimeToday(Account account){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Date daily = account.getDailyTime();
        cal1.setTime(daily);
        cal2.setTime(new Date(System.currentTimeMillis()));
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }

    private boolean checkDailyLimit(Account acc, double charge) {
        double limit = 0;
        if (acc.getAccounttype().equals("Saving"))
            limit = 5000;
        else if (acc.getAccounttype().equals("Debit"))
            limit = 10000;
        if(charge > limit){
            return false;
        }
        if(isDailyTimeToday(acc)) {
            double daily = acc.getDailyAmount();
            if (daily + charge > limit) {
                return false;
            }
        }else{
            acc.resetDailyAmount();
        }
        return true;
    }

    private Pair<Boolean,String> checkDebitWithdraw(Account acc, double charge) {
        double balance = acc.getAmount();
        if(!acc.isOpen()){
            return Pair.create(false,"Account "+acc.getAccounttype()+" is not an active account");
        }
        if(!checkSufficientFund(balance, charge)) {
            // alert msg: insufficient fund

            return Pair.create(false,"Insufficient Fund");
        }
        if(!checkDailyLimit(acc, charge)) {
            //alert msg: exceeded daily limit
            return Pair.create(false,"Exceeded Daily Limit");
        }
        return Pair.create(true,"");
    }

    private Pair<Boolean,String> checkSavingWithdraw(Account acc, double charge) {
        double balance = acc.getAmount();
        if(!acc.isOpen()){
            return Pair.create(false,"Account "+acc.getAccounttype()+" is not an active account");
        }
        if(!checkSufficientFund(balance, charge)) {
            //alert msg: insufficient fund
            return Pair.create(false, "Insufficient Fund");
        }
        if(!checkDailyLimit(acc, charge)) {
            //alert msg: exceeded daily limit
            return Pair.create(false,"Exceeded Daily Limit");
        }
        return Pair.create(true, "");
    }

    public Pair<Boolean, String> canWithdraw(Account acc, double charge) {
        if(charge <= 0.01){
            return Pair.create(false, "Amount has to be greater than $0.01");
        }
        if(acc.getAccounttype().equals("Debit")) {

            return checkDebitWithdraw(acc, charge);
        }
        else if(acc.getAccounttype().equals("Credit")) {
            return Pair.create(false, "Can Not Withdraw Credit Account");
        }
        else if(acc.getAccounttype().equals("Saving")) {
            return checkSavingWithdraw(acc, charge);
        }
        else
            return Pair.create(false,"Account Type Error");
    }

    public Pair<Boolean, String> canDeposit(Account acc, double value){
        if(value < 0.01){
            return Pair.create(false, "Amount has to be greater than $0.01");
        }else if(!acc.isOpen()){
            return Pair.create(false, acc.getAccounttype()+" account is not an active account");
        }
        return Pair.create(true, "");
    }

    public Pair<Boolean, String> canTransfer(Account acc, double value) {
        return canWithdraw(acc, value);
    }

    public boolean canTransferToAnother(Account source, Account target, double value) {
        return canWithdraw(source, value).first;
    }
    public boolean canTransferToThis(Account account){
        return account.isOpen();
    }
    public boolean isAmountCorsstheLine(Account acc, double value) {
        double original = acc.getAmount();
        double result = original - value;
        if(original >= 100 && result < 100)
            return true;
        if(original < 1000 && result >= 1000 && result < 2000)
            return true;
        if(original < 2000 && result >= 3000 && result < 3000)
            return true;
        return original < 3000 && result >= 3000;
    }
}
