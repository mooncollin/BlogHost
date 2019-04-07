package models;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

/**
 * This class represents the "BlogHostMods" schema from the database.
 * @author collin
 *
 */
public final class BlogHostMods extends Model
{
	private Integer mod_id;
	private Integer creator_id;
	private Integer mod_id_old;
	private Integer creator_id_old;
	
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
			columnParameters.put("mod_id", BlogHostMods.class.getMethod("setModID", Integer.class));
			columnParameters.put("creator_id", BlogHostMods.class.getMethod("setCreatorID", Integer.class));
		}
		catch(NoSuchMethodException | SecurityException e)
		{
			throw new RuntimeException("Check column parameters");
		}
	}
	
	/**
	 * Constructor. Creates a brand new BlogHostMods row.
	 * @param mod_id
	 * @param creator_id
	 */
	public BlogHostMods(Integer mod_id, Integer creator_id)
	{
		setModID(mod_id);
		setCreatorID(creator_id);
		this.creator_id_old = getCreatorID();
		this.mod_id_old = getModID();
	}
	
	/**
	 * Constructor. This constructor is meant to be called when the
	 * parameters come from an existing row in a table of this schema.
	 * @param columnParameters column name to value mappings
	 */
	public BlogHostMods(Map<String, Object> columnParameters)
	{
		super(columnParameters);
		this.creator_id_old = getCreatorID();
		this.mod_id_old = getModID();
	}
	
	/**
	 * Gets the mod ID of this row.
	 * @return mod_id
	 */
	public Integer getModID()
	{
		return mod_id;
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
	 * Sets the mod ID of this row.
	 * @param mod_id Mod ID
	 */
	public void setModID(Integer mod_id)
	{
		if(checkVariable(this.mod_id, mod_id))
		{
			changed = true;
			this.mod_id = mod_id;
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
		String querySQL = String.format("select * from %s where mod_id=? and creator_id=?", getClass().getSimpleName());
		PreparedStatement statement = connection.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		statement.setInt(1, this.mod_id_old);
		statement.setInt(2, this.creator_id_old);
		ResultSet results = statement.executeQuery();
		
		if(results.next())
		{
			results.updateInt("mod_id", getModID());
			results.updateInt("creator_id", getCreatorID());
			results.updateRow();
		}
		else
		{
			results.moveToInsertRow();
			results.updateInt("mod_id", getModID());
			results.updateInt("creator_id", getCreatorID());
			results.insertRow();
		}
		
		this.creator_id_old = getCreatorID();
		this.mod_id_old = getModID();
		
		return true;
	}
	
	/**
	 * The implementation-specific version of delete for this schema.
	 * @return true if successful, false otherwise
	 */
	protected boolean delete(Connection connection) throws SQLException
	{
		String deleteSQL = String.format("delete from %s where mod_id=? and creator_id=?", getClass().getSimpleName());
		PreparedStatement statement = connection.prepareStatement(deleteSQL);
		statement.setInt(1, this.mod_id_old);
		statement.setInt(2, this.creator_id_old);
		int result = statement.executeUpdate();
		
		return result != 0;
	}
	
	/**
	 * Checks if the given object is equal to this one.
	 * Two BlogHostComments are equal if their IDs are equal.
	 * @param other object for comparison
	 * @return true if they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof BlogHostMods)
		{
			return getModID().equals(((BlogHostMods) other).getModID()) &&
				   getCreatorID().equals(((BlogHostMods) other).getCreatorID());
		}
		
		return false;
	}
}
