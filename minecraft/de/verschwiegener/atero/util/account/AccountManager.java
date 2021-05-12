package de.verschwiegener.atero.util.account;

import java.util.ArrayList;

public class AccountManager {
    
    public AccountManager() {
	accounts.add(new Account("dj.nmoretti@gmail.com", "cabras83"));
    }
    
    ArrayList<Account> accounts = new ArrayList<>();

    public ArrayList<Account> getAccounts() {
	return accounts;
    }
    
    

}