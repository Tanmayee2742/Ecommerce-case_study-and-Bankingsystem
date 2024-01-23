
package exceptions;

public class CustomerNotFoundException extends EcomException {

   
	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException() {
        super("Customer not found");
    }

    public CustomerNotFoundException(int id) {
        super("Customer not found for id: " + Integer.toString(id));
    }
}

