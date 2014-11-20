package cse110.com.goldencash;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by Xin Wen on 11/20/14.
 */
public class TransactionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 11. Add a spinning progress bar (and make sure it's off)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);

        //set title
        setTitle("Transaction");

        setContentView(R.layout.activity_transaction);

    }


}
