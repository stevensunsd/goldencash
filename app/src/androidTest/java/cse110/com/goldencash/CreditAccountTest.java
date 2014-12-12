package cse110.com.goldencash;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.AndroidTestCase;
import android.test.TouchUtils;
import android.test.UiThreadTest;
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

import cse110.com.goldencash.modelAccount.CreditAccount;

/**
 * Created by ADMV on 11/16/2014.
 */

/* Change due to interface-d class */
public class CreditAccountTest extends AndroidTestCase {

    private CreditAccount t_account;
    private CreditAccount t_acc2;

    public static final String MAIN_STRING = "1XwyXTQlIQDuwcjETTTmvEaysvJVZLsSasuxibY3";
    public static final String CLIENT_STRING = "hQIEN0MfhYKiBPCHsPZ6djei7myOjcRpX56Cd4Xc";
    public static final double INITIAL_FUNDS = 100.1;

    public CreditAccountTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Parse.initialize(getContext(), MAIN_STRING, CLIENT_STRING);
        ParseUser.enableAutomaticUser();

        t_account = new CreditAccount();
        t_acc2 = new CreditAccount();
        t_account.put("openCredit", true);
        t_account.put("Credit", 50.0);
        t_account.put("accountnumber", "1");
        t_acc2.put("openCredit", true);
        t_acc2.put("Credit", 5.0);
        t_acc2.put("accountnumber", "2");
    }

    public void testPreconditions() {
        assertNotNull("account is not null", t_account);
        assertTrue("account is open", t_account.isOpen());
        assertEquals("account has funds", t_account.getAmount(), 50.0);
        assertNotNull("account number exists", t_account.getAccountNumber());
        assertEquals("account is credit", t_account.getAccounttype(), "Credit");
        assertNull("account log is null", t_account.getLog());
    }

    public void testTransfers() {
        t_account.withdraw(10.0);
        assertEquals("account has withdrawn 10 units", t_account.getAmount(), 40.0);
        t_account.deposit(60.0);
        assertEquals("account has deposited 60 units", t_account.getAmount(), 100.0);
        //t_account.transfer("Debit", 15.50);
        //assertEquals("account has transferred 15.50 to anonymous account",
         //       t_account.getAmount(), 84.50);
        t_account.transferOut(t_acc2, 14.50);
        assertEquals("account has transferred 14.50 to account #2", t_account.getAmount(), 85.5);
        assertNotNull("account has logs", t_account.getLog());
    }

    public void testClose() {
        t_account.closeAccount();
        assertFalse("account is closed", t_account.isOpen());
    }
}
