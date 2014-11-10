package cse110.com.goldencash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 11. Add a spinning progress bar (and make sure it's off)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);

        setContentView(R.layout.activity_userlist);
        listView = (ListView)findViewById(R.id.listView_userlist);
        listView.setOnItemClickListener(this);

        queryUsers();
    }

    //query limit to 100 at most one time.
    private void queryUsers(){
        setProgressBarIndeterminateVisibility(true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null){

                    setupAdapter(parseObjects);
                }else{
                    setProgressBarIndeterminateVisibility(false);
                    Log.d(getString(R.string.debugInfo_text),"usernames error");
                    //TODO:display error message/networking
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

        Intent intent = new Intent(this, AccountsActivity.class);
        ParseObject po = parseObjects.get(i).getParseObject("account");
        String id = po.getObjectId();
        intent.putExtra("userID", id);
        startActivity(intent);
    }
}
