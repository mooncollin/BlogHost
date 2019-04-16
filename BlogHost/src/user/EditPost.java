package user;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import forms.Form;
import forms.Input;
import html.CompoundElement;
import html.Element;
import main.HomePage;
import main.Site;
import models.BlogHostPosts;
import models.Model;
import templates.BootstrapTemplates;
import templates.MainTemplate;
import util.Template;
import utils.StreamUtils;

@WebServlet("/EditPost")
@MultipartConfig
public class EditPost extends HttpServlet
{
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String URL = "/BlogHost/EditPost";
	
	public static Form makePostEditForm()
	{
		Form form = NewPost.makePostForm();
		CompoundElement checkGroup = BootstrapTemplates.formGroupCheckbox("Delete Picture", null, "deletePicture");
		Input check = (Input) checkGroup.getElementById("deletePicture"); 
		check.setName("deletePicture");
		
		form.getElementsByTag("small").stream()
									  .filter(e -> e.getData().equals("Optional Picture"))
									  .forEach(e -> e.setData("Keep this blank to keep original image, upload to replace"));
		
		
		Element button  = form.getElement(3);
		Element textGroup = form.getElement(2);
		form.removeElement(button);
		form.removeElement(textGroup);
		form.addElement(checkGroup);
		form.addElement(textGroup);
		form.addElement(button);
		
		return form;
	}

	/**
	 * Creates a new post form and fills in previous data and delivers that
	 * to the user.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Integer userSiteID = UserUtils.getUserSiteID(request);
		String userName = UserUtils.getUserName(request);
		Object postIDStr = request.getParameter("id");
		BigInteger postID;
		if(userSiteID == null || userName == null || postIDStr == null)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		try
		{
			postID = new BigInteger((String) postIDStr);
		}
		catch(NumberFormatException e)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		List<BlogHostPosts> post = Model.getAll(BlogHostPosts.class, "id=?", postID);
		if(post.isEmpty())
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		if(!post.get(0).getSiteID().equals((Integer) userSiteID))
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		Template template = new MainTemplate(userName, UserUtils.getUserSiteID(request)).getCurrentTemplate();
		CompoundElement container = new CompoundElement("div");
		container.addClasses("container", "mt-5");
		
		Form form = makePostEditForm();
		
		form.getInputByName("titleInput").setValue(post.get(0).getPostTitle());
		form.getElementById("postInput").setData(post.get(0).getPostText());
		form.getElementById("postInput").setAttribute("rows", String.valueOf(post.get(0).getPostText().split("\n").length));
		
		
		container.addElement(form);
		template.getBody().addElement(container);
		response.setContentType("text/html");
		response.getWriter().print(template);
	}
	
	/**
	 * Processes an edited post.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Integer userSiteID = UserUtils.getUserSiteID(request);
		if(userSiteID == null)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		BigInteger id;
		String idStr = request.getParameter("id");
		if(idStr == null)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		try
		{
			
			id = new BigInteger(idStr);
		}
		catch(NumberFormatException e)
		{
			response.sendRedirect(HomePage.URL);
			return;
		}
		
		Form form = makePostEditForm();
		if(form.validate(request.getParameterMap())
				&& request.getParameter("postInput") != null
				&& !request.getParameter("postInput").isEmpty())
		{
			String title = form.getInputByName("titleInput").getValue();
			String postText = request.getParameter("postInput");
			Timestamp time = Timestamp.from(Instant.now());
			Collection<Part> parts = request.getParts();
			List<BlogHostPosts> posts = Model.getAll(BlogHostPosts.class, "id=?", id);
			if(posts.isEmpty())
			{
				response.sendRedirect(HomePage.URL);
				return;
			}
			BlogHostPosts post = posts.get(0);
			byte[] picture = post.getPicture();
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
			if(form.getInputByName("deletePicture").getValue() != null)
			{
				picture = null;
			}
			post.setPicture(picture);
			post.setPostText(postText);
			post.setPostTitle(title);
			post.setDateModified(time);
			if(post.commit())
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
