package cse110.com.goldencash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by ADMV on 11/14/2014.
 */
public class SignupActivityTest extends ActivityInstrumentationTestCase2<SignupActivity> {

    private SignupActivity t_signup;
    Button t_cancelButton;
    Button t_confirmButton;
    boolean t_usernameOk;
    String t_username;
    String t_password1;
    String t_password2;
    String t_firstname;
    String t_lastname;
    String t_saltvalue;
    boolean t_openDebit;
    boolean t_openCredit;
    boolean t_openSaving;

    boolean t_finishTag = false;

    ProgressDialog t_proDialog;

    EditText usertext;
    EditText passtext1;
    EditText passtext2;
    EditText nametext1;
    EditText nametext2;
    CheckBox debitbox;
    CheckBox creditbox;
    CheckBox savingbox;

    public SignupActivityTest() {
        super(SignupActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        final String GIVEN_USER = "USER";
        final String GIVEN_PASS = "pass";
        super.setUp();
        setActivityInitialTouchMode(true);
        t_signup = getActivity();
        t_cancelButton = (Button) t_signup.findViewById(R.id.button_cancel);
        t_confirmButton = (Button) t_signup.findViewById(R.id.button_confirm);
        testPreconditions();
        usertext = (EditText) t_signup.findViewById(R.id.signup_username);
        passtext1 = (EditText) t_signup.findViewById(R.id.signup_password);
        passtext2 = (EditText) t_signup.findViewById(R.id.signup_password2);
        nametext1 = (EditText) t_signup.findViewById(R.id.signup_firstname);
        nametext2 = (EditText) t_signup.findViewById(R.id.signup_lastname);
        creditbox = (CheckBox) t_signup.findViewById(R.id.check_credit);
        debitbox = (CheckBox) t_signup.findViewById(R.id.check_debit);
        savingbox = (CheckBox) t_signup.findViewById(R.id.check_saving);

        addString("USER", usertext);
        addString("pass", passtext1);
        addString("pass", passtext2);
        addString("Albert", nametext1);
        addString("Wily", nametext2);
        //checkBox(debitbox);
        //checkBox(creditbox);
        //checkBox(savingbox);
        testButtons_view();

        testStrings(GIVEN_USER, usertext.getText().toString().trim());
        testStrings(GIVEN_PASS, passtext1.getText().toString().trim());
        testStrings(GIVEN_PASS, passtext2.getText().toString().trim());
    }

    public void testPreconditions() {
        assertNotNull("signup is not null", t_signup);
        assertNotNull("cancel button is not null", t_cancelButton);
        assertNotNull("confirm button is not null", t_confirmButton);
    }

    @UiThreadTest
    public void testButtons_view() {
        final ViewGroup.LayoutParams confirm_layout =
                t_confirmButton.getLayoutParams();
        assertNotNull(confirm_layout);
        assertEquals(confirm_layout.width, 150); // hard-coded 100dp
        assertEquals(confirm_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);

        final ViewGroup.LayoutParams cancel_layout =
                t_cancelButton.getLayoutParams();
        assertNotNull(cancel_layout);
        assertEquals(cancel_layout.width, 150); // hard-coded 100dp
        assertEquals(cancel_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }
    @UiThreadTest
    public void testButtons_clicking() {
       /* Intent intent = new Intent(getInstrumentation().getTargetContext(), SigninActivity.class);
        setActivityIntent(intent);
        final Button continueButton  =
                (Button) getActivity()
                        .findViewById(R.id.signin_button);
       */ //continueButton.performClick();
    }

    @UiThreadTest
    public void addString(final String value, final EditText et) {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                et.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync(value);
        getInstrumentation().waitForIdleSync();
    }

    @UiThreadTest
    public void checkBox(final CheckBox cb) {
        /* BROKEN */
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                cb.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();
        if(cb.isChecked()) cb.setChecked(false);
        else cb.setChecked(true);
        getInstrumentation().waitForIdleSync();
    }

    public void testStrings(String given, String expected) {
        assertEquals(given, expected);
    }
}
