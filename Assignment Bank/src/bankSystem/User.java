package bankSystem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
	private String firstName;
	
	private String lastName;
	
	private String uuid;
	
	private byte pinHash[];
	
	private ArrayList<Account> accounts;
	
	private Bank bank;
	
	public User(String firstName, String lastName, String pin, Bank bank) {
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.bank = bank;
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			this.pinHash = md.digest(pin.getBytes());
		} catch(NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}
		
		this.uuid = bank.getNewUserUUID();
		
		this.accounts = new ArrayList<>();
		
		System.out.printf("New user %s, %s with ID %s created. \n", lastName, firstName, this.uuid);
		System.out.printf("Your pin is %s.\n", pin);
	}
	
	public Bank getBank() {
		return bank;
	}

	public void addAccount(Account account) {
		this.accounts.add(account);
	}
	public String getUUID() {
		return this.uuid;
	}
	public boolean validatePin(String aPin) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return MessageDigest.isEqual(md.digest(aPin.getBytes()),this.pinHash);
		} catch(NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}
		
		return false;
	}

	public String getFirstName() {
		return firstName;
	}

	public void printAccountsSummary() {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.accounts.size(); i++) {
			System.out.printf("%d) %s\n", i+1, this.accounts.get(i).getSummaryLine());
		}
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public void printAcctTransHistory(int acct) {
		this.accounts.get(acct).printTransHistory();
	}

	public double getAcctBalance(int idx) {
		// TODO Auto-generated method stub
		return this.accounts.get(idx).getBalance();
	}
	
	public String getAcctUUID(int acctIdx) {
		return this.accounts.get(acctIdx).getUUID();
	}

	public void addAcctTransaction(int idx, double amount, String memo) {
		// TODO Auto-generated method stub
		this.accounts.get(idx).addTransaction(amount, memo);
		
	}

	public String getLastName() {
		// TODO Auto-generated method stub
		return lastName;
	} 
	
	
}
