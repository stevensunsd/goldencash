package cse110.com.goldencash;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Xin Wen on 11/20/14.
 */
public class TransactionActivity extends Activity {

    protected Spinner spinnerFrom, spinnerTo;
    protected Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 11. Add a spinning progress bar (and make sure it's off)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);

        //set title
        setTitle("New Transaction");

        setContentView(R.layout.activity_transaction);

        addItemsOnSpinners();

        addListenerOnButton();

        addListenerOnSpinnerItemSelection();


    }
    private void addItemsOnSpinners(){
        spinnerFrom = (Spinner) findViewById(R.id.spinner_transaction_from);
        spinnerTo = (Spinner) findViewById(R.id.spinner_transaction_to);

        ArrayList list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
    }
    private void addListenerOnButton(){
        button = (Button)findViewById(R.id.button_confirm_transaction);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getString(R.string.debugInfo_text),""+spinnerFrom.getSelectedItem()+spinnerTo.getSelectedItem());
            }
        });

    }
    private void addListenerOnSpinnerItemSelection(){

    }

}
