package cse110.com.goldencash;

/**
 * Created by Yang on 12/3/2014.
 */
public class AccountRule {
    public boolean checkWithdraw(Account acc, double value) {
        double balance = acc.getDebit();
        if((balance - value) < 0) {
            // alert msg: insufficient fund
            return false;
        }
        //todo: check daily limit
        /*
        if(acc.getDailyAmount + value > 10000) {
           //alert msg: exceeded daily limit
           return false;
        }
         */
        return true;
    }

    public boolean checkDeposit(Account acc, double value) {
        return true;
    }

    public boolean checkTransfer(Account acc, double value) {
        return checkWithdraw(acc, value);
    }

    public boolean checkTransferToAnother(Account source, Account target, double value) {
        return checkWithdraw(source, value);
    }

    public boolean recordTime(Account acc, double value) {
        double original = acc.getDebit();
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
