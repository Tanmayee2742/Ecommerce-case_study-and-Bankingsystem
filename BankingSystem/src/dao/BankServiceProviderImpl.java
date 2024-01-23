package dao;
import entity.*;
import exception.*;

import java.util.*;
public class BankServiceProviderImpl extends CustomerServiceProviderImpl implements IBankServiceProvider {
	private Map<Long, Account> accountList;
    BankRepositoryImpl bankdb=null;
	private String branchName;
	private String branchAddress;
	public BankServiceProviderImpl() {
		
		
	}
    public BankServiceProviderImpl(String branchName, String branchAddress) {
        this.branchName = branchName;
        this.branchAddress = branchAddress;

        bankdb=new BankRepositoryImpl();
        accountList=listAccounts();
      
    }

	@Override
    public Account createAccount(Customer customer, long accNo, String accType, double balance) {
      
    	Account account=null;
        if ("Savings".equals(accType)) {
            account = new SavingsAccount(4.5, customer);  
        } else if ("Current".equals(accType)) {
            account = new CurrentAccount(0.0, customer); 
        } else if ("ZeroBalance".equals(accType)){
            account = new ZeroBalanceAccount(customer);
        }
        else
        {
        	System.out.println("Invalid Account Type");
        	return null;
        }

        account.setAccountNumber(accNo);
        account.setAccountBalance(balance);

        accountList.put(accNo,account);
        bankdb.createAccount(customer, accNo, accType, (float) balance);
        return account;
    }

    @Override
    public Map<Long, Account> listAccounts() {
    	accountList=castToMap(bankdb.listAccounts());
    	
//    	if(accountList.size()==0)
//    	{
//    		throw new NullPointerException("No Accounts created");
//    	}
////    	System.out.println("accounts:;");
////    	System.out.println(accountList.size());
        return accountList;
    }

    private Map<Long, Account> castToMap(List<Account> listAccounts) {
    	Map<Long,Account> hm=new HashMap<>();
    	for(int i=0;i<listAccounts.size();i++)
    	{
    		hm.put(listAccounts.get(i).getAccountNumber(), listAccounts.get(i));
    	}
		return hm;
	}

	@Override
    public void calculateInterest() {
    	
    	for (Map.Entry<Long, Account> entry : accountList.entrySet()) {
    		long accountNumber = entry.getKey();
            Account account = entry.getValue();
    		if (account instanceof SavingsAccount) {
                double interestRate = ((SavingsAccount) account).getInterestRate();
                double interest = (account.getAccountBalance()/100) * interestRate;
                account.setAccountBalance(account.getAccountBalance() + interest);
                accountList.put(accountNumber, account);
                System.out.println("Interest calculated for Savings Account " + account.getAccountNumber() +
                        ": Rs." + interest);
            }
    	}
    	
            
        }
   
    public Account findAccountObject(long accountNumber)
    {
    	if(accountList.get(accountNumber)!=null)
    	{
    		return accountList.get(accountNumber);
    	}
    	return null;
    }
    public void setAccountObject(Account acc)
    {
    	accountList.put(acc.getAccountNumber(),acc);
    	
    }
    
    @Override
    public double getAccountBalance(long accountNumber) {
    	Account acc=findAccountObject(accountNumber);
    	if(acc==null)
    	{
    		throw new InvalidAccountException("No account Found");
    	}
    	return bankdb.getAccountBalance(accountNumber);
    }
    
    public double deposit(long accountNumber, double amount) {
      
    	Account acc=findAccountObject(accountNumber);
    	if(acc==null)
    	{
    		System.out.println("Receiver Account Invalid");
    		throw new InvalidAccountException("Receiver Account Invalid");
    	}
    	acc.setAccountBalance(acc.getAccountBalance()+amount);
    	
    	Transaction tran=new Transaction(acc,"Deposit by self","Deposit",amount);
    	bankdb.deposit(accountNumber,(float) amount);
    	bankdb.addTransaction(tran);
    	accountList=listAccounts();
    	return bankdb.getAccountBalance(accountNumber);
    	
    }

    @Override
    public double withdraw(long accountNumber, double amount) {
    	Account account = findAccountObject(accountNumber);	
        if (account != null) {
        	try
        	{
        		setAccountObject(account);
                Transaction tran=new Transaction(account,"withdraw by self","Withdraw",amount);
            	bankdb.withdraw(accountNumber,(float) amount);
            	bankdb.addTransaction(tran);
            	accountList=listAccounts();
            	return bankdb.getAccountBalance(accountNumber);
        	}
        	catch (InvalidAccountException e) {
        		System.out.println("Sender Account Invalid");
    			throw new InvalidAccountException("Sender Account Invalid");
    		}
        	catch (InsufficientFundException e) {
    			
        		System.out.println("Insufficient Funds in sender account");
    			throw new InsufficientFundException("Insufficient Funds in sender account");
    		}
        	catch (OverDraftLimitExcededException e) {
        		System.out.println("Overdraft Limit Exceeded");
    			throw new OverDraftLimitExcededException("Overdraft Limit Exceeded");
    		}
            
            
        } else {
           throw new InvalidAccountException("Account Not Found");
        }
    }
    @Override
    public void transfer(long fromAccountNumber, long toAccountNumber, double amount) {
//   
    	if(accountList.containsKey(fromAccountNumber)==false)
    	{
    		System.out.println("Sender Account Invalid");
    		throw new InvalidAccountException("Sender Account Invalid");
    	}
    	if(accountList.containsKey(toAccountNumber)==false)
    	{
    		System.out.println("Receiver Account Invalid");
    		throw new InvalidAccountException("Receiver Account Invalid");
    	}
    	try {
			withdraw(fromAccountNumber,amount);
		} catch (InvalidAccountException e) {
			throw new InvalidAccountException("Sender Account Invalid");
		}
    	catch (InsufficientFundException e) {
			throw new InsufficientFundException("Insufficient Funds in sender account");
		}
    	catch (OverDraftLimitExcededException e) {
			throw new OverDraftLimitExcededException("Overdraft Limit Exceeded");
		}
    	try
    	{
        	deposit(toAccountNumber,amount);
    	}
    	catch (InvalidAccountException e) {
			
    		double newAmount=deposit(fromAccountNumber,amount);
    		System.out.println("Deposited back to Sender account, new balance Rs. "+newAmount);
			throw new InvalidAccountException("Receiver Account Invalid");
    	}

    	
        System.out.println("Transferred Rs." + amount + " from account " + fromAccountNumber + " to account " + toAccountNumber);
    }
    public String getAccountDetails(long accountNumber) {
    	Account account = findAccountObject(accountNumber);
    	if(account==null)
    	{
    		throw new InvalidAccountException("Invalid Account Number");
    	}
    	String customerdetails=" Customer Firstname: "+account.getCustomer().getFirstName()+" Customer lastname: "+account.getCustomer().getLastName()+" Customer ID: "+account.getCustomer().getCustomerId()+" Customer email: "+account.getCustomer().getEmail()+" Customer Phonenumber: "+account.getCustomer().getPhoneNumber()+" Customer address: "+account.getCustomer().getAddress();
    	String result=" Account Type: "+account.getAccountType()+" Account Balance: "+account.getAccountBalance();
        return "Account details for account number " + accountNumber+result+customerdetails;
    }
    @Override
	public List<Transaction> getTransactions(long accountNumber, String startDate, String endDate) {
		return bankdb.getTransactions(accountNumber, startDate, endDate);
	}
    
   
}
