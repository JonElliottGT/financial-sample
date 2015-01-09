package com.crazy88.financialsample.support;

/**
 * Created by jonathanelliott on 1/8/15.
 */
public class Transaction {

    private long id;
    private long accountId;
    private int userId;
    private String transactionName;
    private long date;
    private double amount;

    public Transaction() {
        this(0, 0, 0, "N/a", 0, 0.0);
    }

    public Transaction(long id, long aid, int userId, String name, long date, double amount) {
        this.id = id;
        this.accountId = aid;
        this.userId = userId;
        this.transactionName = name;
        this.date = date;
        this.amount = amount;
    }

    public long getId(){
        return id;
    }
    public void setId(long id) { this.id = id; }

    public long getAccountId() { return this.accountId; }
    public void setAccountId(long aid) { this.accountId = aid; }

    public int getUserId() { return this.userId; }
    public void setUserId(int id) { this.userId = id; }

    public String getTransactionName() { return this.transactionName; }
    public void setTransactionName(String name) { this.transactionName = name; }

    public long getDate(){
        return date;
    }
    public void setDate(long date){
        this.date = date;
    }

    public double getAmount(){
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }


}
