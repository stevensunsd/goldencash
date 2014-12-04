package cse110.com.goldencash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.mock.MockContext;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import junit.framework.TestCase;

/**
 * Created by ADMV on 11/21/2014.
 */

public class UserTest extends AndroidTestCase {

    private User t_user;
    private boolean t_isAdmin;
    private String t_firstname;
    private String t_lastname;

    public static final String GIVEN_FIRST = "Fischer";
    public static final String GIVEN_LAST = "King";
    public static final String MAIN_STRING = "1XwyXTQlIQDuwcjETTTmvEaysvJVZLsSasuxibY3";
    public static final String CLIENT_STRING = "hQIEN0MfhYKiBPCHsPZ6djei7myOjcRpX56Cd4Xc";

    public UserTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Parse.initialize(getContext(), MAIN_STRING, CLIENT_STRING);
        ParseUser.enableAutomaticUser();
        t_user = new User();
        t_isAdmin = t_user.isAdmin();
        t_firstname = t_user.getFirstName();
        t_lastname = t_user.getLastName();
    }

    public void testPreconditions() {
        assertNotNull("user is not null", t_user);
        assertFalse("user is not admin", t_isAdmin);
        assertNull("user has no firstname", t_firstname);
        assertNull("user has no surname", t_lastname);
    }

    public void testStrings(String given, String expected) {
        t_user.setFirstName("Fischer");
        t_user.setLastName("King");
        assertEquals(t_firstname, GIVEN_FIRST);
        assertEquals(t_lastname, GIVEN_LAST);
    }
}
