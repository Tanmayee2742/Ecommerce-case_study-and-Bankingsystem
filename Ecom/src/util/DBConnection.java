
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static String props;

	public static Connection getConnection()
	{
		Connection connection=null;
		String path = "C:\\Users\\tanma\\eclipse-workspace1\\Ecom\\src\\database.properties";
		props = PropertyUtil.getPropertyString(path);;
		String[] properties=props.split(" ");
		try {
			Class.forName(properties[2]);
			System.out.println("class loaded");
		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Established");
			e.printStackTrace();
		}
		
		try {
			connection = DriverManager.getConnection(  
					properties[3],properties[0],properties[1]);
			System.out.println("con established");
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return connection;
	}

}

