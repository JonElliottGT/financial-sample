package com.example.jonathanelliott.financialsample;

import android.app.Activity;
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
import java.util.List;

/**
 * Unfinished
 */
public class HistoryActivity extends ActionBarActivity {

    //ListView
    private ListView listView;

    //Getting/Retrieving the user and account
    private String[] userAndAccount;

    private DatabaseHandler db;


    private Activity currentActivity;

    List<String> stringList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        currentActivity = this;

        //Initialize the DatabaseHandler
        db = new DatabaseHandler(this);

        Intent intent = getIntent();
        userAndAccount = intent.getExtras().getStringArray("user_account");

        //Set up the ListView and populate with all transactions
        listView = (ListView) findViewById(R.id.historyView);
        stringList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringList);

        //Set the adapter for the array
        listView.setAdapter(arrayAdapter);

        findViewById(R.id.startDateButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startDate();
                    }
                }
        );

        findViewById(R.id.endDateButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        endDate();
                    }
                }
        );
        findViewById(R.id.getDateTransactionsButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getTransactionByDates();
                    }
                }
        );


        findViewById(R.id.HistoryAllTransactionsButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        allTransactions();
                    }
                }
        );

    }

    private void allTransactions(){
        //Get the Current Account and User and All the Transactions of the User and Account

        List<Transaction> transactionList = db.getAllTransactionsForUserAndAccount(userAndAccount[0], userAndAccount[1]);

        updateArrayAdapter(transactionList);

    }

    private void startDate() {

        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH);
        int cDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        TextView t = (TextView) findViewById(R.id.startDateTextView);
                        t.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, cYear, cMonth, cDay);
        dpd.show();

    }

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

    private void getTransactionByDates(){

        //TRY 1
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

        TextView startDateTextView = (TextView) findViewById(R.id.startDateTextView);
        TextView endDateTextView = (TextView) findViewById(R.id.endDateTextView);

        String startDate = startDateTextView.getText().toString();
        String endDate = endDateTextView.getText().toString();

        Date start1 = new Date();
        Date end1 = new Date();

        long start2 = 0;
        long end2 = 0;


        try {
            start1 = sdf.parse(startDate);
            end1 = sdf.parse(endDate);
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Problem", Toast.LENGTH_SHORT).show();

        }

        start2 = start1.getTime();
        end2 = end1.getTime();


        //List<Transaction> transactionList = db.getTransactionsByDate(userAndAccount[0], userAndAccount[1], start2, end2);
        //updateArrayAdapter(transactionList);

        Toast.makeText(getApplicationContext(), start1.toString() + " " + end1.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), start2 + " " + end2, Toast.LENGTH_LONG).show();

        //TRY 2
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.clear();
        transactionList = db.getAllTransactionsForUserAndAccount(userAndAccount[0], userAndAccount[1]);

        for(int i = 0; i < transactionList.size(); i++) {
            Transaction t = transactionList.get(i);
            long date = t.getDate();
            long ssum = start2 - date; //less - great = -
            long esum = date - end2; //less - great = -
            boolean listen1 = ssum <= 0; //true if positive
            boolean listen2 = esum <= 0; //true if negative
            boolean hey = (date <= start2) || (date >= end2);
            boolean hey2 = (start2 >= date) || (date >= end2);
            Log.d("Hey Buddy Listen!", start2 + " " + t.getDate() + " " + end2 + " " + hey + " " + hey2);
            Log.d("Hey Buddy Yo Listen!", ssum + " " + listen1 + " " + esum + " " + listen2);

            boolean theTruth = (date >= end2) || (date <= start2);


            boolean isBetween = ((start2 <= date) && (date <= end2));



            //if(theTruth) {
                if(!isBetween) {
                    transactionList.remove(i);
                    Log.d("Remove","remove" + !isBetween);
            }
        }
        updateArrayAdapter(transactionList);

    }

    private void updateArrayAdapter(List<Transaction> transactionList) {

        stringList.clear();
        arrayAdapter.notifyDataSetChanged();
        if(!transactionList.isEmpty()) {

            //Set up Date Format and Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = new Date();

            for (Transaction t : transactionList) {
                try{
                    d = sdf.parse(Long.toString(t.getDate()));
                    Toast.makeText(getApplicationContext(), " " + t.getDate(), Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    //Pray there is no exception (very unusual circumstances)
                }
                stringList.add("Memo: " + t.getTransactionName() + " " + t.getType() + " $" + t.getAmount() + " " + d + " Long: " + t.getDate());
                arrayAdapter.notifyDataSetChanged();
            }



        } else {
            //Toast.makeText(getApplicationContext(), "Adapter = nothing", Toast.LENGTH_SHORT).show();
        }

    }


}
