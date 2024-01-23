
package exceptions;

public class OrderNotFoundException extends EcomException {

   
	private static final long serialVersionUID = 1L;

	public OrderNotFoundException() {
        super("Order not found");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
