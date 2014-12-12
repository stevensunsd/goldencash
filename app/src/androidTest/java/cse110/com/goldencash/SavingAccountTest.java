package cse110.com.goldencash;

import android.test.AndroidTestCase;

import com.parse.Parse;
import com.parse.ParseUser;

import cse110.com.goldencash.modelAccount.SavingAccount;

/**
 * Created by ADMV on 11/16/2014.
 */

/* Change due to interface-d class */
public class SavingAccountTest extends AndroidTestCase {

    private SavingAccount t_account;
    private SavingAccount t_acc2;

    public static final String MAIN_STRING = "1XwyXTQlIQDuwcjETTTmvEaysvJVZLsSasuxibY3";
    public static final String CLIENT_STRING = "hQIEN0MfhYKiBPCHsPZ6djei7myOjcRpX56Cd4Xc";
    public static final double INITIAL_FUNDS = 100.1;

    public SavingAccountTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Parse.initialize(getContext(), MAIN_STRING, CLIENT_STRING);
        ParseUser.enableAutomaticUser();

        t_account = new SavingAccount();
        t_acc2 = new SavingAccount();
        t_account.put("openSaving", true);
        t_account.put("Saving", 23.49);
        t_account.put("accountnumber", "7692");
        t_acc2.put("openSaving", true);
        t_acc2.put("Saving", 45.0);
        t_acc2.put("accountnumber", "2348");
    }

    public void testPreconditions() {
        assertNotNull("account is not null", t_account);
        assertTrue("account is open", t_account.isOpen());
        assertEquals("account has funds", t_account.getAmount(), 23.49);
        assertNotNull("account number exists", t_account.getAccountNumber());
        assertEquals("account is saving", t_account.getAccounttype(), "Saving");
        assertNull("account log is null", t_account.getLog());
    }

    public void testTransfers() {
        t_account.withdraw(23.49);
        assertEquals("account has withdrawn 23.49 units", t_account.getAmount(), 0.0);
        t_account.deposit(100.10);
        assertEquals("account has deposited 100.10 units", t_account.getAmount(), 100.10);
        //t_account.transfer("Saving", 15.50);
        //assertEquals("account has transferred 15.50 to anonymous account",
         //       t_account.getAmount(), 84.50);
        t_account.transferOut(t_acc2, 5.00);
        assertEquals("account has transferred 95.10 to account #22", t_account.getAmount(), 95.10);
        assertNotNull("account has logs", t_account.getLog());
    }

    public void testClose() {
        t_account.closeAccount();
        assertFalse("account is closed", t_account.isOpen());
    }
}
