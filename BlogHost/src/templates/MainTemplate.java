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
		createForm();
	}
	
	public Template basicTemplate()
	{
		tmp.getHead().addStylesheet("css/bootstrap.min.css");
		tmp.getHead().addStylesheet("css/style.css");
		tmp.getBody().addElement(topBar());
		//tmp.getBody().addElement(form);
		tmp.getBody().addElement(botBar());
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
		/*topBar = new CompoundElement("div");
		topBar.setAttribute("class", "topnav");
		topBar.addElement(createLink("HomePage", "Home"));
		topBar.addElement(createLink("LogIn", "Log In"));*/
		
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

		CompoundElement logIn = createListItem("nav-item");				//another link
		logIn.addElement(createLinkClass("#", "Link", "nav-link"));
		list.addElement(logIn);
		
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
		
		menu.addElement(createLinkClass("#", "Log In", "dropdown-item"));	//first link in dropdown
		menu.addElement(createLinkClass("#", "Register", "dropdown-item"));	//second link in dropdown
		
		CompoundElement divider = new CompoundElement("div");
		divider.setAttribute("class", "dropdown-divider");
		menu.addElement(divider);
		
		menu.addElement(createLinkClass("#", "Home", "dropdown-item"));		//third link after divider

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
	
	private static CompoundElement createSpan(String classValue, String data)
	{
		CompoundElement elem = new CompoundElement("span");
		elem.setAttribute("class", classValue);
		elem.setData(data);
		
		return elem;
	}
	
	private static CompoundElement createListItem(String classValue)
	{
		CompoundElement elem = new CompoundElement("li");
		elem.setAttribute("class", classValue);
		
		return elem;
	}
	
	private static CompoundElement botBar()
	{
		botBar = new CompoundElement("div");
		botBar.setAttribute("class", "botnav");
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
	
	public CompoundElement addElementWithSingleAttribute(String element, String attr, String attrData)
	{
		CompoundElement elem = new CompoundElement(element);
		elem.setAttribute(attr, attrData);

		return elem;
	}
	
	private static void createTmp()
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

}