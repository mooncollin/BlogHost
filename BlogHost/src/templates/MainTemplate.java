package templates;

import html.*;
import forms.*;
import util.*;

public class MainTemplate 
{
	private static Form form;
	private static Template tmp;
	private static CompoundElement botBar;
	private static CompoundElement topBar;
	
	public MainTemplate()
	{
		createTmp();
		basicTemplate();
	}
	
	public static Template basicTemplate()
	{
		tmp.getHead().addStylesheet("css/bootstrap.min.css");
		tmp.getHead().addStylesheet("css/style.css");
		tmp.getBody().addElement(topBar());
		tmp.getBody().addEndElement(botBar());
		tmp.getBody().addScript("js/jquery-3.3.1.min.js");
		tmp.getBody().addScript("js/popper.min.js");
		tmp.getBody().addScript("js/bootstrap.min.js");
		
		return tmp;
	}
	
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
		
		CompoundElement dropdownLink = createLinkClass("#", "Dropdown", "nav-link dropdown-toggle"); //dropdown
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
		
		menu.addElement(createLinkClass("LogIn", "Log In", "dropdown-item"));			//first link in dropdown
		menu.addElement(createLinkClass("Registration", "Register", "dropdown-item"));	//second link in dropdown
		//menu.addElement(createLinkClass("Site", "Site", "dropdown-item"));				//site
		
		/*
		CompoundElement divider = new CompoundElement("div");
		divider.setAttribute("class", "dropdown-divider");
		menu.addElement(divider);
		
		menu.addElement(createLinkClass("#", "Home", "dropdown-item"));*/	//third link after divider

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
		/*botBar = new CompoundElement("div");
		botBar.setAttribute("class", "botnav");
		botBar.addElement(createLink("About", "About"));*/
		
		
		botBar = new CompoundElement("nav");
		botBar.setAttribute("class", "navbar fixed-bottom navbar-light bg-light");
		//botBar.addElement(createLink("BlogHost", "HomePage"));
		botBar.addElement(createLink("About", "About"));
		 // <a class="navbar-brand" href="#">Fixed bottom</a>

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

		return elem;
	}
	
	public static void createTmp()
	{
		tmp = new Template();
	}
	
	public static void createForm()
	{
		form = new Form();
		form.setAttribute("class", "main");
	}
	
	public Form getForm()
	{
		return form;
	}

	
	public CompoundElement createContainer()
	{
		CompoundElement container = new CompoundElement("div");
		container.setAttribute("class", "container");
		CompoundElement jumbotron = new CompoundElement("div");
		container.setAttribute("class", "jumbotron");
		container.addElement(jumbotron);
		
		return container;
	}
}