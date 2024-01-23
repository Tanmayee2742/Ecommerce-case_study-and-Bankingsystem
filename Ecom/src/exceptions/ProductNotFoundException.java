
package exceptions;

public class ProductNotFoundException extends EcomException {

   
	private static final long serialVersionUID = 1L;

	public ProductNotFoundException() {
        super("Product not found");
    }

    public ProductNotFoundException(int id) {
        super("Product not found for id: " + Integer.toString(id));
    }
}

