package BlogHostHome;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import LoginClasses.DBConnection;

/**
 * Servlet implementation class addEditedPost
 */
@WebServlet("/addEditedPost")
public class addEditedPost extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addEditedPost() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String postID = (String) request.getSession().getAttribute("postID");
		int siteID = Integer.parseInt((String) request.getSession().getAttribute("lastSite"));
        
        Connection connection = null;
        String insertSql = " UPDATE BlogHostPosts SET POST_TITLE = ?, POST_TEXT = ? WHERE id = ?";
        try {
           DBConnection.getDBConnection();
           connection = DBConnection.connection;
           PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
           preparedStmt.setString(1, title);
           preparedStmt.setString(2, request.getParameter("postText"));
           preparedStmt.setString(3, postID);
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

}
