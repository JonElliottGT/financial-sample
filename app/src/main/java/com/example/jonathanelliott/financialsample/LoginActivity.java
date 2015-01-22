package com.example.jonathanelliott.financialsample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.crazy88.financialsample.support.User;
import com.crazy88.financialsample.support.DatabaseHandler;

public class LoginActivity extends ActionBarActivity {

    private Intent userHomeActivity;

    private DatabaseHandler db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Setting up the Database Handler (This is used to Check the credentials in the database)
        db = new DatabaseHandler(this);

        //Setting up the Intents
        userHomeActivity = new Intent(this, UserHomeActivity.class);

        //setting up the login button which goes to private method attemptLogin()
        findViewById(R.id.loginButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        attemptLogin();
                    }
                });
    }

    /**
     * Private Method
     * Activates on Login Button
     *
     * Attempt Login checks the credentials in username and password.
     * If they are correct then it logs in to the appropriate page.
     */
    private void attemptLogin(){

        EditText editTextUsername = (EditText) findViewById(R.id.loginUsernameEditText);
        EditText editTextPassword = (EditText) findViewById(R.id.loginPasswordEditText);

        //Get the username and password from the text fields
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        //if they are not empty
        if (username.length() > 0 && password.length() > 0) {
            //get the user out of the database (returns null if username or password are non existent)
            User u = db.getUserByUsernamePassword(username, password);
            if (u != null) {
                //putExtra is simply so we can see who is logged in.
                userHomeActivity.putExtra("username", u.getUsername());
                startActivity(userHomeActivity);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Username or password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Cannot have empty boxes", Toast.LENGTH_SHORT).show();

        }
    }

}
