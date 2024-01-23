package Entity;

public class Customers {
	private int customer_id;
	private String name;
	private String email;
	private String password;
	

	public Customers() {
	}
	public Customers(int customer_id,String name,String email,String password) {
		this.customer_id=customer_id;
		this.name=name;
		this.email=email;
		this.password=password;
	}
	public int getCustomerID() {
		return customer_id;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	
	
	public void setCustomerId(int customer_id) {
        this.customer_id = customer_id;
    }
	public void setName(String name) {
        this.name = name;
    }
	public void setEmail(String email) {
        this.email = email;
    }
	public void setPassword(String password) {
        this.password = password;
    }
	
	

	public void printCustomerInformation() {
        System.out.println("Customer ID: " + customer_id);
        System.out.println("First Name: " + name);
        System.out.println("Email Address: " + email);
        System.out.println("Password: " + password);
    }
}
