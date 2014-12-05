package cse110.com.goldencash;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ADMV on 11/9/2014.
 */
public class SigninActivityTest extends ActivityInstrumentationTestCase2<SigninActivity> {

    private SigninActivity t_signin;
    Button t_signinButton;
    Button t_signupButton;
    Button t_forgotButton;
    EditText t_username_field;
    EditText t_password_field;

    public static final String GIVEN_USER = "USER";
    public static final String GIVEN_PASS = "pass";
    public static final int DPVALUE = 144;
    public static final int FORGETVALUE = -2;

    public SigninActivityTest() {
        super(SigninActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        t_signin = getActivity();

        t_signinButton = (Button) t_signin.findViewById(R.id.signin_button);
        t_signupButton = (Button) t_signin.findViewById(R.id.signup_button);
        t_forgotButton = (Button) t_signin.findViewById(R.id.button_reset_password);
        t_username_field = (EditText) t_signin.findViewById(R.id.usernameField);
        t_password_field = (EditText) t_signin.findViewById(R.id.passwordField);
    }

    public void testPreconditions() {
        assertNotNull("signin is not null", t_signin);
        assertNotNull("signin button is not null", t_signinButton);
        assertNotNull("signup button is not null", t_signupButton);
        assertNotNull("reset button is not null", t_forgotButton);
        assertNotNull("edittext username is not null", t_username_field);
        assertNotNull("edittext password is not null", t_password_field);
        assertEquals("username field is empty", "", t_username_field.getText().toString().trim());
        assertEquals("password field is empty", "", t_password_field.getText().toString().trim());
    }

    @UiThreadTest
    public void testButtons_view() {
        final ViewGroup.LayoutParams signin_layout =
                t_signinButton.getLayoutParams();
        assertNotNull(signin_layout);
        assertEquals(signin_layout.width, DPVALUE);
        assertEquals(signin_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);

        final ViewGroup.LayoutParams signup_layout =
                t_signupButton.getLayoutParams();
        assertNotNull(signup_layout);
        assertEquals(signup_layout.width, DPVALUE);
        assertEquals(signup_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);

        final ViewGroup.LayoutParams forgot_layout =
                t_forgotButton.getLayoutParams();
        assertNotNull(forgot_layout);
        assertEquals(forgot_layout.width, FORGETVALUE);
        assertEquals(forgot_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void testButtons_clicking() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(SignupActivity.class.getName(), null, false);
        Instrumentation.ActivityMonitor activityMonitor2 = getInstrumentation().addMonitor(ResetPasswordActivity.class.getName(), null, false);

        t_signin.runOnUiThread(
                new Runnable() {
                    public void run() {
                        t_signupButton.performClick();
                        t_signinButton.performClick();
                    }
                }
        );

        // assertNotNull(getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000));
        (getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000)).finish();
        t_signin.runOnUiThread(
                new Runnable() {
                    public void run() {
                        t_forgotButton.performClick();
                    }
                }
        );
        (getInstrumentation().waitForMonitorWithTimeout(activityMonitor2, 5000)).finish();
    }

    public void testStrings() {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                t_username_field.setText("USER", TextView.BufferType.EDITABLE);
                t_password_field.setText("pass", TextView.BufferType.EDITABLE);
            }
        });

        assertEquals(GIVEN_USER, t_username_field.getText().toString().trim());
        assertEquals(GIVEN_PASS, t_password_field.getText().toString().trim());
    }
}
