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
		topBar = new TopBar(user);
		botBar = new BotBar();
		basicTemplate();
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
	
	public CompoundElement createContainer()
	{
		CompoundElement container = new CompoundElement("div");
		container.setAttribute("class", "container");
		
		CompoundElement jumbotron = new CompoundElement("div");
		container.setAttribute("class", "jumbotron");
		container.addElement(jumbotron);

		return container;
	}
	
	public static void createTmp()
	{
		tmp = new Template();
	}
	
	public Template getCurrentTemplate()
	{
		return tmp;
	}
}