package main;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.BlogHostCreators;
import models.BlogHostSubscriptions;
import models.Model;
import user.UserUtils;

@WebServlet("/Subscribe")
public class Subscribe extends HttpServlet
{

	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;

	public static final String URL = "/BlogHost/Subscribe";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Integer thisCreatorID = UserUtils.getUserID(request);
		String creatorIDStr = request.getParameter("id");
		Integer creatorID;
		
		if(thisCreatorID == null || creatorIDStr == null)
		{
			response.setStatus(400);
			return;
		}
		
		try
		{
			creatorID = Integer.parseInt(creatorIDStr);
		}
		catch(NumberFormatException e)
		{
			response.setStatus(400);
			return;
		}
		
		if(thisCreatorID.equals(creatorID))
		{
			response.setStatus(406);
			return;
		}
		
		List<BlogHostCreators> creators = Model.getAll(BlogHostCreators.class, 
				"id=?", creatorID);
		if(creators.isEmpty())
		{
			response.setStatus(406);
			return;
		}
		
		List<BlogHostSubscriptions> subs = Model.getAll(BlogHostSubscriptions.class, 
				"subscriber_id=? and creator_id=?", thisCreatorID, creatorID);
		
		
		if(subs.isEmpty())
		{
			BlogHostSubscriptions newSub = new BlogHostSubscriptions(thisCreatorID, creatorID);
			if(newSub.commit())
			{
				response.setStatus(201);
			}
			else
			{
				response.setStatus(500);
			}
		}
		else
		{
			if(subs.get(0).delete())
			{
				response.setStatus(200);
			}
			else
			{
				response.setStatus(500);
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
