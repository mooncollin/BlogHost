package main;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.util.StringUtils;

import templates.MainTemplate;
import html.*;

@WebServlet("/HomePage")
public class HomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String URL = "/BlogHost/HomePage";

    
    public HomePage() 
    {

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		MainTemplate temp;
		if(request.getSession().getAttribute("userSiteId") != null){
			temp = new MainTemplate((String) request.getSession().getAttribute("userName"));
		}
		else {
			temp = new MainTemplate();
		}
		response.setContentType("text/html");
		CompoundElement container = temp.createContainer();
		container.setData("<center><h1>Welcome to BlogHost</h1></center>");
		temp.getCurrentTemplate().getBody().addElement(container);

		response.getWriter().println(temp.getCurrentTemplate());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

}