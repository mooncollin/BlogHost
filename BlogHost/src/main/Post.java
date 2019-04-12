package main;

import java.sql.Timestamp;

public class Post {
	public Post(int iD, String pOST_TITLE, String pOST_TEXT, String pICTURE, Timestamp dATE_POSTED, int lIKE_COUNT,
			boolean lIKED_BY_USER) {
		super();
		ID = iD;
		POST_TITLE = pOST_TITLE;
		POST_TEXT = pOST_TEXT;
		PICTURE = pICTURE;
		DATE_POSTED = dATE_POSTED;
		LIKE_COUNT = lIKE_COUNT;
		LIKED_BY_USER = lIKED_BY_USER;
	}
	
	public int ID;
	public String POST_TITLE;
	public String POST_TEXT;
	public String PICTURE;
	public Timestamp DATE_POSTED;
	public int LIKE_COUNT;
	public boolean LIKED_BY_USER;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getPOST_TEXT() {
		return POST_TEXT;
	}
	public void setPOST_TEXT(String pOST_TEXT) {
		POST_TEXT = pOST_TEXT;
	}
	public String getPOST_TITLE() {
		return POST_TITLE;
	}
	public void setPOST_TITLE(String pOST_TITLE) {
		POST_TITLE = pOST_TITLE;
	}
	public Timestamp getDATE_POSTED() {
		return DATE_POSTED;
	}
	public void setDATE_POSTED(Timestamp dATE_POSTED) {
		DATE_POSTED = dATE_POSTED;
	}
	public int getLIKE_COUNT() {
		return LIKE_COUNT;
	}
	public void setLIKE_COUNT(int lIKE_COUNT) {
		LIKE_COUNT = lIKE_COUNT;
	}
	public boolean isLIKED_BY_USER() {
		return LIKED_BY_USER;
	}
	public void setLIKED_BY_USER(boolean lIKED_BY_USER) {
		LIKED_BY_USER = lIKED_BY_USER;
	}
	public String getPICTURE() {
		return PICTURE;
	}
	public void setPICTURE(String pICTURE) {
		PICTURE = pICTURE;
	}
	
	
}
