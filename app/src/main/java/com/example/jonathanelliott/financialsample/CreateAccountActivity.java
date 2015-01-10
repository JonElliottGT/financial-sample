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

    private String accountName;
    private double accountBalance;

    private EditText editTextAccountName;
    private EditText editTextAccountBalance;

    private DatabaseHandler db;

    private Intent userHomeActivity;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //setting up Intents
        userHomeActivity = new Intent(this, UserHomeActivity.class);

        //setting up edit text inputs
        editTextAccountName = (EditText) findViewById(R.id.accountNameEditText);
        editTextAccountBalance = (EditText) findViewById(R.id.accountBalanceEditText);

        //database is used to add a new account to the database
        db = new DatabaseHandler(this);

        findViewById(R.id.createAccountButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        createAccount();

                    }
                });

        //This gets the current user in that is logged in (not the best way of doing it).
        Intent intent = getIntent();
        String username = intent.getExtras().getString("username");
        currentUser = db.getUserByUsername(username);

    }

    private void createAccount(){

        accountName = editTextAccountName.getText().toString();
        accountBalance = Double.parseDouble(editTextAccountBalance.getText().toString());

        //very basic criteria
        if (accountName.length() > 0) {

            String username = currentUser.getUsername();

            Account account = new Account(accountName, username, accountBalance);
            if(db.checkAccount(username, accountName)) {
                db.addAccount(account);

                userHomeActivity.putExtra("username", username);
                startActivity(userHomeActivity);
            } else {
                Toast.makeText(getApplicationContext(), "Account Name already exists", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Cannot have empty boxes", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_account, menu);
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
