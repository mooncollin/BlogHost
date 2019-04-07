import static org.junit.Assert.*;

import java.sql.Connection;
import org.junit.Test;

import dbConnection.DBConnection;


public class DatabaseTests
{
	@Test
	public void dbConnectionTest() throws ClassNotFoundException
	{
		Connection c = DBConnection.getDBConnection();
		assertNotNull(c);
		DBConnection.closeDBConnection();
	}
}
