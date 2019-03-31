package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class used for establishing a database connection with the information in a
 * configuration file.
 */
public class DBConnection
{
	/**
	 * The database connection.
	 */
	private static Connection connection = null;

	/**
	 * Gets the database connnection. If the database has not been connected yet, it
	 * will connect, otherwise it will return the already connected connection.
	 * 
	 * @return a database connection
	 * @throws ClassNotFoundException if the JDBC SQL driver library cannot be found
	 */
	public static Connection getDBConnection() throws ClassNotFoundException
	{
		if (connection != null)
		{
			return connection;
		}

		Class.forName("com.mysql.jdbc.Driver");
		connection = null;
		try
		{
			UtilProp.loadProperty();
			connection = DriverManager.getConnection(getURL(), getUserName(), getPassword());
		} catch (Exception e)
		{
		}

		return connection;
	}

	/**
	 * Attempts to close the current database connection if connected.
	 */
	public static void closeDBConnection()
	{
		if (connection != null)
		{
			try
			{
				connection.close();
			} catch (SQLException e)
			{
			}
			connection = null;
		}
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