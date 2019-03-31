import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;

import org.junit.BeforeClass;
import org.junit.Test;

import dbConnection.DBConnection;
import dbConnection.UtilProp;


public class DatabaseTests
{
	@BeforeClass
	public static void setup() throws IOException
	{
		UtilProp.loadProperty();
	}
	
	@Test
	public void dbConnectionTest() throws ClassNotFoundException
	{
		Connection c = DBConnection.getDBConnection();
		assertNotNull(c);
	}
}
