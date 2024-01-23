package entity;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount() {
        super("Savings", 500.0, null);
    }

    public SavingsAccount(double interestRate, Customer customer) {
        super("Savings", 500.0, customer);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public void printAccountInfo() {
        super.printAccountInfo();
        System.out.println("Interest Rate: " + interestRate + "%");
    }
}

