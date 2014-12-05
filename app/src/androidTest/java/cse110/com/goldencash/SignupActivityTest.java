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
import android.widget.TextView;

/**
 * Created by ADMV on 11/14/2014.
 */
public class SignupActivityTest extends ActivityInstrumentationTestCase2<SignupActivity> {

    private SignupActivity t_signup;
    Button t_cancelButton;
    Button t_confirmButton;

    boolean t_finishTag = false;

    EditText usertext;
    EditText passtext1;
    EditText passtext2;
    EditText nametext1;
    EditText nametext2;
    EditText email;
    CheckBox debitbox;
    CheckBox creditbox;
    CheckBox savingbox;

    public static final String GIVEN_USER = "USER";
    public static final String GIVEN_PASS = "pass";
    public static final String GIVEN_FIRST = "Albert";
    public static final String GIVEN_LAST = "Wily";
    public static final String GIVEN_EMAIL = "robom@ster.com";
    public static final int DPVALUE = 150;

    public SignupActivityTest() {
        super(SignupActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        t_signup = getActivity();
        t_cancelButton = (Button) t_signup.findViewById(R.id.button_cancel);
        t_confirmButton = (Button) t_signup.findViewById(R.id.button_confirm);
        usertext = (EditText) t_signup.findViewById(R.id.signup_username);
        passtext1 = (EditText) t_signup.findViewById(R.id.signup_password);
        passtext2 = (EditText) t_signup.findViewById(R.id.signup_password2);
        nametext1 = (EditText) t_signup.findViewById(R.id.signup_firstname);
        nametext2 = (EditText) t_signup.findViewById(R.id.signup_lastname);
        email = (EditText) t_signup.findViewById(R.id.signup_email);
        creditbox = (CheckBox) t_signup.findViewById(R.id.check_credit);
        debitbox = (CheckBox) t_signup.findViewById(R.id.check_debit);
        savingbox = (CheckBox) t_signup.findViewById(R.id.check_saving);
    }

    public void testPreconditions() {
        assertNotNull("signup is not null", t_signup);
        assertNotNull("cancel button is not null", t_cancelButton);
        assertNotNull("confirm button is not null", t_confirmButton);
        assertEquals("usertext field is empty", "", usertext.getText().toString().trim());
        assertEquals("passtext1 field is empty", "", passtext1.getText().toString().trim());
        assertEquals("passtext2 field is empty", "", passtext2.getText().toString().trim());
        assertEquals("nametext1 field is empty", "", nametext1.getText().toString().trim());
        assertEquals("nametext2 field is empty", "", nametext2.getText().toString().trim());
        assertEquals("email field is empty", "", email.getText().toString().trim());
        assertEquals("debit box is not checked", debitbox.isChecked(), false);
        assertEquals("credit box is not checked", creditbox.isChecked(), false);
        assertEquals("saving box is not checked", savingbox.isChecked(), false);
    }

    @UiThreadTest
    public void testButtons_view() {
        final ViewGroup.LayoutParams confirm_layout =
                t_confirmButton.getLayoutParams();
        assertNotNull(confirm_layout);
        assertEquals(confirm_layout.width, DPVALUE);
        assertEquals(confirm_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);

        final ViewGroup.LayoutParams cancel_layout =
                t_cancelButton.getLayoutParams();
        assertNotNull(cancel_layout);
        assertEquals(cancel_layout.width, DPVALUE);
        assertEquals(cancel_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void testButtons_clicking() {
        t_signup.runOnUiThread(
                new Runnable() {
                    public void run() {
                        t_confirmButton.performClick();
                        t_cancelButton.performClick();
                    }
                }
        );
    }

    public void testStrings() {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                usertext.setText(GIVEN_USER, TextView.BufferType.EDITABLE);
                passtext1.setText(GIVEN_PASS, TextView.BufferType.EDITABLE);
                passtext2.setText(GIVEN_PASS, TextView.BufferType.EDITABLE);
                nametext1.setText(GIVEN_FIRST, TextView.BufferType.EDITABLE);
                nametext2.setText(GIVEN_LAST, TextView.BufferType.EDITABLE);
                email.setText(GIVEN_EMAIL, TextView.BufferType.EDITABLE);
            }
        });

        assertEquals(GIVEN_USER, usertext.getText().toString().trim());
        assertEquals(GIVEN_PASS, passtext1.getText().toString().trim());
        assertEquals(GIVEN_PASS, passtext2.getText().toString().trim());
        assertEquals(GIVEN_FIRST, nametext1.getText().toString().trim());
        assertEquals(GIVEN_LAST, nametext2.getText().toString().trim());
        assertEquals(GIVEN_EMAIL, email.getText().toString().trim());
    }

    public void testCheckBoxes() {
        t_signup.runOnUiThread(
                new Runnable() {
                    public void run() {
                        creditbox.setChecked(false);
                        debitbox.setChecked(true);
                        savingbox.setChecked(true);
                    }
                }
        );
        boolean credit = ((CheckBox)t_signup.findViewById(R.id.check_credit)).isChecked();
        boolean debit = ((CheckBox)t_signup.findViewById(R.id.check_debit)).isChecked();
        boolean saving = ((CheckBox)t_signup.findViewById(R.id.check_saving)).isChecked();

        assertSame(debit, saving);
        assertEquals(credit, false);
    }
}
