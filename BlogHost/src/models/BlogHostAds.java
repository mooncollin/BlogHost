package models;

import java.util.Map;

/**
 * This class represents the "BlogHostAds" schema from the database.
 * @author collin
 *
 */
@Table("BlogHostAds")
public final class BlogHostAds extends Model
{
	/**
	 * Primary key. Auto increments from the database.
	 */
	@Column(columnName="ad_id", methodName="setAdID", methodParameter=Integer.class, autoGenerated=true, primaryKey=true)
	private Integer ad_id;
	
	@Column(columnName="html", methodName="setHTML", methodParameter=String.class)
	private String html;
	
	/**
	 * Constructor. Creates a brand new BlogHostAds row.
	 * @param html HTML of this ad
	 */
	public BlogHostAds(String html)
	{
		resetAutoGenerated();
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
	
	protected void resetAutoGenerated()
	{
		setAdID(-1);
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
