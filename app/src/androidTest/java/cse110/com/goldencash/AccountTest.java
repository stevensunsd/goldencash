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
 * Created by ADMV on 11/16/2014.
 */

/* Work in progress */
public class AccountTest extends TestCase {

    private Account t_account;
    private double t_debit;
    private double t_credit;
    private double t_saving;

    public AccountTest() {
        //super(User.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testPreconditions();
        // t_account = getActivity();
        t_debit = t_account.getDebit();
        t_credit = t_account.getCredit();
        t_saving = t_account.getSaving();
        testBalances(t_debit, 100.1); // hard-coded; change later
        testBalances(t_credit, 100.1);
        testBalances(t_saving, 100.1);

    }

    public void testPreconditions() {
        assertNotNull("user is not null", t_account);
    }

    public void testStrings(String given, String expected) {
        assertEquals(given, expected);
    }

    public void testBalances(double amt, double given) {
        assertEquals(amt, given);
    }
}
