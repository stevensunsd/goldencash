package cse110.com.goldencash;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import junit.framework.TestCase;

/**
 * Created by ADMV on 11/21/2014.
 */

/* Work in progress */
public class UserTest extends TestCase {

    private User t_user;
    private boolean t_isAdmin;
    private String t_firstname;
    private String t_lastname;

    public UserTest() {
        //super(User.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // t_user = getActivity();
        t_isAdmin = t_user.isAdmin();
        t_firstname = t_user.getFirstName();
        t_lastname = t_user.getLastName();
        testStrings(t_firstname, "USER");
        testStrings(t_lastname, "pass");
    }

    public void testPreconditions() {
        assertNotNull("user is not null", t_user);
    }

    public void testStrings(String given, String expected) {
        assertEquals(given, expected);
    }
}
