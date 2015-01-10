package com.example.jonathanelliott.financialsample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;

import com.crazy88.financialsample.support.Account;
import com.crazy88.financialsample.support.DatabaseHandler;
import com.crazy88.financialsample.support.User;

import java.util.ArrayList;
import java.util.List;


public class UserHomeActivity extends ActionBarActivity {

    private Intent mainActivity;
    private Intent transferActivity;
    private Intent createAccountActivity;
    private Intent accountViewActivity;

    private TextView userAccountText;

    private User currentUser;
    private DatabaseHandler db;

    private ListView listView;
    private List<Account> accountList;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_activity);

        //The Account Text at the top
        userAccountText =  (TextView) findViewById(R.id.homeTextView);

        //this is the list that fills up with User Accounts hopefully
        listView = (ListView) findViewById(R.id.homeListView);


        //Setting up all the activities/Intents
        mainActivity = new Intent(this, MainActivity.class);
        transferActivity = new Intent(this, TransferActivity.class);
        createAccountActivity = new Intent(this, CreateAccountActivity.class);
        accountViewActivity = new Intent(this, AccountActivity.class);

        //Set up the database Handler (This is only used once to get the current user).
        db = new DatabaseHandler(this);

        //This is a bad, but This little bit of code gets the current user that is logged in
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        currentUser = db.getUserByUsername(username);

        //This code sets the Logout Button to go back to the Main Page
        findViewById(R.id.homeLogoutButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Closing all the Activities
                        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Add new Flag to start new Activity
                        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(mainActivity);

                    }
                });

        //This code sets the Transfers Button to go to the Transfers activity
        findViewById(R.id.homeTransfersButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(transferActivity);

                    }
                });

        //This code sets the Add Account Button to go to the Create Account activity.
        findViewById(R.id.homeAddAccountButton).setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View v) {
                     //   Intent intent = getIntent();
                     //   String username = intent.getExtras().getString("username");
                     //   User currentUser = db.getUserByUsername(username);
                        if(currentUser != null) {
                            createAccountActivity.putExtra("username", currentUser.getUsername());
                            startActivity(createAccountActivity);
                        }

                    }
                });


        //Set the text to the current user's username
        userAccountText.setText(username + "'s Accounts");

        //Get all of the User's Accounts
        accountList = db.getAllAccountsByUsername(username);
        List<String> stringList = new ArrayList<String>();

        //If there exist Accounts linked to this user
        if(!accountList.isEmpty()) {
            //Create the string list (used to populate the listview)
            for (Account a : accountList) {
                stringList.add(a.getAccountName());
            }
            //Array Adapter adapts an arraylist for use in an Android list view
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    stringList);

            //Set both the adapter (show the accounts).
            //and set the onClickListener (when you click the account it goes to that account page)
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Account selectedAccount = accountList.get(position);
                    String[] userAndAccount = {currentUser.getUsername(), selectedAccount.getAccountName() };
                    accountViewActivity.putExtra("user_account", userAndAccount);
                   startActivity(accountViewActivity);

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
