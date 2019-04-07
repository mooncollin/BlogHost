package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Method;

/**
 * This class represents the "BlogHostCreators" schema from the database.
 * @author collin
 *
 */
final public class BlogHostCreators extends Model
{
	/**
	 * Primary key. Auto increment from the database.
	 */
	private Integer id;
	private String user_name;
	private String first_name;
	private String last_name;
	private Integer age;
	private String password;
	private byte[] profile_picture;
	private Boolean admin;
	
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
			columnParameters.put("id", BlogHostCreators.class.getMethod("setID", Integer.class));
			columnParameters.put("user_name", BlogHostCreators.class.getMethod("setUserName", String.class));
			columnParameters.put("first_name", BlogHostCreators.class.getMethod("setFirstName", String.class));
			columnParameters.put("last_name", BlogHostCreators.class.getMethod("setLastName", String.class));
			columnParameters.put("age", BlogHostCreators.class.getMethod("setAge", Integer.class));
			columnParameters.put("password", BlogHostCreators.class.getMethod("setPassword", String.class));
			columnParameters.put("profile_picture", BlogHostCreators.class.getMethod("setProfilePicture", byte[].class));
			columnParameters.put("admin", BlogHostCreators.class.getMethod("setAdmin", Boolean.class));
		} catch (NoSuchMethodException | SecurityException e)
		{
			throw new RuntimeException("Check column parameters");
		}
	}
	
	/**
	 * Constructor. Creates a brand new BlogHostCreators row.
	 * @param user_name
	 * @param first_name
	 * @param last_name
	 * @param age
	 * @param password
	 * @param profile_picture
	 * @param admin
	 */
	public BlogHostCreators(String user_name, String first_name,
			String last_name, Integer age, String password, byte[] profile_picture,
			Boolean admin)
	{
		setID(-1);
		setUserName(user_name);
		setFirstName(first_name);
		setLastName(last_name);
		setAge(age);
		setPassword(password);
		setProfilePicture(profile_picture);
		setAdmin(admin);
	}
	
	/**
	 * Constructor. This constructor is meant to be called when the
	 * parameters come from an existing row in a table of this schema.
	 * @param columnParameters column name to value mappings
	 */
	public BlogHostCreators(Map<String, Object> columnParameters)
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
	 * Gets the user name of this row.
	 * @return user_name
	 */
	public String getUserName()
	{
		return user_name;
	}
	
	/**
	 * Gets the first name of this row.
	 * @return first_name
	 */
	public String getFirstName()
	{
		return first_name;
	}
	
	/**
	 * Gets the last name of this row.
	 * @return last_name
	 */
	public String getLastName()
	{
		return last_name;
	}
	
	/**
	 * Gets the age of this row.
	 * @return age
	 */
	public Integer getAge()
	{
		return age;
	}
	
	/**
	 * Gets the password of this row.
	 * @return password
	 */
	public String getPassword()
	{
		return password;
	}
	
	/**
	 * Gets the profile picture of this row.
	 * @return profile_picture
	 */
	public byte[] getProfilePicture()
	{
		return profile_picture;
	}
	
	/**
	 * Gets the admin of this row.
	 * @return admin
	 */
	public Boolean getAdmin()
	{
		return admin;
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
	 * Sets the user name of this row.
	 * @param user_name User Name
	 */
	public void setUserName(String user_name)
	{
		if(checkVariable(this.user_name, user_name))
		{
			changed = true;
			this.user_name = user_name;
		}
	}
	
	/**
	 * Sets the first name of this row.
	 * @param first_name First Name
	 */
	public void setFirstName(String first_name)
	{
		if(checkVariable(this.first_name, first_name))
		{
			changed = true;
			this.first_name = first_name;
		}
	}
	
	/**
	 * Sets the last name of this row.
	 * @param last_name Last Name
	 */
	public void setLastName(String last_name)
	{
		if(checkVariable(this.last_name, last_name))
		{
			changed = true;
			this.last_name = last_name;
		}
	}
	
	/**
	 * Sets the age of this row.
	 * @param age Age
	 */
	public void setAge(Integer age)
	{
		if(checkVariable(this.age, age))
		{
			changed = true;
			this.age = age;
		}
	}
	
	/**
	 * Sets the password of this row.
	 * @param password Password
	 */
	public void setPassword(String password)
	{
		if(checkVariable(this.password, password))
		{
			changed = true;
			this.password = password;
		}
	}
	
	/**
	 * Sets the profile picture of this row.
	 * @param profile_picture Profile Picture
	 */
	public void setProfilePicture(byte[] profile_picture)
	{
		if(checkVariable(this.profile_picture, profile_picture))
		{
			changed = true;
			this.profile_picture = profile_picture;
		}
	}
	
	/**
	 * Sets the admin of this row.
	 * @param admin Admin
	 */
	public void setAdmin(Boolean admin)
	{
		if(checkVariable(this.admin, admin))
		{
			changed = true;
			this.admin = admin;
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
			results.updateString("user_name", getUserName());
			results.updateString("first_name", getFirstName());
			results.updateString("last_name", getLastName());
			results.updateInt("age", getAge());
			results.updateString("password", password);
			results.updateBytes("profile_picture", profile_picture);
			results.updateBoolean("admin", getAdmin());
			results.updateRow();
		}
		else
		{
			results.moveToInsertRow();
			results.updateString("user_name", getUserName());
			results.updateString("first_name", getFirstName());
			results.updateString("last_name", getLastName());
			results.updateInt("age", getAge());
			results.updateString("password", password);
			results.updateBytes("profile_picture", profile_picture);
			results.updateBoolean("admin", getAdmin());
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
	 * Two BlogHostCreators are equal if their IDs are equal.
	 * @param other object for comparison
	 * @return true if they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof BlogHostCreators)
		{
			return getID().equals(((BlogHostCreators) other).getID());
		}
		
		return false;
	}
}
