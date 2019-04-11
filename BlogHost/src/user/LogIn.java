package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dbConnection.DBConnection;
import forms.Form;
import forms.Password;
import forms.Submit;
import forms.TextField;
import html.CompoundElement;
import html.Element;
import templates.MainTemplate;

@WebServlet("/LogIn")
public class LogIn extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public LogIn() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String username = request.getParameter("Username");
		String password = request.getParameter("Password");
		String user = authentication(username, password, request);
		
		HttpSession session = request.getSession(true);
		
		MainTemplate temp;
		
		if (user != null && user != "")
		{
			temp = new MainTemplate(user);
			String userID = new String("userID");
		    String userName = new String(user);
		    session.setAttribute("user", userName);
		    response.setHeader("Refresh", "0; URL=" + request.getContextPath() + "/Site?site="+session.getAttribute("userSiteId"));
		}
		
		else
		{
			temp = new MainTemplate();
			response.setHeader("Refresh", "0; URL=" + request.getContextPath() + "/HomePage");
		}
		
		
		
		response.setContentType("text/html");
		response.getWriter().println(temp.getCurrentTemplate());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
	
	
	private static String authentication(String user, String pass, HttpServletRequest request)
	{
		Connection connection = null;
		PreparedStatement ps = null;
		
		try
		{
			connection = DBConnection.getDBConnection();
			
			String query = "SELECT * FROM BlogHostCreators as c join BlogHostSites as s on"
					+ " s.Creator_ID = c.id  WHERE USER_NAME=? AND PASSWORD=md5(?)";
			ps = connection.prepareStatement(query);
			ps.setString(1, user);
			ps.setString(2, pass);

			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				String un =  new String(rs.getString("USER_NAME").trim());
				int siteId =  rs.getInt(10);
				request.getSession().setAttribute("userName", un);
				request.getSession().setAttribute("userSiteId", siteId);
				rs.close();
				ps.close();
				connection.close();	
				return un;
			}
			
			else
			{	
				rs.close();
				ps.close();
				connection.close();	
				return new String();
			}
		}
		
		catch (SQLException se) 
		{
			se.printStackTrace();
		} 
		
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		
		finally 
		{
			try 
			{
				if (ps != null)
					ps.close();
			} 
			
			catch (SQLException se2) 
			{}
			
			try 
			{
				if (connection != null)
					connection.close();
			} 
			
			catch (SQLException se) 
			{
				se.printStackTrace();
			}
		}
		
		return "";
	}
	
	
}
