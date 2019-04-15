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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import templates.BootstrapTemplates;
import templates.MainTemplate;
import user.NewPost;
import util.Template;
import utils.StreamUtils;

@WebServlet("/Site")
public class Site extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	public static final String URL = "/BlogHost/Site";
	
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
	    protected List<Post> postList = new ArrayList<Post>();
	    protected Map<Integer,Integer> likeList = new HashMap<Integer,Integer>();
	    
	    
	    protected void getInfo(int siteID, HttpServletRequest request) {
	    	postList.clear();
	    	int readerId = (request.getSession().getAttribute("userId") == null) 
	    			? -1:(Integer) request.getSession().getAttribute("userId");
	    	Connection connection = DBConnection.getDBConnection();
	        String selectSQL = "Select USER_NAME, FIRST_NAME, LAST_NAME, SITE_NAME, " + 
	        		"PROFILE_PICTURE, SITE_ID, SITE_NAME,p.id as POST_ID, POST_TITLE, " + 
	        		"POST_TEXT, PICTURE, DATE_POSTED, Count(READER_ID) as LIKE_COUNT, " + 
	        		"(SELECT 1=1 FROM bloghost.BlogHostLikes " + 
	        		"WHERE READER_ID = ? AND POST_ID = p.id) as LIKED_BY_USER " + 
	        		"From bloghost.BlogHostSites as s " + 
	        		"left join bloghost.BlogHostPosts as p on p.SITE_ID = s.id   " + 
	        		"left join bloghost.BlogHostLikes as l on l.POST_ID = p.id  " + 
	        		"left join bloghost.BlogHostCreators as c  on c.id = s.CREATOR_ID " + 
	        		"WHERE s.id  = ? " + 
	        		"Group BY CREATOR_ID, SITE_ID, SITE_NAME,p.id, POST_TITLE, POST_TEXT, PICTURE, DATE_POSTED " + 
	        		"ORDER BY POST_ID DESC;";

	        PreparedStatement preparedStatement = null;
	        try {
	        	connection = DBConnection.getDBConnection();
		        preparedStatement = connection.prepareStatement(selectSQL);
		        preparedStatement.setInt(1, readerId);
		        preparedStatement.setInt(2, siteID);
		        List<Post> postListLocal = new ArrayList<Post>();
		        ResultSet rs = preparedStatement.executeQuery();
		        boolean rowFound = false;
		        
		        while(rs.next()) {
		        	if(!rowFound) {
		        		if(rs.getString("USER_NAME")!=null)
		        			userName = rs.getString("USER_NAME");
		        		if(rs.getString("FIRST_NAME")!=null)
		        			firstName = rs.getString("FIRST_NAME");
		        		if(rs.getString("LAST_NAME")!=null)
		        			lastName = rs.getString("LAST_NAME");
		        		if(rs.getString("SITE_NAME")!=null)
		        			siteName = rs.getString("SITE_NAME");
			        	if(rs.getBlob("PROFILE_PICTURE")!=null)
				        	profilePic = Base64.getEncoder().encodeToString(rs.getBlob("PROFILE_PICTURE").getBytes(1,(int)rs.getBlob("PROFILE_PICTURE").length()));
			        	if(rs.getString("SITE_NAME")!=null)
			        		siteName = rs.getString("SITE_NAME");
			        	if(rs.getString("SITE_ID")!=null)
			        		siteId = rs.getInt("SITE_ID");
		        	}
		        	int postId = -1;
		        	String postTitle = "";
		        	String postText = "";
		        	String picture;
		        	if(rs.getString("POST_ID")!=null)
		        		postId = rs.getInt("POST_ID");
		        	if(rs.getString("POST_TITLE")!=null)
		        		postTitle = rs.getString("POST_TITLE");
		        	if(rs.getString("POST_TEXT")!=null)
		        		postText = rs.getString("POST_TEXT");
		        	picture = null;;
		        	if(rs.getBlob("PICTURE")!=null)
		        		picture = Base64.getEncoder().encodeToString(rs.getBlob("PICTURE").getBytes(1,(int)rs.getBlob("PICTURE").length()));
		        	Timestamp datePosted = rs.getTimestamp("DATE_POSTED");
		        	int likeCount = rs.getInt("LIKE_COUNT");
		        	boolean likedByUser = rs.getBoolean("LIKED_BY_USER");
		        	rowFound = true;
		        	if(postId != -1) {
			        	Post p = new Post(postId, postTitle, postText, picture, datePosted, likeCount, likedByUser);
			        	postListLocal.add(p);
		        	}
		        }
		        if(!rowFound) {
		        	siteName = "NoRow";
		        }
		        postList = postListLocal;
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
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			getInfo(Integer.parseInt(request.getParameter("site")),request);
			boolean siteOwner = false;
			boolean loggedIn = false;
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
			int counter = 0;
			for (Post p : postList) {
				CompoundElement item  = new CompoundElement("li");
				item.addClass("list-group-item");
				
				
				Element title = new Element("h2");
				title.setData(p.getPOST_TITLE());
				//title.addClasses("w-75", "p-3");
				Element text = new Element("h5");
				text.addClass("p-3 col-12");
				text.setAttribute("id", "postText");
				text.setData(p.getPOST_TEXT().replaceAll("\n", "<br />"));
				Element date = new Element("p");
				date.setData("Date Posted: " + p.getDATE_POSTED());
				
				CompoundElement likeArea  = new CompoundElement("div");
				likeArea.addClass("row col-12");
				CompoundElement likes = new CompoundElement("div");
				likes.setAttribute("style", "line-height:auto;text-align: center;col-12");
				Element likesSpan = new Element("span");
				likesSpan.setData("Likes: " + p.getLIKE_COUNT());
				likesSpan.setAttribute("name","likeCount"+p.getID());
				likesSpan.setAttribute("style","display: inline-block;vertical-align: middle;line-height: normal;");
				likes.addClass("col-6");
				Element likeButton = new Element("input");
				likeButton.setAttribute("value",(p.LIKED_BY_USER) ? "Unlike":"Like");
				likeButton.setAttribute("style","float:right");
				likeButton.setAttribute("id","likeButton"+p.getID());
				String action = (p.LIKED_BY_USER) ? "0":"1";
				likeButton.setAttribute("onclick", 
						"like("+request.getSession().getAttribute("userId")+","+
						p.getID()+","+action+",'"+request.getContextPath()+"');");
				likeButton.addClasses("btn", "btn-outline-primary", "col-6");
				likes.addElement(likesSpan);
				likeArea.addElement(likes);
				if(loggedIn)likes.addElement(likeButton);
				
				Element img = new Element("img");
				if(p.getPICTURE() != null) {
					img.setAttribute("src", "data:image/jpg;base64," + p.getPICTURE());
					img.setAttribute("style","border:3px solid black;max-width:100%;");
				}
				Form form = new Form();
				if(siteOwner) {
					form.setMethod("POST");
					CompoundElement submitButton = new CompoundElement("button", "Delete Post");
					submitButton.addClasses("btn", "btn-danger");
					submitButton.setAttribute("style", "float:right");
					form.addElement(submitButton);
					Element postId = new Element("input");
					postId.setAttribute("type", "hidden");
					postId.setAttribute("name", "postId");
					postId.setAttribute("value", String.valueOf(p.getID()));
					form.addElement(postId);
				}
				item.addElement(title);
				item.addElement(text);
				
				if(p.getPICTURE() != null) {
					item.addElement(img);
				}
				item.addElement(date);
				item.addElement(likesSpan);
				item.addElement(form);
				
				list.addElement(item);
				
				
				CompoundElement cardButton = new CompoundElement("button", "Open Post");
				cardButton.addClasses("btn-primary","mt-auto", "p-2");
				cardButton.setAttribute("data-toggle", "modal");
				cardButton.setAttribute("data-target", "#modal" + counter);
				
				item.addElement(cardButton);
				
				List<Element> modalFooter = new LinkedList<Element>();
			
				List<Element> modalBody = new LinkedList<Element>();
				modalBody.add(text);
				if(p.getPICTURE() != null) {
					modalBody.add(img);
				}
				modalBody.add(date);
				modalBody.add(likeArea);
				CompoundElement modal = BootstrapTemplates.scrollableModal(p.getPOST_TITLE(), "modal" + counter, modalBody, modalFooter);
				temp.getBody().addEndElement(modal);
				counter++;
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


