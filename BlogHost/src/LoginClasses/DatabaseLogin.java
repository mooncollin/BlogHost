package LoginClasses;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import BlogHostHome.UserAccount;

public class DatabaseLogin {
	public static boolean validate(HttpServletRequest request, String name, String password) {
		boolean status = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement secondStatement = null;
		password = LoginUtil.makeHash(name, password);
		try {
			DBConnection.getDBConnection();
	         connection = DBConnection.connection;
	         System.out.println(password);
	         String selectSQL = "SELECT * FROM BlogHostCreators where USER_NAME=? and PASSWORD=?";
	         preparedStatement = connection.prepareStatement(selectSQL);
	         preparedStatement.setString(1,name);
	         preparedStatement.setString(2,password);
	         
	         ResultSet resultSet = preparedStatement.executeQuery();
	         status = resultSet.next();
	         if(status) {
	        	 int userID = Integer.parseInt(resultSet.getString("id").trim());
	        	 String userName = resultSet.getString("user_name").trim();
	             String firstName = resultSet.getString("first_name").trim();
	             String lastName = resultSet.getString("last_name").trim();
	             int age = Integer.parseInt(resultSet.getString("age").trim());
	             UserAccount user = new UserAccount(userID, userName, firstName, lastName, age);
	             System.out.println(" user username -> " + user.getUserName());
	             LoginUtil.storeUser(request.getSession(), user);
	         }
	         resultSet.close();
	         preparedStatement.close();
	         connection.close();
	      } catch (SQLException se) {
	         se.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      } finally {
	         try {
	            if (preparedStatement != null)
	               preparedStatement.close();
	         } catch (SQLException se2) {
	         }
	         try {
	            if (connection != null)
	               connection.close();
	         } catch (SQLException se) {
	            se.printStackTrace();
	         }
	      }
		return status;
	}
}
