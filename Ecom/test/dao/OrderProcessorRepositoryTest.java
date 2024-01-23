package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import Entity.Customers;
import Entity.Products;
import exceptions.CustomerNotFoundException;
import exceptions.ProductNotFoundException;
import util.DBConnection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderProcessorRepositoryTest {

	OrderProcessorRepository ord = new OrderProcessorRepositoryImp();
	Customers customer1 = new Customers(1, "John", "john@gmail.com", "pwd");
	Customers customer2 = new Customers(2, "James", "james@gmail.com", "pwd2");	
	Products product1 = new Products(1, "iPhone", 100, "Apple's iPhone", 10000);
	Products product2 = new Products(2, "iPad", 500, "Apple's iPad", 5000);
	Products product3 = new Products(3, "iMac", 700, "Apple's iMac", 2000);
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.out.println("Running...");
		// clear database
		 try (Connection connection = DBConnection.getConnection();
	             PreparedStatement statement = connection.prepareStatement(
	            		 "SET FOREIGN_KEY_CHECKS = 0; \r\n"+
	                     "TRUNCATE TABLE cart; \r\n" +
	                     "TRUNCATE TABLE order_items; \n" +
	                     "TRUNCATE TABLE orders; \n" +
	                     "TRUNCATE TABLE products; \n" +
	                     "TRUNCATE TABLE customers; \n" +
	            		 "SET FOREIGN_KEY_CHECKS = 1; ")) {
			 statement.execute();
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	@Order(1)
	void testCreateProduct() {
		boolean result1 = ord.createProduct(product1);
		boolean result2 = ord.createProduct(product2);
		boolean result3 = ord.createProduct(product3);
		assertTrue(result1);
		assertTrue(result2);
		assertTrue(result3);
	}

	@Test
	@Order(2)
	void testCreateCustomer() {
		boolean result1 = ord.createCustomer(customer1);
		assertTrue(result1);
		boolean result2 = ord.createCustomer(customer2);
		assertTrue(result2);
	}
	
	@Test
	@Order(3)
	void testGetCustomer() {
		try {
			Customers customer = ord.getCustomer(1);
			assertEquals(customer.getName(), customer1.getName());
			assertEquals(customer.getEmail(), customer1.getEmail());
		} catch (CustomerNotFoundException e) {
			fail("Get Customer Failed");
		}
	}
	
	@Test
	@Order(4)
	void testGetCustomerFail() {
		assertThrowsExactly(CustomerNotFoundException.class, () -> ord.getCustomer(10000));
	}

	@Test
	@Order(5)
	void testGetProduct() {
		try {
			Products product = ord.getProduct(1);
			assertEquals(product.getName(), product1.getName());
		} catch (ProductNotFoundException e) {
			System.out.println(e.getMessage());
			fail("Get Product Failed");
		}
	}


	@Test
	@Order(6)
	void testDeleteProduct() {
		boolean success = ord.deleteProduct(3);
		assertTrue(success);
	}

	@Test
	@Order(7)
	void testDeleteCustomer() {
		boolean success = ord.deleteCustomer(2);
		assertTrue(success);
	}

	@Test
	@Order(8)
	void testAddToCart() {
		boolean success = ord.addToCart(customer1, product1, 10L);
		assertTrue(success);
	}

	@Test
	@Order(9)
	void testRemoveFromCart() {
		ord.addToCart(customer1, product2, 10L);
		boolean success = ord.removeFromCart(customer1, product2);
		assertTrue(success);

	}

	@Test
	@Order(10)
	void testGetAllFromCart() {
		List<Products> result = ord.getAllFromCart(customer1);
		result.get(0).PrintProductInfo();
	}
	
	@Test
	@Order(11)
	void testPlaceOrder() {
		Map<Products, Long> products = new HashMap<Products, Long>();
		products.put(product1, 10L);
		products.put(product2, 5L);
		boolean result = ord.placeOrder(customer1, products, "USA");
		assertTrue(result);
	}

	@Test
	@Order(12)
	void testGetOrdersByCustomer() {
		List<Map<Products,Long>> result = ord.getOrdersByCustomer(1);
		result.get(0);
	}

}
