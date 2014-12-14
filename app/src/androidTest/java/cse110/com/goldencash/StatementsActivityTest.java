package cse110.com.goldencash;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseUser;

import cse110.com.goldencash.AppActivity.StatementsActivity;
import cse110.com.goldencash.modelUser.User;

public class StatementsActivityTest extends ActivityInstrumentationTestCase2<StatementsActivity> {

    private StatementsActivity t_states;
    Button t_print;
    TextView t_sview;
    User t_user;

    public static final int DPVALUE = 144;

    public static final String NEW_TEXT = "this is a test";
    public static final String MAIN_STRING = "1XwyXTQlIQDuwcjETTTmvEaysvJVZLsSasuxibY3";
    public static final String CLIENT_STRING = "hQIEN0MfhYKiBPCHsPZ6djei7myOjcRpX56Cd4Xc";

    public StatementsActivityTest() {
        super(StatementsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //startActivity(new Intent(getInstrumentation().getTargetContext(), StatementsActivity.class), null, null);
        t_states = getActivity();
        Parse.initialize(t_states, MAIN_STRING, CLIENT_STRING);
        ParseUser.enableAutomaticUser();


        t_print = (Button) t_states.findViewById(R.id.button_print_statement);
        t_sview = (TextView) t_states.findViewById(R.id.statement_text);
    }

    public void testPreconditions() {
        assertNotNull("statements is not null", t_states);
        assertNotNull("print button is not null", t_print);
        assertNotNull("statement view is not null", t_sview);
    }

    @UiThreadTest
    public void testButtons_view() {
        final ViewGroup.LayoutParams print_layout =
                t_print.getLayoutParams();
        assertNotNull(print_layout);
        assertEquals(print_layout.width, DPVALUE);
        assertEquals(print_layout.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void testButtons_clicking() {
        t_states.runOnUiThread(
                new Runnable() {
                    public void run() {
                        t_print.performClick();
                    }
                }
        );
    }

    public void testStrings() {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                t_sview.setText("this is a test", TextView.BufferType.EDITABLE);
            }
        });

        assertEquals(NEW_TEXT, t_sview.getText().toString().trim());
    }
}
