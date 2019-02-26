package BlogHostHome;

import java.io.IOException;
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

@WebServlet("/HomePage/*")
public class HomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public HomePage() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		
		String pageOwner = request.getPathInfo().substring(1);
		int pageOwnerID = UserAccount.getUserID(pageOwner);
		System.out.println("PageOwnerID: " + pageOwnerID);
		
		String currentUser = null;
		try{
			currentUser = LoginUtil.getCurrentUser(request.getSession()).getUserName();
			System.out.println("Current User = " + currentUser);
		} catch(Exception e) {
			response.getWriter().append("<a href=\"ec2-3-17-132-90.us-east-2.compute.amazonaws.com:8080/login.html\">login</a>");
		}
		request.getSession().setAttribute("pageOwnerID", pageOwnerID);
		
		//System.out.println("Current User: " + currentUser);
		//System.out.println("Page Owner: " + pageOwner);
		if(pageOwnerID == -1) {
			response.getWriter().append("<p> user " + request.getPathInfo().substring(1) + " not found");
			pageOwner = null;
		} else{
			boolean isMod = false;
			for(int mod : LoginUtil.getCurrentUser(request.getSession()).getMods()) {
				if(mod == pageOwnerID) {
					isMod = true;
				}
			}
		
		
		
		if (currentUser != null && pageOwner.equals(currentUser)) {
			String name = ", " + LoginUtil.getCurrentUser(request.getSession()).getFirstName();
			response.getWriter().append("<p>Welcome to your Homepage" + name + "!</p>");
		} else {
			response.getWriter().append("<p>" + pageOwner + "'s homepage</p>");
			if(!isMod) {
				response.getWriter().append("<form action=\"../addMod\" method=\"POST\">\n");
   		 		response.getWriter().append( "<input type=\"submit\" value=\"add as mod\"/></form>");
			}
		}
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			DBConnection.getDBConnection();
	        connection = DBConnection.connection;
	         String selectSQL = "SELECT * FROM BlogHostSites where CREATOR_ID=?";
	         preparedStatement = connection.prepareStatement(selectSQL);
	         preparedStatement.setString(1, String.valueOf(pageOwnerID));
	         
	         ResultSet resultSet = preparedStatement.executeQuery();
	         int count = 0;
	         while(resultSet.next()) {
	        	 count++;
	        	 response.getWriter().append("<p><a href=\"Site?siteID=" + resultSet.getString("id") + "\">" +  resultSet.getString("SITE_NAME") + "</a></p>" );
	         }
	         if( count == 0) {
	        	 if(currentUser != null && pageOwner.equals(currentUser)) {
	        		 response.getWriter().append("<p>Share your link <a href=\"ec2-3-17-132-90.us-east-2.compute.amazonaws.com:8080/HomePage/" + currentUser + "\" >ec2-3-17-132-90.us-east-2.compute.amazonaws.com:8080/HomePage/" + currentUser + "</a> with all your friends!</p>");
	        		 response.getWriter().append("<form action=\"addSite\" method=\"POST\">\n");
	        		 response.getWriter().append("Choose a name for your Blog:<input type=\"text\" name=\"name\"/><br/>\n" + 
	 					"<input type=\"submit\" value=\"Create\"/>\n" + 
	 					"</form>");
	        	 } else {
	        		 response.getWriter().append(pageOwner + "has not created a site yet.");
	        	 }
	         } else {
	        	 
	         }
	         
	         response.getWriter().append("<p><b>Featured Sites</b><p>");
	         selectSQL = "SELECT * FROM BlogHostSites";
	         preparedStatement = connection.prepareStatement(selectSQL);
	         resultSet = preparedStatement.executeQuery();
	         while(resultSet.next()) {
	        	 response.getWriter().append("<p><a href=\"Site?siteID=" + resultSet.getString("id") + "\">" +  resultSet.getString("SITE_NAME") + "</a></p>" );
	         }
	         
	         
	         resultSet.close();
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
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	

}
