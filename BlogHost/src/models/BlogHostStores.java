package models;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the "BlogHostStores" schema from the database.
 * @author collin
 *
 */
final public class BlogHostStores extends Model
{
	/**
	 * Primary key. Auto increment from the database.
	 */
	private Integer id;
	private Integer creator_id;
	private String item_name;
	private String item_description;
	private byte[] picture;
	private BigDecimal amount;
	
	/**
	 * This schema's unique column to method mapping.
	 */
	private static Map<String, Method> columnParameters;
	
	/**
	 * Creates the columnParameter mappings
	 */
	static
	{
		columnParameters = new HashMap<String, Method>();
		try
		{
			columnParameters.put("id", BlogHostStores.class.getMethod("setID", Integer.class));
			columnParameters.put("creator_id", BlogHostStores.class.getMethod("setCreatorID", Integer.class));
			columnParameters.put("item_name", BlogHostStores.class.getMethod("setItemName", String.class));
			columnParameters.put("item_description", BlogHostStores.class.getMethod("setItemDescription", String.class));
			columnParameters.put("picture", BlogHostStores.class.getMethod("setPicture", byte[].class));
			columnParameters.put("amount", BlogHostStores.class.getMethod("setAmount", BigDecimal.class));
		}
		catch(NoSuchMethodException | SecurityException e)
		{
			throw new RuntimeException("Check column parameters");
		}
	}
	
	/**
	 * Constructor. Creates a brand new BlogHostStores row.
	 * @param creator_id
	 * @param item_name
	 * @param item_description
	 * @param picture
	 */
	public BlogHostStores(Integer creator_id, String item_name,
			String item_description, byte[] picture, BigDecimal amount)
	{
		setID(-1);
		setCreatorID(creator_id);
		setItemName(item_name);
		setItemDescription(item_description);
		setPicture(picture);
		setAmount(amount);
	}
	
	/**
	 * Constructor. This constructor is meant to be called when the
	 * parameters come from an existing row in a table of this schema.
	 * @param columnParameters column name to value mappings
	 */
	public BlogHostStores(Map<String, Object> columnParameters)
	{
		super(columnParameters);
	}

	/**
	 * Gets the ID of this row.
	 * @return id
	 */
	public Integer getID()
	{
		return id;
	}

	/**
	 * Gets the creator ID of this row.
	 * @return creator_id
	 */
	public Integer getCreatorID()
	{
		return creator_id;
	}

	/**
	 * Gets the item name of this row.
	 * @return item_name
	 */
	public String getItemName()
	{
		return item_name;
	}

	/**
	 * Gets the item description of this row.
	 * @return item_description
	 */
	public String getItemDescription()
	{
		return item_description;
	}

	/**
	 * Gets the picture of this row.
	 * @return picture
	 */
	public byte[] getPicture()
	{
		return picture;
	}
	
	/**
	 * Gets the amount of this row.
	 * @return amount
	 */
	public BigDecimal getAmount()
	{
		return amount;
	}

	/**
	 * Sets the ID of this row.
	 * Use this method with caution as some
	 * database schema ID's are managed
	 * automatically.
	 * @param id ID
	 */
	public void setID(Integer id)
	{
		if(checkVariable(this.id, id))
		{
			changed = true;
			this.id = id;
		}
	}

	/**
	 * Sets the creator ID of this row.
	 * @param creator_id Creator ID
	 */
	public void setCreatorID(Integer creator_id)
	{
		if(checkVariable(this.creator_id, creator_id))
		{
			changed = true;
			this.creator_id = creator_id;
		}
	}

	/**
	 * Sets the item name of this row.
	 * @param item_name Item Name
	 */
	public void setItemName(String item_name)
	{
		if(checkVariable(this.item_name, item_name))
		{
			changed = true;
			this.item_name = item_name;
		}
	}

	/**
	 * Sets the item description of this row.
	 * @param item_description Item Description
	 */
	public void setItemDescription(String item_description)
	{
		if(checkVariable(this.item_description, item_description))
		{
			changed = true;
			this.item_description = item_description;
		}
	}

	/**
	 * Sets the picture of this row.
	 * @param picture Picture
	 */
	public void setPicture(byte[] picture)
	{
		if(checkVariable(this.picture, picture))
		{
			changed = true;
			this.picture = picture;
		}
	}
	
	/**
	 * Sets the amount of this row.
	 * @param amount Amount
	 */
	public void setAmount(BigDecimal amount)
	{
		if(checkVariable(this.amount, amount))
		{
			changed = true;
			this.amount = amount;
		}
	}
	
	/**
	 * Returns this schema's column mappings.
	 */
	protected Map<String, Method> getColumnParameters()
	{
		return columnParameters;
	}
	
	/**
	 * The implementation-specific version of commit for this schema.
	 * If this model does not exist in the database, it is inserted.
	 * If this model does exist in the database, it will be updated.
	 * @return true if successful, false otherwise
	 */
	protected boolean commit(Connection connection) throws SQLException
	{
		String querySQL = String.format("select * from %s where id=?", getClass().getSimpleName());
		PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		statement.setInt(1, getID());
		ResultSet results = statement.executeQuery();
		
		if(results.next())
		{
			results.updateInt("creator_id", getCreatorID());
			results.updateString("item_name", getItemName());
			results.updateString("item_description", getItemDescription());
			results.updateBytes("picture", getPicture());
			results.updateBigDecimal("amount", getAmount());
			results.updateRow();
		}
		else
		{
			results.moveToInsertRow();
			results.updateInt("creator_id", getCreatorID());
			results.updateString("item_name", getItemName());
			results.updateString("item_description", getItemDescription());
			results.updateBytes("picture", getPicture());
			results.updateBigDecimal("amount", getAmount());
			results.insertRow();
			results.last();
			setID(results.getInt("id"));
		}
		
		return true;
	}
	
	/**
	 * The implementation-specific version of delete for this schema.
	 * @return true if successful, false otherwise
	 */
	protected boolean delete(Connection connection) throws SQLException
	{
		String deleteSQL = String.format("delete from %s where id=?", getClass().getSimpleName());
		PreparedStatement statement = connection.prepareStatement(deleteSQL);
		statement.setInt(1, getID());
		int result = statement.executeUpdate();
		if(result != 0)
		{
			setID(-1);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if the given object is equal to this one.
	 * Two BlogHostStores are equal if their IDs are equal.
	 * @param other object for comparison
	 * @return true if they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof BlogHostStores)
		{
			return getID().equals(((BlogHostStores) other).getID());
		}
		
		return false;
	}
}
