package main;
import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import html.CompoundElement;
import models.BlogHostLikes;
import templates.MainTemplate;

@WebServlet("/Like")
public class Like extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public Like() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int r_id = (Integer.parseInt(request.getParameter("reader")));
		BigInteger post_id = BigInteger.valueOf(Integer.parseInt(request.getParameter("post")));
		int action = (Integer.parseInt(request.getParameter("action")));
		if(action == 1) {
			
			BlogHostLikes like = new BlogHostLikes(r_id, post_id);
			
			if(!like.commit()) {
				response.setStatus(590);
			}
		}
		else {
			BlogHostLikes like = BlogHostLikes.getAll(BlogHostLikes.class,
					"READER_ID = ? AND POST_ID = ?", r_id, post_id).get(0);
			if(!like.delete()) {
				response.setStatus(591);
			}
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		doGet(request, response);
	}

}
