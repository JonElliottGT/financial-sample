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
import com.crazy88.financialsample.support.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CreateTransactionActivity extends ActionBarActivity {


    private String username;

    private RadioGroup radioGroup;
    private Spinner accountSpinner;

    private List<Account> accountList;

    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        db = new DatabaseHandler(this);

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        //Initialize and Populate Spinner
        accountList = db.getAllAccountsByUsername(username);
        List<String> stringList = new ArrayList<String>();
        accountSpinner = (Spinner) findViewById(R.id.wadAccountSpinner);
        if(!accountList.isEmpty()) {
            //Create the string list (used to populate the spinner)
            for (Account a : accountList) {
                stringList.add(a.getAccountName());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, stringList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            accountSpinner.setAdapter(dataAdapter);
        }

        findViewById(R.id.confirmButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RadioButton selectRadio = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                        String type = selectRadio.getText().toString();
                        String accountName = accountSpinner.getSelectedItem().toString();
                        Double amount = Double.parseDouble(((EditText)findViewById(R.id.amountEditText)).getText().toString());
                        String transactionName = ((EditText)findViewById(R.id.wadTransNameEditText)).getText().toString();
                        Date d = new Date();

                        Account selectedAccount = db.getAccountByUsernameAndAccountName(username, accountName);

                        if(transactionName.length() > 0) {
                            if(amount > 0) {
                                if(type.equals("Deposit") || (type.equals("Withdrawal") && selectedAccount.getAccountBalance() >= amount)) {
                                    if(type.equals("Deposit")) {
                                        selectedAccount.deposit(amount);
                                    } else if(type.equals("Withdrawal") && selectedAccount.getAccountBalance() >= amount) {
                                        selectedAccount.withdrawal(amount);
                                    }
                                    Transaction t = new Transaction();
                                    t.setAccountName(selectedAccount.getAccountName());
                                    t.setUsername(username);
                                    t.setAmount(Double.parseDouble(((EditText)findViewById(R.id.amountEditText)).getText().toString()));
                                    t.setDate(d.getTime());
                                    t.setType(type);
                                    t.setTransactionName(((EditText)findViewById(R.id.wadTransNameEditText)).getText().toString());
                                    String date = d.toString();

                                    db.addTransaction(t);
                                    db.updateAccountBalance(selectedAccount);


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
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_transaction, menu);
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
