package templates;

import javax.servlet.http.HttpServletRequest;
import forms.File;
import forms.Form;
import html.CompoundElement;
import html.Element;
import user.UserUtils;

public class TopBar 
{
	private static CompoundElement topBar;
	private static String username;
	private static Integer siteID;
	private static Integer userID;
	
	public TopBar()
	{
		this(null, -1, -1);
	}
	
	public TopBar(String uname, Integer siteId, Integer userId)
	{
		username = uname;
		siteID = siteId;
		userID = userId;
		topBar();
	}
	
	public TopBar(HttpServletRequest request)
	{
		this(UserUtils.getUserName(request),
			 UserUtils.getUserSiteID(request) == null ? -1 : UserUtils.getUserSiteID(request),
			 UserUtils.getUserID(request) == null ? -1 : UserUtils.getUserID(request));
	}
	
	public CompoundElement getTopBar()
	{
		return topBar;
	}
	
	private static void topBar()
	{
		topBar = new CompoundElement("nav");
		topBar.setAttribute("id", "topBar");
		topBar.setAttribute("class",  "navbar navbar-expand-lg navbar-light bg-light");
		CompoundElement brandLink = createLink("HomePage", "BlogHost"); //logo + link
		brandLink.setAttribute("class", "navbar-brand");
		topBar.addElement(brandLink);
		
		//button
		CompoundElement navButton = new CompoundElement("button");
		navButton.setAttribute("class", "navbar-toggler");
		navButton.setAttribute("type", "button");
		navButton.setAttribute("data-toggle", "collapse");
		navButton.setAttribute("data-target", "#navbarSupportedContent");
		navButton.setAttribute("aria-controls", "navbarSupportedContent");
		navButton.setAttribute("aria-expanded", "false");
		navButton.setAttribute("aria-label", "Toggle navigation");
		navButton.setAttribute("id", "navButton");
		topBar.addElement(navButton);						//add button
		
		//span inside button
		CompoundElement togglerIcon = new CompoundElement("span");
		togglerIcon.setAttribute("class", "navbar-toggler-icon");
		navButton.addElement(togglerIcon);					//add icon
		
		//global navbar content
		CompoundElement supportedContent = new CompoundElement("div");
		supportedContent.addClasses("collapse", "navbar-collapse");
		supportedContent.setAttribute("id", "navbarSupportedContent");
		topBar.addElement(supportedContent);		
		
		CompoundElement list = new CompoundElement("ul");  //list of elements on navbar
		list.setAttribute("class", "navbar-nav mr-auto");						
		
//		CompoundElement home = createListItem("nav-item active");		//home button
//		home.addElement(createLink("HomePage", "Home", "nav-link"));
//		list.addElement(home);
		
		CompoundElement dropdown = createListItem("nav-item dropdown");
		list.addElement(dropdown);
		
		CompoundElement dropdownLink;
		
		if (username == null)
		{
			dropdownLink = createLink("#", "Login/Register", "nav-link dropdown-toggle"); //dropdown for non user
		}
		
		else
		{
			dropdownLink = createLink("#", username, "nav-link dropdown-toggle"); //dropdown for authed user
		}
		
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
		
		if (username == null)
		{
			menu.addElement(logModal()); //login
			menu.addElement(regModal()); //register
		}
		
		else
		{
			if(siteID != -1 || (Integer) siteID != null) {
				CompoundElement site = new CompoundElement("a");
				site.setAttribute("class", "dropdown-item btn");
				site.setAttribute("href", "Site?site="+siteID);
				site.setData("My Site");
				menu.addElement(site);
				CompoundElement store = new CompoundElement("a");
				store.setAttribute("class", "dropdown-item btn");
				store.setAttribute("href", "Store?id="+ userID);
				store.setData("My Store");
				menu.addElement(store);
			}
			menu.addElement(logoutForm());
		}
		
		supportedContent.addElement(list);
	}
	
	private static CompoundElement modalButton(String id, String data)
	{
		CompoundElement button = new CompoundElement("button");
		button.setAttribute("type", "button");
		button.setAttribute("class", "dropdown-item");
		button.setAttribute("data-toggle", "modal");
		button.setAttribute("data-target", "#" + id);
		button.setData(data);
		
		return button;
	}
	
	private static CompoundElement logModal()
	{
		return createModal("loginModal", "Login", loginForm());	
	}
	
	private static CompoundElement logoutForm()
	{
		

		Form form = new Form();
		form.setAttribute("id", "logoutForm");
		
		
		//container to hold inputs
		
		form.setAction("LogOut");
		form.setMethod("POST");
		//list of inputs
		Element a = new Element("a", "Logout");
		a.setAttribute("class", "dropdown-item btn");
		a.setAttribute("onclick", "document.getElementById('logoutForm').submit()");
		form.addElement(a);
	
		//container for buttons
		//CompoundElement buttons = new CompoundElement("div");
		//wrap.addElement(buttons);
		//buttons.setAttribute("class", "container");
		//buttons.addElement(addSubmiButton("Login"));
		
		return form;
	}
	
	private static CompoundElement loginForm()
	{
		CompoundElement container = new CompoundElement("div");

		//container to hold everything
		CompoundElement wrap = new CompoundElement("div");
		container.addElement(wrap);
		wrap.setAttribute("class", "row");
		
		//container to hold inputs
		Form form = new Form();
		wrap.addElement(form);
		form.setAction("LogIn");
		form.setMethod("POST");
		
		//CompoundElement input = new CompoundElement("div");
		//wrap.addElement(input);
		form.setAttribute("class", "container");
		
		//list of inputs
		form.addElement(inputContainer("text","Username"));
		form.addElement(inputContainer("password","Password"));
		form.addElement(addSubmiButton("LogIn"));
	
		//container for buttons
		//CompoundElement buttons = new CompoundElement("div");
		//wrap.addElement(buttons);
		//buttons.setAttribute("class", "container");
		//buttons.addElement(addSubmiButton("Login"));

		return container;
	}

	private static CompoundElement regModal()
	{
		return createModal("registrationModal", "Register", registrationForm());
	}
	
	private static CompoundElement createModal(String modalId, String modalData, CompoundElement modalForm)
	{
		CompoundElement modal = modalButton(modalId, modalData);
		
		CompoundElement myModal = new CompoundElement("div");
		topBar.addEndElement(myModal);
		myModal.setAttribute("id", modalId);
		myModal.setAttribute("class", "modal fade");
		myModal.setAttribute("role", "dialog");
		
		CompoundElement modalDialog = new CompoundElement("div");
		myModal.addElement(modalDialog);
		modalDialog.setAttribute("class", "modal-dialog");
		CompoundElement modalContent = new CompoundElement("div");
		modalDialog.addElement(modalContent);
		modalContent.setAttribute("class", "modal-content");
	
		CompoundElement modalBody = new CompoundElement("div");
		modalContent.addElement(modalBody);
		modalBody.setAttribute("class", "modal-body");
		modalBody.addElement(modalForm);
		
		return modal;
	}
	
	private static CompoundElement registrationForm()
	{
		CompoundElement container = new CompoundElement("div");

		//container to hold everything
		CompoundElement wrap = new CompoundElement("div");
		container.addElement(wrap);
		wrap.setAttribute("class", "row");
		
		//container to hold inputs
		/*
		CompoundElement input = new CompoundElement("div");
		wrap.addElement(input);
		input.setAttribute("class", "container");*/
		
		Form form = new Form();
		wrap.addElement(form);
		form.setAction("Registration"); 
		form.setMethod("POST");
		form.setAttribute("class", "container");
		//form.setEnctype("multipart/form-data");
		//list of inputs
		form.addElement(inputContainer("text", "Enter Username", "Uname"));
		form.addElement(inputContainer("text", "Enter Sitename", "Site"));
		form.addElement(inputContainer("text", "Enter First Name", "Fname"));
		form.addElement(inputContainer("text", "Enter Last Name", "Lname"));
		form.addElement(inputContainer("text", "Enter Age", "Age"));
		form.addElement(inputContainer("text", "Enter Email", "Email"));
		form.addElement(inputContainer("password", "Enter password", "Password"));
		form.addElement(inputContainer("password", "Repeat Password"));
		form.addElement(BootstrapTemplates.formGroup("Profile Picture", File.class, "Optional Picture", "profilePicture"));
		form.addElement(addSubmiButton("Signup"));
		
		/*
		//container for buttons
		CompoundElement buttons = new CompoundElement("div");
		wrap.addElement(buttons);
		buttons.setAttribute("class", "container");
		buttons.addElement(addSubmiButton("Signup"));*/

		return container;
	}
	
	private static CompoundElement inputContainer(String inputType, String placeholder)
	{
		CompoundElement inputContainer = new CompoundElement("div");
		inputContainer.setAttribute("class", "input-group mb-3");
		inputContainer.addElement(createInput(inputType, placeholder));
		
		return inputContainer;
	}
	
	private static CompoundElement inputContainer(String inputType, String placeholder, String name)
	{
		CompoundElement inputContainer = new CompoundElement("div");
		inputContainer.setAttribute("class", "input-group mb-3");
		inputContainer.addElement(createInput(inputType, placeholder, name));
		
		return inputContainer;
	}
	
	private static CompoundElement createListItem(String classValue)
	{
		CompoundElement elem = new CompoundElement("li");
		elem.setAttribute("class", classValue);
		
		return elem;
	}
	
	private static CompoundElement createLink(String link, String name)
	{
		CompoundElement a = new CompoundElement("a");
		a.setAttribute("href", link);
		a.setData(name);
		
		return a;
	}
	
	private static CompoundElement createLink(String link, String name, String classValue)
	{
		CompoundElement a = new CompoundElement("a");
		a.setAttribute("href", link);
		a.setAttribute("class", classValue);
		a.setData(name);
		
		return a;
	}
	
	private static CompoundElement addSubmiButton(String setData)
	{
		CompoundElement button = new CompoundElement("button");
		button.setAttribute("class", "btn btn-primary btn-md btn-block");
		button.setAttribute("type", "submit");
		button.setData(setData);
		
		return button;
	}
	
	private static Element createInput(String type, String placeholder)
	{
		Element element = new Element("input");
		element.setAttribute("type", type);
		element.setAttribute("class", "form-control");
		element.setAttribute("placeholder", placeholder);
		element.setAttribute("name", placeholder);
		
		return element;
	}
	
	
	private static Element createInput(String type, String placeholder, String name)
	{
		Element element = new Element("input");
		element.setAttribute("type", type);
		element.setAttribute("class", "form-control");
		element.setAttribute("placeholder", placeholder);
		element.setAttribute("name", name);
		
		return element;
	}
}
