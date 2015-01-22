package com.example.jonathanelliott.financialsample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    private Intent loginActivity;
    private Intent registerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up all the intents
        loginActivity = new Intent(this, LoginActivity.class);
        registerActivity = new Intent(this, RegisterActivity.class);

        //This is another way to do the on click listener
        //Setting up the Login Button which goes to the login activity
        findViewById(R.id.mainLoginButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(loginActivity);
                    }
                });

        //Setting up the Register Button which goes to the Register Activity
        findViewById(R.id.mainRegisterButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(registerActivity);
                    }
                });
    }

}
