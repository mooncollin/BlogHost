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
 * Servlet implementation class addMod
 */
@WebServlet("/addMod")
public class addMod extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addMod() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String modID =  String.valueOf(request.getSession().getAttribute("pageOwnerID"));
		
        Connection connection = null;
        String insertSql = " INSERT INTO BlogHostMods (MOD_ID, CREATOR_ID) values ( ?, ?)";
        String currentUser = LoginUtil.getCurrentUser(request.getSession()).getUserName();
        try {
           DBConnection.getDBConnection();
           connection = DBConnection.connection;
           PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
           preparedStmt.setString(1, modID);
           preparedStmt.setString(2, String.valueOf(UserAccount.getUserID(currentUser)));
           preparedStmt.execute();
           preparedStmt.close();
           connection.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
        
        int[] mods = LoginUtil.getCurrentUser(request.getSession()).getMods();
        int[] newModArray = new int[mods.length + 1];
        for(int i = 0; i < mods.length; i++) {
        	newModArray[i] = mods[i];
        }
        newModArray[mods.length] = Integer.parseInt(modID);
        LoginUtil.getCurrentUser(request.getSession()).setMods(newModArray);
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
