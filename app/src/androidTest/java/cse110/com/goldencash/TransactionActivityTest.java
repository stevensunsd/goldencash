package cse110.com.goldencash;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TransactionActivityTest extends ActivityInstrumentationTestCase2<TransactionActivity> {

    private TransactionActivity t_transact;
    Button t_confirm;
    Button t_transfer;
    // Account t_account; // add later
    boolean t_mode;

    public static final int DPVALUE = -2;

    public TransactionActivityTest() {
        super(TransactionActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        t_transact = getActivity();

        t_confirm = (Button) t_transact.findViewById(R.id.button_confirm_transaction);
        t_transfer = (Button) t_transact.findViewById(R.id.button_transfer_to_account);
    }

    public void testPreconditions() {
        assertNotNull("transact is not null", t_transact);
        assertNotNull("confirm button is not null", t_confirm);
        assertNotNull("tranfser button is not null", t_transfer);
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
}
