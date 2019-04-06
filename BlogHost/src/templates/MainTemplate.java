package templates;

import html.*;
import forms.*;
import util.*;

public class MainTemplate 
{
	private static Template tmp;
	private static TopBar topBar;
	private static BotBar botBar;

	public MainTemplate()
	{
		createTmp();
		topBar = new TopBar();
		botBar = new BotBar();
		basicTemplate();
	}
	
	public MainTemplate(String user)
	{
		createTmp();
		topBar = new TopBar();
		botBar = new BotBar();
		basicTemplate();
		setBody();
	}
	
	public static Template basicTemplate()
	{
		tmp.getHead().addStylesheet("css/bootstrap.min.css");
		tmp.getHead().addStylesheet("css/style.css");
		tmp.getBody().addElement(topBar.getTopBar());
		tmp.getBody().addEndElement(botBar.getBotBar());
		tmp.getBody().addScript("js/jquery-3.3.1.min.js");
		tmp.getBody().addScript("js/popper.min.js");
		tmp.getBody().addScript("js/bootstrap.min.js");
		
		return tmp;
	}
	
<<<<<<< dev_collin_Database_Classes
	public CompoundElement createContainer()
	{
		CompoundElement container = new CompoundElement("div");
		container.setAttribute("class", "container");
		
		CompoundElement jumbotron = new CompoundElement("div");
		container.setAttribute("class", "jumbotron");
		container.addElement(jumbotron);
=======
	public Template getCurrentTemplate()
	{
		return tmp;
	}
	
	private static CompoundElement createLink(String link, String name)
	{
		CompoundElement a = new CompoundElement("a");
		a.setAttribute("href", link);
		a.setData(name);
		
		return a;
	}
	
	private void setBody()
	{

	}
	
	private static CompoundElement loginForm()
	{
		CompoundElement container = new CompoundElement("div");

		Form login = new Form();
		container.addElement(login);
		login.setAttribute("class", "login-form");
		login.setAttribute("action", "HomePage");
		
		CompoundElement wrap = new CompoundElement("div");
		container.addElement(wrap);
		wrap.setAttribute("class", "row");
		
		CompoundElement input = new CompoundElement("div");
		wrap.addElement(input);
		input.setAttribute("class", "container");
				
		CompoundElement inputUsername = new CompoundElement("div");
		input.addElement(inputUsername);
		inputUsername.setAttribute("class", "input-group mb-3");
		
			Element username = new Element("input");
			inputUsername.addElement(username);
			username.setAttribute("type", "text");
			username.setAttribute("class", "form-control");
			username.setAttribute("placeholder", "Username");
		
		CompoundElement divide = new CompoundElement("div");
		input.addElement(divide);
		divide.setAttribute("class", "w-100 d-none d-md-block");	
			
		CompoundElement inputPassword = new CompoundElement("div");
		input.addElement(inputPassword);
		inputPassword.setAttribute("class", "input-group mb-3");
		
			Element password = new Element("input");
			inputPassword.addElement(password);
			password.setAttribute("type", "password");
			password.setAttribute("class", "form-control");
			password.setAttribute("placeholder", "Password");

			
		CompoundElement buttons = new CompoundElement("div");
		wrap.addElement(buttons);
		buttons.setAttribute("class", "container");

		CompoundElement log_in = new CompoundElement("button");
		buttons.addElement(log_in);
		log_in.setAttribute("class", "btn btn-primary btn-md btn-block");
		log_in.setAttribute("type", "submit");
		log_in.setData("Login");
	

		CompoundElement sign_up = new CompoundElement("button");
		buttons.addElement(sign_up);
		sign_up.setAttribute("class", "btn btn-primary btn-md btn-block");
		sign_up.setAttribute("type", "submit");
		sign_up.setData("Signup");
		
		return container;
	}
	
	private static CompoundElement topBar()
	{
		topBar = new CompoundElement("nav"); //
		topBar.setAttribute("class",  "navbar navbar-expand-lg navbar-light bg-light");
		CompoundElement brandLink = createLink("HomePage", "BlogHost"); //logo + link
		brandLink.setAttribute("class", "navbar-brand");
		topBar.addElement(brandLink);
		
		//button
		CompoundElement navButton = new CompoundElement("button");
		navButton.setAttribute("class", "navbar-toggler");
		navButton.setAttribute("data-toggle", "#navbarSupportedContent");
		navButton.setAttribute("aria-controls", "navbarSupportedContent");
		navButton.setAttribute("aria-expanded", "false");
		navButton.setAttribute("aria-label", "Toggle navigation");
		topBar.addElement(navButton);						//add button
		
		//span inside button
		CompoundElement togglerIcon = new CompoundElement("span");
		togglerIcon.setAttribute("class", "navbar-toggler-icon");
		navButton.addElement(togglerIcon);					//add icon
		
		//global navbar content
		CompoundElement supportedContent = new CompoundElement("div");
		supportedContent.setAttribute("collapse navbar-collapse", "navbarSupportedContent");
		supportedContent.setAttribute("id", "navbarSupportedContent");
		topBar.addElement(supportedContent);		
		
		CompoundElement list = new CompoundElement("ul");  //list of elements on navbar
		list.setAttribute("class", "navbar-nav mr-auto");
		topBar.addElement(list);							
		
		CompoundElement home = createListItem("nav-item active");		//home button
		home.addElement(createLinkClass("HomePage", "Home", "nav-link"));
		list.addElement(home);
		
		CompoundElement dropdown = createListItem("nav-item dropdown");
		list.addElement(dropdown);
		
		CompoundElement dropdownLink = createLinkClass("#", "User area", "nav-link dropdown-toggle"); //dropdown
		dropdownLink.setAttribute("id", "navbarDropdown");
		dropdownLink.setAttribute("role", "button");
		dropdownLink.setAttribute("data-toggle", "dropdown");
		dropdownLink.setAttribute("aria-haspopup", "true");
		dropdownLink.setAttribute("aria-expanded", "false");
		dropdown.addElement(dropdownLink);

		CompoundElement menu = new CompoundElement("div");
		menu.setAttribute("class", "dropdown-menu");
		menu.setAttribute("aria-labelledby", "navbarDropdown");
		dropdown.addElement(menu);

		
		/*modal*/
		CompoundElement loginModal = new CompoundElement("button");
		menu.addElement(loginModal);
		loginModal.setAttribute("type", "button");
		loginModal.setAttribute("class", "dropdown-item");
		loginModal.setAttribute("data-toggle", "modal");
		loginModal.setAttribute("data-target", "#myModal");
		loginModal.setData("Login");
		
		CompoundElement myLoginModal = new CompoundElement("div");
		topBar.addEndElement(myLoginModal);
		
		myLoginModal.setAttribute("id", "myModal");
		myLoginModal.setAttribute("class", "modal fade");
		myLoginModal.setAttribute("role", "dialog");
		
		CompoundElement modalDialog = new CompoundElement("div");
		myLoginModal.addElement(modalDialog);
		modalDialog.setAttribute("class", "modal-dialog");
		
		CompoundElement modalContent = new CompoundElement("div");
		modalDialog.addElement(modalContent);
		modalContent.setAttribute("class", "modal-content");
		
		/*
		CompoundElement modalHeader = new CompoundElement("div");
		modalContent.addElement(modalHeader);
		modalHeader.setAttribute("class", "modal-header");
		modalHeader.setData("");*/
		
		CompoundElement modalExit = new CompoundElement("button");
		//modalHeader.addElement(modalExit);
		modalExit.setAttribute("type", "button");
		modalExit.setAttribute("class", "close");
		modalExit.setAttribute("data-dismiss", "modal");
		modalExit.setData("&times;");
		

		
		CompoundElement modalBody = new CompoundElement("div");
		modalContent.addElement(modalBody);
		modalBody.setAttribute("class", "modal-body");
		modalBody.addElement(loginForm());
		
		/* end modal*/
		
		menu.addElement(createLinkClass("Registration", "Register", "dropdown-item"));	
	
		return topBar;
	}
	
	
	private static CompoundElement createLinkClass(String link, String name, String classValue)
	{
		CompoundElement a = new CompoundElement("a");
		a.setAttribute("href", link);
		a.setAttribute("class", classValue);
		a.setData(name);
		
		return a;
	}
	
	
	private static CompoundElement createListItem(String classValue)
	{
		CompoundElement elem = new CompoundElement("li");
		elem.setAttribute("class", classValue);
		
		return elem;
	}
	
	private static CompoundElement botBar()
	{
		botBar = new CompoundElement("nav");
		botBar.setAttribute("class", "navbar fixed-bottom navbar-light bg-light");
		botBar.addElement(createLink("About", "About"));

		return botBar;
	}
	
	public CompoundElement getTopBar()
	{
		return topBar;
	}
	
	public CompoundElement getBotBar()
	{
		return botBar;
	}
	
	public static CompoundElement addElementWithSingleAttribute(String element, String attr, String attrData)
	{
		CompoundElement elem = new CompoundElement(element);
		elem.setAttribute(attr, attrData);
>>>>>>> Authorization modal window

		return container;
	}
	
	public static void createTmp()
	{
		tmp = new Template();
	}
	
	public Template getCurrentTemplate()
	{
<<<<<<< dev_collin_Database_Classes
		return tmp;
=======
		CompoundElement container = new CompoundElement("div");
		container.setAttribute("class", "container");
		
		CompoundElement jumbotron = new CompoundElement("div");
		container.setAttribute("class", "jumbotron");
		container.addElement(jumbotron);

		return container;
>>>>>>> Authorization modal window
	}
}