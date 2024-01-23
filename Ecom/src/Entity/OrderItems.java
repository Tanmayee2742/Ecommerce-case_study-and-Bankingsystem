package Entity;

public class OrderItems {
	    private int order_item_id;
	    private int order_id;
	    private int product_id;
	    private long quantity;


	public OrderItems() {
	}
	public OrderItems(int order_item_id, int order_id, int product_id,long quantity) {
		this.order_item_id=order_item_id;
		this.order_id=order_id;
		this.product_id=product_id;
		this.quantity=quantity;

	}
	public int getOrderItemId() {
		return order_item_id;
	}
	public int getOrderId() {
		return order_id;
	}
	public int getProductId() {
		return product_id;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setOrderItemId(int order_item_id) {
        this.order_item_id = order_item_id;
    }
	public void setOrderId(int order_id) {
        this.order_id = order_id;
    }
	public void setproductId(int product_id) {
        this.product_id = product_id;
    }
	public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
	public void PrintOrderItemInfo() {
		System.out.println("Order Item ID: " + order_item_id);

		System.out.println("Order ID: " + order_id);

		System.out.println("product_id: " + product_id);

		System.out.println("quantity: " + quantity);

	}

}
