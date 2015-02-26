package com.example.jonathanelliott.financialsample;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.Double;
import com.crazy88.financialsample.support.DatabaseHandler;
import com.crazy88.financialsample.support.Account;
import com.crazy88.financialsample.support.User;


public class CreateAccountActivity extends ActionBarActivity {

    //Intents used to go to userHomeActivity and mainActivity
    private Intent userHomeActivity;
    private Intent mainActivity;

    //Current user that is logged in
    private User currentUser;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Initialize the DatabaseHandler
        db = new DatabaseHandler(this);

        //Setting up Intents
        //1. Initialize main activity (logout button)
        mainActivity = new Intent(this, MainActivity.class);

        //2. Intent - UserHomeActivity (used with Create Account Button)
        userHomeActivity = new Intent(this, UserHomeActivity.class);

        //Get the current user that is Logged into the system (via database query)
        Intent intent = getIntent();
        String username = intent.getExtras().getString("username");
        currentUser = db.getUserByUsername(username);

        //Listener - Logout Button (return to the main page)
        findViewById(R.id.createAccountLogout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainActivity);
                        finish();
                    }
                });

        //Listener - Create Account Button (adds account to database)
        findViewById(R.id.createAccountButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        createAccount();

                    }
                });

        findViewById(R.id.createAccountBackButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

    }

    /**
     * Private Method
     * Activates on Create Account Button
     *
     * Creates a new account from the user input and adds it to the database
     * Returns to the main screen if a new account is created or notifies user of error
     */
    private void createAccount() {

        //Getting User Input
        //Initialize the EditText for the created account's name
        EditText editTextAccountName = (EditText) findViewById(R.id.accountNameEditText);

        //Initialize the EditText for the created account's balance
        EditText editTextAccountBalance = (EditText) findViewById(R.id.accountBalanceEditText);

        //Get the account name (via editTextAccountName)
        String accountName = editTextAccountName.getText().toString();

        if(!(editTextAccountBalance.getText().toString().isEmpty())) {
            //Get the account balance (via editTextAccountBalance)
            double accountBalance = Double.parseDouble(editTextAccountBalance.getText().toString());

            if (accountName.length() > 0) {


                //Get the current user's username
                String username = currentUser.getUsername();

                //Create the new account using the User input (account Name, account Balance) and the currentUser
                Account account = new Account(accountName, username, accountBalance);

                if (db.checkAccount(username, accountName)) {

                    //add the new account to the database
                    db.addAccount(account);

                    //Go back home
                    userHomeActivity.putExtra("username", username);
                    startActivity(userHomeActivity);

                } else {
                    Toast.makeText(getApplicationContext(), "Account Name already exists", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Cannot have empty boxes", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Cannot have empty boxes", Toast.LENGTH_SHORT).show();
        }

    }
}
