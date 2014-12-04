package cse110.com.goldencash.modelAccount;

import com.parse.ParseClassName;

import cse110.com.goldencash.User;

@ParseClassName("DebitAccount")
public class DebitAccount extends Account {
    private int dailyAmount;

    public DebitAccount(){
        this.accountType = "Debit";
    }

}
