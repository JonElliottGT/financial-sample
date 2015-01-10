package com.example.jonathanelliott.financialsample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import com.crazy88.financialsample.support.User;
import com.crazy88.financialsample.support.DatabaseHandler;

public class LoginActivity extends ActionBarActivity {

    private Intent userHomeActivity;

    private String username;
    private String password;

    private EditText editTextUsername;
    private EditText editTextPassword;

    private DatabaseHandler db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Setting up the Intents
        userHomeActivity = new Intent(this, UserHomeActivity.class);

        //Setting up the Database Handler (This is used to Check the credentials in the database)
        db = new DatabaseHandler(this);

        //Getting the edit text fields for user input
        editTextUsername = (EditText) findViewById(R.id.loginUsernameEditText);
        editTextPassword = (EditText) findViewById(R.id.loginPasswordEditText);

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
     * Attempt Login checks the credentials in username and password.
     * If they are correct then it logs in to the appropriate page.
     */
    private void attemptLogin(){

        //Get the username and password from the text fields
        username = editTextUsername.getText().toString();
        password = editTextPassword.getText().toString();

        //if they are not empty
        if (username.length() > 0 && password.length() > 0) {
            //get the user out of the database (returns null if username or password are non existent)
            User u = db.getUserByUsernamePassword(username, password);
            if (u != null) {
                //putExtra is simply so we can see who is logged in.
                //This is not the best way to do this. I should have made a session manager or something.
                userHomeActivity.putExtra("username", u.getUsername());
                startActivity(userHomeActivity);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
