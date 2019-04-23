package user;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.servlet.annotation.MultipartConfig;

import dbConnection.DBConnection;
import templates.MainTemplate;
import forms.*;
import html.*;
import util.*;


@WebServlet("/Registration")
@MultipartConfig(maxFileSize = 16177215)
public class Registration extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
 
    public Registration() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		MainTemplate temp = new MainTemplate();
		CompoundElement container = new CompoundElement("div");
		container.setAttribute("class", "container");
		CompoundElement jumbotron = new CompoundElement("div");
		container.setAttribute("class", "jumbotron");
		container.addElement(jumbotron);
		
		if(register(request))
		{	
			jumbotron.setData("Success");
			response.sendRedirect(response.encodeRedirectURL("HomePage"));
		}
		
		else
		{
			jumbotron.setData("Try again");
			response.getWriter().println(temp.getCurrentTemplate());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
	
	
	private static boolean register(HttpServletRequest request)
	{
		Boolean bool = true;
		Connection connection = null;
		PreparedStatement ps = null;
		InputStream inputStream = null;
		
		try
		{
			connection = DBConnection.getDBConnection();
			
			//query for BlogHostCreators
			String query = "INSERT INTO BlogHostCreators (ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, AGE, PASSWORD, PROFILE_PICTURE, ADMIN) "
					+ "VALUES(DEFAULT, ?, ?, ?, ?, ?, md5(?), ?, DEFAULT)";
			ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			String uname = request.getParameter("Uname");
			String fname = request.getParameter("Fname");
			String lname = request.getParameter("Lname");
			String email = request.getParameter("Email");
			String age = request.getParameter("Age");
			String password = request.getParameter("Password");
			
			//image parts
			Part image = request.getPart("profilePicture");
			if (image != null)
			{
				System.out.println("image");
				image = request.getPart("profilePicture");
				inputStream = image.getInputStream();
			}
			//endImage parts
			
			ps.setString(1, uname);
			ps.setString(2, fname);
			ps.setString(3, lname);
			ps.setString(4, email);
			ps.setString(5, age);
			ps.setString(6, password);
			
			//blob for picture
			if (inputStream != null)
				ps.setBlob(7, inputStream);
			
			int rs = ps.executeUpdate(); // checks if insertion was successful
			int id = -1;
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	id = (int) generatedKeys.getLong(1);
	            }
	            else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
			
			if (rs <= 0)
			{
				bool = false;
			}
			ps.close();
			
			query = "INSERT INTO BlogHostSites (ID, CREATOR_ID, SITE_URL, SITE_NAME, CUSTOM_HTML) "
					+ "VALUES(DEFAULT, ?, DEFAULT, ?, DEFAULT)";

			
			String site = request.getParameter("Site");
			System.out.println(site);
			ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ps.setString(2, site);
			rs = ps.executeUpdate(); // checks if insertion was successful
			if (rs <= 0)
			{
				bool = false;
			}
			ps.close();
			
			connection.close();
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
		return bool;
	}
	
}
