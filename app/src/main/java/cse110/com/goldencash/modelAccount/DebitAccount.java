package cse110.com.goldencash.modelAccount;

import com.parse.ParseClassName;

@ParseClassName("DebitAccount")
public class DebitAccount extends Account {
    private int dailyAmount;

    public DebitAccount(){
        this.accountType = "Debit";
    }

    public int getCurrentInterestRate() {
        double balance = getAmount();
        int rate;
        if (balance >= 3000)
            rate = 3;
        else if(balance >=2000 && balance < 3000)
            rate = 2;
        else if(balance >=1000 && balance < 2000)
            rate = 1;
        else
            rate = 0;
        return rate;
    }

    public int getMonthInterestRate() {
        double balance = getAmount();
        int rate;
        if (isOver30days()) {
            if (balance >= 3000)
                rate = 3;
            else if (balance >= 2000 && balance < 3000)
                rate = 2;
            else if (balance >= 1000 && balance < 2000)
                rate = 1;
            else
                rate = 0;
        } else {
            rate = 0;
        }
        return rate;
    }

    public double getMonthInterest() {
        return getAmount()>100? getAmount() * getMonthInterestRate()/100:-25;
    }



}
