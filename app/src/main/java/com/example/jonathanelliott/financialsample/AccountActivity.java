package com.example.jonathanelliott.financialsample;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.crazy88.financialsample.support.Account;
import com.crazy88.financialsample.support.DatabaseHandler;


public class AccountActivity extends ActionBarActivity {

    //Intents used to go to the main and history activity
    private Intent mainActivity;
    private Intent historyActivity;

    //For retrieving/sending the username and account to the next activity
    private String[] userAndAccount;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Initialize DatabaseHandler
        db = new DatabaseHandler(this);

        //Setting up Intents
        //1. Initialize main activity (logout button)
        mainActivity = new Intent(this, MainActivity.class);

        //2. Initialize historyActivity (transaction history button)
        historyActivity = new Intent(this, HistoryActivity.class);

        //Get the current account (via db using the username and the account name(since account stores username)
        Intent intent = getIntent();
        userAndAccount = intent.getExtras().getStringArray("user_account");
        Account currentAccount = db.getAccountByUsernameAndAccountName(userAndAccount[0], userAndAccount[1]);

        //Set the Account Name TextView to the current account's name
        TextView textViewTitle = (TextView) findViewById(R.id.savingTitle);
        textViewTitle.setText(currentAccount.getAccountName());

        //Set the Account balance TextView to the current account's balance
        TextView textViewBalance = (TextView) findViewById(R.id.amountInput);
        textViewBalance.setText("$"+Double.toString(currentAccount.getAccountBalance()));

        //Set the Account id TextView to the current account's id
        TextView textViewId = (TextView) findViewById(R.id.accNoInput);
        textViewId.setText(Long.toString(currentAccount.getId()));

        //Listener - Logout Button (return to main page)
        findViewById(R.id.accountLogout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainActivity);
                        finish();
                    }
                }
        );

        //Listener - Transaction History button (Go to History Activity)
        findViewById(R.id.historyBt).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Send the history activity the username and account
                        historyActivity.putExtra("user_account", userAndAccount);
                        startActivity(historyActivity);
                    }
                }
        );
        //Listener - goes back the userHomeActivity
        findViewById(R.id.accBackButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();

                    }
                }
        );


    }

}
