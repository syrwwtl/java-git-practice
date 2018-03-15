package bankSystem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

public class Bank {
	private String bankName;

	private ArrayList<User> users;

	private ArrayList<Account> accounts;

	public Bank(String name) {
		this.bankName = name;
		this.users = new ArrayList<User>();
		this.accounts = new ArrayList<Account>();
	}

	public String getBankName() {
		return bankName;
	}

	public String getNewUserUUID() {
		String uuid;
		Random rand = new Random();
		int length = 6;
		boolean nonUnique = false;

		do {
			uuid = "";
			for (int c = 0; c < length; c++) {
				uuid += ((Integer) rand.nextInt(10)).toString();
			}

			for (User user : this.users) {
				if (uuid.equals(user.getUUID())) {
					nonUnique = true;
					break;
				}
			}
		} while (nonUnique);

		return uuid;
	}

	public String getNewAccountUUID() {
		String uuid;
		Random rand = new Random();
		int length = 10;
		boolean nonUnique = false;

		do {
			uuid = "";
			for (int c = 0; c < length; c++) {
				uuid += ((Integer) rand.nextInt(10)).toString();
			}

			for (Account account : this.accounts) {
				if (uuid.equals(account.getUUID())) {
					nonUnique = true;
					break;
				}
			}
		} while (nonUnique);

		return uuid;
	}

	public void addAccount(Account account) {
		this.accounts.add(account);
	}

	public User addUser(String firstName, String lastName, String pin) {
		User newUser = new User(firstName, lastName, pin, this);
		this.users.add(newUser);

		Account newAccount = new Account("Savings", newUser, this);
		newUser.addAccount(newAccount);
		this.addAccount(newAccount);

		return newUser;
	}

	public User userLogin(String userID, String pin) {
		for (User u : this.users) {
			if (u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
				return u;
			}
		}

		return null;
	}

}
