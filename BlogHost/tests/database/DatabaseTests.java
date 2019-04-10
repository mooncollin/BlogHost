package database;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import dbConnection.DBConnection;


public class DatabaseTests
{	
	@Test
	public void dbConnectionTest() throws SQLException
	{
		Connection c = DBConnection.getDBConnection();
		assertNotNull(c);
		c.close();
	}
}
