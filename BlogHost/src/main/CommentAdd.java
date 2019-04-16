package main;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbConnection.DBConnection;
import forms.Date;
import html.CompoundElement;
import html.Element;
import models.BlogHostComments;
import models.BlogHostLikes;
import templates.MainTemplate;

@WebServlet("/CommentAdd")
public class CommentAdd extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public CommentAdd() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int r_id = (Integer.parseInt(request.getParameter("reader")));
		BigInteger post_id = BigInteger.valueOf(Integer.parseInt(request.getParameter("post")));
		String comment = request.getParameter("comment");
		BlogHostComments newComment = 
				new BlogHostComments(r_id, post_id, comment, false,-1,-1,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()));
			
		if(!newComment.commit()) {
			response.setStatus(590);
		}
		else {
			List<Comment> list = new ArrayList<Comment>();
			Connection connection = DBConnection.getDBConnection();
	        String selectSQL = "Select co.id,c2.USER_NAME as COMMENTOR_NAME, c2.id as COMMENTOR, COMMENT_TEXT, co.DATE_POSTED as COMMENT_POSTED_DATE " + 
	        		"From bloghost.BlogHostPosts as p " + 
	        		"left join bloghost.BlogHostComments as co on co.Post_ID = p.id " +
	        		"left join bloghost.BlogHostCreators as c2  on c2.id = co.CREATOR_ID " + 
	        		"WHERE p.id  = ? " + 
	        		"ORDER BY co.id DESC;";

	        PreparedStatement preparedStatement = null;
	        try {
	        	connection = DBConnection.getDBConnection();
		        preparedStatement = connection.prepareStatement(selectSQL);
		        preparedStatement.setInt(1, post_id.intValue());
		        List<Post> postListLocal = new ArrayList<Post>();
		        ResultSet rs = preparedStatement.executeQuery();
		    	int commentor = -1;
	        	String commentorName = "";
	        	String commentTextSQL = "";
	        	while(rs.next()) {
		        	if(rs.getString("commentor_Name")!=null)
		        		commentorName = rs.getString("commentor_Name");
		        	commentor = rs.getInt("COMMENTOR");
		        	if(rs.getString("COMMENT_TEXT")!=null)
		        		commentTextSQL = rs.getString("COMMENT_TEXT");
		        	Timestamp commentDatePosted = rs.getTimestamp("COMMENT_POSTED_DATE");
		        	if(commentorName != "") {
		        		Comment c = new Comment(commentor, commentorName, commentTextSQL, commentDatePosted);
		        		list.add(c);
		        	}
	        	}
        	
        	
				CompoundElement commentList = new CompoundElement("ul");
				commentList.addClass("list-group");
				commentList.setAttribute("style","list-style: none;");
				int comCounter = 0;
				if (list.size() > 0) {
					
					for(Comment c : list) {
						CompoundElement commentItem = new CompoundElement("li");
						CompoundElement commentTop = new CompoundElement("div");
						commentTop.addClass("row col-12");
						if (comCounter % 2 == 1)
							commentItem.addClass("list-group-item list-group-item-secondary");
						else
							commentItem.addClass("list-group-item ");
						comCounter++;
						Element commentText = new Element("p");
						commentText.setData(c.getCOMMENT_TEXT());
						commentText.addClass("col-8 breakText");
						Element commentDate = new Element("small");
						int comTime = (int)((new Timestamp(System.currentTimeMillis()).getTime() - c.getCOMMENT_DATE_POSTED().getTime())/1000);
						String comPosted = comTime + " second(s) ago";
						if(comTime > 59) {
							comTime = comTime/60;
							comPosted = comTime + " minute(s) ago";
							if(comTime > 59) {
								comTime = comTime/60;
								comPosted = comTime + " hour(s) ago";
								if(comTime > 23) {
									comTime = comTime/24;
									comPosted = comTime + " day(s) ago";
								}
							}
						}
						commentDate.addClass("col-4");
						commentDate.setData(comPosted);
						commentDate.setAttribute("style", "text-align:right");
						
						Element commentAuthor = new Element("small");
						commentAuthor.setData("Comment by: " + c.getCOMMENTOR_NAME());
						commentTop.addElement(commentText);
						commentTop.addElement(commentDate);
						commentItem.addElement(commentTop);
						commentItem.addElement(commentAuthor);
						commentList.addElement(commentItem);
					}
				}
				response.setContentLength(commentList.getHTML().length());
				PrintWriter out = response.getWriter();
				out.append(commentList.getHTML());
				out.close();
	       
	        } catch (Exception e) {
	            e.printStackTrace();
	         } finally {
	            try {
	               if (preparedStatement != null)
	                  preparedStatement.close();
	            } catch (Exception e2) {
	            }
	            try {
	                if (connection != null)
	                	connection.close();
	            } catch (Exception se) {
	                se.printStackTrace();
	             }
	         }
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		doGet(request, response);
	}

}
