package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Class used for establishing a database connection with the information in a
 * configuration file.
 */
public class DBConnection
{

	/**
	 * Gets the database connnection. If the database has not been connected yet, it
	 * will connect, otherwise it will return the already connected connection.
	 * 
	 * @return a database connection
	 * @throws ClassNotFoundException if the JDBC SQL driver library cannot be found
	 */
	public static Connection getDBConnection()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			UtilProp.loadProperty();
			Connection connection = DriverManager.getConnection(getURL(), getUserName(), getPassword());
			return connection;
		} catch (Exception e)
		{
		}

		return null;
	}

	/**
	 * Gets the connection URL.
	 * @return connection URL
	 */
	private static String getURL()
	{
		return UtilProp.getProp("url");
	}

	/**
	 * Gets the connection user.
	 * @return connection user
	 */
	private static String getUserName()
	{
		return UtilProp.getProp("user");
	}

	/**
	 * Gets the connection password.
	 * @return connection password 
	 */
	private static String getPassword()
	{
		return UtilProp.getProp("password");
	}
}