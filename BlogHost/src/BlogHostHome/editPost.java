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

/**
 * Servlet implementation class editPost
 */
@WebServlet("/editPost")
public class editPost extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public editPost() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String postID = request.getParameter("postID");
		request.getSession().setAttribute("postID", postID);
		System.out.println("postID = " + postID);
		PrintWriter out = response.getWriter();
		String title = "Edit Post";
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
	         String selectSQL = "SELECT * FROM BlogHostPosts where id=?";
	         preparedStatement = connection.prepareStatement(selectSQL);
	         preparedStatement.setString(1, request.getParameter("postID"));
	         
	         ResultSet resultSet = preparedStatement.executeQuery();
	         resultSet.next();
	         out.println("<h4>Create new Post</h4>"+ 
	 				"<form action=\"addEditedPost\" method=\"POST\">\n" + 
	 				"Title<input type=\"text\" name=\"title\" value=\"" + resultSet.getString("POST_TITLE") + "\"/><br/>\n" +
	 				"<textarea name=\"postText\" rows=\"12\" cols=\"36\">\n" + 
	 				resultSet.getString("POST_TEXT") + 
	 				"</textarea>" +  
	 				"<input type=\"submit\" value=\"Post\"/>");
	         out.print("<a href=\"/BlogHost/deletePost?postID=" + resultSet.getString("id") + "\">Delete</a>");
	         out.println("</body></html>");
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
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
