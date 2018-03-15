package bankSystem;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Scanner;

public class BankSystem {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		Bank bank = new Bank("Chase Bank");

		User userA = bank.addUser("Tyler", "Wu", "8888");

		Account newAccount = new Account("Checking", userA, bank);

		userA.addAccount(newAccount);
		bank.addAccount(newAccount);

		User curUser;
		while (true) {
			curUser = BankSystem.mainMenuPrompt(bank, sc);

			BankSystem.printUserMenu(curUser, sc);
		}
	}

	public static User mainMenuPrompt(Bank bank, Scanner sc) {

		String userID;
		String pin;
		User authUser;

		do {
			System.out.printf("\n\nWelcome to %s\n\n", bank.getBankName());
			System.out.print("Enter user ID:");
			userID = sc.nextLine();
			System.out.print("Enter pin :");
			pin = sc.nextLine();

			authUser = bank.userLogin(userID, pin);
			if (authUser == null) {
				System.out.println("Incorrect user ID/pin. Please try again.");
			}
		} while (authUser == null);

		return authUser;
	}

	public static void printUserMenu(User theUser, Scanner sc) {
		theUser.printAccountsSummary();

		int choice;

		do {
			System.out.printf("Welcome %s, please select an option?\n", theUser.getFirstName());
			System.out.println("(1) Show account transaction history");
			System.out.println("(2) Withdraw");
			System.out.println("(3) Deposit");
			System.out.println("(4) Transfer");
			System.out.println("(5) Create new account");
			System.out.println("(6) Print receipt");
			System.out.println("(7) Quit");
			System.out.println();
			System.out.println("Enter option:");
			choice = sc.nextInt();

			if (choice < 1 || choice > 5) {
				System.out.println("Invalid input. Please try again.");
			}
		} while (choice < 1 || choice > 5);

		switch (choice) {
		case 1:
			BankSystem.showTransHistory(theUser, sc);
			break;
		case 2:
			BankSystem.withdrawl(theUser, sc);
			break;
		case 3:
			BankSystem.deposit(theUser, sc);
			break;
		case 4:
			BankSystem.transfer(theUser, sc);
			break;
		case 5:
			BankSystem.addAccount(theUser, sc);
			break;
		case 6:
			BankSystem.printReceipt(theUser);
			break;
		case 7:
			sc.nextLine();
			break;
		}

		if (choice != 7) {
			BankSystem.printUserMenu(theUser, sc);
		}
	}

	private static void addAccount(User theUser, Scanner sc) {
		// TODO Auto-generated method stub
		int choice;
		do {
		System.out.println("What kind of Account do you want to create?");
		System.out.println("Please Select: \n");
		System.out.println("(1) Checking\n" + "(2) Savings\n");
		choice = sc.nextInt();
		if(choice < 1 || choice > 2) {
			System.out.println("Invalid input, please try again.");
		}
		} while (choice < 1 || choice > 2);
		
		switch(choice) {
			case 1:
				Account newAccount = new Account("Checking", theUser, theUser.getBank());
				theUser.addAccount(newAccount);
				theUser.getBank().addAccount(newAccount);
				break;
			case 2:
				Account newAccount2 = new Account("Savings", theUser, theUser.getBank());
				theUser.addAccount(newAccount2);
				theUser.getBank().addAccount(newAccount2);
				break;				
		}
		
	}

	private static void printReceipt(User theUser) {
		// TODO Auto-generated method stub
		File file = new File(theUser.getFirstName() + "receipt.txt");
		try {
			FileWriter out = new FileWriter(file);
			String text = "Receipt\n" + "User Name:" + theUser.getFirstName() + " " + theUser.getLastName() + "\n" + "Time:" + new Date()+ "\n";
			for (int i = 0; i < theUser.getAccounts().size(); i++) {
				text += "Account:" + theUser.getAccounts().get(i).getUUID() + " Balance:" + String.format( "%.2f", theUser.getAccounts().get(i).getBalance())+"\n";
			}
			out.write(text);
			out.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private static void deposit(User theUser, Scanner sc) {
		// TODO Auto-generated method stub
		int toAcct;
		double amount;
		double acctBal;
		String memo;

		do {
			System.out.printf("Enter the number (1 - %d) of the account\n" + "to deposit in: ",
					theUser.getAccounts().size());
			toAcct = sc.nextInt() - 1;
			if (toAcct < 0 || toAcct >= theUser.getAccounts().size()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (toAcct < 0 || toAcct >= theUser.getAccounts().size());
		acctBal = theUser.getAcctBalance(toAcct);

		do {
			System.out.printf("Enter the amount to deposit (max $%.02f): $", acctBal);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero.");
			}
		} while (amount < 0);

		sc.nextLine();
		System.out.println("Enter a memo");
		memo = sc.nextLine();

		theUser.addAcctTransaction(toAcct, amount, memo);
	}

	private static void withdrawl(User theUser, Scanner sc) {
		// TODO Auto-generated method stub
		int fromAcct;
		double amount;
		double acctBal;
		String memo;

		do {
			System.out.printf("Enter the number (1 - %d) of the account\n" + "to withdraw from: ",
					theUser.getAccounts().size());
			fromAcct = sc.nextInt() - 1;
			if (fromAcct < 0 || fromAcct >= theUser.getAccounts().size()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (fromAcct < 0 || fromAcct >= theUser.getAccounts().size());
		acctBal = theUser.getAcctBalance(fromAcct);

		do {
			System.out.printf("Enter the amount to withdraw (max $%.02f): $", acctBal);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero.");
			} else if (amount > acctBal) {
				System.out.printf("Amount must be greater than\n" + "$%.02f \n", acctBal);
			}
		} while (amount < 0 || amount > acctBal);

		sc.nextLine();
		System.out.println("Enter a memo");
		memo = sc.nextLine();

		theUser.addAcctTransaction(fromAcct, -1 * amount, memo);
	}

	private static void transfer(User theUser, Scanner sc) {
		// TODO Auto-generated method stub
		int fromAcct;
		int toAcct;
		double amount;
		double acctBal;

		do {
			System.out.printf("Enter the number (1 - %d) of the account\n" + "to transfer from: ",
					theUser.getAccounts().size());
			fromAcct = sc.nextInt() - 1;
			if (fromAcct < 0 || fromAcct >= theUser.getAccounts().size()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (fromAcct < 0 || fromAcct >= theUser.getAccounts().size());
		acctBal = theUser.getAcctBalance(fromAcct);

		do {
			System.out.printf("Enter the number (1 - %d) of the account\n" + "to transfer to: ", theUser.getAccounts().size());
			toAcct = sc.nextInt() - 1;
			if (toAcct < 0 || toAcct >= theUser.getAccounts().size()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (toAcct < 0 || toAcct >= theUser.getAccounts().size());

		do {
			System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero.");
			} else if (amount > acctBal) {
				System.out.printf("Amount must be greater than\n" + "$%.02f \n", acctBal);
			}
		} while (amount < 0 || amount > acctBal);

		theUser.addAcctTransaction(fromAcct, -1 * amount,
				String.format("Transfer to account %s", theUser.getAcctUUID(toAcct)));
		theUser.addAcctTransaction(toAcct, amount,
				String.format("Transfer to account %s", theUser.getAcctUUID(fromAcct)));

	}

	private static void showTransHistory(User theUser, Scanner sc) {
		// TODO Auto-generated method stub
		int acct;

		do {
			System.out.printf("Enter the number (1-%d) of the account\n", theUser.getAccounts().size());
			acct = sc.nextInt() - 1;
			if (acct < 0 || acct >= theUser.getAccounts().size()) {
				System.out.println("Invalid input, please try again.");
			}
		} while (acct < 0 || acct >= theUser.getAccounts().size());

		theUser.printAcctTransHistory(acct);
	}

}
