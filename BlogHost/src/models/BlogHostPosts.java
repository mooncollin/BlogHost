package models;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the "BlogHostPosts" schema from the database.
 * @author collin
 *
 */
public final class BlogHostPosts extends Model
{
	/**
	 * Primary key. Auto increment from the database.
	 */
	private BigInteger id;
	private Integer site_id;
	private String post_title;
	private String post_text;
	private byte[] picture;
	private Timestamp date_posted;
	private Timestamp date_modified;
	
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
			columnParameters.put("id", BlogHostPosts.class.getMethod("setID", BigInteger.class));
			columnParameters.put("site_id", BlogHostPosts.class.getMethod("setSiteID", Integer.class));
			columnParameters.put("post_title", BlogHostPosts.class.getMethod("setPostTitle", String.class));
			columnParameters.put("post_text", BlogHostPosts.class.getMethod("setPostText", String.class));
			columnParameters.put("picture", BlogHostPosts.class.getMethod("setPicture", byte[].class));
			columnParameters.put("date_posted", BlogHostPosts.class.getMethod("setDatePosted", Timestamp.class));
			columnParameters.put("date_modified", BlogHostPosts.class.getMethod("setDateModified", Timestamp.class));
		} catch (NoSuchMethodException | SecurityException e)
		{
			throw new RuntimeException("Check column parameters");
		}
	}
	
	/**
	 * Constructor. Creates a brand new BlogHostPosts rows.
	 * @param site_id
	 * @param post_title
	 * @param post_text
	 * @param picture
	 * @param date_posted
	 * @param date_modified
	 */
	public BlogHostPosts(Integer site_id, String post_title,
			String post_text, byte[] picture, Timestamp date_posted,
			Timestamp date_modified)
	{
		setID(new BigInteger("-1"));
		setSiteID(site_id);
		setPostTitle(post_title);
		setPostText(post_text);
		setPicture(picture);
		setDatePosted(date_posted);
		setDateModified(date_modified);
	}
	
	/**
	 * Constructor. This constructor is meant to be called when the
	 * parameters come from an existing row in a table of this schema.
	 * @param columnParameters column name to value mappings
	 */
	public BlogHostPosts(Map<String, Object> columnParameters)
	{
		super(columnParameters);
	}
	
	/**
	 * Returns this schema's column mappings.
	 */
	protected Map<String, Method> getColumnParameters()
	{
		return columnParameters;
	}
	
	/**
	 * Gets the ID of this row.
	 * @return id
	 */
	public BigInteger getID()
	{
		return id;
	}
	
	/**
	 * Gets the site ID of this row.
	 * @return site_id
	 */
	public Integer getSiteID()
	{
		return site_id;
	}
	
	/**
	 * Gets the post title of this row.
	 * @return post_title
	 */
	public String getPostTitle()
	{
		return post_title;
	}
	
	/**
	 * Gets the post text of this row.
	 * @return post_text
	 */
	public String getPostText()
	{
		return post_text;
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
	 * Gets the date posted of this row.
	 * @return date_posted
	 */
	public Timestamp getDatePosted()
	{
		return date_posted;
	}
	
	/**
	 * Gets the date modified of this row.
	 * @return date_modified
	 */
	public Timestamp getDateModified()
	{
		return date_modified;
	}
	
	/**
	 * Sets the ID of this row.
	 * Use this method with caution as some
	 * database schema ID's are managed automatically.
	 * @param id ID
	 */
	public void setID(BigInteger id)
	{
		if(checkVariable(this.id, id))
		{
			changed = true;
			this.id = id;
		}
	}
	
	/**
	 * Sets the site ID of this row.
	 * @param site_id Site ID
	 */
	public void setSiteID(Integer site_id)
	{
		if(checkVariable(this.site_id, site_id))
		{
			changed = true;
			this.site_id = site_id;
		}
	}
	
	/**
	 * Sets the post title of this row.
	 * @param post_title Post Title
	 */
	public void setPostTitle(String post_title)
	{
		if(checkVariable(this.post_title, post_title))
		{
			changed = true;
			this.post_title = post_title;
		}
	}
	
	/**
	 * Sets the post text of this row.
	 * @param post_text Post Text
	 */
	public void setPostText(String post_text)
	{
		if(checkVariable(this.post_text, post_text))
		{
			changed = true;
			this.post_text = post_text;
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
	 * Sets the date posted of this row.
	 * @param date_posted Date Posted
	 */
	public void setDatePosted(Timestamp date_posted)
	{
		if(checkVariable(this.date_posted, date_posted))
		{
			changed = true;
			this.date_posted = date_posted;
		}
	}
	
	/**
	 * Sets the date modified of this row.
	 * @param date_modified Date Modified
	 */
	public void setDateModified(Timestamp date_modified)
	{
		if(checkVariable(this.date_modified, date_modified))
		{
			changed = true;
			this.date_modified = date_modified;
		}
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
		statement.setObject(1, getID(), Types.BIGINT);
		ResultSet results = statement.executeQuery();
		
		if(results.next())
		{
			results.updateInt("site_id", getSiteID());
			results.updateString("post_title", getPostTitle());
			results.updateString("post_text", getPostText());
			results.updateBytes("picture", getPicture());
			results.updateTimestamp("date_posted", getDatePosted());
			results.updateTimestamp("date_modified", getDateModified());
			results.updateRow();
		}
		else
		{
			results.moveToInsertRow();
			results.updateInt("site_id", getSiteID());
			results.updateString("post_title", getPostTitle());
			results.updateString("post_text", getPostText());
			results.updateBytes("picture", getPicture());
			results.updateTimestamp("date_posted", getDatePosted());
			results.updateTimestamp("date_modified", getDateModified());
			results.insertRow();
			results.last();
			setID(results.getObject("id", BigInteger.class));
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
		statement.setObject(1, getID(), Types.BIGINT);
		int result = statement.executeUpdate();
		if(result != 0)
		{
			setID(new BigInteger("-1"));
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if the given object is equal to this one.
	 * Two BlogHostPosts are equal if their IDs are equal.
	 * @param other object for comparison
	 * @return true if they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof BlogHostPosts)
		{
			return getID().equals(((BlogHostPosts) other).getID());
		}
		
		return false;
	}
}
