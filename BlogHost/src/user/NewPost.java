package user;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import forms.File;
import forms.Form;
import forms.Input;
import forms.TextField;
import html.CompoundElement;
import main.HomePage;
import main.Site;
import models.BlogHostPosts;
import templates.BootstrapTemplates;
import templates.MainTemplate;
import util.Template;
import utils.StreamUtils;

/**
 * This servlet handles delivering the form for a new post
 * and saving a submitted post.
 * @author collin
 *
 */
@WebServlet("/NewPost")
@MultipartConfig
public class NewPost extends HttpServlet
{
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String URL = "/BlogHost/NewPost";
	
	/**
	 * Makes a form for submitting a post.
	 * @return a form
	 */
	public static Form makePostForm()
	{
		Form form = new Form();
		form.setMethod("POST");
		form.setEnctype("multipart/form-data");
		
		CompoundElement titleGroup = BootstrapTemplates.formGroup("Title", TextField.class, null, "titleInput");
		CompoundElement textGroup = BootstrapTemplates.formGroupTextarea("Post", null, "postInput");
		CompoundElement pictureGroup = BootstrapTemplates.formGroup("Picture", File.class, "Optional Picture", "pictureInput");
		CompoundElement submitButton = new CompoundElement("button", "Submit");
		
		Input titleInput = (Input) titleGroup.getElementById("titleInput");
		titleInput.setRequired(true);
		titleInput.setName(titleInput.getAttribute("id"));
		Input pictureInput = (Input) pictureGroup.getElementById("pictureInput");
		pictureInput.setName(pictureInput.getAttribute("id"));
		textGroup.getElementById("postInput").setAttribute("required", "");
		textGroup.getElementById("postInput").setAttribute("name", "postInput");
		
		pictureInput.removeAttribute("class");
		pictureInput.addClass("form-control-file");
		submitButton.addClasses("btn", "btn-primary");
		
		form.addElement(titleGroup);
		form.addElement(pictureGroup);
		form.addElement(textGroup);
		form.addElement(submitButton);
		
		return form;
	}
	
	/**
	 * Creates a new post form and delivers that to the user.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Integer userSiteID = UserUtils.getUserSiteID(request);
		if(userSiteID == null)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		Template template = new MainTemplate(UserUtils.getUserName(request)).getCurrentTemplate();
		CompoundElement container = new CompoundElement("div");
		container.addClasses("container", "mt-5");
		container.addElement(NewPost.makePostForm());
		
		
		template.getBody().addElement(container);
		response.setContentType("text/html");
		response.getWriter().print(template);
	}
	
	/**
	 * Processes a new post.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Integer userSiteID = UserUtils.getUserSiteID(request);
		if(userSiteID == null)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		Form form = NewPost.makePostForm();
		if(form.validate(request.getParameterMap())
				&& request.getParameter("postInput") != null
				&& !request.getParameter("postInput").isEmpty())
		{
			String title = form.getInputByName("titleInput").getValue();
			String post = request.getParameter("postInput");
			Timestamp time = Timestamp.from(Instant.now());
			Collection<Part> parts = request.getParts();
			byte[] picture = null;
			for(Part p : parts)
			{
				if(p != null && 
					p.getContentType() != null &&
					p.getContentType().contains("image"))
				{
					picture = StreamUtils.getStreamBytes(p.getInputStream());
					break;
				}
			}
			BlogHostPosts newPost = new BlogHostPosts(userSiteID, title, post, picture, time, time);
			if(newPost.commit())
			{
				response.sendRedirect(Site.URL + "?site=" + userSiteID);
				return;
			}
			else
			{
				System.err.println("Error in committing Post");
			}
		}
		
		doGet(request, response);
	}
}
