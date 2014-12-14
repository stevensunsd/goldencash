package cse110.com.goldencash.modelUser;


import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Sun on 2014/11/20.
 */
@ParseClassName("User")

/**
 *  Title: public class User
 *  Description: model User for a single User whose using the App
 */
public class User extends ParseUser {
    private ParseUser user;

    /**
     *  Default user constructor set as current user who has been logged in
     */
    public User() {
        user = ParseUser.getCurrentUser();
    }

    /**
     *  Getter for User,access data admin
     */
    public boolean isAdmin() {
        return user.getBoolean("admin");
    }

    /**
     *  Getter for User,access data username
     */
    public String getUserName() {return user.getString("username");}

    /**
     *  Getter for User,access data firstname
     */
    public String getFirstName() {
        return user.getString("firstname");
    }

    /**
     *  Getter for User,access data lastname
     */
    public String getLastName() {
        return user.getString("lastname");
    }

    /**
     *  Getter for User,access data email
     */
    public String getEmail(){return user.getString("email");}

    /**
     *  Setter for User,access data firstname
     */
    public void setFirstName(String value) {
        user.put("firstname", value);
        user.saveInBackground();
    }

    /**
     *  Setter for User,access data lastname
     */
    public void setLastName(String value) {
        user.put("lastname", value);
        user.saveInBackground();
    }

    /**
     *  Getter for User,access data User's Account
     *  1. User has a pointer in database point to his account
     *  2. fetch the pointer to the concrete account
     *  3. return the Account
     */
    public cse110.com.goldencash.modelAccount.Account getAccount(String accountType) {
        cse110.com.goldencash.modelAccount.Account account = (cse110.com.goldencash.modelAccount.Account) user.getParseObject(accountType+"account");
        try {
            account.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return account;
    }
}