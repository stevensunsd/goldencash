package cse110.com.goldencash;

import android.app.ProgressDialog;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

/**
 * Created by ADMV on 11/4/2014.
 */

// TODO: Broken at the moment. Fixing.
public class AccountsActivityTest extends ActivityInstrumentationTestCase2<AccountsActivity> {
    private AccountsActivity t_accounts;
    private ListView t_listview;
    private boolean t_openCredit;
    private boolean t_openDebit;
    private boolean t_openSaving;
    private float t_credit;
    private float t_debit;
    private float t_saving;

    private Accounts t_account;

    private ProgressDialog t_proDialog;

    public AccountsActivityTest() {
        super(AccountsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {

        super.setUp();
        setActivityInitialTouchMode(false);
        t_accounts = getActivity();
        //t_listview = t_accounts.listview;
        //t_openCredit = t_accounts.openCredit;
        //t_openDebit = t_accounts.openDebit;
        //t_openSaving = t_account.openSaving;
        //t_credit = t_accounts.credit;
        //t_debit = t_account.debit;
        //t_saving = t_account.saving;
        //testPreconditions();
    }

    public void testPreconditions() {
        //assertNotNull("accounts is not null", t_accounts);
        //assertNotNull("listview button is not null", t_listview);
        //assertNotNull("opendebit username is not null", t_openDebit);
        //assertNotNull("opencredit password is not null", t_openCredit);
        //assertNotNull("opensaving button is not null", t_openSaving);
        //assertNotNull("debit username is not null", t_debit);
        //assertNotNull("credit password is not null", t_credit);
        //assertNotNull("saving button is not null", t_saving);
        //assertNotNull("saving button is not null", t_account);
    }
}
