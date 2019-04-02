package main;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import html.CompoundElement;
import templates.MainTemplate;

@WebServlet("/About")
public class About extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public About() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String info = "<h1>About</h1><p>What kind of platform are ordinary users looking for to create their own blog? "
				+ "Beginners will need a platform that can be easily customized and does not require coding skills. "
				+ "If users just want to join communities dedicated to the specific topic of interest and share ideas, "
				+ "this is one thing. If they have big plans and they want to use a blog to grow their business to make "
				+ "money using a blog, then this is completely different. Even if they do not plan to make a profit from "
				+ "blogging now, they may want to make money from it in the future. Users will need to choose a flexible "
				+ "platform with the possibility of further growth.";
		
		MainTemplate temp = new MainTemplate();
		CompoundElement container = new CompoundElement("div");
		container.setAttribute("class", "container");
		CompoundElement jumbotron = new CompoundElement("div");
		container.setAttribute("class", "jumbotron");
		container.addElement(jumbotron);
		jumbotron.setData(info);
	
		temp.getCurrentTemplate().getBody().addElement(container);

		response.setContentType("text/html");
		response.getWriter().println(temp.getCurrentTemplate());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		doGet(request, response);
	}

}
