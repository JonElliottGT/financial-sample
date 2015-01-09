package com.crazy88.financialsample.support;
import java.util.ArrayList;

/**
 * Created by jonathanelliott on 1/8/15.
 */
public class Account {

    private long id;
    private long userId;
    private String accountName;
    private double accountBalance;

    public Account(){
        this(0, 0, "Savings", 1000);
    }

    public Account(String accountName, double accountBalance) {
        this(0, 0, "Savings", 1000);
    }

    public Account(long id, long userId, String accountName, double balance) {
        this.id = id;
        this.userId = userId;
        this.accountName = accountName;
        this.accountBalance = balance;
    }

    public long getId(){
        return id;
    }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long id) { this.userId = id; }

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
