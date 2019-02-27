package BlogHostHome;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import LoginClasses.DBConnection;
import LoginClasses.LoginUtil;

/**
 * Servlet implementation class Site
 */
@WebServlet("/Site")
public class Site extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Site() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int pageOwnerID = Integer.parseInt(getSiteOwner(request.getParameter("siteID")));
		int currentUserID = LoginUtil.getCurrentUser(request.getSession()).getUserID();
		boolean isModFor = false;
		for(int creator : LoginUtil.getCurrentUser(request.getSession()).getModFor()) {
			if(creator == pageOwnerID) {
				isModFor = true;
			}
		}
		boolean canEdit = (pageOwnerID == currentUserID) || isModFor;
		
		
		String siteID = request.getParameter("siteID");
		request.getSession().setAttribute("lastSite", siteID);
		System.out.println("siteID is:" + siteID);
		response.setContentType("text/html");
	      PrintWriter out = response.getWriter();
	      String title = getSiteName(request.getParameter("siteID"));
	      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
	      out.println(docType + //
	            "<html>\n" + //
	            "<head><title>" + title + "</title></head>\n" + //
	            "<body bgcolor=\"#f0f0f0\">\n" + //
	            "<h2 align=\"center\">" + title + "</h2>\n");
	      
	      Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				DBConnection.getDBConnection();
		        connection = DBConnection.connection;
		         String selectSQL = "SELECT * FROM BlogHostPosts where SITE_ID=? ORDER BY DATE_POSTED DESC";
		         preparedStatement = connection.prepareStatement(selectSQL);
		         preparedStatement.setString(1, request.getParameter("siteID"));
		         
		         ResultSet resultSet = preparedStatement.executeQuery();
		         while(resultSet.next()) {
		        	 out.print("<div> <h4>" + resultSet.getString("POST_TITLE") + "</h4><p>" + resultSet.getString("POST_TEXT") +"</p>");
		        	 out.print("<p><b>Posted " + resultSet.getString("DATE_POSTED") + "</b>");
		        	 if(resultSet.getString("DATE_POSTED").trim() != resultSet.getString("DATE_MODIFIED").trim()) {
		        		 out.print(" Edited " + resultSet.getString("DATE_MODIFIED"));
		        	 }
		        	 if(canEdit) {
		        		 out.print("<a href=\"/BlogHost/editPost?postID=" + resultSet.getString("id") + "\">edit</a>");
		        	 }
		        	 out.print("</p></div>");
		        	 
		        	 selectSQL = "SELECT * FROM BlogHostComments where POST_ID=? ORDER BY DATE_POSTED DESC";
			         preparedStatement = connection.prepareStatement(selectSQL);
			         preparedStatement.setString(1, resultSet.getString("id"));
			         ResultSet commentSet = preparedStatement.executeQuery();
			         while(commentSet.next()) {
			        	 out.print("<p>" + commentSet.getString("Creator_ID") + ": " + commentSet.getString("COMMENT_TEXT") + "</p>");
			         }
			         
		        	 out.print( "<form action=\"addComment?postID=" + resultSet.getString("id") + "\" method=\"POST\">\n" + 
								"<input type=\"text\" name=\"text\" value=\"comment\"/><br/>\n" +
								"<input type=\"submit\" value=\"Comment\"/></form>");
		         }
		         preparedStatement.close();
		         connection.close();
		      } catch (SQLException se) {
		         se.printStackTrace();
		      } catch (Exception e) {
		         e.printStackTrace();
		      } finally {
		         try {
		            if (preparedStatement != null)
		               preparedStatement.close();
		         } catch (SQLException se2) {
		         }
		         try {
		            if (connection != null)
		               connection.close();
		         } catch (SQLException se) {
		            se.printStackTrace();
		         }
		      }
				if(pageOwnerID == currentUserID){
				out.println("<h4>Create new Post</h4>"+ 
						"<form action=\"addPost\" method=\"POST\">\n" + 
						"Title<input type=\"text\" name=\"title\"/><br/>\n" +
						"<textarea name=\"postText\" rows=\"12\" cols=\"36\">\n" + 
						"Enter your post text here\n" + 
						"</textarea>" +  
						"<input type=\"submit\" value=\"Post\"/></form>");
			}
		    out.println("</body></html>");
	      
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public String getSiteName(String siteID) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String result = "";
		try {
			DBConnection.getDBConnection();
	        connection = DBConnection.connection;
	         String selectSQL = "SELECT * FROM BlogHostSites where id=?";
	         preparedStatement = connection.prepareStatement(selectSQL);
	         preparedStatement.setString(1, siteID);
	         
	         ResultSet resultSet = preparedStatement.executeQuery();
	         resultSet.next();
	         result = resultSet.getString("SITE_NAME");
	         preparedStatement.close();
	         connection.close();
	      } catch (SQLException se) {
	         se.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      } finally {
	         try {
	            if (preparedStatement != null)
	               preparedStatement.close();
	         } catch (SQLException se2) {
	         }
	         try {
	            if (connection != null)
	               connection.close();
	         } catch (SQLException se) {
	            se.printStackTrace();
	         }
	      }
		return result;
	}
	
	public String getSiteOwner(String siteID) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String result = "";
		try {
			DBConnection.getDBConnection();
	        connection = DBConnection.connection;
	         String selectSQL = "SELECT * FROM BlogHostSites where id=?";
	         preparedStatement = connection.prepareStatement(selectSQL);
	         preparedStatement.setString(1, siteID);
	         
	         ResultSet resultSet = preparedStatement.executeQuery();
	         resultSet.next();
	         result = resultSet.getString("Creator_ID");
	         preparedStatement.close();
	         connection.close();
	      } catch (SQLException se) {
	         se.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      } finally {
	         try {
	            if (preparedStatement != null)
	               preparedStatement.close();
	         } catch (SQLException se2) {
	         }
	         try {
	            if (connection != null)
	               connection.close();
	         } catch (SQLException se) {
	            se.printStackTrace();
	         }
	      }
		return result;
	}

}
