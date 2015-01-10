package com.crazy88.financialsample.support;
import java.util.ArrayList;

/**
 * Created by jonathanelliott on 1/8/15.
 *
 * Note: It is best to get a user by their username not their userId
 */
public class Account {

    private long id;
    private long userId;
    private String username;
    private String accountName;
    private double accountBalance;

    public Account(){
        this(0, 0, "username", "Savings", 1000);
    }

    public Account(String accountName, String username, double accountBalance) {
        this(0, 0, accountName, username, accountBalance);
    }

    public Account(long id, long userId, String accountName, String username, double balance) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.accountName = accountName;
        this.accountBalance = balance;
    }

    //Getter and Setter for ID
    public long getId(){
        return id;
    }
    public void setId(long id) { this.id = id; }

    //Getter and Setter for UserID
    public long getUserId() { return userId; }
    public void setUserId(long id) { this.userId = id; }

    //Getter and Setter for username
    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    //Getter and setter for accountName
    public String getAccountName(){
        return accountName;
    }
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }

    //Getter and setter for accountBalance
    public double getAccountBalance(){
        return accountBalance;
    }
    public void setAccountBalance(double amount) {
        this.accountBalance = amount;
    }

}
