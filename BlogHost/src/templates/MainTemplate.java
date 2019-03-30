package templates;

import html.*;
import forms.*;
import util.*;

public class MainTemplate 
{
	private static Form form;
	private static Template tmp;
	
	public MainTemplate()
	{
		createTmp();
		createForm();
	}
	
	public Template basicTemplate()
	{
		tmp.getHead().addStylesheet("style.css");
		tmp.getBody().addElement(topBar());
		tmp.getBody().addElement(form);
		tmp.getBody().addElement(botBar());
		
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
		CompoundElement topBar = new CompoundElement("div");
		topBar.setAttribute("class", "topnav");
		topBar.addElement(createLink("HomePage", "Home"));
		topBar.addElement(createLink("LogIn", "Log In"));
		return topBar;
	}
	
	private static CompoundElement botBar()
	{
		CompoundElement botBar = new CompoundElement("div");
		botBar.setAttribute("class", "botnav");
		botBar.addElement(createLink("About", "About"));
		
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