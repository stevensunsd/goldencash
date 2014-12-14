package cse110.com.goldencash.modelAccount;

import com.parse.ParseClassName;

@ParseClassName("SavingAccount")
public class SavingAccount extends Account {

    public SavingAccount(){
        this.accountType = "Saving";
    }

    /**
     *  Calculate how much current interest rate should be
     */
    public int getCurrentInterestRate() {
        double balance = getAmount();
        int rate;
        if (balance >= 3000)
            rate = 4;
        else if(balance >=2000 && balance < 3000)
            rate = 3;
        else if(balance >=1000 && balance < 2000)
            rate = 2;
        else
            rate = 0;
        return rate;
    }

    /**
     *  Calculate how much interest rate should given based on amount in account
     */
    public int getMonthInterestRate() {
        double balance = getAmount();
        int rate;
        if (sff.isOver30days(getupdateTime())) {
            if (balance >= 3000)
                rate = 4;
            else if (balance >= 2000 && balance < 3000)
                rate = 3;
            else if (balance >= 1000 && balance < 2000)
                rate = 2;
            else
                rate = 0;
        } else {
            rate = 0;
        }
        return rate;
    }

    /**
     *  Calculate how much interest should given based on month interest rate
     */
    public double getMonthInterest() {
        return getAmount()>100? getAmount() * getMonthInterestRate()/100:-25;
    }
}


