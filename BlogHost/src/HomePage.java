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
		CompoundElement div = temp.addElementWithSingleAttribute("div", "align", "center");
		CompoundElement h1 = new CompoundElement("h1");
		h1.setData("Welcome!");
		
		div.addElement(h1);
		temp.getForm().addElement(div);

		response.setContentType("text/html");
		response.getWriter().println(temp.basicTemplate());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

}