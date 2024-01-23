package Entity;

public class Cart {
	private int cart_id;
	private int customer_id;
	private int product_id;
	private long quantity;

	public Cart() {
		
	}
	public Cart(int cart_id,int customer_id,int product_id,long quantity) {
		this.cart_id=cart_id;
		this.customer_id=customer_id;
		this.product_id=product_id;	
		this.quantity=quantity;

		

	}
	public int getCartId() {
		return cart_id;
	}
	public int getCustomerID() {
		return customer_id;
	}
	public int getProductId() {
		return product_id;
	}
	public long getQuantity() {
		return quantity;
	}
	
	public void setCartId(int cart_id) {
        this.cart_id = cart_id;
    }
	public void setCustomerId(int customer_id) {
        this.customer_id = customer_id;
    }
	public void setProductId(int product_id) {
        this.product_id = product_id;
    }
	public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
	public void PrintCartInfo() {
		 System.out.println("Cart ID: " + cart_id);
        System.out.println("Customer ID: " + customer_id);
		 System.out.println("Product ID: " + product_id);
		 System.out.println("quantity: " + quantity);



	}
	

}
