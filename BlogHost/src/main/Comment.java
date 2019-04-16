package main;

import java.sql.Timestamp;
import java.util.List;

public class Comment {

	public Comment(int cOMMENTOR, String cOMMENTOR_NAME, String cOMMENT_TEXT, Timestamp cOMMENT_DATE_POSTED) {
		super();
		COMMENTOR = cOMMENTOR;
		COMMENTOR_NAME = cOMMENTOR_NAME;
		COMMENT_TEXT = cOMMENT_TEXT;
		COMMENT_DATE_POSTED = cOMMENT_DATE_POSTED;
	}
	public int COMMENTOR;
	public String COMMENTOR_NAME;
	public String COMMENT_TEXT;
	public Timestamp COMMENT_DATE_POSTED;
	public int getCOMMENTOR() {
		return COMMENTOR;
	}
	public void setCOMMENTOR(int cOMMENTOR) {
		COMMENTOR = cOMMENTOR;
	}
	public String getCOMMENT_TEXT() {
		return COMMENT_TEXT;
	}
	public void setCOMMENT_TEXT(String cOMMENT_TEXT) {
		COMMENT_TEXT = cOMMENT_TEXT;
	}
	public Timestamp getCOMMENT_DATE_POSTED() {
		return COMMENT_DATE_POSTED;
	}
	public void setCOMMENT_DATE_POSTED(Timestamp cOMMENT_DATE_POSTED) {
		COMMENT_DATE_POSTED = cOMMENT_DATE_POSTED;
	}
	public String getCOMMENTOR_NAME() {
		return COMMENTOR_NAME;
	}
	public void setCOMMENTOR_NAME(String cOMMENTOR_NAME) {
		COMMENTOR_NAME = cOMMENTOR_NAME;
	}
	
	
}
