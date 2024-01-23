package Entity;

public class Orders {
	private int order_id;
	private int customer_id;
	private int order_date;
	private long total_price;
	private String shipping_address;
	public Orders() {
	}
	public Orders(int order_id,int customer_id,int order_date,long total_price,String shipping_address)
	{
		this.order_id=order_id;
		this.customer_id=customer_id;
		this.order_date=order_date;
		this.total_price=total_price;
		this.shipping_address=shipping_address;

	}
	public int getOrderId() {
		return order_id;
	}
	public int getCustomerId() {
		return customer_id;
	}
	public int getOrderDate() {
		return order_date;
	}
	public long getTotalPrice() {
		return total_price;
	}
	public String getShippingAddress() {
		return shipping_address;
	}
	
	public void setOrderid(int order_id) {
        this.order_id = order_id;
    }
	public void setCustomerId(int customer_id) {
        this.customer_id = customer_id;
    }
	public void setOrdeDate(int order_date) {
        this.order_date = order_date;
    }
	public void setTotalPrice(long total_price) {
        this.total_price = total_price;
    }
	public void setShippingAddress(String shipping_address) {
        this.shipping_address = shipping_address;
    }
	public void PrintOrderInfo() {
		System.out.println("Order ID: " + order_id);
		System.out.println("Customer ID: " + customer_id);
		System.out.println("order_date: " + order_date);
		System.out.println("total_price: " + total_price);
		System.out.println("shipping_address: " + shipping_address);
	}
	
}
