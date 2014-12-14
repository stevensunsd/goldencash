package cse110.com.goldencash;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.parse.Parse;
import com.parse.ParseUser;

import cse110.com.goldencash.AppActivity.TransactionActivity;
import cse110.com.goldencash.modelAccount.Account;
import cse110.com.goldencash.modelAccount.Rule;
import cse110.com.goldencash.modelUser.User;

public class TransactionActivityTest extends ActivityInstrumentationTestCase2<TransactionActivity> {

    private TransactionActivity t_transact;
    Button t_confirm;
    Button t_transfer;

    User t_user;
    private Account t_debit;
    private Account t_credit;
    private Account t_saving;

    private Account sourceAccount = t_debit;
    private Rule t_rule = new Rule();
    private String t_email;

    public static final int DPVALUE = -2;
    public static final String MAIN_STRING = "1XwyXTQlIQDuwcjETTTmvEaysvJVZLsSasuxibY3";
    public static final String CLIENT_STRING = "hQIEN0MfhYKiBPCHsPZ6djei7myOjcRpX56Cd4Xc";
    public static final String EMAIL_ADDR = "velocity@cceleration.com";

    public TransactionActivityTest() {
        super(TransactionActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        setActivityInitialTouchMode(true);
        t_transact = getActivity();
        Parse.initialize(t_transact, MAIN_STRING, CLIENT_STRING);
        ParseUser.enableAutomaticUser();

        t_user = new User();

        t_confirm = (Button) t_transact.findViewById(R.id.button_confirm_transaction);
        t_transfer = (Button) t_transact.findViewById(R.id.button_transfer_to_account);
        t_email = "";
        t_debit = t_user.getAccount2("Debit");
        t_credit = t_user.getAccount2("Credit");
        t_saving = t_user.getAccount2("Saving");
    }

    public void testPreconditions() {
        assertNotNull("transact is not null", t_transact);
        assertNotNull("confirm button is not null", t_confirm);
        assertNotNull("transfer button is not null", t_transfer);
        assertNotNull("email string is not null", t_email);
        assertNotNull("transact is not null", t_transact);
        assertNotNull("user is not null", t_user);
        assertNotNull("debit account is not null", t_debit);
        assertNotNull("credit account is not null", t_credit);
        assertNotNull("saving account is not null", t_saving);

    }

    @UiThreadTest
    public void testButtons_view() {
        final ViewGroup.LayoutParams confirm_layout =
                t_confirm.getLayoutParams();
        assertNotNull(confirm_layout);
        assertEquals(confirm_layout.width, DPVALUE);
        assertEquals(confirm_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);

        final ViewGroup.LayoutParams transfer_layout =
                t_transfer.getLayoutParams();
        assertNotNull(transfer_layout);
        assertEquals(transfer_layout.width, DPVALUE);
        assertEquals(transfer_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void testButtons_clicking() {
        final Button confirmBtn =
                (Button) t_confirm
                        .findViewById(R.id.button_confirm_transaction);
        final Button transferBtn =
                (Button) t_transfer
                        .findViewById(R.id.button_transfer_to_account);

        t_transact.runOnUiThread(
                new Runnable() {
                    public void run() {
                        confirmBtn.performClick();
                        transferBtn.performClick();
                    }
                }
        );
    }

    public void testMisc() {
        // most of the actual transfer functions are already tested in the AccountTests
        t_email = EMAIL_ADDR;
        assertEquals(t_email, "velocity@cceleration.com");
        assertTrue(t_rule.canTransferToAnother(sourceAccount,t_credit,20));
        assertTrue(t_rule.canDeposit(t_saving,15.50).first);
    }

}
