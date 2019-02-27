package BlogHostHome;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import LoginClasses.DBConnection;

/**
 * Servlet implementation class addComment
 */
@WebServlet("/addComment")
public class addComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addComment() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int siteID = Integer.parseInt((String) request.getSession().getAttribute("lastSite"));
        String postID = request.getParameter("postID");
		
        Connection connection = null;
        String insertSql = " INSERT INTO BlogHostComments (id, CREATOR_ID, POST_ID, COMMENT_TEXT, THREAD, LIKES, DATE_POSTED) values (default, ?, ?, ?, ?, 0, NOW())";
        try {
           DBConnection.getDBConnection();
           connection = DBConnection.connection;
           PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
           preparedStmt.setString(1, getCreatorID(siteID));
           preparedStmt.setString(2, postID);
           preparedStmt.setString(3, request.getParameter("text"));
           preparedStmt.setString(4, "0");
           preparedStmt.execute();
           preparedStmt.close();
           connection.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
        RequestDispatcher rd = request.getRequestDispatcher("Site?siteID=" + siteID);
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public String getCreatorID(int siteID) {
		String Creator_ID = null;
		boolean status = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			DBConnection.getDBConnection();
	        connection = DBConnection.connection;
	        String selectSQL = "SELECT * FROM BlogHostSites where id=?";
	        preparedStatement = connection.prepareStatement(selectSQL);
	        preparedStatement.setLong(1, siteID);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        status = resultSet.next();
	        if(status) {
	        	 Creator_ID = resultSet.getString("CREATOR_ID").trim();
	        }
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
		return Creator_ID;
	}

}
