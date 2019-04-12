package models;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Method;

/**
 * This class represents the "BlogHostLikes" schema from the database.
 * @author collin
 *
 */
public final class BlogHostLikes extends Model
{
	private Integer reader_id;
	private BigInteger post_id;
	
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
			columnParameters.put("reader_id", BlogHostLikes.class.getMethod("setReaderID", Integer.class));
			columnParameters.put("post_id", BlogHostLikes.class.getMethod("setPostID", BigInteger.class));
		}
		catch(NoSuchMethodException | SecurityException e)
		{
			throw new RuntimeException("Check column parameters");
		}
	}
	
	/**
	 * Constructor. Creates a brand new BlogHostLikes row.
	 * @param reader_id
	 * @param post_id
	 */
	public BlogHostLikes(Integer reader_id, BigInteger post_id)
	{
		setReaderID(reader_id);
		setPostID(post_id);
	}
	
	/**
	 * Constructor. This constructor is meant to be called when the
	 * parameters come from an existing row in a table of this schema.
	 * @param columnParameters column name to value mappings
	 */
	public BlogHostLikes(Map<String, Object> columnParameters)
	{
		super(columnParameters);
	}

	/**
	 * Gets the reader ID of this row.
	 * @return reader_id
	 */
	public Integer getReaderID()
	{
		return reader_id;
	}

	/**
	 * Gets the post ID of this row.
	 * @return post_id
	 */
	public BigInteger getPostID()
	{
		return post_id;
	}

	/**
	 * Sets the reader ID of this row.
	 * @param reader_id Reader ID
	 */
	public void setReaderID(Integer reader_id)
	{
		if(checkVariable(this.reader_id, reader_id))
		{
			changed = true;
			this.reader_id = reader_id;
		}
	}

	/**
	 * Sets the post ID of this row.
	 * @param post_id Post ID
	 */
	public void setPostID(BigInteger post_id)
	{
		if(checkVariable(this.post_id, post_id))
		{
			changed = true;
			this.post_id = post_id;
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
		String querySQL = String.format("select * from %s where reader_id=? and post_id=?", getClass().getSimpleName());
		PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		statement.setInt(1, getReaderID());
		statement.setObject(2, getPostID(), Types.BIGINT);
		ResultSet results = statement.executeQuery();
		
		if(results.next())
		{
			results.updateInt("reader_id", getReaderID());
			results.updateObject("post_id", getPostID());
			results.updateRow();
		}
		else
		{
			results.moveToInsertRow();
			results.updateInt("reader_id", getReaderID());
			results.updateObject("post_id", getPostID());
			results.insertRow();
		}
		
		return true;
	}
	
	/**
	 * The implementation-specific version of delete for this schema.
	 * @return true if successful, false otherwise
	 */
	protected boolean delete(Connection connection) throws SQLException
	{
		String deleteSQL = String.format("delete from %s where reader_id=? and post_id=?", getClass().getSimpleName());
		PreparedStatement statement = connection.prepareStatement(deleteSQL);
		statement.setInt(1, getReaderID());
		statement.setObject(2, getPostID(), Types.BIGINT);
		int result = statement.executeUpdate();
		
		return result != 0;
	}
	
	/**
	 * Checks if the given object is equal to this one.
	 * Two BlogHostLikes are equal if their reader IDs and post IDs are equal.
	 * @param other object for comparison
	 * @return true if they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof BlogHostLikes)
		{
			return getReaderID().equals(((BlogHostLikes) other).getReaderID()) &&
				   getPostID().equals(((BlogHostLikes) other).getPostID());
		}
		
		return false;
	}
}
