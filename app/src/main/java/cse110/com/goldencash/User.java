package cse110.com.goldencash;

import com.parse.ParseUser;

/**
 * Created by Sun on 2014/11/20.
 */
public class User extends ParseUser{
    private ParseUser user;

    public User() {
        user = ParseUser.getCurrentUser();
    }

    public boolean isAdmin() {
        return user.getBoolean("admin");
    }

    public String getFirtName() { return user.getString("firstname"); }

    public void setFirtName(String value) { user.put("firstname", value); saveInBackground(); }

    public String getLastName() { return user.getString("lasttname"); }

    public void setLastName(String value) { user.put("lastname",value); saveInBackground(); }

    public Account getAccount() {  return Account.createWithoutData(Account.class, user.getString("account")); }

}
