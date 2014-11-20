package cse110.com.goldencash;

// Account.java
import com.parse.ParseObject;
import com.parse.ParseClassName;

@ParseClassName("Account")

public class Account extends ParseObject {
    public Account() {


    }

    public void set(boolean openDebit,boolean openCredit,boolean openSaving) {
        put("opendebit",openDebit);
        put("opencredit",openCredit);
        put("opensaving",openSaving);
        put("debit",100);
        put("credit",100);
        put("saving",100);
    }

    public boolean isOpenDebit() {
        return getBoolean("opendebit");
    }

    public boolean isOpenCredit() {
        return getBoolean("opencredit");
    }

    public boolean isOpenSaving() {
        return getBoolean("openSaving");
    }

    public int getDebit() {
        return getInt("debit");
    }

    public void setDebit(int value) {
        put("debit",value);
    }

    public int getCredit() {
        return getInt("credit");
    }

    public void setCredit(int value) {
        put("credit",value);
    }

    public int getSaving() {
        return getInt("saving");
    }

    public void setSaving(int value) {
        put("saving",value);
    }
}