package Entity;

public class Products {
	private int product_id;
	private String name;
	private double price;
	private String description;
	private long stockQuantity;
	public Products() {
	}
	public Products(int product_id,String name,double price,String description,long stockQuantity) {
		this.product_id=product_id;
		this.name=name;
		this.price=price;
		this.description=description;
		this.stockQuantity=stockQuantity;
	}
	public int getProductId() {
		return product_id;
	}
	public String getName() {
		return name;
	}
	public double getPrice() {
		return price;
	}
	public String getDescription() {
		return description;
	}
	public long getstockQuantity() {
		return stockQuantity;
	}
	
	
	public void setProductId(int product_id) {
        this.product_id = product_id;
    }
	public void setName(String name) {
        this.name = name;
    }
	public void setPrice(double price) {
        this.price = price;
    }
	public void setDescription(String description) {
        this.description = description;
    }
	public void setstockQuantity(long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
	public void PrintProductInfo() {
		 System.out.println("Product ID: " + product_id);
		 System.out.println("Name: " + name);
		 System.out.println("Price: " + price);
		 System.out.println(" description: " + description);
		 System.out.println("stockQuantity " + stockQuantity);
	}
	
	
	
	

}
