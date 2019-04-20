package models;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import dbConnection.DBConnection;

/**
 * This class is the base for all database schemas. It has helper functions
 * for generic pulling of data and dynamically creating model objects
 * based on given parameters.
 * @author collin
 *
 */
@Table
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
		String sql = "select * from " + clazz.getAnnotation(Table.class).value();
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
			e.printStackTrace();
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
		List<Column> columns = getColumns().stream().map(f -> f.getAnnotation(Column.class)).collect(Collectors.toList());
		
		Set<String> columnNames = columns.stream()
										.map(field -> field.columnName())
										.collect(Collectors.toSet());
		
		if(!columnParameters.keySet().equals(columnNames))
		{
			throw new IllegalArgumentException("Column parameters do not match");
		}
		
		for(Column column : columns)
		{
			try
			{
				this.getClass().getMethod(column.methodName(), column.methodParameter())
							   .invoke(this, columnParameters.get(column.columnName()));
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				throw new RuntimeException("Column method failed to be executed!\n" + e.getMessage());
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
					String querySQL = String.format("select * from %s", getClass().getDeclaredAnnotation(Table.class).value());
					List<Column> primaryKeys = getPrimaryKeys().stream().map(k -> k.getAnnotation(Column.class)).collect(Collectors.toList());
					
					if(!primaryKeys.isEmpty())
					{
						querySQL += " where ";
						ListIterator<Column> it = primaryKeys.listIterator();
						while(it.hasNext())
						{
							querySQL += String.format("%s=?", it.next().columnName());
							if(it.hasNext())
							{
								querySQL += " and ";
							}
						}
					}
					
					PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					updateStatement(statement);
					
					ResultSet results = statement.executeQuery();
					Map<Column, String> truncatedFields;
					
					if(results.next())
					{
						truncatedFields = updateFields(results);
						results.updateRow();
					}
					else
					{
						results.moveToInsertRow();
						truncatedFields = updateFields(results);
						results.insertRow();
						
						List<Column> autoGenFields = getAutoGenerated().stream().map(k -> k.getAnnotation(Column.class)).collect(Collectors.toList());
						if(!autoGenFields.isEmpty())
						{
							results.last();
							for(Column c : autoGenFields)
							{
								this.getClass().getMethod(c.methodName(), c.methodParameter())
											   .invoke(this, results.getObject(c.columnName()));
							}
						}
					}

					for(Column column : truncatedFields.keySet())
					{
						getClass().getMethod(column.methodName(), column.methodParameter()).invoke(this, truncatedFields.get(column));
					}
					
					connection.close();
					success = true;
					changed = false;
				} catch (SQLException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return success;
	}
	
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
				String deleteSQL = String.format("delete from %s", getClass().getAnnotation(Table.class).value());
				
				List<Column> primaryKeys = getPrimaryKeys().stream().map(k -> k.getAnnotation(Column.class)).collect(Collectors.toList());
				
				if(!primaryKeys.isEmpty())
				{
					deleteSQL += " where ";
					ListIterator<Column> it = primaryKeys.listIterator();
					while(it.hasNext())
					{
						deleteSQL += String.format("%s=?", it.next().columnName());
						if(it.hasNext())
						{
							deleteSQL += " and ";
						}
					}
				}
				
				
				PreparedStatement statement = connection.prepareStatement(deleteSQL);
				updateStatement(statement);
				
				int result = statement.executeUpdate();
				connection.close();
				
				if(result != 0)
				{
					resetAutoGenerated();
					success = true;
				}
				
			} catch (SQLException | IllegalArgumentException | IllegalAccessException e)
			{
			}
		}
		
		return success;
	}
	
	abstract protected void resetAutoGenerated();
	
	private Map<Column, String> updateFields(ResultSet results) throws SQLException, IllegalArgumentException, IllegalAccessException
	{
		Map<Column, String> columnsTruncated = new HashMap<Column, String>();
		for(Field field : getColumns().stream().filter(field -> !field.getAnnotation(Column.class).autoGenerated()).collect(Collectors.toList()))
		{
			field.setAccessible(true);
			Object addition = field.get(this);
			if(addition instanceof String && field.getAnnotation(Column.class).length() != Integer.MAX_VALUE)
			{
				int previousLength = ((String) addition).length();
				addition = ((String) addition).substring(0, Math.min(field.getAnnotation(Column.class).length(), ((String) addition).length()));
				if(previousLength != ((String) addition).length())
				{
					columnsTruncated.put(field.getAnnotation(Column.class), (String) addition);
				}
			}
			results.updateObject(field.getAnnotation(Column.class).columnName(), addition);
		}
		
		return columnsTruncated;
	}
	
	private void updateStatement(PreparedStatement statement) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		ListIterator<Field> it = getPrimaryKeys().listIterator();
		
		while(it.hasNext())
		{
			Field field = it.next();
			field.setAccessible(true);
			statement.setObject(it.nextIndex(), field.get(this));
		}
	}
	
	private List<Field> getPrimaryKeys()
	{
		List<Field> keys = new LinkedList<Field>();
		for(Field field : getColumns())
		{
			if(field.getAnnotation(Column.class).primaryKey())
			{
				keys.add(field);
			}
		}
		return keys;
	}
	
	private List<Field> getAutoGenerated()
	{
		List<Field> keys = new LinkedList<Field>();
		for(Field field : getColumns())
		{
			if(field.getAnnotation(Column.class).autoGenerated())
			{
				keys.add(field);
			}
		}
		return keys;
	}
	
	private List<Field> getColumns()
	{
		List<Field> keys = new LinkedList<Field>();
		for(Field field : getClass().getDeclaredFields())
		{
			if(field.isAnnotationPresent(Column.class))
			{
				keys.add(field);
			}
		}
		return keys;
	}
}
