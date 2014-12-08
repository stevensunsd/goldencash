package cse110.com.goldencash;

import android.test.AndroidTestCase;

import com.parse.Parse;
import com.parse.ParseUser;

import cse110.com.goldencash.modelAccount.DebitAccount;

/**
 * Created by ADMV on 11/16/2014.
 */

/* Change due to interface-d class */
public class DebitAccountTest extends AndroidTestCase {

    private DebitAccount t_account;
    private DebitAccount t_acc2;

    public static final String MAIN_STRING = "1XwyXTQlIQDuwcjETTTmvEaysvJVZLsSasuxibY3";
    public static final String CLIENT_STRING = "hQIEN0MfhYKiBPCHsPZ6djei7myOjcRpX56Cd4Xc";
    public static final double INITIAL_FUNDS = 100.1;

    public DebitAccountTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Parse.initialize(getContext(), MAIN_STRING, CLIENT_STRING);
        ParseUser.enableAutomaticUser();

        t_account = new DebitAccount();
        t_acc2 = new DebitAccount();
        t_account.put("openDebit", true);
        t_account.put("Debit", 150.10);
        t_account.put("accountnumber", "123");
        t_acc2.put("openDebit", true);
        t_acc2.put("Debit", 5.0);
        t_acc2.put("accountnumber", "22");
    }

    public void testPreconditions() {
        assertNotNull("account is not null", t_account);
        assertTrue("account is open", t_account.isOpen());
        assertEquals("account has funds", t_account.getAmount(), 150.10);
        assertNotNull("account number exists", t_account.getAccountNumber());
        assertEquals("account is debit", t_account.getAccounttype(), "Debit");
        assertNull("account log is null", t_account.getLog());
    }

    public void testTransfers() {
        t_account.withdraw(90.10);
        assertEquals("account has withdrawn 90.10 units", t_account.getAmount(), 60.0);
        t_account.deposit(5000.0);
        assertEquals("account has deposited 5000 units", t_account.getAmount(), 5060.0);
        //t_account.transfer("Debit", 15.50);
        //assertEquals("account has transferred 15.50 to anonymous account",
         //       t_account.getAmount(), 84.50);
        t_account.transfer(t_acc2, 0.01);
        assertEquals("account has transferred 0.01 to account #22", t_account.getAmount(), 5059.99);
        assertNotNull("account has logs", t_account.getLog());
    }

    public void testClose() {
        t_account.closeAccount();
        assertFalse("account is closed", t_account.isOpen());
    }
}
