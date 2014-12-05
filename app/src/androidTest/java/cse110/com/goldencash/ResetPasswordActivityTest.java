package cse110.com.goldencash;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ADMV on 12/4/2014.
 */
public class ResetPasswordActivityTest extends ActivityInstrumentationTestCase2<ResetPasswordActivity> {

    private ResetPasswordActivity t_reset;
    Button t_cancelButton;
    Button t_confirmButton;

    EditText email;

    public static final String GIVEN_EMAIL = "robotm@ster.com";
    public static final int DPVALUE = -2;
    public static final int EPVALUE = 150;


    public ResetPasswordActivityTest() {
        super(ResetPasswordActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        t_reset = getActivity();
        t_cancelButton = (Button) t_reset.findViewById(R.id.button_rp_cancel);
        t_confirmButton = (Button) t_reset.findViewById(R.id.button_rp_confirm);
        email = (EditText) t_reset.findViewById(R.id.rp_email);
    }

    public void testPreconditions() {
        assertNotNull("reset is not null", t_reset);
        assertNotNull("cancel button is not null", t_cancelButton);
        assertNotNull("confirm button is not null", t_confirmButton);
        assertEquals("email field is empty", "", email.getText().toString().trim());
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
        assertEquals(cancel_layout.width, EPVALUE);
        assertEquals(cancel_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void testButtons_clicking() {
        //Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(SigninActivity.class.getName(), null, false);

        t_reset.runOnUiThread(
                new Runnable() {
                    public void run() {
                        t_cancelButton.performClick();
                        //t_confirmButton.performClick(); // does not work properly... not sure why

                    }
                }
        );
    }

    public void testStrings() {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                email.setText(GIVEN_EMAIL, TextView.BufferType.EDITABLE);
            }
        });

        assertEquals(GIVEN_EMAIL, email.getText().toString().trim());
    }
}
