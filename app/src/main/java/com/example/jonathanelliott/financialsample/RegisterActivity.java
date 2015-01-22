package com.example.jonathanelliott.financialsample;
import com.crazy88.financialsample.support.DatabaseHandler;
import com.crazy88.financialsample.support.User;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;


public class RegisterActivity extends ActionBarActivity {

    private Intent loginActivity;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPass;

    private String username;
    private String password;
    private String confirmPassword;

    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Set up the Intents (This case just Login)
        loginActivity = new Intent(this, LoginActivity.class);

        //Set up the database handler (This is used to create a new user in the database)
        db = new DatabaseHandler(this);

        //initiate all of the edit texts so we can get the user input
        editTextUsername = (EditText) findViewById(R.id.registerUsernameEditText);
        editTextPassword = (EditText) findViewById(R.id.registerPasswordEditText);
        editTextConfirmPass = (EditText) findViewById(R.id.registerConfirmPassEditText);

        //Set the listener for the register button
        findViewById(R.id.registerButton).setOnClickListener(new RegisterClickListener());

        findViewById(R.id.regCancelButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Send the userHomeActivity the username
                        finish();

                    }
                }
        );

    }


    //This is an alternative way of setting up a Click Listener
    //This is the way they initially teach in CS 1331
    //Private Inner Class that implements View.OnClickListener
    private class RegisterClickListener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {

            //Get the user input from EditText's
            username = editTextUsername.getText().toString();
            password = editTextPassword.getText().toString();
            confirmPassword = editTextConfirmPass.getText().toString();


            //won't register unless it passes the following criteria
            //cannot be empty, must be > 5, password and confirm must match, username must not exist in system
            //NOTE: New criteria could be added as it might not cover all edge cases
            if (username.length() > 0 && password.length() > 0 && confirmPassword.length() > 0) {
                if (password.length() > 5) {
                    if (password.equals(confirmPassword)) {
                        if (db.checkUsername(username)) {

                            //add the user to the database if it passes the criteria
                            long id = db.addUser(new User(username, password));
                            User u = db.getUser(id);
                            u.setId(id);

                            //Go to the login activity
                            startActivity(loginActivity);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Password must be > 5 characters", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Cannot have empty boxes", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
