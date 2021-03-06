package models;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Map;

/**
 * This class represents the "BlogHostComments" schema from the database.
 * @author collin
 *
 */
@Table("BlogHostComments")
public final class BlogHostComments extends Model
{
	/**
	 * Primary key. Auto increment from the database.
	 */
	@Column(columnName="id", methodName="setID", methodParameter=BigInteger.class, autoGenerated=true, primaryKey=true)
	private BigInteger id;
	
	@Column(columnName="creator_id", methodName="setCreatorID", methodParameter=Integer.class)
	private Integer creator_id;
	
	@Column(columnName="post_id", methodName="setPostID", methodParameter=BigInteger.class)
	private BigInteger post_id;
	
	@Column(columnName="comment_text", methodName="setCommentText", methodParameter=String.class)
	private String comment_text;
	
	@Column(columnName="thread", methodName="setThread", methodParameter=Boolean.class)
	private Boolean thread;
	
	@Column(columnName="reply_id", methodName="setReplyID", methodParameter=Integer.class)
	private Integer reply_id;
	
	@Column(columnName="likes", methodName="setLikes", methodParameter=Integer.class)
	private Integer likes;
	
	@Column(columnName="date_posted", methodName="setDatePosted", methodParameter=Timestamp.class)
	private Timestamp date_posted;
	
	@Column(columnName="date_modified", methodName="setDateModified", methodParameter=Timestamp.class)
	private Timestamp date_modified;
	
	/**
	 * Constructor. Creates a brand new BlogHostComments row.
	 * @param creator_id
	 * @param post_id
	 * @param comment_text
	 * @param thread
	 * @param reply_id
	 * @param likes
	 * @param date_posted
	 * @param date_modified
	 */
	public BlogHostComments(Integer creator_id, BigInteger post_id,
			String comment_text, Boolean thread, Integer reply_id, Integer likes,
			Timestamp date_posted, Timestamp date_modified)
	{
		resetAutoGenerated();
		setCreatorID(creator_id);
		setPostID(post_id);
		setCommentText(comment_text);
		setThread(thread);
		setReplyID(reply_id);
		setLikes(likes);
		setDatePosted(date_posted);
		setDateModified(date_modified);
	}
	
	/**
	 * Constructor. This constructor is meant to be called when the
	 * parameters come from an existing row in a table of this schema.
	 * @param columnParameters column name to value mappings
	 */
	public BlogHostComments(Map<String, Object> columnParameters)
	{
		super(columnParameters);
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
	 * Gets the creator ID of this row.
	 * @return creator_id
	 */
	public Integer getCreatorID()
	{
		return creator_id;
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
	 * Gets the comment text of this row.
	 * @return comment_text
	 */
	public String getCommentText()
	{
		return comment_text;
	}
	
	/**
	 * Gets the thread of this row.
	 * @return thread
	 */
	public Boolean getThread()
	{
		return thread;
	}
	
	/**
	 * Gets the reply ID of this row.
	 * @return reply_id
	 */
	public Integer getReplyID()
	{
		return reply_id;
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
	 * database schema ID's are managed
	 * automatically.
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
	 * Sets the comment text of this row.
	 * @param comment_text Comment Text
	 */
	public void setCommentText(String comment_text)
	{
		if(checkVariable(this.comment_text, comment_text))
		{
			changed = true;
			this.comment_text = comment_text;
		}
	}
	
	/**
	 * Sets the thread of this row.
	 * @param thread Thread
	 */
	public void setThread(Boolean thread)
	{
		if(checkVariable(this.thread, thread))
		{
			changed = true;
			this.thread = thread;	
		}
		
	}
	
	/**
	 * Sets the reply ID of this row.
	 * @param reply_id Reply ID
	 */
	public void setReplyID(Integer reply_id)
	{
		if(checkVariable(this.reply_id, reply_id))
		{
			changed = true;
			this.reply_id = reply_id;	
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
	
	protected void resetAutoGenerated()
	{
		setID(new BigInteger("-1"));
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
		if(other instanceof BlogHostComments)
		{
			return getID().equals(((BlogHostComments) other).getID());
		}
		
		return false;
	}
}
