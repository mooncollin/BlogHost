package main;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.BlogHostCreators;
import models.BlogHostDonations;
import models.Model;
import user.UserUtils;

@WebServlet("/Donation")
public class Donation extends HttpServlet
{

	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;

	public static final String URL = "/BlogHost/Donation";
	public static final double MAX_DONATION = 10000;
	public static final double MIN_DONATION = 0.01;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Integer creatorID = UserUtils.getUserID(request);
		String creatorIDStr = request.getParameter("id");
		Integer creatorIDVar;
		String amountStr = request.getParameter("amount");
		Double amount;
		if(creatorID == null || creatorIDStr == null || amountStr == null)
		{
			response.setStatus(400);
			return;
		}
		try
		{
			creatorIDVar = Integer.valueOf(creatorIDStr);
			amount = Double.valueOf(amountStr);
		}
		catch(NumberFormatException e)
		{
			response.setStatus(400);
			return;
		}
		
		if(creatorID.equals(creatorIDVar) || amount < MIN_DONATION || amount > MAX_DONATION)
		{
			response.setStatus(406);
			return;
		}
		
		List<BlogHostCreators> creator = Model.getAll(BlogHostCreators.class, "id=?", creatorIDVar);
		if(creator.isEmpty())
		{
			response.setStatus(406);
			return;
		}
		
		BlogHostDonations donation = new BlogHostDonations(creatorID, creatorIDVar, new BigDecimal(amount));
		
		if(donation.commit())
		{
			response.setStatus(200);
		}
		else
		{
			response.setStatus(500);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
