package cse110.com.goldencash.modelAccount;

import com.parse.ParseClassName;

import cse110.com.goldencash.User;

@ParseClassName("SavingAccount")
public class SavingAccount extends Account{
    private int dailyAmount;

    public SavingAccount(){
        this.accountType = "Saving";
    }
}


