

package dao;


import Entity.Customers;

import Entity.Products;
import exceptions.CustomerNotFoundException;
import exceptions.ProductNotFoundException;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderProcessorRepositoryImp implements OrderProcessorRepository {

    @Override
    public boolean createProduct(Products product) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO products (name, price, description, stockQuantity) VALUES (?,?,?,?)")) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setString(3, product.getDescription());
            statement.setLong(4, product.getstockQuantity());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createCustomer(Customers customer) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO customers (name, email, password) VALUES (?, ?, ?)")) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPassword());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   

    @Override
    public List<Map<Products, Long>> getOrdersByCustomer(int customerId) {
        List<Map<Products, Long>> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT p.*, oi.quantity FROM order_items oi " +
                             "JOIN orders o ON oi.order_id = o.order_id " +
                             "JOIN products p ON oi.product_id = p.product_id " +
                             "WHERE o.customer_id = ?")) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Products product = new Products();
                    product.setProductId(resultSet.getInt("product_id"));
                    product.setName(resultSet.getString("name"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setDescription(resultSet.getString("description"));
                    product.setstockQuantity(resultSet.getInt("stockQuantity"));


                    Map<Products, Long> orderItem = new HashMap<>();
                    Long quantity = resultSet.getLong("quantity");
					orderItem.put(product, quantity);

                    orders.add(orderItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    @Override
    public Customers getCustomer(int customerId) throws CustomerNotFoundException {
    	Customers customer = new Customers();
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM customers  WHERE customer_id = ?")) {
               statement.setInt(1, customerId);
               try (ResultSet resultSet = statement.executeQuery()) {
                   if (resultSet.next()) {
                	   customer.setCustomerId(resultSet.getInt("customer_id"));
                	   customer.setName(resultSet.getString("name"));
                	   customer.setEmail(resultSet.getString("email"));
                   } else {
                       throw new CustomerNotFoundException(customerId);
                   }
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
        return customer;
    }
    
    
    @Override
    public Products getProduct(int productId) throws ProductNotFoundException {
    	Products product = new Products();
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM products  WHERE product_id = ?")) {
               statement.setInt(1, productId);
               try (ResultSet resultSet = statement.executeQuery()) {
                   if (resultSet.next()) {
                	   product.setProductId(resultSet.getInt("product_id"));
                	   product.setName(resultSet.getString("name"));
                	   product.setPrice(resultSet.getDouble("price"));
                	   product.setDescription(resultSet.getString("price"));
                	   product.setstockQuantity(resultSet.getLong("price"));
                   } else {
                	   throw new ProductNotFoundException(productId);
                   }
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
        return product;
    }
	@Override
	public boolean deleteProduct(int productId) {
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                		"DELETE from products where product_id=?") ) {
        	statement.setInt(1,productId);
        	statement.execute();
    		return true;

        }
        catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
  		return false;

        }

        
		
	}

	@Override
	public boolean deleteCustomer(int customerId) {
		 try (Connection connection = DBConnection.getConnection();
	                PreparedStatement statement = connection.prepareStatement(
	                		"DELETE from customers where customer_id=?") ) {
	        	statement.setInt(1,customerId);
	        	statement.execute();
	        	return true;
	        }
	        catch (Exception e)
	        {
	          System.err.println("Got an exception! ");
	          System.err.println(e.getMessage());
	          return false;
	        }		
	}

	@Override
	public boolean addToCart(Customers customer, Products product, Long quantity) {
		try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                		"INSERT INTO cart (customer_id,product_id,quantity) values (?,?,?)" )){
			statement.setInt(1,customer.getCustomerID());
			statement.setInt(2,product.getProductId());
			statement.setLong(3,quantity);
			statement.execute();
			return true;
		} catch (SQLException e) {
            e.printStackTrace();
            return false;
		}
	}

	@Override
	public boolean removeFromCart(Customers customer, Products product) {
		try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                		"DELETE from cart where customer_id=? AND product_id=?")){
			statement.setInt(1,customer.getCustomerID());
			statement.setInt(2, product.getProductId());;
			statement.execute();
			return true;
		}
		 catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
          return false;
        }
	}

	@Override
	public List<Products> getAllFromCart(Customers customer) {
		 List<Products> cartItems = new ArrayList<>();
	        try (Connection connection = DBConnection.getConnection();
	             PreparedStatement statement = connection.prepareStatement(
	    "SELECT p.* FROM cart c  JOIN products p ON c.product_id = p.product_id WHERE c.customer_id = ?")) {
	            statement.setInt(1, customer.getCustomerID());
	            try (ResultSet resultSet = statement.executeQuery()) {
	                while (resultSet.next()) {
	                    Products product = new Products();
	                    product.setProductId(resultSet.getInt("product_id"));
	                    product.setName(resultSet.getString("name"));
	                    product.setPrice(resultSet.getDouble("price"));
	                    product.setDescription(resultSet.getString("description"));
	                    product.setstockQuantity(resultSet.getInt("stockQuantity"));
	                    cartItems.add(product);
	                }}}
	            catch (SQLException e) {
	                    e.printStackTrace();}
	            
		return cartItems;
	}

	@Override
	public boolean placeOrder(Customers customer, Map<Products, Long> productsQuantityMap,String shippingAddress) {
		try (Connection connection = DBConnection.getConnection()){
		 connection.setAutoCommit(false);

	        try (
	        	PreparedStatement orderStatement = connection.prepareStatement(
	        "INSERT INTO orders (customer_id, order_date, total_price, shipping_address) "
	        + "VALUES (?, NOW(), ?, ?) ", Statement.RETURN_GENERATED_KEYS );
	            
	        	PreparedStatement orderItemStatement = connection.prepareStatement(
	            "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)")) {
	
		            double totalOrderPrice = 0;
		
		            orderStatement.setInt(1, customer.getCustomerID());
		            orderStatement.setDouble(2, totalOrderPrice);
		            orderStatement.setString(3, shippingAddress);

	                if (orderStatement.executeUpdate() > 0) {
	                    ResultSet generatedKeys = orderStatement.getGeneratedKeys();
	                    if (generatedKeys.next()) {
	                        int orderId = generatedKeys.getInt(1);

	                            for (Map.Entry<Products, Long> productEntry : productsQuantityMap.entrySet()) {
	                                Products product = productEntry.getKey();
	                                Long quantity=productEntry.getValue();

	                                orderItemStatement.setInt(1, orderId);
	                                orderItemStatement.setInt(2, product.getProductId());
	                                orderItemStatement.setLong(3, quantity);

	                                if (orderItemStatement.executeUpdate() > 0) {
	                                    totalOrderPrice += product.getPrice() * quantity;
	                                } else {
	                                    connection.rollback();
	                                    return false;
	                                }
	                            }
	                       

	                        orderStatement.setDouble(2, totalOrderPrice);
	                        connection.commit();
	                        return true;
	                    }
	                }

	                connection.rollback();
	                return false;
	            } catch (SQLException e) {
	                connection.rollback();
	                e.printStackTrace();
	                return false;
	            } finally {
	                connection.setAutoCommit(true);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	}
}
