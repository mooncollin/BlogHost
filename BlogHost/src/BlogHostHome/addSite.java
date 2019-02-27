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
import LoginClasses.LoginUtil;

/**
 * Servlet implementation class addSite
 */
@WebServlet("/addSite")
public class addSite extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addSite() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String siteName = request.getParameter("name");
        
        Connection connection = null;
        String insertSql = " INSERT INTO BlogHostSites (id, CREATOR_ID, SITE_NAME) values (default, ?, ?)";
        String currentUser = LoginUtil.getCurrentUser(request.getSession()).getUserName();
        try {
           DBConnection.getDBConnection();
           connection = DBConnection.connection;
           PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
           preparedStmt.setString(1, String.valueOf(UserAccount.getUserID(currentUser)));
           preparedStmt.setString(2, siteName);
           preparedStmt.execute();
           preparedStmt.close();
           connection.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
        RequestDispatcher rd = request.getRequestDispatcher("HomePage/" + currentUser);
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
