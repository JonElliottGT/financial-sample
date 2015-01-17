package com.example.jonathanelliott.financialsample;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;

import com.crazy88.financialsample.support.Account;
import com.crazy88.financialsample.support.DatabaseHandler;
import com.crazy88.financialsample.support.Transaction;
import com.crazy88.financialsample.support.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HistoryActivity extends ActionBarActivity {

    private Intent accountActivity;
    private String[] userAndAccount;
    private DatabaseHandler db;
    private Account currentAccount;

    private DatePicker datePicker;

    private List<Transaction> transactionList;
    private ListView listView;

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
        transactionList = db.getAllTransactionsForUserAndAccount(userAndAccount[0], userAndAccount[1]);
        listView = (ListView) findViewById(R.id.historyView);
        if(!transactionList.isEmpty()) {
            //Create the string list (used to populate the listview)
            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = new Date();
            List<String> stringList = new ArrayList<>();
            for (Transaction t : transactionList) {
                try{
                    d = iso8601Format.parse(Long.toString(t.getDate()));
                } catch(Exception e) {

                }
                stringList.add("Memo: " + t.getTransactionName() + " " + t.getType() + " $" + t.getAmount() + " " + d);
            }
            //Array Adapter adapts an arraylist for use in an Android list view
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    stringList);

            //Set both the adapter (show the accounts).
            //and set the onClickListener (when you click the account it goes to that account page)
            listView.setAdapter(arrayAdapter);
        }



        accountActivity = new Intent(this, AccountActivity.class);


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
