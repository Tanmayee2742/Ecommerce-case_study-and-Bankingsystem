package entity;

public class ZeroBalanceAccount extends Account {
    public ZeroBalanceAccount() {
        super("Zero Balance", 0.0, null);
    }

    public ZeroBalanceAccount(Customer customer) {
        super("Zero Balance", 0.0, customer);
    }

    @Override
    public void printAccountInfo() {
        super.printAccountInfo();
    }
}
