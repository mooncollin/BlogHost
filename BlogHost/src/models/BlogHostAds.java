package models;

import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents the "BlogHostAds" schema from the database.
 * @author collin
 *
 */
public final class BlogHostAds extends Model
{
	/**
	 * Primary key. Auto increments from the database.
	 */
	private Integer ad_id;
	private String html;
	
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
			columnParameters.put("ad_id", BlogHostAds.class.getMethod("setAdID", Integer.class));
			columnParameters.put("html", BlogHostAds.class.getMethod("setHTML", String.class));
		}
		catch(NoSuchMethodException | SecurityException e)
		{
			throw new RuntimeException("Check column parameters");
		}
	}
	
	/**
	 * Constructor. Creates a brand new BlogHostAds row.
	 * @param html HTML of this ad
	 */
	public BlogHostAds(String html)
	{
		setAdID(-1);
		setHTML(html);
	}
	
	/**
	 * Constructor. This constructor is meant to be called when the
	 * parameters come from an existing row in a table of this schema.
	 * @param columnParameters column name to value mappings
	 */
	public BlogHostAds(Map<String, Object> columnParameters)
	{
		super(columnParameters);
	}

	/**
	 * Gets the ID of this row.
	 * @return id
	 */
	public Integer getAdID()
	{
		return ad_id;
	}

	/**
	 * Gets the HTML of this row.
	 * @return html
	 */
	public String getHTML()
	{
		return html;
	}

	/**
	 * Sets the ID of this row. This should be used with caution
	 * as some database manage IDs.
	 * @param ad_id ID
	 */
	public void setAdID(Integer ad_id)
	{
		if(checkVariable(this.ad_id, ad_id))
		{
			changed = true;
			this.ad_id = ad_id;
		}
	}

	/**
	 * Sets the HTML of this row.
	 * @param html HTML
	 */
	public void setHTML(String html)
	{
		if(checkVariable(this.html, html))
		{
			changed = true;
			this.html = html;
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
		String querySQL = String.format("select * from %s where ad_id=?", getClass().getSimpleName());
		PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		statement.setInt(1, getAdID());
		ResultSet results = statement.executeQuery();
		
		if(results.next())
		{
			results.updateString("html", getHTML());
			results.updateRow();
		}
		else
		{
			results.moveToInsertRow();
			results.updateString("html", getHTML());
			results.insertRow();
			results.last();
			setAdID(results.getInt("ad_id"));
		}
		
		return true;
	}
	
	/**
	 * The implementation-specific version of delete for this schema.
	 * @return true if successful, false otherwise
	 */
	protected boolean delete(Connection connection) throws SQLException
	{
		String deleteSQL = String.format("delete from %s where ad_id=?", getClass().getSimpleName());
		PreparedStatement statement = connection.prepareStatement(deleteSQL);
		statement.setInt(1, getAdID());
		int result = statement.executeUpdate();
		if(result != 0)
		{
			setAdID(-1);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if the given object is equal to this one.
	 * Two BlogHostAds are equal if their AdIDs are equal.
	 * @param other object for comparison
	 * @return true if they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof BlogHostAds)
		{
			return getAdID().equals(((BlogHostAds) other).getAdID());
		}
		
		return false;
	}
}
