package cse110.com.goldencash;

/**
 * Created by Yang on 12/3/2014.
 */

import cse110.com.goldencash.modelAccount.Account;

public class AccountRule {
    private boolean checkSufficientFund(double balance, double charge) {
        return balance - charge >= 0;
    }

    private boolean checkDailyLimit(Account acc, double charge) {
        double limit = 0;
        double daily = 0;
        //double daily = acc.getDailyAmount();
        if(acc.getAccounttype().equals("Saving"))
            limit = 5000;
        else if(acc.getAccounttype().equals("Debit"))
            limit = 10000;
        if(daily + charge > limit) {
            return false;
        }
        return true;
    }

    private boolean checkDebitWithdraw(Account acc, double charge) {
        double balance = acc.getAmount();
        if(!checkSufficientFund(balance, charge)) {
            // alert msg: insufficient fund
            return false;
        }
        if(!checkDailyLimit(acc, charge)) {
            //alert msg: exceeded daily limit
            return false;
        }
        return true;
    }

    private boolean checkSavingWithdraw(Account acc, double charge) {
        double balance = acc.getAmount();
        if(!checkSufficientFund(balance, charge)) {
            //alert msg: insufficient fund
            return false;
        }
        if(!checkDailyLimit(acc, charge)) {
            //alert msg: exceeded daily limit
            return false;
        }
        return true;
    }
    /*
    Public functions for banking rule
     */
    public boolean canWithdraw(Account acc, double charge) {
        if(acc.getAccounttype().equals("Debit")) {
            return checkDebitWithdraw(acc, charge);
        }
        else if(acc.getAccounttype().equals("Credit")) {
            return false;
        }
        else if(acc.getAccounttype().equals("Saving")) {
            return checkSavingWithdraw(acc, charge);
        }
        else
            return false;
    }

    public boolean canDeposit(Account acc, double value) {
        return true;
    }

    public boolean canTransfer(Account acc, double value) {
        return canWithdraw(acc, value);
    }

    public boolean canTransferToAnother(Account source, Account target, double value) {
        return canWithdraw(source, value);
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

    public boolean applyPenalty(Account acc) {
        // if over 30 days && balance below 100
        // currentTime - acc.getRecordTime
        // return true
        return false;
    }
}
