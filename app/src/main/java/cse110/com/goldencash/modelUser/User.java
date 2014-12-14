package cse110.com.goldencash.modelUser;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import cse110.com.goldencash.modelAccount.DebitAccount;
import cse110.com.goldencash.modelAccount.SavingAccount;

/**
 * Created by Sun on 2014/11/20.
 */
@ParseClassName("User")
public class User extends ParseUser {
    private ParseUser user;
    /*
    Default user constructor set as current user who has been logged in
     */
    public User() {
        user = ParseUser.getCurrentUser();
    }
    /*
    Construct a User object from given ParseUser object id
     */
    public User(String id) {


    }

    public boolean isAdmin() {
        return user.getBoolean("admin");
    }

    public String getUserName() {return user.getString("username");}

    public String getFirstName() {
        return user.getString("firstname");
    }

    public String getLastName() {
        return user.getString("lastname");
    }

    public void setFirstName(String value) {
        user.put("firstname", value);
        user.saveInBackground();
    }

    public void setLastName(String value) {
        user.put("lastname", value);
        user.saveInBackground();
    }

    public void LogOut() {
        user.logOut();
    }
    public String getEmail(){return user.getString("email");}
    /**
     * @deprecated
    public Account getAccount() {
             Account account = (Account) user.getParseObject("account");
             try {
                 account.fetchIfNeeded();
             } catch (ParseException e) {
                 e.printStackTrace();
             }
             return account;
    }
    */

    public cse110.com.goldencash.modelAccount.Account getAccount2(String accountType) {
        cse110.com.goldencash.modelAccount.Account account = (cse110.com.goldencash.modelAccount.Account) user.getParseObject(accountType+"account");
        try {
            account.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return account;
    }
}