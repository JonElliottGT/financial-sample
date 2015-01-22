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

/**
 * Unfinished
 * TODO: DatePicker
 */
public class HistoryActivity extends ActionBarActivity {

    //ListView
    private ListView listView;

    //Unimplemented Feature
    private DatePicker datePicker;

    //Getting/Retrieving the user and account
    private String[] userAndAccount;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Initialize the DatabaseHandler
        db = new DatabaseHandler(this);

        //Get the Current Account and User and All the Transactions of the User and Account
        Intent intent = getIntent();
        userAndAccount = intent.getExtras().getStringArray("user_account");
        List<Transaction> transactionList = db.getAllTransactionsForUserAndAccount(userAndAccount[0], userAndAccount[1]);

        //Set up the ListView and populate with all transactions
        listView = (ListView) findViewById(R.id.historyView);

        if(!transactionList.isEmpty()) {

            //Create the string list (used to populate the listview)
            List<String> stringList = new ArrayList<>();

            //Set up Date Format and Date
            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = new Date();

            for (Transaction t : transactionList) {
                try{
                    d = iso8601Format.parse(Long.toString(t.getDate()));
                } catch(Exception e) {
                    //Pray there is no exception (very unusual circumstances)
                }
                stringList.add("Memo: " + t.getTransactionName() + " " + t.getType() + " $" + t.getAmount() + " " + d);
            }
            //Array Adapter adapts an arraylist for use in an Android list view
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    stringList);

            //Set the adapter for the array
            listView.setAdapter(arrayAdapter);
        }

    }


}
