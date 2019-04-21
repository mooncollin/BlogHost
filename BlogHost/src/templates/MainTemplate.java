package templates;

import html.*;
import main.HomePage;
import main.Site;
import main.Store;
import models.BlogHostCreators;
import models.BlogHostSites;
import models.BlogHostSubscriptions;
import models.Model;
import user.UserUtils;

import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import util.*;

public class MainTemplate 
{
	private static Template tmp;
	private static TopBar topBar;
	private static BotBar botBar;
	
	private static String userName;
	private static Integer siteID;
	private static Integer userID;

	public MainTemplate()
	{
		userName = null;
		siteID = null;
		userID = null;
		createTmp();
		topBar = new TopBar();
		botBar = new BotBar();
		basicTemplate();
	}
	
	public MainTemplate(HttpServletRequest request)
	{
		userName = UserUtils.getUserName(request);
		siteID = UserUtils.getUserSiteID(request);
		userID = UserUtils.getUserID(request);
		createTmp();
		topBar = new TopBar(request);
		botBar = new BotBar();
		basicTemplate();
	}
	
	public MainTemplate(String user, Integer siteId, Integer userId)
	{
		userName = user;
		siteID = siteId;
		userID = userId;
		createTmp();
		topBar = new TopBar(user, siteId, userId);
		botBar = new BotBar();
		basicTemplate();
	}
	
	public static Template basicTemplate()
	{
		tmp.getHead().addStylesheet("css/bootstrap.min.css");
		tmp.getHead().addStylesheet("css/style.css");
		tmp.getBody().addElement(topBar.getTopBar());
		tmp.getBody().addElement(getSideBar());
		tmp.getBody().addEndElement(botBar.getBotBar());
		tmp.getBody().addScript("js/jquery-3.3.1.min.js");
		tmp.getBody().addScript("js/popper.min.js");
		tmp.getBody().addScript("js/bootstrap.min.js");
		tmp.getBody().addScript("js/like.js");
		tmp.getBody().addScript("js/comment.js");
		tmp.getBody().addScript("js/statPost.js");
		tmp.getBody().addScript("js/sidebar.js");
		
		return tmp;
	}
	
	private static CompoundElement getSideBar()
	{
		CompoundElement mainNav = new CompoundElement("nav");
		mainNav.setAttribute("id", "sideBar");
		mainNav.addClasses("float-left","col-xl-1", "col-lg-12", "sticky-top", "border", "shadow");
		mainNav.setAttribute("style", "background-color: #e9ecef;");
		CompoundElement sideBar = new CompoundElement("div");
		CompoundElement ul = new CompoundElement("ul");
		ul.addClasses("nav", "flex-row", "d-flex");
		
		CompoundElement homeListItem = createSideListItem(HomePage.URL,
				"/BlogHost/Images/house.png", "Home");
		homeListItem.addClass("d-flex");
		
		ul.addElement(homeListItem);
		
		CompoundElement subscribersUl = new CompoundElement("div");
		subscribersUl.addClasses("d-flex", "flex-row", "mt-3");
		
		CompoundElement subscriberLinksUl = new CompoundElement("div");
		subscriberLinksUl.addClasses("d-flex", "flex-row", "flex-wrap");
		
		if(siteID != null && siteID != -1)
		{
			CompoundElement siteListItem = createSideListItem(Site.URL + "?site=" + siteID,
					"/BlogHost/Images/site.png", "My Site");
			siteListItem.addClass("d-flex");
			ul.addElement(siteListItem);
		}
		
		if(userID != null && userID != -1)
		{
			CompoundElement storeListItem = createSideListItem(Store.URL + "?id=" + userID, 
					"/BlogHost/Images/store.png", "My Store");
			storeListItem.addClass("d-flex");
			ul.addElement(storeListItem);
			
			CompoundElement subscriberListItem = new CompoundElement("li");
			subscriberListItem.addClasses("nav-item", "d-flex");
			CompoundElement subscriberText = new CompoundElement("span", "Subscriptions");
			subscriberText.addClasses("ml-2", "mb-3");
			subscriberListItem.addElement(subscriberText);
			
			List<BlogHostSubscriptions> subs = Model.getAll(BlogHostSubscriptions.class, 
					"subscriber_id=?", userID);
			
			subscribersUl.addElement(subscriberListItem);
			
			for(BlogHostSubscriptions sub : subs)
			{
				List<BlogHostCreators> creator = Model.getAll(BlogHostCreators.class, 
						"id=?", sub.getCreatorID());
				List<BlogHostSites> sites = Model.getAll(BlogHostSites.class, "creator_id=?", sub.getCreatorID());
				if(!creator.isEmpty())
				{
					CompoundElement subscriberItem = new CompoundElement("li");
					subscriberItem.addClasses("nav-item", "text-center", "d-flex");
					
					Element subscriberImage = new Element("img");
					subscriberImage.addClass("rounded-circle");
					if(creator.get(0).getProfilePicture() == null)
					{
						subscriberImage.setAttribute("src", "/BlogHost/Images/blank-profile-picture.png");
					}
					else
					{
						subscriberImage.setAttribute("src", "data:image/jpg;base64," + Base64.getEncoder().encodeToString(creator.get(0).getProfilePicture()));
					}
					
					subscriberImage.setAttribute("width", "64");
					subscriberImage.setAttribute("height", "64");
					
					CompoundElement subscriberLink = new CompoundElement("a");
					subscriberLink.addClass("nav-link");
					subscriberLink.setAttribute("href", Site.URL + "?site=" + sites.get(0).getID());
					subscriberLink.addElement(subscriberImage);
					subscriberLink.addElement(new Element("br"));
					CompoundElement subscriberName = new CompoundElement("span", creator.get(0).getUserName());
					subscriberLink.addElement(subscriberName);
					
					subscriberItem.addElement(subscriberLink);
					
					subscriberLinksUl.addElement(subscriberItem);
				}
			}
		}
		
		sideBar.addElement(ul);
		sideBar.addElement(subscribersUl);
		sideBar.addElement(subscriberLinksUl);
		mainNav.addElement(sideBar);
		
		return mainNav;
	}
	
	private static CompoundElement createSideListItem(String siteURL, String imageURL, String text)
	{
		CompoundElement listItem = new CompoundElement("li");
		listItem.addClass("nav-item");
		CompoundElement listLink = new CompoundElement("a");
		listLink.addClass("nav-link");
		listLink.setAttribute("href", siteURL);
		Element image = new Element("img");
		image.setAttribute("src", imageURL);
		CompoundElement displayText = new CompoundElement("span", text);
		displayText.addClasses("ml-1", "align-middle");
		
		listLink.addElement(image);
		listLink.addElement(displayText);
		listItem.addElement(listLink);
		
		return listItem;
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