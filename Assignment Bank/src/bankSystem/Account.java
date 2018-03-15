package bankSystem;

import java.util.ArrayList;

public class Account {
	
	private String name;
	
	private double balance;
	
	private String uuid;
	
	private User holder;
	
	private ArrayList<Transaction> transactions;
	
	public Account(String name, User holder, Bank bank) {
		this.name = name;
		this.holder = holder;
		
		this.uuid = bank.getNewAccountUUID();
		
		this.transactions = new ArrayList<Transaction>();
		
		//holder.addAccount(this);
		bank.addAccount(this);
		
	}
	
	public String getUUID() {
		return this.uuid;
	}

	public String getSummaryLine() {
		// TODO Auto-generated method stub
		double balance = this.getBalance();
		if(balance >= 0) {
			return String.format("%s : $%.02f : %s", this.uuid, balance, this.name);
		} else {
			return String.format("%s : $(%.02f) : %s", this.uuid, balance, this.name);
		}
	}

	public double getBalance() {
		// TODO Auto-generated method stub
		double balance = 0;
		for(Transaction t : this.transactions) {
			balance += t.getAmount();
			
		}
		return balance;
	}

	public void printTransHistory() {
		// TODO Auto-generated method stub
		System.out.printf("\nTransaction history for account %s \n", this.uuid);
		for (int i = this.transactions.size() - 1; i >= 0; i--) {
			System.out.printf(this.transactions.get(i).getSummaryLine());
		}
		System.out.println();
	}

	public void addTransaction(double amount, String memo) {
		// TODO Auto-generated method stub
		Transaction t = new Transaction(amount, this, memo);
		this.transactions.add(t);
	}
	
}
