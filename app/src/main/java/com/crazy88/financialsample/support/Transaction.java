package com.crazy88.financialsample.support;

/**
 * Created by jonathanelliott on 1/8/15.
 */
public class Transaction {

    private String date;    //change to Date maybe
    private long amount;

    public Transaction(){
        this("01/01/2015", 1000);
    }

    public Transaction(String date, long amount) {
        this.date = date;
        this.amount = amount;
    }

    public String getDate(){
        return date;
    }

    public long getAmount(){
        return amount;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

}
