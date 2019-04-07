package templates;

import html.CompoundElement;

public class BotBar 
{	
	private static CompoundElement botBar;

	public BotBar()
	{
		botBar = new CompoundElement("nav");
		botBar();
	}
	
	private static void botBar()
	{
		botBar.setAttribute("class", "navbar fixed-bottom navbar-light bg-light");
		CompoundElement aboutLink = new CompoundElement("a");
		botBar.addElement(aboutLink);
		aboutLink.setAttribute("href", "About");
		aboutLink.setData("About");
	}
	
	public CompoundElement getBotBar()
	{
		return botBar;
	}
}
