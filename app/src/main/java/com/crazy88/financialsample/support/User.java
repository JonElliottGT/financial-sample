package com.crazy88.financialsample.support;
import java.util.ArrayList;


/**
 * Created by jonathanelliott on 1/8/15.
 */
public class User {
    private String username;
    private String password;
    private ArrayList<Account> accountList = new ArrayList<Account>();

    public User(){
       this("username", "password");
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void addAccount(Account account) {
        accountList.add(account);
    }

}
