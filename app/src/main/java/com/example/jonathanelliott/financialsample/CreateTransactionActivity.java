package com.example.jonathanelliott.financialsample;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.crazy88.financialsample.support.Account;
import com.crazy88.financialsample.support.DatabaseHandler;
import com.crazy88.financialsample.support.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CreateTransactionActivity extends ActionBarActivity {

    //The User Home Activity
    private Intent userHomeActivity;

    //account spinner holds all the accounts the user can transaction to/from
    private Spinner accountSpinner;

    //The current User's username
    private String username;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);

        //Initialize the DatabaseHandler
        db = new DatabaseHandler(this);

        //Setting up Intents
        //1. Initialize userHomeActivity (create transaction button)
        userHomeActivity = new Intent(this, UserHomeActivity.class);

        //Get the current user
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        //Initialize and Populate Spinner
        accountSpinner = (Spinner) findViewById(R.id.wadAccountSpinner);

        //Get All Accounts Using the current user's username
        List<Account> accountList = db.getAllAccountsByUsername(username);

        if(!accountList.isEmpty()) {

            //Create the string list (used to populate the spinner)
            List<String> stringList = new ArrayList<String>();
            for (Account a : accountList) {
                stringList.add(a.getAccountName());
            }

            //Array Adapter to adapt the String ArrayList to the ViewList
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, stringList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            accountSpinner.setAdapter(dataAdapter);
        }

        //
        findViewById(R.id.confirmButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        confirm();

                    }
                });
    }

    /**
     * Private Method
     * Activates on the Confirm Button
     *
     * Sets up the new Account give the user input
     * Returns to the main screen upon completion or notifies user of an error
     */
    private void confirm(){

        //Get the account they want to transaction to (via spinner)
        String accountName = accountSpinner.getSelectedItem().toString();
        Account selectedAccount = db.getAccountByUsernameAndAccountName(username, accountName);

        //Get the user inputted memo
        String memo = ((EditText)findViewById(R.id.wadMemoEditText)).getText().toString();

        //make sure memo is > 0 (not really needed >= 0 is fine)
        if(memo.length() > 0) {

            //Get the user inputted amount
            Double amount = Double.parseDouble(((EditText)findViewById(R.id.amountEditText)).getText().toString());

            //make sure amount isn't negative
            if(amount > 0) {

                //Get the selected Radio Button (Withdrawal or Deposit)
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                RadioButton selectRadio = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                String type = selectRadio.getText().toString();

                //make sure withdrawal amount >= current account's balance
                if(type.equals("Deposit") || (type.equals("Withdrawal") && selectedAccount.getAccountBalance() >= amount)) {

                    //Set up the accounts new balance (withdrawal cannot exceed current account balance)
                    if(type.equals("Deposit")) {
                        selectedAccount.deposit(amount);
                    } else if(type.equals("Withdrawal")) {
                        selectedAccount.withdrawal(amount);
                    }

                    //Set up date
                    Date d = new Date();

                    //Set up the transaction for the database
                    Transaction t = new Transaction();
                    t.setAccountName(selectedAccount.getAccountName());
                    t.setUsername(username);
                    t.setAmount(Double.parseDouble(((EditText)findViewById(R.id.amountEditText)).getText().toString()));
                    t.setDate(d.getTime());
                    t.setType(type);
                    t.setTransactionName(((EditText)findViewById(R.id.wadMemoEditText)).getText().toString());

                    //Call the database (add transaction, update account's balance)
                    db.addTransaction(t);
                    db.updateAccountBalance(selectedAccount);

                    Toast.makeText(getApplicationContext(), "Transaction Complete", Toast.LENGTH_SHORT).show();

                    //Go back the home page
                    userHomeActivity.putExtra("username", username);
                    startActivity(userHomeActivity);


                } else {
                    Toast.makeText(getApplicationContext(), "Insufficient Funds", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Amount", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Invalid Transaction Name", Toast.LENGTH_SHORT).show();
        }
    }

}
