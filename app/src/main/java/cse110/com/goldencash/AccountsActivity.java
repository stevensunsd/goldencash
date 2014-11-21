package cse110.com.goldencash;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

//Our class
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by Xin Wen on 10/27/14.
 */
public class AccountsActivity extends Activity{

    ListView listview;

    private User user = new User();
    private Account account = user.getAccount();

    protected ArrayAdapter<String> adapter;

    private void refreshData() {
        Intent intent = new Intent(AccountsActivity.this, AccountsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
                   /*
                    adapter.clear();
                    adapter.addAll(setAdapterarray());
                    adapter.notifyDataSetChanged();
                    listview.invalidate();
                    Log.d(getString(R.string.debugInfo_text),"Done refresh.");
                } else {
                    //TODO: error alert
                }
            }
        });
        */
    }

    private ArrayList<String> setAdapterarray(){
        ArrayList<String> account_list=new ArrayList<String>();
        String stringCredit = "Credit Account\nAvailable Balance:" + account.getCredit();
        String stringSaving = "Saving Account\nAvailable Balance:" + account.getSaving();
        String stringDebit = "Debit Account\nAvailable Balance:" + account.getDebit();
        if(account.isOpenDebit()){
            account_list.add(stringDebit);
        }
        if(account.isOpenCredit()){
            account_list.add(stringCredit);
        }
        if(account.isOpenSaving()){
            account_list.add(stringSaving);
        }
        return account_list;
    }

    private void setAdapter(){
        ArrayList<String> data = setAdapterarray();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, data);
        // Assign adapter to ListView
        listview.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 11. Add a spinning progress bar (and make sure it's off)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);
        //set title
        //setTitle(getIntent().getExtras().getString("username"));
        //set view
        setContentView(R.layout.activity_accounts);

        //Intent intent = getIntent();
        //String id = intent.getExtras().getString("userID");

        //get listview from XML
        listview = (ListView) findViewById(R.id.listView_accounts);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        // Assign adapter to ListView
        // listview.setAdapter(adapter);

        // ListView Item Click Listener
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // add id to give choose about which account you want to change
                editbox(id);
            }
        });
        setAdapter();
    }

    protected void editbox(long id) {
        final EditText input = new EditText(this);
        final int choose = (int)id;
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter The Amount You Want to Change").setIcon(android.R.drawable.ic_dialog_info).setView(input).setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                switch (choose) {
                    case 0:account.setDebit(Double.parseDouble(input.getText().toString())); break;
                    case 1:account.setCredit(Double.parseDouble(input.getText().toString())); break;
                    case 2:account.setSaving(Double.parseDouble(input.getText().toString())); break;
                    default: // error
                }
                refreshData();
            }

        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_transaction) {
            Log.d(getString(R.string.debugInfo_text),"clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}