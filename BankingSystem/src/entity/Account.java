package entity;

public class Account {
    private static long lastAccNo = 2000; 
    private long accountNumber;
    private String accountType;
    private double accountBalance;
    private Customer customer;

    public Account() {
        this.accountNumber = generateAccountNumber();
    }

    public Account(String accountType, double accountBalance, Customer customer) {
        this.accountNumber = lastAccNo;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
        this.customer = customer;
    }
    public Account(long accNo,String accountType, double accountBalance, Customer customer) {
        this.accountNumber = accNo;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
        this.customer = customer;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static long generateAccountNumber() {
        return ++lastAccNo;
    }

    public void printAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Type: " + accountType);
        System.out.println("Account Balance: $" + accountBalance);
        System.out.println("Customer Information:");
        customer.printInfo();
    }

	public void withdraw(double amount) {
		
	}
}
