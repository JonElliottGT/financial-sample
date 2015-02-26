package com.example.jonathanelliott.financialsample;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crazy88.financialsample.support.DatabaseHandler;
import com.crazy88.financialsample.support.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Note: The TimeZone of the Date can matter. Default is sometimes GMT rather than EST.
 * I think it works. Needs more testing
 */
public class HistoryActivity extends ActionBarActivity {

    //ListView
    private ListView listView;

    //For Logout
    private Intent mainActivity;

    //Array Adapter and the array its adapting (for ListView)
    private List<String> stringList;
    private ArrayAdapter<String> arrayAdapter;

    //Getting/Retrieving the user and account
    private String[] userAndAccount;

    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Initialize the DatabaseHandler
        db = new DatabaseHandler(this);

        //Set up main Activity for logout
        mainActivity = new Intent(this, MainActivity.class);

        //Get the current user and account
        Intent intent = getIntent();
        userAndAccount = intent.getExtras().getStringArray("user_account");

        //Set up the ListView and populate with all transactions
        listView = (ListView) findViewById(R.id.historyView);
        stringList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringList);

        //Set the adapter for the array
        listView.setAdapter(arrayAdapter);

        //Listener - Start Date Button -> Opens DatePickerDialog and sets start text
        findViewById(R.id.startDateButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startDate();
                    }
                }
        );

        //Listener - End Date Button -> Opens DatePickerDiaglog and sets end text
        findViewById(R.id.endDateButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        endDate();
                    }
                }
        );

        //Listener - Get Date Transaction -> Updates the View with new transaction between the chosen dates
        findViewById(R.id.getDateTransactionsButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //getTransactionByDates();
                        allTransactions();
                    }
                }
        );

        //Listener - Get All Transactions -> Updates the View with all the transactions in the account
        findViewById(R.id.HistoryAllTransactionsButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        allTransactions();
                    }
                }
        );
        //Listener - Logs out of the system
        findViewById(R.id.historyLogoutButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainActivity);
                        finish();
                    }
                });

        //Listener -
        findViewById(R.id.historyBackButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

    }

    /**
     * Private Method
     * Activates on All Transactions Button
     *
     * Gets all the transactions related to this account and sends the list to update Array Adapter Method
     */
    private void allTransactions(){

        //Get all transactions attached to this account
        List<Transaction> transactionList = db.getAllTransactionsForUserAndAccount(userAndAccount[0], userAndAccount[1]);

        updateArrayAdapter(transactionList);

    }

    /**
     * Private Method
     * Activates on Start Date Button
     *
     * Opens a date picker dialog box and sets the startDate TextView to the chosen date
     */
    private void startDate() {

        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH);
        int cDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                        TextView t = (TextView) findViewById(R.id.startDateTextView);
                        t.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, cYear, cMonth, cDay);
        dpd.show();

    }

    /**
     * Private Method
     * Activates on End Date Button
     *
     * Opens a date picker dialog box and sets the endDate TextView to the chosen date
     */
    private void endDate(){

        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH);
        int cDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                       TextView t = (TextView) findViewById(R.id.endDateTextView);
                       t.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);

                    }
                }, cYear, cMonth, cDay);

        dpd.show();

    }

    /**
     * Private Method
     * Activates on Get Date
     *
     * Using the startDate TextView and the endDate TextView, it retriveds all transactions between those two dates
     */
    private void getTransactionByDates(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

        //Get the two text views
        TextView startDateTextView = (TextView) findViewById(R.id.startDateTextView);
        TextView endDateTextView = (TextView) findViewById(R.id.endDateTextView);

        //Get the strings inside the textviews
        String startDateString = startDateTextView.getText().toString();
        String endDateString = endDateTextView.getText().toString();

        //Prepare Date class
        Date startDate = new Date();
        Date endDate = new Date();

        //Initialize start
        long startLong = 0;
        long endLong = 0;


        //Parse the String to get a Date using the SimpleDateFormat
        try {
            startDate = sdf.parse(startDateString);
            endDate = sdf.parse(endDateString);
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Please Use Valid Dates", Toast.LENGTH_SHORT).show();
        }

        //get the Time in long format (milliseconds since 1970)
        startLong = startDate.getTime();
        endLong = endDate.getTime();

        if(!(endLong < startLong)) {
            //Set up transactionList
            List<Transaction> transactionList;
            //transactionList = db.getAllTransactionsForUserAndAccount(userAndAccount[0], userAndAccount[1]);
            transactionList = db.getTransactionsByDate(userAndAccount[0], userAndAccount[1], startLong, endLong);

            //Keep threads in balance with Iterator
            Iterator<Transaction> iterator = transactionList.iterator();
            while(iterator.hasNext()) {
                Transaction t = iterator.next();
                if(!((startLong <= t.getDate()) && (t.getDate() <= endLong))){
                    iterator.remove();
                }
            }

            updateArrayAdapter(transactionList);
        } else {
            Toast.makeText(getApplicationContext(), "Please set the Start Date before the End Date", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateArrayAdapter(List<Transaction> transactionList) {

        //Clear the stringList notify the adapter data has been changed
        stringList.clear();
        arrayAdapter.notifyDataSetChanged();

        if(!transactionList.isEmpty()) {

            //Set up Date Format and Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //update the stringList
            for (Transaction t : transactionList) {

                Date d = new Date(t.getDate());

                //Add a new String to stringList and notify the adapter data hase been changed
                stringList.add("Memo: " + t.getTransactionName() + " - " + t.getType() + " for $" + t.getAmount() + " on " + d);
                arrayAdapter.notifyDataSetChanged();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Sorry, there are no Transactions", Toast.LENGTH_SHORT).show();
        }

    }


}
