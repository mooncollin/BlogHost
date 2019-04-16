package user;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import forms.File;
import forms.Form;
import forms.Number;
import forms.TextField;
import html.CompoundElement;
import html.Element;
import main.HomePage;
import main.Store;
import models.BlogHostSites;
import models.BlogHostStores;
import models.Model;
import templates.BootstrapTemplates;
import templates.MainTemplate;
import util.Template;
import utils.StreamUtils;

@WebServlet("/NewStoreItem")
@MultipartConfig
public class NewStoreItem extends HttpServlet
{

	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String URL = "/BlogHost/NewStoreItem";
	
	public static Form newStoreItemForm()
	{
		Form form = new Form();
		form.setMethod("POST");
		form.setEnctype("multipart/form-data");
		
		CompoundElement itemNameGroup = BootstrapTemplates.formGroup("Name", TextField.class, null, "itemNameInput");
		CompoundElement itemDescriptionGroup = BootstrapTemplates.formGroupTextarea("Description", null, "itemDescriptionInput");
		CompoundElement amountGroup = BootstrapTemplates.formGroup("Price", Number.class, null, "itemAmountInput");
		CompoundElement pictureGroup = BootstrapTemplates.formGroup("Picture", File.class, "Optional Picture", "pictureInput");
		CompoundElement submitButton = new CompoundElement("button", "Submit");
		
		TextField nameInput = (TextField) itemNameGroup.getElementById("itemNameInput");
		nameInput.setMaxLength(50);
		nameInput.setRequired(true);
		nameInput.setName(nameInput.getAttribute("id"));
		
		Element textAreaInput = itemDescriptionGroup.getElementById("itemDescriptionInput");
		textAreaInput.setAttribute("name", textAreaInput.getAttribute("id"));
		textAreaInput.setAttribute("required", "");
		
		Number amountInput = (Number) amountGroup.getElementById("itemAmountInput");
		amountInput.setName(amountInput.getAttribute("id"));
		amountInput.setRequired(true);
		amountInput.setMin(0);
		amountInput.setStep(0.01);
		
		File pictureInput = (File) pictureGroup.getElementById("pictureInput");
		pictureInput.setName(pictureInput.getAttribute("id"));
		
		submitButton.addClasses("btn", "btn-primary");
		
		form.addElement(itemNameGroup);
		form.addElement(itemDescriptionGroup);
		form.addElement(amountGroup);
		form.addElement(pictureGroup);
		form.addElement(submitButton);
		
		return form;
	}
	
	/**
	 * Creates a new store item form and delivers that to the user.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Integer userID = UserUtils.getUserID(request);
		if(userID == null)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		Template template = new MainTemplate(UserUtils.getUserName(request), UserUtils.getUserSiteID(request)).getCurrentTemplate();
		CompoundElement container = new CompoundElement("div");
		container.addClasses("container", "mt-5");
		
		CompoundElement flexRow = new CompoundElement("div");
		flexRow.addClasses("d-flex", "align-items-center");
		
		CompoundElement header = new CompoundElement("h1", "Add Store Item");
		header.addClasses("d-flex", "flex-fill");
		
		CompoundElement backLink = new CompoundElement("a", "Back");
		backLink.addClasses("btn", "btn-warning", "d-flex");
		backLink.setAttribute("href", Store.URL + "?id=" + userID);
		
		flexRow.addElement(header);
		flexRow.addElement(backLink);
		
		container.addElement(flexRow);
		container.addElement(new Element("hr"));
		container.addElement(NewStoreItem.newStoreItemForm());
		
		template.getBody().addElement(container);
		response.setContentType("text/html");
		response.getWriter().print(template);
	}
	
	/**
	 * Processes a new store item.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Integer userSiteID = UserUtils.getUserSiteID(request);
		if(userSiteID == null)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		Form form = newStoreItemForm();
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
			
			byte[] picture = null;
			Collection<Part> parts = request.getParts();
			for(Part p : parts)
			{
				if(p != null &&
					p.getContentType() != null &&
					p.getContentType().contains("image"))
				{
					picture = StreamUtils.getStreamBytes(p.getInputStream());
					break;
				}
			}
			
			BlogHostStores newStoreItem = new BlogHostStores(UserUtils.getUserID(request),
					itemName, itemDescription, picture, amount);
			
			if(newStoreItem.commit())
			{
				response.sendRedirect(Store.URL + "?id=" + UserUtils.getUserID(request));
				return;
			}
			else
			{
				System.err.println("Error in committing store item");
			}
		}
		
		doGet(request, response);
	}
}
