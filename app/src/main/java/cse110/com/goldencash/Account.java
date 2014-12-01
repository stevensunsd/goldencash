package cse110.com.goldencash;

// Account.java
import com.parse.ParseObject;
import com.parse.ParseClassName;

import java.util.Random;

@ParseClassName("Account")
public class Account extends ParseObject {
    public Account() {

    }

    public void setup(boolean openDebit,boolean openCredit,boolean openSaving) {
        put("opendebit",openDebit);
        put("opencredit",openCredit);
        put("opensaving",openSaving);
        put("debit",100.1);
        put("credit",100.1);
        put("saving",100.1);
        put("number",accountNumberGenerator());
    }

    public boolean isOpenDebit() {
        return getBoolean("opendebit");
    }
    public void setOpenDebit(boolean value){ put("opendebit",value); saveInBackground();}

    public boolean isOpenCredit() {
        return getBoolean("opencredit");
    }
    public void setOpenCredit(boolean value){put("opencredit",value); saveInBackground();}

    public boolean isOpenSaving() {
        return getBoolean("opensaving");
    }
    public void setOpenSaving(boolean value){put("opensaving",value); saveInBackground();}

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

    public void transferFromSaving(String To, double value){
        if (To.equals("Debit")){
            withdrawSaving(value);
            depositDebit(value);
        }else{
            withdrawSaving(value);
            depositCredit(value);
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

    public void saveAccount(){
        saveInBackground();
    }

    private String accountNumberGenerator(){
        Random r = new Random();
        return r.nextInt(999999-100000)+100000+"";
    }

    public String getAccountNumber(){   return getString("number"); }
}