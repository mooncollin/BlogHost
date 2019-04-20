package models;

import java.math.BigInteger;
import java.util.Map;

/**
 * This class represents the "BlogHostFavorites" schema from the database.
 * @author collin
 *
 */
@Table("BlogHostFavorites")
public final class BlogHostFavorites extends Model
{
	@Column(columnName="reader_id", methodName="setReaderID", methodParameter=Integer.class, primaryKey=true)
	private Integer reader_id;
	
	@Column(columnName="post_id", methodName="setPostID", methodParameter=BigInteger.class)
	private BigInteger post_id;
	
	/**
	 * Constructor. Creates a brand new BlogHostFavorites row.
	 * @param reader_id
	 * @param post_id
	 */
	public BlogHostFavorites(Integer reader_id, BigInteger post_id)
	{
		setReaderID(reader_id);
		setPostID(post_id);
	}
	
	/**
	 * Constructor. This constructor is meant to be called when the
	 * parameters come from an existing row in a table of this schema.
	 * @param columnParameters column name to value mappings
	 */
	public BlogHostFavorites(Map<String, Object> columnParameters)
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
	
	protected void resetAutoGenerated()
	{
		
	}
	
	/**
	 * Checks if the given object is equal to this one.
	 * Two BlogHostFavorites are equal if their post IDs and reader IDs are equal.
	 * @param other object for comparison
	 * @return true if they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof BlogHostFavorites)
		{
			return getPostID().equals(((BlogHostFavorites) other).getPostID()) &&
				   getReaderID().equals(((BlogHostFavorites) other).getReaderID());
		}
		
		return false;
	}
}
