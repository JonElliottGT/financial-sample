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

    private TextView textViewTitle;
    private TextView textViewBalance;

    private DatabaseHandler db;

    private Account currentAccount;
    private String[] userAndAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Set up the Intents
        mainActivity = new Intent(this, MainActivity.class);
        historyActivity = new Intent(this, HistoryActivity.class);

        //Set up the DB
        db = new DatabaseHandler(this);

        //This is a bad, but This little bit of code gets the current account that is being used
        //The account also comes with the user that owns the account (since account stores username)
        Intent intent = getIntent();
        userAndAccount = intent.getExtras().getStringArray("user_account");
        currentAccount = db.getAccountByUsernameAndAccountName(userAndAccount[0], userAndAccount[1]);

        //LogOutButton
        findViewById(R.id.logOutBt).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(mainActivity);
                        finish();
                    }
                }
        );

        //History Activity
        findViewById(R.id.historyBt).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        historyActivity.putExtra("user_account", userAndAccount);
                        startActivity(historyActivity);
                    }
                }
        );

        textViewTitle = (TextView) findViewById(R.id.savingTitle);
        textViewTitle.setText(currentAccount.getAccountName());

        textViewBalance = (TextView) findViewById(R.id.amountInput);
        textViewBalance.setText("$"+Double.toString(currentAccount.getAccountBalance()));

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
