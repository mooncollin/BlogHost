package main;

public class Post {
	public int ID;
	public String POST_TEXT;
	public String POST_TITLE;
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
	public Post(int iD, String pOST_TEXT, String pOST_TITLE) {
		super();
		ID = iD;
		POST_TEXT = pOST_TEXT;
		POST_TITLE = pOST_TITLE;
	}
	public Post() {
		
	}
	
	
}
