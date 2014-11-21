package cse110.com.goldencash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Xin Wen on 11/20/14.
 */
public class TransactionActivity extends Activity{

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

        //set Spinner From
        ArrayList<String> list = new ArrayList<String>();
        list.add("Debit");
        list.add("Saving");
        //list.add("Credit");

        spinnerFrom.setAdapter(createAdapter(list));
        //spinnerTo.setAdapter(adapter);
    }
    private void addListenerOnButton(){
        button = (Button)findViewById(R.id.button_confirm_transaction);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getString(R.string.debugInfo_text),""+spinnerFrom.getSelectedItem()+spinnerTo.getSelectedItem());
                editbox();
            }
        });

    }
    private void addListenerOnSpinnerItemSelection(){

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0){
                    ArrayList<String> list = new ArrayList<String>();
                    list.add("Credit");
                    list.add("Saving");
                    spinnerTo.setAdapter(createAdapter(list));
                }else{
                    ArrayList<String> list = new ArrayList<String>();
                    list.add("Credit");
                    list.add("Debit");
                    spinnerTo.setAdapter(createAdapter(list));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        //spinnerTo.setOnItemSelectedListener(this);
    }

    private ArrayAdapter createAdapter(ArrayList list){
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    protected void editbox() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Amount").setIcon(android.R.drawable.ic_dialog_info).setView(input).setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                String amount = input.getText().toString();
                makeTransaction(Integer.parseInt(amount),spinnerFrom.getSelectedItem().toString(),
                        spinnerTo.getSelectedItem().toString());
            }

        });
        builder.show();
    }

    private void makeTransaction(int amount,String from, String to){

    }
}
