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
 * This class represents the "BlogHostDonations" schema from the database.
 * @author collin
 *
 */
public final class BlogHostDonations extends Model
{
	/**
	 * Primary key. Auto increment from the database.
	 */
	private Integer id;
	private Integer subscriber_id;
	private Integer creator_id;
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
			columnParameters.put("id", BlogHostDonations.class.getMethod("setID", Integer.class));
			columnParameters.put("subscriber_id", BlogHostDonations.class.getMethod("setSubscriberID", Integer.class));
			columnParameters.put("creator_id", BlogHostDonations.class.getMethod("setCreatorID", Integer.class));
			columnParameters.put("amount", BlogHostDonations.class.getMethod("setAmount", BigDecimal.class));
		}
		catch(NoSuchMethodException | SecurityException e)
		{
			throw new RuntimeException("Check column parameters");
		}
	}
	
	/**
	 * Constructor. Creates a brand new BlogHostDonations row.
	 * @param subscriber_id
	 * @param creator_id
	 * @param amount
	 */
	public BlogHostDonations(Integer subscriber_id, Integer creator_id,
			BigDecimal amount)
	{
		setID(-1);
		setSubscriberID(subscriber_id);
		setCreatorID(creator_id);
		setAmount(amount);
	}
	
	/**
	 * Constructor. This constructor is meant to be called when the
	 * parameters come from an existing row in a table of this schema.
	 * @param columnParameters column name to value mappings
	 */
	public BlogHostDonations(Map<String, Object> columnParameters)
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
	 * Gets the subscriber ID of this row.
	 * @return subscriber_id
	 */
	public Integer getSubscriberID()
	{
		return subscriber_id;
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
	 * automcatically.
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
	 * Sets the subscriber ID of this row.
	 * @param subscriber_id Subscriber ID
	 */
	public void setSubscriberID(Integer subscriber_id)
	{
		if(checkVariable(this.subscriber_id, subscriber_id))
		{
			changed = true;
			this.subscriber_id = subscriber_id;
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
			results.updateInt("subscriber_id", getSubscriberID());
			results.updateInt("creator_id", getCreatorID());
			results.updateBigDecimal("amount", getAmount());
			results.updateRow();
		}
		else
		{
			results.moveToInsertRow();
			results.updateInt("subscriber_id", getSubscriberID());
			results.updateInt("creator_id", getCreatorID());
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
	 * Two BlogHostDonations are equal if their IDs are equal.
	 * @param other object for comparison
	 * @return true if they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof BlogHostDonations)
		{
			return getID().equals(((BlogHostDonations) other).getID());
		}
		
		return false;
	}
}
