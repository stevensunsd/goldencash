package cse110.com.goldencash.modelAccount;

import com.parse.ParseObject;
import com.parse.ParseClassName;

import java.util.Date;

@ParseClassName("Account")
public abstract class Account extends ParseObject implements AccountInterface{
    public Account() {

    }

    public Date getDailyTime() {return getDate("dailytime");}

    public String getAccountNumber() {
        return getString("accountnumber");
    }
}


