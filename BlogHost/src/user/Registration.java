package user;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import templates.MainTemplate;
import forms.*;
import html.*;
import util.*;


@WebServlet("/Registration")
public class Registration extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
 
    public Registration() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		MainTemplate temp = new MainTemplate();
		Form form = new Form();
		form.setAttribute("method", "POST");
		
		Element br = new Element("br");
		CompoundElement username = createLabel("username", "Username:");
		CompoundElement first_name = createLabel("first_name", "First Name:");
		CompoundElement last_name = createLabel("last_name", "Last Name:");
		CompoundElement email = createLabel("email", "EMail:");
		CompoundElement password = createLabel("password", "Password:");
		CompoundElement re_password = createLabel("re_password", "Repeat password:");
		
		TextField usName = createInput("Username", "usName");
		TextField fiName = createInput("First Name", "fiName");
		TextField laName = createInput("Last Name", "laName");
		TextField eml = createInput("EMail", "eml");
		TextField pass1 = createInput("Password", "pass1");
		TextField pass2 = createInput("Repeat Password", "pass2");
		
		
		form.addElement(username);
		form.addElement(usName);
		form.addElement(br);
		
		form.addElement(first_name);
		form.addElement(fiName);
		form.addElement(br);
		
		form.addElement(last_name);
		form.addElement(laName);
		form.addElement(br);
		
		form.addElement(email);
		form.addElement(eml);
		form.addElement(br);
		
		form.addElement(password);
		form.addElement(pass1);
		form.addElement(br);

		form.addElement(re_password);
		form.addElement(pass2);
		form.addElement(br);
		
		Submit button = new Submit();
		button.setAttribute("style", "float:left");
		form.addElement(button);
		
		temp.getForm().setAttribute("style", "width: 15%; height: 30%;");
		temp.getForm().addElement(form);
		
		response.setContentType("text/html");
		response.getWriter().println(temp.basicTemplate());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
	
	private static CompoundElement createLabel(String attr, String data)
	{
		CompoundElement label = new CompoundElement("label");
		label.setAttribute("for", attr);
		label.setData("<b>" + data + "</b>");
		return label;
	}
	
	private static TextField createInput(String placeholder, String name)
	{
		TextField input = new TextField();
		input.setPlaceholder(placeholder);
		input.setName(name);
		input.setRequired(true);
		
		return input;
	}
	
}
