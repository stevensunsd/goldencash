package cse110.com.goldencash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xin Wen on 11/9/14.
 */
public class UserListActivity extends Activity
        implements AdapterView.OnItemClickListener {

    ListView listView;

    ArrayAdapter<String> adapter;
    List<ParseObject> parseObjects;

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 11. Add a spinning progress bar (and make sure it's off)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);

        //set title
        setTitle("Welcome,Teller "+user.getUserName());

        setContentView(R.layout.activity_userlist);
        listView = (ListView)findViewById(R.id.listView_userlist);
        listView.setOnItemClickListener(this);

        queryUsers();
    }

    //query limit to 100 at most one time.
    private void queryUsers(){
        setProgressBarIndeterminateVisibility(true);
        ParseQuery query = ParseUser.getQuery();
        query.whereEqualTo("admin",false);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null){
                    setupAdapter(parseObjects);
                }else{
                    Log.d(getString(R.string.debugInfo_text),e.getMessage());
                    Toast.makeText(getApplicationContext(), "Network error, please try again.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setupAdapter(List<ParseObject> parseObjects){
        int i = 0;
        ArrayList<String> usernames = new ArrayList<String>();
        this.parseObjects = parseObjects;
        for(ParseObject oneObject : parseObjects){
            usernames.add(i,oneObject.getString("username"));

            Log.d(getString(R.string.debugInfo_text),"username: "+usernames.get(i));
            i++;
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                android.R.id.text1,usernames);
        listView.setAdapter(adapter);
        //stop the progress bar
        setProgressBarIndeterminateVisibility(false);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String username = parseObjects.get(i).getString("username");
        String userId = parseObjects.get(i).getObjectId();
        Intent intent = new Intent(this, AccountsActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        //TODO:Show up log out dialog
        AdminLogOut();
    }

    private void AdminLogOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setPositiveButton("Log Out",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        User.getCurrentUser().logOut();
                        gotoSigninActtivity();
                    }
                });
        builder.setNegativeButton("Cancel",null);
        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }
    private void gotoSigninActtivity(){
        Intent intent = new Intent(this,SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
