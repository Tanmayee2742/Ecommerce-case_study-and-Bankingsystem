package dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

import entity.Account;
import entity.Customer;
import entity.Transaction;
import util.DBUtil;
import exception.*;

public class BankRepositoryImpl implements IBankRepository {

	Connection con=null;
	BankRepositoryImpl()
	{
		this.con=DBUtil.getDBConn();
	}
	
	@Override
	public void createAccount(Customer customer, long accNo, String accType, float balance) {
		
		try {
	        String sql = "INSERT INTO Customers (CustomerID,FirstName, LastName, Email, PhoneNumber, Address) VALUES (?, ?, ?, ?, ?, ?)";

	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	        	preparedStatement.setInt(1, customer.getCustomerId());
	            preparedStatement.setString(2, customer.getFirstName());
	            preparedStatement.setString(3, customer.getLastName());
	            preparedStatement.setString(4, customer.getEmail());
	            preparedStatement.setLong(5, customer.getPhoneNumber());
	            preparedStatement.setString(6, customer.getAddress());

	            preparedStatement.executeUpdate();	            
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		try {
	        String sql = "INSERT INTO Accounts (AccountNumber, AccountType, AccountBalance, CustomerID) VALUES (?, ?, ?, ?)";

	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	            preparedStatement.setLong(1, accNo);
	            preparedStatement.setString(2, accType);
	            preparedStatement.setFloat(3, balance);
	            preparedStatement.setInt(4, customer.getCustomerId());
	            
	            preparedStatement.executeUpdate();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		
	}

	@Override
	public List<Account> listAccounts() {
		List<Account> accounts = new ArrayList<>();

        try {
            String sql = "SELECT * FROM Accounts";

            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        long accountNumber = resultSet.getLong("AccountNumber");
                        String accountType = resultSet.getString("AccountType");
                        float accountBalance = resultSet.getFloat("AccountBalance");

                        int customerId = resultSet.getInt("CustomerID");
                        Customer customer = getCustomerById(customerId);
                        if(customer==null)
                        {
                        	throw new NullPointerException("No customer associated with account");
                        }
                        Account account = new Account(accountType, accountBalance, customer);
                        account.setAccountNumber(accountNumber);
                        accounts.add(account);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
	}

	@Override
	public void calculateInterest() {
		try {
	        String sql = "SELECT * FROM Accounts WHERE AccountType='savings'";
	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                while (resultSet.next()) {
	                	
	                    resultSet.getLong("AccountNumber");
	                    resultSet.getString("AccountType");
	                    double accountBalance=resultSet.getDouble("AccountBalance");
	                    resultSet.getInt("CustomerID");
	                    double interestRate = 4.5;
	                    double interest = (accountBalance / 100) * interestRate;
	                    System.out.print("Interest is Rs. "+interest);
	                    }
	                }
	            }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public float getAccountBalance(long accountNumber) {
		try {
	        String sql = "SELECT AccountBalance FROM Accounts WHERE AccountNumber = ?";

	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	            preparedStatement.setLong(1, accountNumber);

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    float accountBalance = resultSet.getFloat("AccountBalance");
	                    return accountBalance;
	                } else {
	                    System.out.println("Account not found with account number: " + accountNumber);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return 0;
	}

	@Override
	public float deposit(long accountNumber, float amount) {
		try {
	        String sql = "UPDATE Accounts SET AccountBalance = AccountBalance + ? WHERE AccountNumber = ?";

	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	            preparedStatement.setFloat(1, amount);
	            preparedStatement.setLong(2, accountNumber);

	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                float newBalance = getAccountBalance(accountNumber);
	                System.out.println("Database Updated Deposit successful. New balance: RS. " + newBalance);
	                return newBalance;
	            } else {
	                System.out.println("Account not found with account number: " + accountNumber);
	                throw new InvalidAccountException("Account not found");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();	    }
	    return 0;
	}

	@Override
	public float withdraw(long accountNumber, float amount) {
		try {
	        String sqlSelect = "SELECT AccountBalance, AccountType FROM Accounts WHERE AccountNumber = ?";
	        String sqlUpdate = "UPDATE Accounts SET AccountBalance = AccountBalance - ? WHERE AccountNumber = ?";

	        try (PreparedStatement selectStatement = con.prepareStatement(sqlSelect);
	             PreparedStatement updateStatement = con.prepareStatement(sqlUpdate)) {

	            selectStatement.setLong(1, accountNumber);

	            try (ResultSet resultSet = selectStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    float currentBalance = resultSet.getFloat("AccountBalance");
	                    String accountType = resultSet.getString("AccountType");

	                    if ("Savings".equals(accountType) && currentBalance - amount < 500.0) {
	                        System.out.println("Withdrawal failed. Minimum balance rule violated.");
	                        throw new InsufficientFundException("Withdrawal failed. Minimum balance rule violated");
	                    }

	                    if ("Current".equals(accountType) && currentBalance - amount < -10000.0) {
	                        System.out.println("Withdrawal failed. Overdraft limit exceeded.");
	                        throw new OverDraftLimitExcededException("Withdrawal failed. Overdraft limit exceeded.");
	                    }
	                    
	                    if ("ZeroBalance".equals(accountType) && currentBalance - amount < 0) {
	                        System.out.println("Withdrawal failed. Minimum balance rule violated.");
	                        throw new InsufficientFundException("Withdrawal failed. Minimum balance rule violated");
	                    }
	                    updateStatement.setFloat(1, amount);
	                    updateStatement.setLong(2, accountNumber);

	                    int rowsAffected = updateStatement.executeUpdate();

	                    if (rowsAffected > 0) {
	                        float newBalance = getAccountBalance(accountNumber);
	                        System.out.println("Withdrawal successful. New balance: Rs ." + newBalance);
	                        return newBalance;
	                    }
	                } else {
	                    System.out.println("Account not found with account number: " + accountNumber);
	                    throw new InvalidAccountException("Account not found");
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return 0;
	}

	@Override
	public void transfer(long fromAccountNumber, long toAccountNumber, float amount) {
		
		try {
	        float senderBalance = withdraw(fromAccountNumber, amount);

	        if (senderBalance != 0) {
	            deposit(toAccountNumber, amount);
	            System.out.println("Transfer successful. Rs." + amount + " transferred from account " + fromAccountNumber + " to account " + toAccountNumber);
	        } else {
	            System.out.println("Transfer failed. Insufficient funds in sender's account.");
	        }
	    } catch (InvalidAccountException e) {
	    	System.out.println(e.getMessage());
	    } catch (InsufficientFundException e) {
	        System.out.println(e.getMessage());
	    } catch (OverDraftLimitExcededException e) {
	        System.out.println(e.getMessage());
	    }
	}

	@Override
	public String getAccountDetails(long accountNumber) {
	    try {
	        String sql = "SELECT A.AccountNumber, A.AccountType, A.AccountBalance, C.CustomerId, C.FirstName, C.LastName, C.Email, C.PhoneNumber, C.Address " +
	                     "FROM Accounts A JOIN Customers C ON A.CustomerId = C.CustomerId " +
	                     "WHERE A.AccountNumber = ?";

	        try (PreparedStatement statement = con.prepareStatement(sql)) {
	            statement.setLong(1, accountNumber);

	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    long accountId = resultSet.getLong("AccountNumber");
	                    String accountType = resultSet.getString("AccountType");
	                    float accountBalance = resultSet.getFloat("AccountBalance");

	                    long customerId = resultSet.getLong("CustomerId");
	                    String firstName = resultSet.getString("FirstName");
	                    String lastName = resultSet.getString("LastName");
	                    String email = resultSet.getString("Email");
	                    long phoneNumber = resultSet.getLong("PhoneNumber");
	                    String address = resultSet.getString("Address");

	                    StringBuilder detailsBuilder = new StringBuilder();
	                    detailsBuilder.append("Account Number: ").append(accountId).append("\n");
	                    detailsBuilder.append("Account Type: ").append(accountType).append("\n");
	                    detailsBuilder.append("Account Balance: ").append(accountBalance).append("\n");
	                    detailsBuilder.append("Customer Details:\n");
	                    detailsBuilder.append("Customer ID: ").append(customerId).append("\n");
	                    detailsBuilder.append("First Name: ").append(firstName).append("\n");
	                    detailsBuilder.append("Last Name: ").append(lastName).append("\n");
	                    detailsBuilder.append("Email: ").append(email).append("\n");
	                    detailsBuilder.append("Phone Number: ").append(phoneNumber).append("\n");
	                    detailsBuilder.append("Address: ").append(address);

	                    return detailsBuilder.toString();
	                } else {
	                    return "Account not found with account number: " + accountNumber;
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "An error occurred while fetching account details.";
	    }
	}

	@Override
	public List<Transaction> getTransactions(long accountNumber, String fromDate, String toDate) {
		List<Transaction> transactions = new ArrayList<>();

	    try {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date startDate = dateFormat.parse(fromDate);
	        Date endDate = dateFormat.parse(toDate);

	        String sql = "SELECT * FROM Transactions WHERE AccountNumber = ? AND DateTime BETWEEN ? AND ?";
	        try (PreparedStatement statement = con.prepareStatement(sql)) {
	            statement.setLong(1, accountNumber);
	            statement.setTimestamp(2, new Timestamp(startDate.getTime()));
	            statement.setTimestamp(3, new Timestamp(endDate.getTime()));

	            try (ResultSet resultSet = statement.executeQuery()) {
	                while (resultSet.next()) {
	                    String transactionType = resultSet.getString("TransactionType");
	                    double transactionAmount = resultSet.getDouble("TransactionAmount");
	                    String description = resultSet.getString("Description");
	                    Date dateTime = resultSet.getTimestamp("DateTime");
	                    Account acc=getAccount(accountNumber);
	                    Transaction transaction = new Transaction(acc, description,transactionType, transactionAmount, dateTime);
	                    transactions.add(transaction);
	                }
	            }
	        }
	    } catch (ParseException | SQLException e) {
	        e.printStackTrace();
	    }

	    return transactions;
	}
	
	private Customer getCustomerById(int customerId) {
		try {
	        String sql = "SELECT * FROM Customers WHERE CustomerID = ?";

	        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
	            preparedStatement.setInt(1, customerId);

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    String firstName = resultSet.getString("FirstName");
	                    String lastName = resultSet.getString("LastName");
	                    String email = resultSet.getString("Email");
	                    long phoneNumber = resultSet.getLong("PhoneNumber");
	                    String address = resultSet.getString("Address");

	                    return new Customer(customerId, firstName, lastName, email, phoneNumber, address);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null;
    }
	
	public Account getAccount(long accountNumber) {
	    try {
	        String sql = "SELECT A.AccountNumber, A.AccountType, A.AccountBalance, C.CustomerId, C.FirstName, C.LastName, C.Email, C.PhoneNumber, C.Address " +
	                     "FROM Accounts A JOIN Customers C ON A.CustomerId = C.CustomerId " +
	                     "WHERE A.AccountNumber = ?";

	        try (PreparedStatement statement = con.prepareStatement(sql)) {
	            statement.setLong(1, accountNumber);

	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    // Extract information from the result set
	                    long accountId = resultSet.getLong("AccountNumber");
	                    String accountType = resultSet.getString("AccountType");
	                    double accountBalance = resultSet.getDouble("AccountBalance");

	                    int customerId = resultSet.getInt("CustomerId");
	                    String firstName = resultSet.getString("FirstName");
	                    String lastName = resultSet.getString("LastName");
	                    String email = resultSet.getString("Email");
	                    long phoneNumber = resultSet.getLong("PhoneNumber");
	                    String address = resultSet.getString("Address");

	                    return new Account(accountId, accountType, accountBalance, new Customer(customerId, firstName, lastName, email, phoneNumber, address));
	                } else {
	                    return null;
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("An error occurred while fetching account details.");
	        return null;
	    }
	}
	void addTransaction(Transaction transaction)
	{
		try {
	        String sql = "INSERT INTO Transactions (AccountNumber, Description, DateTime, TransactionType, TransactionAmount) VALUES (?, ?, ?, ?, ?)";

	        try (PreparedStatement statement = con.prepareStatement(sql)) {
	            statement.setLong(1, transaction.getAccount().getAccountNumber());
	            statement.setString(2, transaction.getDescription());
	            statement.setTimestamp(3, new Timestamp(transaction.getDateTime().getTime()));
	            statement.setString(4, transaction.getTransactionType());
	            statement.setDouble(5, transaction.getTransactionAmount());

	            int rowsAffected = statement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Transaction added successfully.");
	            } else {
	                System.out.println("Failed to add transaction.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("An error occurred while adding the transaction.");
	    }
	}

}
