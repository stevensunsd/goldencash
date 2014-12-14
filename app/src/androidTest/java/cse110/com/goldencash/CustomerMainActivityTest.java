package cse110.com.goldencash;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseUser;

import cse110.com.goldencash.AppActivity.CustomerMainActivity;
import cse110.com.goldencash.modelAccount.Account;
import cse110.com.goldencash.modelUser.User;

public class CustomerMainActivityTest extends ActivityInstrumentationTestCase2<CustomerMainActivity> {

    CustomerMainActivity t_custom;
    String t_key;
    TextView t_number;

    User t_user;
    private Account t_debit;
    private Account t_credit;
    private Account t_saving;

    public static final String MAIN_STRING = "1XwyXTQlIQDuwcjETTTmvEaysvJVZLsSasuxibY3";
    public static final String CLIENT_STRING = "hQIEN0MfhYKiBPCHsPZ6djei7myOjcRpX56Cd4Xc";
    public static final String FINAL_KEY = "ASDFIOUXCOVIUXVC";
    public static final String FINAL_NUMBER = "Account Number: 10";

    public CustomerMainActivityTest() {
        super(CustomerMainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //startActivity(new Intent(getInstrumentation().getTargetContext(), StatementsActivity.class), null, null);
        t_custom = getActivity();
        Parse.initialize(t_custom, MAIN_STRING, CLIENT_STRING);
        ParseUser.enableAutomaticUser();

        t_number = (TextView) t_custom.findViewById(R.id.customer_main_account_number);

        t_user = new User();
        t_debit = t_user.getAccount("Debit");
        t_credit = t_user.getAccount("Credit");
        t_saving = t_user.getAccount("Saving");

    }

    // Just making sure all the values are initialized. Most of the functions merely extend
    // functionality of the user/account classes
    public void testPreconditions() {
        assertNotNull("customer activity exists", t_custom);
        assertNotNull("user exists", t_user);
        assertNull("key is null", t_key);
        assertNull("number is null", t_number);

    }

    public void testStrings() {
        t_key = "ASDFIOUXCOVIUXVC";
        assertEquals(t_key, FINAL_KEY);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                t_number.setText("Account Number: " + 10, TextView.BufferType.EDITABLE);
            }
        });

        assertEquals(FINAL_NUMBER, t_number.getText().toString().trim());
    }
}
