package models;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the "BlogHostSites" schema from the database.
 * @author collin
 *
 */
public final class BlogHostSites extends Model
{
	/**
	 * Primary key. Auto increment from the database.
	 */
	private Integer id;
	private Integer creator_id;
	private String site_url;
	private String site_name;
	private String custom_html;
	
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
			columnParameters.put("id", BlogHostSites.class.getMethod("setID", Integer.class));
			columnParameters.put("creator_id", BlogHostSites.class.getMethod("setCreatorID", Integer.class));
			columnParameters.put("site_url", BlogHostSites.class.getMethod("setSiteURL", String.class));
			columnParameters.put("site_name", BlogHostSites.class.getMethod("setSiteName", String.class));
			columnParameters.put("custom_html", BlogHostSites.class.getMethod("setCustomHTML", String.class));
		}
		catch(NoSuchMethodException | SecurityException e)
		{
			throw new RuntimeException("Check column parameters");
		}
	}
	
	/**
	 * Constructor. Creates a brand new BlogHostSites row.
	 * @param creator_id
	 * @param site_url
	 * @param site_name
	 * @param custom_html
	 */
	public BlogHostSites(Integer creator_id, String site_url,
			String site_name, String custom_html)
	{
		setID(-1);
		setCreatorID(creator_id);
		setSiteURL(site_url);
		setSiteName(site_name);
		setCustomHTML(custom_html);
	}
	
	/**
	 * Constructor. This constructor is meant to be called when the
	 * parameters come from an existing row in a table of this schema.
	 * @param columnParameters column name to value mappings
	 */
	public BlogHostSites(Map<String, Object> columnParameters)
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
	 * Gets the site URL of this row.
	 * @return site_url
	 */
	public String getSiteURL()
	{
		return site_url;
	}
	
	/**
	 * Gets the site name of this row.
	 * @return site_name
	 */
	public String getSiteName()
	{
		return site_name;
	}
	
	/**
	 * Gets the custom HTML of this row.
	 * @return custom_html
	 */
	public String getCustomHTML()
	{
		return custom_html;
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
		this.id = id;
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
	 * Sets the site URL of this row.
	 * @param site_url Site URL
	 */
	public void setSiteURL(String site_url)
	{
		if(checkVariable(this.site_url, site_url))
		{
			changed = true;
			this.site_url = site_url;
		}
	}
	
	/**
	 * Sets the site name of this row.
	 * @param site_name Site Name
	 */
	public void setSiteName(String site_name)
	{
		if(checkVariable(this.site_name, site_name))
		{
			changed = true;
			this.site_name = site_name;
		}
	}
	
	/**
	 * Sets the custom HTML of this row.
	 * @param custom_html Custom HTML
	 */
	public void setCustomHTML(String custom_html)
	{
		if(checkVariable(this.custom_html, custom_html))
		{
			changed = true;
			this.custom_html = custom_html;
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
			results.updateString("site_url", getSiteURL());
			results.updateString("site_name", getSiteName());
			results.updateString("custom_html", getCustomHTML());
			results.updateRow();
		}
		else
		{
			results.moveToInsertRow();
			results.updateInt("creator_id", getCreatorID());
			results.updateString("site_url", getSiteURL());
			results.updateString("site_name", getSiteName());
			results.updateString("custom_html", getCustomHTML());
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
	 * Two BlogHostSites are equal if their IDs are equal.
	 * @param other object for comparison
	 * @return true if they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof BlogHostSites)
		{
			return getID().equals(((BlogHostSites) other).getID());
		}
		
		return false;
	}
}
