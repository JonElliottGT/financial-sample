package com.crazy88.financialsample.support;

/**
 * Created by jonathanelliott on 1/8/15.
 */
public class Transaction {

    private long id;
    private long accountId;
    private long userId;
    private String username;
    private String accountName;
    private String memo;
    private long date;
    private double amount;
    private String type;

    public Transaction() {
        this(0, 0, 0, "name", "username", "account", 0, 0.0, "Withdrawal");
    }

    public Transaction(long id, long uid, long aid, String memo, long date, double amount, String type) {
        this.id = id;
        this.userId = uid;
        this.accountId = aid;
        this.memo = memo;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public Transaction(long id, long aid, int userId, String memo, String username, String accountName, long date, double amount, String type) {
        this.id = id;
        this.accountId = aid;
        this.userId = userId;
        this.memo = memo;
        this.username = username;
        this.accountName = accountName;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public long getId(){
        return id;
    }
    public void setId(long id) { this.id = id; }

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getAccountName() { return this.accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public long getAccountId() { return this.accountId; }
    public void setAccountId(long aid) { this.accountId = aid; }

    public long getUserId() { return this.userId; }
    public void setUserId(long id) { this.userId = id; }

    public String getTransactionName() { return this.memo; }
    public void setTransactionName(String name) { this.memo = name; }

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

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }


}
