package com.example.jonathanelliott.financialsample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.crazy88.financialsample.support.Account;
import com.crazy88.financialsample.support.DatabaseHandler;
import com.crazy88.financialsample.support.User;

import java.util.ArrayList;
import java.util.List;


public class UserHomeActivity extends ActionBarActivity {

    private Intent mainActivity;
    private Intent createAccountActivity;
    private Intent accountViewActivity;
    private Intent transactionActivity;

    //Account List
    private List<Account> accountList;

    //The current User's username
    private String username;


    //private User currentUser;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        //Set up the database Handler
        db = new DatabaseHandler(this);

        //get the current user that is logged into the system (via db query)
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        //Setting up all the activities/Intents
        mainActivity = new Intent(this, MainActivity.class);
        createAccountActivity = new Intent(this, CreateAccountActivity.class);
        accountViewActivity = new Intent(this, AccountActivity.class);
        transactionActivity = new Intent(this, CreateTransactionActivity.class);

        //Set the Account Text to the current user's name(at the top of the screen)
        TextView userAccountText =  (TextView) findViewById(R.id.homeTextView);
        userAccountText.setText(username + "'s Accounts");

        //Initialize and Populate the List View (User's Accounts)
        ListView listView = (ListView) findViewById(R.id.homeListView);

        //Get all of the User's Accounts (via db query)
        accountList = db.getAllAccountsByUsername(username);

        //If there exist Accounts linked to this user
        if(!accountList.isEmpty()) {
            //Create the string list (used to populate the listview)
            List<String> stringList = new ArrayList<String>();
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
                    String[] userAndAccount = {username, selectedAccount.getAccountName()};
                    accountViewActivity.putExtra("user_account", userAndAccount);
                    startActivity(accountViewActivity);

                }
            });
        }

        //This code sets the Logout Button to go back to the Main Page
        findViewById(R.id.homeLogoutButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainActivity);

                    }
                });

        //This code sets the Add Account Button to go to the Create Account activity.
        findViewById(R.id.homeAddAccountButton).setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View v) {

                        createAccountActivity.putExtra("username", username);
                        startActivity(createAccountActivity);

                    }
                });

            //Transaction Button -> Transaction Activity
        findViewById(R.id.transactionButton).setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View v) {

                        if(!accountList.isEmpty()) {
                            transactionActivity.putExtra("username", username);
                            startActivity(transactionActivity);
                        } else {
                            Toast.makeText(getApplicationContext(), "You do not have any Accounts to make a transaction", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
