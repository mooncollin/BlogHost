package main;

import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forms.Form;
import html.CompoundElement;
import html.Element;
import models.BlogHostCreators;
import models.BlogHostStores;
import models.Model;
import templates.BootstrapTemplates;
import templates.MainTemplate;
import user.EditStoreItem;
import user.NewStoreItem;
import user.UserUtils;
import util.Template;

@WebServlet("/Store")
public class Store extends HttpServlet
{

	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;

	public static final String URL = "/BlogHost/Store";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Integer userID = UserUtils.getUserID(request);
		String creatorIDStr = request.getParameter("id");
		if(creatorIDStr == null)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		Integer creatorID;
		
		try
		{
			creatorID = Integer.valueOf(creatorIDStr);
		}
		catch(NumberFormatException e)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		List<BlogHostCreators> creators = Model.getAll(BlogHostCreators.class, "id=?", creatorID);
		if(creators.isEmpty())
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		BlogHostCreators creator = creators.get(0);
		
		List<BlogHostStores> items = Model.getAll(BlogHostStores.class, "creator_id=?", creatorID);
		
		Template template = new MainTemplate(request).getCurrentTemplate();
		CompoundElement container = new CompoundElement("div");
		container.setAttribute("style", "left:75px");
		container.addClasses("container", "mt-5");
		
		CompoundElement flexRow = new CompoundElement("div");
		flexRow.addClasses("d-flex", "align-items-center");
		
		CompoundElement header = new CompoundElement("h1", String.format("%s's Store", creator.getUserName()));
		header.addClasses("d-flex", "flex-fill");
		flexRow.addElement(header);
		
		if(userID != null && userID.equals(creatorID))
		{
			CompoundElement addItemLink = new CompoundElement("a", "Add Item");
			addItemLink.addClasses("btn", "btn-primary", "d-flex");
			addItemLink.setAttribute("href", NewStoreItem.URL);
			
			flexRow.addElement(addItemLink);	
		}
		container.addElement(flexRow);
		container.addElement(new Element("hr"));
		
		CompoundElement cardContainer = new CompoundElement("div");
		cardContainer.addClasses("bg-light", "w-100", "pb-4", "mb-5");
		
		CompoundElement row = new CompoundElement("div");
		row.addClass("row");
		cardContainer.addElement(row);
		
		int counter = 1;
		
		for(BlogHostStores item : items)
		{
			String adjustedDescription = item.getItemDescription();
			if(adjustedDescription.length() >= 100)
			{
				adjustedDescription = adjustedDescription.substring(0, 100) + "...";
			}
			CompoundElement card = BootstrapTemplates.card(String.format("%s - $%s", item.getItemName(), item.getAmount().toPlainString()), 
				adjustedDescription, item.getPicture(), "", "More Information");
			card.addClasses("h-100", "mt-3", "mx-3", "shadow-sm");
			CompoundElement cardBody = (CompoundElement) card.getElementsByClass("card-body").get(0);
			cardBody.addClasses("d-flex", "flex-column");
			if(userID != null && userID.equals(creatorID))
			{
				CompoundElement editLink = new CompoundElement("a", "Edit");
				editLink.addClasses("btn", "btn-warning", "p-2");
				editLink.setAttribute("href", EditStoreItem.URL + "?id=" + item.getID());
				CompoundElement deleteButton = new CompoundElement("button", "Delete");
				deleteButton.addClasses("btn", "btn-danger", "p-2", "w-100");
				Form deleteForm = new Form();
				deleteForm.setMethod("POST");
				deleteForm.setAction(Store.URL + "?id=" + item.getID());
				deleteForm.addElement(deleteButton);
				
				cardBody.addElement(editLink);
				cardBody.addElement(deleteForm);
			}
			CompoundElement cardButton = (CompoundElement) card.getElementsByClass("btn-primary").get(0);
			cardButton.addClasses("mt-auto", "p-2");
			cardButton.setAttribute("data-toggle", "modal");
			cardButton.setAttribute("data-target", "#modal" + counter);
			
			CompoundElement column = new CompoundElement("div");
			column.addClasses("col-xs-12", "col-sm-12", "col-md-6", "col-lg-4", "mt-2");
			
			column.addElement(card);
			row.addElement(column);
			List<Element> modalBody = new LinkedList<Element>();
			Element image = new Element("img");
			image.addClass("w-100");
			if(item.getPicture() == null)
			{
				image.setAttribute("src", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/No_image_available_600_x_450.svg/600px-No_image_available_600_x_450.svg.png");
			}
			else
			{
				image.setAttribute("src", "data:image/jpg;base64," + Base64.getEncoder().encodeToString(item.getPicture()));
			}
			
			CompoundElement price = new CompoundElement("p", "Price: $" + item.getAmount().toPlainString());
			price.addClass("mt-2");
			CompoundElement description = new CompoundElement("textarea", item.getItemDescription());
			description.setAttribute("rows", String.valueOf(item.getItemDescription().split("\n").length + 2));
			description.addClasses("w-100", "border-0");
			description.setAttribute("readonly", "");
			description.setAttribute("style", "resize:none");
			
			List<Element> modalFooter = new LinkedList<Element>();
			CompoundElement buyButton = new CompoundElement("a", "Buy Now");
			buyButton.addClasses("btn", "btn-success", "text-white");
			
			modalBody.add(image);
			modalBody.add(price);
			modalBody.add(description);
			modalFooter.add(buyButton);
			CompoundElement modal = BootstrapTemplates.scrollableModal(item.getItemName(), "modal" + counter, modalBody, modalFooter);
			template.getBody().addEndElement(modal);
			counter++;
		}
		
		if(items.isEmpty())
		{
			CompoundElement jumbotron = new CompoundElement("div");
			jumbotron.addClass("jumbotron");
			CompoundElement h1 = new CompoundElement("h1", "This Store is Empty!");
			CompoundElement lead = new CompoundElement("p", "It looks like this creator has no items for sale. Check back some other time.");
			lead.addClass("lead");
			
			jumbotron.addElement(h1);
			jumbotron.addElement(lead);
			
			container.addElement(jumbotron);
		}
		else
		{
			container.addElement(cardContainer);
		}
		template.getBody().addElement(container);
		
		Element ad = new Element("img");
		Ad adString = new Ad();
		ad.setAttribute("src", adString.getAd());
		
		ad.setAttribute("style","display: block;margin: 0 auto; position: relative;left:75px");
		template.getBody().addElement(ad);
		template.getBody().addElement(new Element("br"));
		template.getBody().addElement(new Element("br"));
		template.getBody().addElement(new Element("br"));
		response.setContentType("text/html");
		response.getWriter().print(template);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Integer userID = UserUtils.getUserID(request);
		String storeIDStr = request.getParameter("id");
		if(storeIDStr == null  || userID == null)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		Integer storeID;
		
		try
		{
			storeID = Integer.valueOf(storeIDStr);
		}
		catch(NumberFormatException e)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		List<BlogHostStores> items = Model.getAll(BlogHostStores.class, "id=? and creator_id=?", storeID, userID);
		if(items.isEmpty())
		{
			response.sendRedirect(Store.URL + "?id=" + userID);
			return;
		}
		
		if(!items.get(0).delete())
		{
			System.err.println("Error deleting Store Item");
		}
		
		response.sendRedirect(Store.URL + "?id=" + userID);
	}
}
