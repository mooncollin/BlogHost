package user;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import forms.Checkbox;
import forms.Form;
import html.CompoundElement;
import html.Element;
import main.HomePage;
import main.Store;
import models.BlogHostStores;
import models.Model;
import templates.BootstrapTemplates;
import templates.MainTemplate;
import util.Template;
import utils.StreamUtils;

@WebServlet("/EditStoreItem")
@MultipartConfig
public class EditStoreItem extends HttpServlet
{

	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String URL = "/BlogHost/EditStoreItem";
	
	public static Form editStoreItemForm()
	{
		Form form = NewStoreItem.newStoreItemForm();
		
		CompoundElement checkGroup = BootstrapTemplates.formGroupCheckbox("Delete Picture", null, "deletePicture");
		Checkbox check = (Checkbox) checkGroup.getElementById("deletePicture");
		
		check.setName(check.getAttribute("id"));
		
		form.getElementsByTag("small").stream()
									  .filter(e -> e.getData().equals("Optional Picture"))
									  .forEach(e -> e.setData("Keep this blank to keep original image, upload to replace"));
		
		Element button = form.getElement(4);
		form.removeElement(button);
		
		form.addElement(checkGroup);
		form.addElement(button);
		
		return form;
	}
	
	/**
	 * Creates an edit store item form and delivers that to the user.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Integer userID = UserUtils.getUserID(request);
		String storeIDStr = request.getParameter("id");
		Integer storeID;
		if(userID == null || storeIDStr == null)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		
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
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		Template template = new MainTemplate(request).getCurrentTemplate();
		CompoundElement container = new CompoundElement("div");
		container.setAttribute("style", "left:75px");
		container.addClasses("container", "mt-5");
		
		CompoundElement flexRow = new CompoundElement("div");
		flexRow.addClasses("d-flex", "align-items-center");
		
		CompoundElement header = new CompoundElement("h1", "Edit Store Item");
		header.addClasses("d-flex", "flex-fill");
		
		CompoundElement backLink = new CompoundElement("a", "Back");
		backLink.addClasses("btn", "btn-warning", "d-flex");
		backLink.setAttribute("href", Store.URL + "?id=" + userID);
		
		flexRow.addElement(header);
		flexRow.addElement(backLink);
		
		container.addElement(flexRow);
		container.addElement(new Element("hr"));
		
		Form form = EditStoreItem.editStoreItemForm();
		
		form.getInputByName("itemNameInput").setValue(items.get(0).getItemName());
		form.getElementById("itemDescriptionInput").setData(items.get(0).getItemDescription());
		form.getElementById("itemDescriptionInput").setAttribute("rows", String.valueOf(items.get(0).getItemDescription().split("\n").length + 2));
		form.getInputByName("itemAmountInput").setValue(items.get(0).getAmount().toPlainString());
		
		container.addElement(form);
		template.getBody().addElement(container);
		response.setContentType("text/html");
		response.getWriter().print(template);
	}
	
	/**
	 * Processes an edited store item.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Integer userID = UserUtils.getUserID(request);
		String storeIDStr = request.getParameter("id");
		if(userID == null || storeIDStr == null)
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
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		BlogHostStores item = items.get(0);
		
		Form form = EditStoreItem.editStoreItemForm();
		if(form.validate(request.getParameterMap())
			&& request.getParameter("itemDescriptionInput") != null
			&& !request.getParameter("itemDescriptionInput").isEmpty())
		{
			String itemName = form.getInputByName("itemNameInput").getValue();
			String itemDescription = request.getParameter("itemDescriptionInput");
			BigDecimal amount;
			
			try
			{
				amount = new BigDecimal(form.getInputByName("itemAmountInput").getValue());
			}
			catch(NumberFormatException e)
			{
				doGet(request, response);
				return;
			}
			
			byte[] picture = item.getPicture();
			for(Part p : request.getParts())
			{
				if(p != null
					&& p.getContentType() != null
					&& p.getContentType().contains("image"))
				{
					picture = StreamUtils.getStreamBytes(p.getInputStream());
					break;
				}
			}
			
			if(form.getInputByName("deletePicture").getValue() != null)
			{
				picture = null;
			}
			
			item.setItemName(itemName);
			item.setItemDescription(itemDescription);
			item.setPicture(picture);
			item.setAmount(amount);
			item.commit();
			response.sendRedirect(Store.URL + "?id=" + userID);
			return;
		}
		
		doGet(request, response);
	}
}
