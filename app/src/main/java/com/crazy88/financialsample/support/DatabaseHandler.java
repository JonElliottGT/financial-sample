package com.crazy88.financialsample.support;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jonathanelliott on 1/9/15.
 *
 * Note: I try out several methods of getting the query in this
 *
 * Tables:
 *
 * Version 6 Tables
 *
 * Users - username is unique
 * ----------------------------
 * | Id |  Username | Password |
 * ----------------------------
 * Accounts - accountName + username is unique
 * ----------------------------------------
 * | Id | AccountName | Username | Balance |
 * ----------------------------------------
 * Transactions - transactionName + username + accountName is unique
 * Note: Added Account Type (Withdrawal, Deposit)
 * -----------------------------------------------------------------------------------
 * | Id | TransactionName | Username | AccountName | TransactionAccount | Date | Type |
 * -----------------------------------------------------------------------------------
 *
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String dbName = "userManager";
    private static final int dbVersion = 6;
    //table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ACCOUNTS = "accounts";
    private static final String TABLE_TRANSACTIONS = "transactions";

    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    //private static final String KEY_ID = "id";
    private static final String KEY_ACCOUNT_NAME = "account_name";
    //private static final String KEY_USERNAME = "username";
    private static final String KEY_BALANCE = "balance";
    private static final String KEY_USER_ID = "user_id";

    //private static final String KEY_ID = "id";
    private static final String KEY_TRANSACTION_NAME = "transaction_name";
    //private static final String KEY_USERNAME = "username";
    //private static final String KEY_ACCOUNT_NAME = "account_name";
    private static final String KEY_ACCOUNT_ID = "account_id";
    private static final String KEY_TRANSACTION_AMOUNT = "transaction_amount";
    private static final String KEY_DATE = "date";
    private static final String KEY_TRANSACTION_TYPE = "transaction_type";

    //Create Table Strings
    //Users Create Table
    private static final String CREATE_USERS_TABLE = "CREATE TABLE "
            + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_USERNAME + " TEXT,"
            + KEY_PASSWORD + " TEXT" + ")";
    //Accounts Create Table
    private static final String CREATE_ACCOUNTS_TABLE = "CREATE TABLE "
            + TABLE_ACCOUNTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ACCOUNT_NAME + " TEXT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_BALANCE + " REAL"
            + ")";
    //Transactions Create Table
    private static final String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE "
            + TABLE_TRANSACTIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TRANSACTION_NAME + " TEXT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_ACCOUNT_NAME + " TEXT,"
            + KEY_TRANSACTION_AMOUNT + " REAL,"
            + KEY_DATE + " TEXT,"
            + KEY_TRANSACTION_TYPE + " TEXT" + ")";


    public DatabaseHandler(Context context) {
        super(context, dbName, null, dbVersion);
    }

    //Creates all the required tables in the database
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ACCOUNTS_TABLE);
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    //Get the current database.
    public SQLiteDatabase getDB() { return this.getWritableDatabase(); }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);

        onCreate(db);
    }

    /**
     * Add a User to the database
     * Users
     * ----------------------------
     * | Id |  Username | Password |
     * ----------------------------
     * @param u - User to be added
     * @return - the User id
     */
    public long addUser(User u) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //get the username and password
        values.put(KEY_USERNAME, u.getUsername());
        values.put(KEY_PASSWORD, u.getPassword());

        //insert row into the table
        long userId = db.insert(TABLE_USERS, null, values);
        db.close();

        return userId;
    }

    /**
     * Add an Account to the database
     * Accounts
     * ----------------------------------------
     * | Id | AccountName | Username | Balance |
     * ----------------------------------------
     * @param a - The Account to be added
     * @return - the Account id
     */
    public long addAccount(Account a) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ACCOUNT_NAME, a.getAccountName());
        values.put(KEY_USERNAME, a.getUsername());
        values.put(KEY_BALANCE, a.getAccountBalance());

        long accountId = db.insert(TABLE_ACCOUNTS, null, values);
        db.close();
        return accountId;
    }

    /**
     * Add a Transaction to the database
     * Transactions
     * ----------------------------------------------------------------------------
     * | Id | TransactionName | Username | AccountName | TransactionAccount | Date |
     * ----------------------------------------------------------------------------
     * @param t - the Transaction to be added
     * @return - the Transaction id
     */
    public long addTransaction(Transaction t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TRANSACTION_NAME, t.getTransactionName());
        values.put(KEY_USERNAME, t.getUsername());
        values.put(KEY_ACCOUNT_NAME, t.getAccountName());
        values.put(KEY_TRANSACTION_AMOUNT, t.getAmount());
        values.put(KEY_DATE, t.getDate());
        values.put(KEY_TRANSACTION_TYPE, t.getType());

        Log.d("HEY! LOOK!", t.getTransactionName() + " " + t.getUsername() + t.getAccountName());

        long transactionId = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();

        return transactionId;
    }

    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] {KEY_ID,
        KEY_USERNAME, KEY_PASSWORD}, KEY_ID + "=?",
                new String[] {String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            User user = new User(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2));
            return user;
        }
        db.close();
        return null;
    }

    /**
     * Check Username
     * Used in RegisterActivity to check if a username exists or not
     * @param username - The username to be checked
     * @return - a boolean whether the user exists or not (not exist = true)
     */
    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[] {KEY_ID,
                        KEY_USERNAME, KEY_PASSWORD }, KEY_USERNAME + "=?",
                new String[] {String.valueOf(username)}, null, null, null,
                null);

        boolean truth =  !((cursor.getCount() != 0) && (cursor.moveToFirst()));
        db.close();
        return truth;
    }

    /**
     * Get User By Username
    public User getUser(long id) {
     * Used whenever you want to get a specific user by Username
     * @param username - The username of the user you want
     * @return
     */
    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USERNAME + " = '" + username + "'";

        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            User user = new User(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2));
            db.close();
            return user;
        }
        db.close();
        return null;

    }

    /**
     * Get User by Username and Password
     * This is used in LoginActivity to Login
     * @param username - username to be searched for
     * @param password - password to check
     * @return - the user or null if they do not exist or have wrong password
     */
    public User getUserByUsernamePassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] {KEY_ID,
                KEY_USERNAME, KEY_PASSWORD}, KEY_USERNAME + "=? AND "
                + KEY_PASSWORD + "=?", new String[] {String.valueOf(username),
                String.valueOf(password)}, null, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2));
            return user;
        }
        db.close();
        return null;
    }

    /**
     * Get all the Accounts under a specific username
     * Used in UserHomeActivity to populate the List View
     * @param username - username to be searched for
     * @return - A List containing all accounts of the user
     */
    public List<Account> getAllAccountsByUsername(String username) {
        List<Account> accountList = new ArrayList<Account>();

        String selectQuery = "SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_USERNAME + " = '" + username + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                Account account = new Account();
                account.setId(c.getLong(c.getColumnIndex(KEY_ID)));
                account.setAccountName(c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME)));
                account.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));
                account.setAccountBalance(c.getDouble(c.getColumnIndex(KEY_BALANCE)));
                accountList.add(account);

            } while (c.moveToNext());
        }
        db.close();
        return accountList;
    }

    /**
     * Get a specific account for a user
     * @param username - User who the account belongs to
     * @param accountName - the account name being searched for
     * @return - The Account or null if it does not exist
     */
    public Account getAccountByUsernameAndAccountName(String username, String accountName) {

        String select = "SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_ACCOUNT_NAME + " = '" + accountName
                + "' AND " + KEY_USERNAME + " = '" + username + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            Account account = new Account(cursor.getLong(0), cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3)));
            db.close();
            return account;
        }
        db.close();
        return null;

    }

    /**
     * Check the account if it already exists
     * @param username
     * @param accountName
     * @return
     */
    public boolean checkAccount(String username, String accountName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_USERNAME + " = '" + username
                + "' AND " + KEY_ACCOUNT_NAME + " = '" + accountName + "'";
        Cursor cursor = db.rawQuery(select, null);
        boolean truth =  !((cursor.getCount() != 0) && (cursor.moveToFirst()));
        db.close();
        return truth;
    }


    /**
     * Get all transactions for a user and account
     * @param username
     * @param accountName
     * @return
     */
    public List<Transaction> getAllTransactionsForUserAndAccount(String username, String accountName) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + KEY_USERNAME + " = '" + username
                + "' AND " + KEY_ACCOUNT_NAME + " = '" + accountName + "'";

        Log.d("HEY! LISTEN!", select);

        Cursor c = db.rawQuery(select, null);
        if(c.moveToFirst()) {
            do {

                Transaction transaction = new Transaction();
                transaction.setId(c.getLong(c.getColumnIndex(KEY_ID)));
                transaction.setTransactionName(c.getString(c.getColumnIndex(KEY_TRANSACTION_NAME)));
                transaction.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));
                transaction.setAccountName(c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME)));
                transaction.setAmount(c.getDouble(c.getColumnIndex(KEY_TRANSACTION_AMOUNT)));
                transaction.setDate(c.getLong(c.getColumnIndex(KEY_DATE)));
                transaction.setType(c.getString(c.getColumnIndex(KEY_TRANSACTION_TYPE)));
                transactionList.add(transaction);

            } while (c.moveToNext());
        }

        db.close();
        return transactionList;
    }

    /**
     * Get all transactions from a certain date
     * @param username
     * @param accountName
     * @param date
     * @return
     */
    public List<Transaction> getTransactionsByDate(String username, String accountName, String date) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + KEY_USERNAME + " = '" + username
                + "' AND " + KEY_ACCOUNT_NAME + " = '" + accountName + "' AND " + KEY_DATE + " = '" + date + "'";
        Cursor c = db.rawQuery(select, null);
        if(c.moveToFirst()) {
            do {

                Transaction transaction = new Transaction();
                transaction.setId(c.getLong(c.getColumnIndex(KEY_ID)));
                transaction.setTransactionName(c.getString(c.getColumnIndex(KEY_TRANSACTION_NAME)));
                transaction.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));
                transaction.setAccountName(c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME)));
                transaction.setAmount(c.getDouble(c.getColumnIndex(KEY_TRANSACTION_AMOUNT)));
                transaction.setDate(c.getLong(c.getColumnIndex(KEY_DATE)));
                transaction.setType(c.getString(c.getColumnIndex(KEY_TRANSACTION_TYPE)));
                transactionList.add(transaction);

            } while (c.moveToNext());
        }

        db.close();
        return transactionList;

    }

    public void updateAccountBalance(Account a) {
        String update = "UPDATE " + TABLE_ACCOUNTS + " SET " + KEY_BALANCE + " = '" + a.getAccountBalance() +
                "' WHERE " + KEY_ID + " = '" + a.getId() + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(update);
        db.close();
    }

}
