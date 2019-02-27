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
 * Servlet implementation class addPost
 */
@WebServlet("/addPost")
public class addPost extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addPost() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		int siteID = Integer.parseInt((String) request.getSession().getAttribute("lastSite"));
        
        Connection connection = null;
        String insertSql = " INSERT INTO BlogHostPosts (id, SITE_ID, POST_TITLE, POST_TEXT, DATE_POSTED) values (default, ?, ?, ?, NOW())";
        try {
           DBConnection.getDBConnection();
           connection = DBConnection.connection;
           PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
           preparedStmt.setString(1, String.valueOf(siteID));
           preparedStmt.setString(2, title);
           preparedStmt.setString(3, request.getParameter("postText"));
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
