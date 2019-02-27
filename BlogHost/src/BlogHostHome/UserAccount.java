package BlogHostHome;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import LoginClasses.DBConnection;

public class UserAccount {
	private int id;
	private String userName;
	private String firstName;
	private String lastName;
	private int age;
	private int[] modForUsers;
	private int[] mods;
	
	public UserAccount() {
		
	}
	
	public UserAccount(int id, String name, String firstName, String lastName, int age) {
		this.id = id;
		this.userName = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		modForUsers = getModForFromDatabase();
		mods = getModsFromDatabase();
		
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String name) {
		this.userName = name;
	}
	
	public int getUserID() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String name) {
		this.firstName = name;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String name) {
		this.lastName = name;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public int[] getModFor() {
		return modForUsers;
	}
	
	public int[] getMods() {
		return mods;
	}
	
	public void setMods(int[] mods) {
		this.mods = mods;
	}
	
	public int[] getModsFromDatabase() {
		int[] modArray = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			DBConnection.getDBConnection();
	        connection = DBConnection.connection;
	        ArrayList<Integer> mods = new ArrayList<>();
	        String selectSQL = "SELECT * FROM BlogHostMods where CREATOR_ID=?";
	        preparedStatement = connection.prepareStatement(selectSQL);
	        preparedStatement.setString(1, String.valueOf(id) );
	        ResultSet rs = preparedStatement.executeQuery();
	        while(rs.next()) {
	        	mods.add(Integer.parseInt(rs.getString("MOD_ID").trim()));
	        }
	        modArray = new int[mods.size()];
	        for (int i = 0; i < mods.size(); i++) {
	        	modArray[i] = mods.get(i);
	        }
	        rs.close();
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
		return modArray;
	}
	
	public int[] getModForFromDatabase() {
		int[] modForArray = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			DBConnection.getDBConnection();
	        connection = DBConnection.connection;
	        ArrayList<Integer> modFor = new ArrayList<>();
	        String selectSQL = "SELECT * FROM BlogHostMods where MOD_ID=?";
	        preparedStatement = connection.prepareStatement(selectSQL);
	        preparedStatement.setString(1, String.valueOf(id) );
	        ResultSet rs = preparedStatement.executeQuery();
	        while(rs.next()) {
	        	modFor.add(Integer.parseInt(rs.getString("CREATOR_ID").trim()));
	        }
	        modForArray = new int[modFor.size()];
	        for (int i = 0; i < modFor.size(); i++) {
	        	modForArray[i] = modFor.get(i);
	        }
	        rs.close();
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
		return modForArray;
	}
	
	
	public static int getUserID(String userNameIn) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int result = -1;
		try {
			DBConnection.getDBConnection();
	        connection = DBConnection.connection;
	         String selectSQL = "SELECT * FROM BlogHostCreators where USER_NAME=?";
	         preparedStatement = connection.prepareStatement(selectSQL);
	         preparedStatement.setString(1, userNameIn);
	         
	         ResultSet resultSet = preparedStatement.executeQuery();
	         resultSet.next();
	         result = Integer.parseInt(resultSet.getString("id").trim());
	         
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
		return result;
	}
	
	
}
