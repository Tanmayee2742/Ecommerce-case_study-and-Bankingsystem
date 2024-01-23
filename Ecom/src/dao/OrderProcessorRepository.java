
package dao;

import Entity.Customers;
import Entity.Products;
import exceptions.CustomerNotFoundException;
import exceptions.ProductNotFoundException;

import java.util.List;
import java.util.Map;
public interface OrderProcessorRepository {
    boolean createProduct(Products product);

    boolean createCustomer(Customers customer);

    boolean deleteProduct(int productId);

    boolean deleteCustomer(int customerId);

    boolean addToCart(Customers customer, Products product, Long quantity);

    boolean removeFromCart(Customers customer, Products product);

    List<Products> getAllFromCart(Customers customer);


    List<Map<Products, Long>> getOrdersByCustomer(int customerId);
    
    public Customers getCustomer(int customerId) throws CustomerNotFoundException;

	public Products getProduct(int productId) throws ProductNotFoundException;

	boolean placeOrder(Customers customer, Map<Products, Long> productsQuantityMap, String shippingAddress);

	
}
