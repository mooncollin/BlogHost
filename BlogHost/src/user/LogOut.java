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

@WebServlet("/LogOut")
public class LogOut extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public LogOut() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		request.getSession().invalidate();
		//response.setHeader("Refresh", "0; URL=" + (String) request.getHeaders("Referer").nextElement());
		response.sendRedirect((String) request.getHeaders("Referer").nextElement());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
}
