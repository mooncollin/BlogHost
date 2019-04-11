package main;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.mysql.cj.util.StringUtils;

//import com.mysql.cj.util.StringUtils;

import dbConnection.DBConnection;
import forms.Form;
import forms.Input;
import html.CompoundElement;
import html.Element;
import models.BlogHostPosts;
import templates.MainTemplate;
import user.NewPost;
import util.Template;
import utils.StreamUtils;

@WebServlet("/Site")
public class Site extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
    public Site() 
    {
        super();
    }

    protected String userName;
    protected String firstName;
    protected String lastName;
    protected String siteName;
    protected String profilePic;
    protected int siteId;
    protected List<BlogHostPosts> postList = new ArrayList<BlogHostPosts>();
    //protected Connection connection;
    
    
    protected void getSiteInfo(int id) {
    	postList.clear();
    	Connection connection = DBConnection.getDBConnection();
        String selectSQL = "Select * from BlogHostSites as s join BlogHostCreators as c on s.id = c.id where s.id = ?";
        PreparedStatement preparedStatement = null;
        try {
        	connection = DBConnection.getDBConnection();
	        preparedStatement = connection.prepareStatement(selectSQL);
	        preparedStatement.setInt(1, id);
	        ResultSet rs = preparedStatement.executeQuery();
	        if(rs.first()) {
	        	siteId = rs.getInt("id");
	        	siteName = rs.getString(4);
	        	userName = rs.getString(7);
	        	firstName = rs.getString(8);
	        	lastName = rs.getString(9);
	        	if(rs.getBlob(13)!=null)
	        	profilePic = Base64.getEncoder().encodeToString(rs.getBlob(13).getBytes(1,(int)rs.getBlob(13).length()));
	        }
	        else {
	        	siteName = "NoRow";
	        }
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            try {
               if (preparedStatement != null)
                  preparedStatement.close();
            } catch (Exception e2) {
            }
            try {
                if (connection != null)
                	connection.close();
            } catch (Exception se) {
                se.printStackTrace();
             }
         }
    }
    protected void getSitePosts(int id) {
    	postList.clear();
	    postList = BlogHostPosts.getAll(BlogHostPosts.class, "site_id = ? order by id desc", id);
	        
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		getSiteInfo(Integer.parseInt(request.getParameter("site")));
		getSitePosts(Integer.parseInt(request.getParameter("site")));
		boolean siteOwner = false;
		boolean loggedIn = false;
		//request.getSession().setAttribute("userSiteID",1);
		if(request.getSession().getAttribute("userSiteId") != null &&  (int)request.getSession().getAttribute("userSiteId") == siteId) {
			siteOwner = loggedIn = true;
		}
		else if(request.getSession().getAttribute("userSiteId") != null){
			loggedIn = true;
		}
		
		String page = request.getParameter("site");
		if(page == null || page == "" || !StringUtils.isStrictlyNumeric(page)) {
			response.sendRedirect("/Error/");
			return;
		}
		
		if(siteName == "NoRow") {
			response.sendRedirect("/Error/"); 
			return;
		}
		//MainTemplate.createTmp();
		//Template temp = MainTemplate.basicTemplate();
		MainTemplate tempMain;
		if (loggedIn) {
			tempMain = new MainTemplate((String) request.getSession().getAttribute("userName"));
		}
		else {
			tempMain = new MainTemplate();
		}
		Template temp = tempMain.getCurrentTemplate();
		CompoundElement cont = new CompoundElement("div");
		cont.addClass("container");
		cont.addClass("col-11");
		CompoundElement jumbo = new CompoundElement("div");
		jumbo.addClass("jumbotron");
		CompoundElement header = new CompoundElement("div");
		Element profilePicture = new Element("img");
		if(profilePic != null) {
			profilePicture.setAttribute("src", "data:image/jpg;base64,"
			+ profilePic);
			profilePicture.setAttribute("style", "border:3px solid black;");
		}
		Element pSite = new Element("p");
		pSite.setData("Welcome to: "+ siteName);
		
		Element pCreator = new Element("p");
		pCreator.setData("Creator: "+ userName);
		
		Element pPosts = new Element("p");
		pPosts.setData(userName + "'s posts: " + postList.size());
		header.addClasses("w-75", "p-3");
		header.addElement(pSite);
		header.addElement(pCreator);
		header.addElement(pPosts);
		if(siteOwner) {
		CompoundElement newPost = new CompoundElement("a", "Add New Post");
			newPost.addClasses("btn", "btn-primary", "button");
			newPost.setAttribute("href", "/BlogHost/NewPost");
			header.addElement(newPost);
		}
		CompoundElement area  = new CompoundElement("div");
		area.addClass("row");
		CompoundElement main  = new CompoundElement("div");
		main.addClass("col-10");
		CompoundElement list  = new CompoundElement("ul");
		list.addClasses("list-group", "p-3");
		for (BlogHostPosts p : postList) {
			CompoundElement item  = new CompoundElement("li");
			item.addClass("list-group-item");
			
			
			Element title = new Element("h2");
			title.setData(p.getPostTitle());
			//title.addClasses("w-75", "p-3");
			Element text = new Element("h5");
			text.addClass("p-3 col-12");
			text.setAttribute("id", "postText");
			text.setData(p.getPostText().replaceAll("\n", "<br />"));
			Element date = new Element("p");
			date.setData("Date Posted: " + p.getDatePosted());
			Element img = new Element("img");
			if(p.getPicture() != null) {
				img.setAttribute("src", "data:image/jpg;base64,"
				+Base64.getEncoder().encodeToString(p.getPicture()));
				img.setAttribute("style","border:3px solid black;max-width:100%;");
			}
			Form form = new Form();
			if(siteOwner) {
				form.setMethod("POST");
				CompoundElement submitButton = new CompoundElement("button", "Delete Post");
				submitButton.addClasses("btn", "btn-danger");
				form.addElement(submitButton);
				Element postId = new Element("input");
				postId.setAttribute("type", "hidden");
				postId.setAttribute("name", "postId");
				postId.setAttribute("value", p.getID().toString());
				form.addElement(postId);
			}
			item.addElement(title);
			item.addElement(text);
			
			if(p.getPicture() != null) {
				item.addElement(img);
			}
			item.addElement(date);
			item.addElement(form);
			list.addElement(item);
		}
		CompoundElement ad  = new CompoundElement("div");
		ad.addClasses("col-2");
		ad.setAttribute("display", "block");
		
		ad.setData("test");
		main.addElement(list);
		area.addElement(main);
		area.addElement(ad);
		jumbo.addElement(header);
		jumbo.addElement(area);
		//jumbo.addElement(ad);
		cont.addElement(jumbo);
		temp.getBody().addElement(cont);
		
		
		response.setContentType("text/html");
		response.getWriter().println(temp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Object userSiteID = request.getSession().getAttribute("userSiteId");
		if(userSiteID == null)
		{
			doGet(request, response);
			return;
		}

		String post = request.getParameter("postId");
		BlogHostPosts test = BlogHostPosts.getAll(BlogHostPosts.class, "id = ?", post).get(0);
		
		if(test.delete())
		{
			response.sendRedirect("/BlogHost/Site?site="+(Integer) userSiteID);
			return;
		}
		else
		{
			System.err.println("Error in deleting Post");
		}
	}	
}


