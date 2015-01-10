package com.example.jonathanelliott.financialsample;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.crazy88.financialsample.support.Account;
import com.crazy88.financialsample.support.DatabaseHandler;
import com.crazy88.financialsample.support.User;


public class HistoryActivity extends ActionBarActivity {

    private Intent accountActivity;
    private String[] userAndAccount;
    private DatabaseHandler db;
    private Account currentAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = new DatabaseHandler(this);

        //get the current account from the extra user_account that was sent by UserHomeActivity
        //This contains username and password
        Intent intent = getIntent();
        userAndAccount = intent.getExtras().getStringArray("user_account");
        currentAccount = db.getAccountByUsernameAndAccountName(userAndAccount[0], userAndAccount[1]);

        accountActivity = new Intent(this, AccountActivity.class);

        findViewById(R.id.backBt).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        accountActivity.putExtra("user_account", userAndAccount);
                        startActivity(accountActivity);
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
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
