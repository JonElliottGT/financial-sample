package com.crazy88.financialsample.support;
import java.util.ArrayList;

/**
 * Created by jonathanelliott on 1/8/15.
 */
public class Account {

    private String accountName;
    private String accountNumber;
    private long accountAmount;
    private String accountType;

    private ArrayList<Transaction> transactionHistory;

    public Account(){
       this("Savings", "00000001", 1000);
    }

    public Account(String accountName, String accountNumber, long accountAmount) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.accountAmount = accountAmount;
        transactionHistory = new ArrayList<Transaction>();
    }

    public String getAccountName(){
        return accountName;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public long getAccountAmount(){
        return accountAmount;
    }

    public void setAccountName(String accountName){
        this.accountName = accountName;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountAmount(int amount) {
        this.accountAmount = amount;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

}
