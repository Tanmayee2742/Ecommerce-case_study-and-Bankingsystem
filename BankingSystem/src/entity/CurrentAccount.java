package entity;

public class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount() {
        super("Current", 0.0, null);
        this.overdraftLimit = 0.0;
    }

    public CurrentAccount(double overdraftLimit, Customer customer) {
        super("Current", 0.0, customer);
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= getAccountBalance() + overdraftLimit) {
            setAccountBalance(getAccountBalance() - amount);
            System.out.println("Withdrawal of Rs." + amount + " successful.");
            System.out.println("Available Balance: Rs." + getAccountBalance());
        } else {
            System.out.println("Insufficient funds. Withdrawal not allowed.");
        }
    }

    @Override
    public void printAccountInfo() {
        super.printAccountInfo();
        System.out.println("Overdraft Limit: Rs." + overdraftLimit);
    }
    
}
