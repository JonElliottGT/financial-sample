package com.crazy88.financialsample.support;
import java.util.ArrayList;

/**
 * Created by jonathanelliott on 1/8/15.
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

    public Account(String accountName, double accountBalance) {
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

    public long getId(){
        return id;
    }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long id) { this.userId = id; }

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getAccountName(){
        return accountName;
    }
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }

    public double getAccountBalance(){
        return accountBalance;
    }
    public void setAccountBalance(double amount) {
        this.accountBalance = amount;
    }

}
