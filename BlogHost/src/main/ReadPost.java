package main;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.BlogHostPostStats;


@WebServlet("/ReadPost")
public class ReadPost extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public ReadPost() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		BigInteger post_id = BigInteger.valueOf(Integer.parseInt(request.getParameter("post")));
		List<BlogHostPostStats> statL = BlogHostPostStats.getAll(BlogHostPostStats.class,"Post_id = ?", post_id.toString());
		BlogHostPostStats stat = null;
		if (statL.size() == 0){
			stat = new BlogHostPostStats(post_id, 1, 0);
		}else {
			stat = statL.get(0);
			stat.setViews(stat.getViews()+1);
		}
		if(!stat.commit()) {
			response.setStatus(590);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		doGet(request, response);
	}

}
