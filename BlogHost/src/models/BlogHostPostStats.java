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
 * This class represents the "BlogHostPostStats" schema from the database.
 * @author collin
 *
 */
public final class BlogHostPostStats extends Model
{
	private BigInteger post_id;
	private Integer views;
	private Integer likes;
	
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
			columnParameters.put("post_id", BlogHostPostStats.class.getMethod("setPostID", BigInteger.class));
			columnParameters.put("views", BlogHostPostStats.class.getMethod("setViews", Integer.class));
			columnParameters.put("likes", BlogHostPostStats.class.getMethod("setLikes", Integer.class));
		}
		catch(NoSuchMethodException | SecurityException e)
		{
			throw new RuntimeException("Check column parameters");
		}
	}
	
	/**
	 * Constructor. Creates a brand new BlogHostPostStats row.
	 * @param post_id
	 * @param views
	 * @param likes
	 */
	public BlogHostPostStats(BigInteger post_id, Integer views, Integer likes)
	{
		setPostID(post_id);
		setViews(views);
		setLikes(likes);
	}
	
	/**
	 * Constructor. This constructor is meant to be called when the
	 * parameters come from an existing row in a table of this schema.
	 * @param columnParameters column name to value mappings
	 */
	public BlogHostPostStats(Map<String, Object> columnParameters)
	{
		super(columnParameters);
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
	 * Gets the views of this row.
	 * @return views
	 */
	public Integer getViews()
	{
		return views;
	}

	/**
	 * Gets the likes of this row.
	 * @return likes
	 */
	public Integer getLikes()
	{
		return likes;
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
	 * Sets the views of this row.
	 * @param views Views
	 */
	public void setViews(Integer views)
	{
		if(checkVariable(this.views, views))
		{
			changed = true;
			this.views = views;
		}
	}

	/**
	 * Sets the likes of this row.
	 * @param likes Likes
	 */
	public void setLikes(Integer likes)
	{
		if(checkVariable(this.likes, likes))
		{
			changed = true;
			this.likes = likes;
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
		String querySQL = String.format("select * from %s where post_id=?", getClass().getSimpleName());
		PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		statement.setObject(1, getPostID(), Types.BIGINT);
		ResultSet results = statement.executeQuery();
		
		if(results.next())
		{
			results.updateObject("post_id", getPostID(), Types.BIGINT);
			results.updateInt("views", getViews());
			results.updateInt("likes", getLikes());
			results.updateRow();
		}
		else
		{
			results.moveToInsertRow();
			results.updateObject("post_id", getPostID(), Types.BIGINT);
			results.updateInt("views", getViews());
			results.updateInt("likes", getLikes());
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
		String deleteSQL = String.format("delete from %s where post_id=?", getClass().getSimpleName());
		PreparedStatement statement = connection.prepareStatement(deleteSQL);
		statement.setObject(1, getPostID(), Types.BIGINT);
		int result = statement.executeUpdate();
		
		return result != 0;
	}
	
	/**
	 * Checks if the given object is equal to this one.
	 * Two BlogHostPostStats are equal if their IDs are equal.
	 * @param other object for comparison
	 * @return true if they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof BlogHostPostStats)
		{
			return getPostID().equals(((BlogHostPostStats) other).getPostID());
		}
		
		return false;
	}
}
