package cse110.com.goldencash;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ADMV on 11/6/2014.
 */
public class SigninActivityTest extends ActivityInstrumentationTestCase2<SigninActivity> {

    private SigninActivity t_signin;
    Button t_signinButton;
    Button t_signupButton;
    EditText t_username_field;
    EditText t_password_field;

    public SigninActivityTest() {
        super(SigninActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        final String GIVEN_USER = "user";
        final String GIVEN_PASS = "pass";
        super.setUp();
        setActivityInitialTouchMode(false);
        t_signin = getActivity();
        t_signinButton = (Button) t_signin.findViewById(R.id.signin_button);
        t_signupButton = (Button) t_signin.findViewById(R.id.signup_button);
        t_username_field = (EditText) t_signin.findViewById(R.id.usernameField);
        t_password_field = (EditText) t_signin.findViewById(R.id.passwordField);
        testPreconditions();
        // testButtons_clicking();
        //testButtons_view(); // asserts false
        addString("user", t_username_field);
        addString("pass", t_password_field);
        testStrings(GIVEN_USER, t_username_field.getText().toString().trim());
        testStrings(GIVEN_PASS, t_password_field.getText().toString().trim());
    }

    public void testPreconditions() {
        assertNotNull("signin is not null", t_signin);
        assertNotNull("signin button is not null", t_signinButton);
        assertNotNull("signup button is not null", t_signupButton);
        assertNotNull("edittext username is not null", t_username_field);
        assertNotNull("edittext password is not null", t_password_field);
    }

    @UiThreadTest
    public void testButtons_view() {
        final ViewGroup.LayoutParams signin_layout =
                t_signinButton.getLayoutParams();
        assertNotNull(signin_layout);
        assertEquals(signin_layout.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(signin_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);

        final ViewGroup.LayoutParams signup_layout =
                t_signupButton.getLayoutParams();
        assertNotNull(signup_layout);
        assertEquals(signup_layout.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(signup_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }
        /* ERROR: Can't test button clicks. Lack INJECT_EVENTS permission */
    @UiThreadTest
    public void testButtons_clicking() {
        String expectedInfoText = String.valueOf(R.string.signin_text);
        TouchUtils.clickView(this, t_signinButton);
        assertTrue(View.VISIBLE == t_signinButton.getVisibility());
        assertEquals(expectedInfoText, t_signinButton.getText());
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

    public void testStrings(String given, String expected) {
        assertEquals(given, expected);
    }
}
