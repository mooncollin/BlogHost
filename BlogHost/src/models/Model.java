package models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbConnection.DBConnection;

/**
 * This class is the base for all database schemas. It has helper functions
 * for generic pulling of data and dynamically creating model objects
 * based on given parameters.
 * @author collin
 *
 */
abstract public class Model
{
	/**
	 * Whether this object's attributes have changed for commiting.
	 */
	protected boolean changed;
	
	/**
	 * Retrieves all rows of the given database schema class name.
	 * @param clazz class that represents a database schema
	 * @return list of models
	 */
	public static <T extends Model> List<T> getAll(Class<T> clazz)
	{
		return getAll(clazz, null);
	}
	
	/**
	 * Retrieves all rows of the given database schema class name, with
	 * the given filter. This will automatically add 'where' in the SQL,
	 * so the given filter is the actual boolean expressions.
	 * @param clazz class that represents a database schema
	 * @param filter string that contains what would be after 'where'
	 * @param inserts any data to be inserted into '?' in the filter
	 * @return list of models
	 */
	public static <T extends Model> List<T> getAll(Class<T> clazz, String filter, Object... inserts)
	{
		Connection connection = null;
		String sql = "select * from " + clazz.getSimpleName();
		if(filter != null)
		{
			sql += " where " + filter;
		}
		List<T> models;
		try
		{
			connection = DBConnection.getDBConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			if(filter != null)
			{
				for(int i = 0; i < inserts.length; i++)
				{
					statement.setObject(i+1, inserts[i]);
				}
			}
			ResultSet results = statement.executeQuery();
			models = new ArrayList<T>(results.getFetchSize());
			ResultSetMetaData metaData = results.getMetaData();
			while(results.next())
			{
				int numberOfColumns = metaData.getColumnCount();
				Map<String, Object> columnToValue = new HashMap<String, Object>(numberOfColumns);
				
				for(int i = 1; i <= numberOfColumns; i++)
				{
					String columnName = metaData.getColumnName(i).toLowerCase();
					Object value = results.getObject(i, Class.forName(metaData.getColumnClassName(i)));
					columnToValue.put(columnName, value);
				}
				
				models.add(clazz.getConstructor(Map.class).newInstance(columnToValue));
			}
		} catch (Exception e)
		{
			return null;
		}
		
		try
		{
			connection.close();
		} catch (SQLException e)
		{
		}
		return models;
	}
	
	/**
	 * Constructor. Sets this object's variables from the given map.
	 * @param columnParameters a map that contains database schema column names
	 * to values
	 * @throws IllegalArgumentException if the set of column names given are not
	 * equilvalent to the described schema.
	 */
	public Model(Map<String, Object> columnParameters) throws IllegalArgumentException
	{
		parseColumnParameters(columnParameters);
		changed = false;
	}
	
	/**
	 * Constructor. Needed blank constructor for child classes.
	 */
	public Model()
	{
		
	}
	
	/**
	 * Parses the given parameters and sets the column values to this model's instance
	 * variables.
	 * @param columnParameters column name -> column value mappings
	 * @throws IllegalArgumentException if the set of column names given are not
	 * equilvalent to the described schema.
	 */
	private void parseColumnParameters(Map<String, Object> columnParameters) throws IllegalArgumentException
	{
		Map<String, Method> parameters = getColumnParameters();
		if(!columnParameters.keySet().equals(parameters.keySet()))
		{
			throw new IllegalArgumentException("Column parameters do not match");
		}
		
		for(String column : parameters.keySet())
		{
			try
			{
				parameters.get(column).invoke(this, columnParameters.get(column));
			} catch (IllegalAccessException | InvocationTargetException e)
			{
			}
		}
	}
	
	/**
	 * Returns the changed status of this model. A model is considered changed if
	 * one of its instance variables is changed from a different value than before.
	 * Once a model is committed successfully, the model is considered unchanged.
	 * If the model is deleted successfully, then it is considered changed.
	 * @return whether this model has been changed since last commit
	 */
	public boolean changed()
	{
		return changed;
	}
	
	/**
	 * Checks the current column value and the new column value whether
	 * or not the new column value should override the current column value.
	 * @param thisObj the current column value
	 * @param otherObj the new column value
	 * @return true if the value should be overridden, false otherwise
	 */
	protected boolean checkVariable(Object thisObj, Object otherObj)
	{
		return (thisObj == null && otherObj != null) || thisObj != null && !thisObj.equals(otherObj);
	}
	
	/**
	 * Saves all the changes made to this object to the database.
	 * @return true if successful, false otherwise
	 */
	public boolean commit()
	{
		boolean success = false;
		if(changed)
		{
			Connection connection = DBConnection.getDBConnection();
			if(connection != null)
			{
				try
				{
					
					success = commit(connection);
					if(success)
					{
						changed = false;
					}
					connection.close();
				} catch (SQLException e)
				{
				}
			}
		}
		
		return success;
	}
	
	/**
	 * This method is implementation-specific code for each schema.
	 * @param connection the current connection to the database
	 * @return true if successful, false otherwise
	 * @throws SQLException if the underlying SQL code is not accepted,
	 * or the data insertion fails
	 */
	abstract protected boolean commit(Connection connection) throws SQLException;
	
	/**
	 * Deletes this model from the database.
	 * @return true if successful, false otherwise
	 */
	public boolean delete()
	{
		boolean success = false;
		Connection connection = DBConnection.getDBConnection();
		if(connection != null)
		{
			try
			{
				success = delete(connection);
				if(success)
				{
					changed = true;
				}
				connection.close();
			} catch (SQLException e)
			{
			}
		}
		
		return success;
	}
	
	/**
	 * This method is implementation-specific ocde for each schema.
	 * @param connection the current connection to the database
	 * @return true if successful, false otherwise
	 * @throws SQLException if the underlying SQL code is not accepted,
	 * or the data insertion fails
	 */
	abstract protected boolean delete(Connection connection) throws SQLException;
	
	/**
	 * Gets this schema's column names to setter method mapping.
	 * @return mapping from column name to variable setter methods
	 */
	abstract protected Map<String, Method> getColumnParameters();
}
