package cse110.com.goldencash;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Sun on 2014/11/20.
 */
@ParseClassName("User")
public class User extends ParseUser {
    private ParseUser user;

    public User() {
        user = ParseUser.getCurrentUser();
    }

    public boolean isAdmin() {
        return user.getBoolean("admin");
    }

    public String getFirstName() {
        return user.getString("firstname");
    }

    public void setFirstName(String value) {
        user.put("firstname", value);
        user.saveInBackground();
    }

    public String getLastName() {
        return user.getString("lastname");
    }

    public void setLastName(String value) {
        user.put("lastname", value);
        user.saveInBackground();
    }

    public void LogOut() {
        user.logOut();
    }

    public Account getAccount() {
        Account account = (Account) user.getParseObject("account");
        try {
            account.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return account;
    }

}