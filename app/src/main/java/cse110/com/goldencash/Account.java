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
        put("debit",100.1);
        put("credit",100.1);
        put("saving",100.1);
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

    public double getDebit() {
        return getDouble("debit");
    }

    public void setDebit(double value) {
        put("debit",value);
        saveInBackground();
    }

    public void withdrawDebit(double value){
        setDebit(getDebit() - value);
    }

    public void depositDebit(double value){
        setDebit(getDebit() + value);
    }

    public void transferFromDebit(String To,double value){
        if (To.equals("Saving")) {
            withdrawDebit(value);
            depositSaving(value);
        }
        else if(To.equals("Credit")) {
            withdrawDebit(value);
            depositCredit(value);
        }
        else{
            // transfer to other accounts
        }
    }

    public double getCredit() {
        return getDouble("credit");
    }

    public void setCredit(double value) {
        put("credit",value);
        saveInBackground();
    }

    public void depositCredit(double value){
        setCredit(getCredit() + value);
    }

    public void transferCredit(double value){
        setCredit(getCredit() + value);
    }

    public double getSaving() {
        return getDouble("saving");
    }

    public void setSaving(double value) {
        put("saving",value);
        saveInBackground();
    }

    public void withdrawSaving(double value){
        setSaving(getSaving() - value);
    }

    public void depositSaving(double value){
        setSaving(getSaving() + value);
    }

    public void transferSaving(double value){
        setCredit(getCredit() + value);
    }

}