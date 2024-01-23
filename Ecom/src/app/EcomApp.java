
package app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import Entity.Customers;
import Entity.Products;
import dao.OrderProcessorRepository;
import dao.OrderProcessorRepositoryImp;

public class EcomApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderProcessorRepository orderProcessorRepository = new OrderProcessorRepositoryImp();

        while (true) {
            System.out.println("E-Commerce Application Menu:");
            System.out.println("1. Register Customer");
            System.out.println("2. Create Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Add to Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Place Order");
            System.out.println("7. View Customer Order");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    registerCustomer(scanner, orderProcessorRepository);
                    break;
                case 2:
                    createProduct(scanner, orderProcessorRepository);
                    break;
                case 3:
                    deleteProduct(scanner, orderProcessorRepository);
                    break;
                case 4:
                    addToCart(scanner, orderProcessorRepository);
                    break;
                case 5:
                    viewCart(scanner, orderProcessorRepository);
                    break;
                case 6:
                    placeOrder(scanner, orderProcessorRepository);
                    break;
                case 7:
                    viewCustomerOrder(scanner, orderProcessorRepository);
                    break;
                case 8:
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void placeOrder(Scanner scanner, OrderProcessorRepository orderProcessorRepository) {
		System.out.println("Enter order details to place order");
		System.out.println("Enter Customer ID:");
		int cus_id=scanner.nextInt();
		
		System.out.println("Enter shipping addresss");
		String addr=scanner.next();
		try {
		Customers customer = orderProcessorRepository.getCustomer(cus_id);
        if (customer != null) {
            Map<Products, Long> productsQuantityMap = new HashMap<>();
            System.out.print("Enter number of products in the order: ");
            int numProducts = scanner.nextInt();

            for (int i = 0; i < numProducts; i++) {
                System.out.print("Enter product ID: ");
                int pro_Id = scanner.nextInt();
                System.out.print("Enter quantity: ");
                Long quantity = scanner.nextLong();
                
               

                Products product = orderProcessorRepository.getProduct(pro_Id);
                if (product != null) {
                    productsQuantityMap.put(product, quantity);
                 
                } else {
                    System.out.println("Product not found. Please enter a valid product ID.");
                    return;
                }
            }

            boolean success = orderProcessorRepository.placeOrder(customer, productsQuantityMap,addr);
            if (success) {
                System.out.println("Order placed successfully!");
            } else {
                System.out.println("Failed to place order. Please try again.");
            }
        } else {
            System.out.println("Customer not found.");
        }}

        catch (Exception e) {
        System.out.println(e.getMessage());
    }
    }
		
	

	private static void viewCart(Scanner scanner, OrderProcessorRepository orderProcessorRepository) {	
		System.out.println("Enter details");
		  System.out.print("Enter customer ID: ");
	        int customerId = scanner.nextInt();
	        
	    

	        try {
	            Customers customer = orderProcessorRepository.getCustomer(customerId);
			


	            if (customer != null) {
	                List<Products> cartItems = orderProcessorRepository.getAllFromCart(customer);

	                if (cartItems.isEmpty()) {
	                    System.out.println("Cart is empty.");
	                } else {
	                    System.out.println("Cart Items:");
	                    for (Products product : cartItems) {
	                        System.out.println("Product ID: " + product.getProductId() +
	                                ", Name: " + product.getName() +
	                                ", Price:" + product.getPrice() +
	                                ", Quantity: " + product.getstockQuantity());
	                    }
	                }
	            }
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        }

		
	}

	private static void addToCart(Scanner scanner, OrderProcessorRepository orderProcessorRepository) {
		System.out.println("Add into cart");
		
		System.out.println("Customer id:");
		int cus_id=scanner.nextInt();
		
		System.out.println("Product id:");
		int pro_id=scanner.nextInt();
		
		System.out.println("Quantity");
		Long quantity=scanner.nextLong();
		
		boolean success = true;
		try {
			Customers customer = orderProcessorRepository.getCustomer(cus_id);
			Products product = orderProcessorRepository.getProduct(pro_id);
			orderProcessorRepository.addToCart(customer,product,quantity);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			success = false;
		}
		
        if (success) {
            System.out.println("Cart added successfully");
        } else {
            System.out.println("Failed to add cart Please try again.");
        }
	}

	private static void deleteProduct(Scanner scanner, OrderProcessorRepository orderProcessorRepository) {
		System.out.println("Enter product_id");
		int id=scanner.nextInt();
		 boolean success = orderProcessorRepository.deleteProduct(id);
	        if (success) {
	            System.out.println("Product deleted successfully!");
	        } else {
	            System.out.println("Failed to delete product. Please try again.");
	        }
			
	}

	private static void createProduct(Scanner scanner, OrderProcessorRepository orderProcessorRepository) {
        System.out.println("Enter product details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Price: ");
        Double price = scanner.nextDouble();
        System.out.print("Description: ");
        String description = scanner.next();
        System.out.print("StockQuantity ");
        Long stockQuantity = scanner.nextLong();

        Products product = new Products();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setstockQuantity(stockQuantity);

        boolean success = orderProcessorRepository.createProduct(product);
        if (success) {
            System.out.println("Product registered successfully!");
        } else {
            System.out.println("Failed to register product. Please try again.");
        }
		
	}

	private static void registerCustomer(Scanner scanner, OrderProcessorRepository orderProcessorRepository) {
        System.out.println("Enter customer details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Customers customer = new Customers();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPassword(password);

        boolean success = orderProcessorRepository.createCustomer(customer);
        if (success) {
            System.out.println("Customer registered successfully!");
        } else {
            System.out.println("Failed to register customer. Please try again.");
        }
    }


    private static void viewCustomerOrder(Scanner scanner, OrderProcessorRepository orderProcessorRepository) {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); 

        try {
			Customers customer = orderProcessorRepository.getCustomer(customerId);

            List<Map<Products, Long>> orders = orderProcessorRepository.getOrdersByCustomer(customerId);
            if (orders.isEmpty()) {
                System.out.println("No orders found for the customer.");
            } else {
                System.out.println("Customer Orders:");
                for (Map<Products, Long> order : orders) {
                    for (Entry<Products, Long> entry : order.entrySet()) {
                        Products product = entry.getKey();
                        Long quantity = entry.getValue();
                        System.out.println("Product: " + product.getName() + ", Quantity: " + quantity);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Customer not found. Please enter a valid customer ID.");
        }
    }


}
