package user;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import forms.Form;
import forms.Password;
import forms.Submit;
import forms.TextField;
import html.CompoundElement;
import html.Element;
import templates.MainTemplate;

@WebServlet("/LogIn")
public class LogIn extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public LogIn() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		MainTemplate temp = new MainTemplate();
		Form form = new Form();
		form.setAttribute("method", "POST");
		Element br = new Element("br");
		
		CompoundElement body = new CompoundElement("div");
		Form log = new Form();
		log.setAttribute("id", "inputUsername");
		CompoundElement uname = createLabel("uname", "Username");
		uname.setAttribute("style", "float:left;");
		TextField username = createInput("Enter Username", "uname");
		log.addElement(uname);
		log.addElement(username);
		
		Form inputPassword = new Form();
        inputPassword.setAttribute("id", "inputPassword");
        CompoundElement psw = createLabel("psw", "Password");
        Password password = new Password();
        password.setPlaceholder("Enter password");
        password.setName("pswrd");
        password.setRequired(true);
        
        inputPassword.addElement(psw);
        inputPassword.addElement(password);
        
    	Form buttons = new Form();
		buttons.setAttribute("class", "buttons");
		Submit login = new Submit();
		login.setAttribute("style", "float:left");
		login.setAttribute("onclick", "Authorize");
		Submit register = new Submit();
		register.setAttribute("style", "float:right");
		register.setAttribute("onclick", "Register");
		buttons.addElement(login);
		buttons.addElement(register);
		

		temp.getForm().setAttribute("style", "width: 15%; height: 30%;");
		
		body.addElement(log);
		body.addElement(br);
		body.addElement(inputPassword);
		body.addElement(br);
		body.addElement(buttons);
		form.addElement(body);
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
