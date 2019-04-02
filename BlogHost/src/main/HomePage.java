package main;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import templates.MainTemplate;
import html.*;
import util.*;

@WebServlet("/HomePage")
public class HomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;

    
    public HomePage() 
    {

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		MainTemplate temp = new MainTemplate();
		
		response.setContentType("text/html");
		
		CompoundElement container = new CompoundElement("div");
		container.setAttribute("class", "container");
		CompoundElement jumbotron = new CompoundElement("div");
		container.setAttribute("class", "jumbotron");
		container.addElement(jumbotron);
		jumbotron.setData("<center><h1>Welcome to BlogHost</h1></center>");
		temp.getCurrentTemplate().getBody().addElement(container);
		
		response.getWriter().println(temp.getCurrentTemplate());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

}