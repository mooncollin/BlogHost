package main;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.util.StringUtils;

import dbConnection.DBConnection;
import forms.Form;
import html.CompoundElement;
import html.Element;
import models.BlogHostCreators;
import models.BlogHostDonations;
import models.BlogHostPosts;
import models.BlogHostSubscriptions;
import models.Model;
import templates.BootstrapTemplates;
import templates.MainTemplate;
import user.UserUtils;
import util.Template;

@WebServlet("/Site")
public class Site extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	public static final String URL = "/BlogHost/Site";
	
	 public Site() 
	    {
	        super();
	    }
	 
	    
	    
	    protected List<Object> getInfo(int siteID, HttpServletRequest request) {
	    	String userName;
		    String firstName;
		    String lastName;
		    String siteName;
		    String profilePic;
		    int siteId;
		    int creatorId;
		    List<Post> postList = new ArrayList<Post>();
		    Map<Integer,Integer> likeList = new HashMap<Integer,Integer>();
		   
		    siteId=creatorId=-1;
	    	userName=firstName=lastName=siteName=profilePic = null;
	    	int readerId = (request.getSession().getAttribute("userId") == null) 
	    			? -1:(Integer) request.getSession().getAttribute("userId");
	    	Connection connection = DBConnection.getDBConnection();
	        String selectSQL = "Select c.id as CREATOR_ID, c.USER_NAME, c.FIRST_NAME, c.LAST_NAME, SITE_NAME, " + 
	        		"c.PROFILE_PICTURE, SITE_ID, SITE_NAME,p.id as POST_ID, POST_TITLE, " + 
	        		"POST_TEXT, PICTURE, p.DATE_POSTED as DATE_POSTED, Count(READER_ID) as LIKE_COUNT, " + 
	        		"(SELECT 1=1 FROM BlogHostLikes " + 
	        		"WHERE READER_ID = ? AND POST_ID = p.id) as LIKED_BY_USER, " + 
	        		"c2.USER_NAME as COMMENTOR_NAME, c2.id as COMMENTOR, COMMENT_TEXT, co.DATE_POSTED as COMMENT_POSTED_DATE " + 
	        		"From BlogHostSites as s " + 
	        		"left join BlogHostPosts as p on p.SITE_ID = s.id   " + 
	        		"left join BlogHostLikes as l on l.POST_ID = p.id  " + 
	        		"left join BlogHostCreators as c  on c.id = s.CREATOR_ID " + 
	        		"left join BlogHostComments as co on co.Post_ID = p.id " +
	        		"left join BlogHostCreators as c2  on c2.id = co.CREATOR_ID " + 
	        		"WHERE s.id  = ? " + 
	        		"Group BY s.CREATOR_ID, SITE_ID, SITE_NAME,p.id, POST_TITLE, POST_TEXT, PICTURE, p.DATE_POSTED, co.Creator_ID,COMMENT_TEXT,co.DATE_POSTED,c2.id " + 
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
		        int currID = -1;
		        boolean postMade = false;
		        while(rs.next()) {
		        	if(!rowFound) {
		        		if(rs.getString("CREATOR_ID")!=null)
		        			creatorId = rs.getInt("CREATOR_ID");
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
		        	int commentor = -1;
		        	String commentorName = "";
		        	String commentText = "";
		        	if(rs.getString("POST_ID")!=null) {
		        		postId = rs.getInt("POST_ID");
		        		if(postId == currID) {
		        			postMade = true;
		        			currID = postId;
		        		}
		        		else {
		        			postMade = false;
		        			currID = postId;
		        		}
		        	}
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
		        	if(rs.getString("commentor_Name")!=null)
		        		commentorName = rs.getString("commentor_Name");
		        	commentor = rs.getInt("COMMENTOR");
		        	if(rs.getString("COMMENT_TEXT")!=null)
		        		commentText = rs.getString("COMMENT_TEXT");
		        	Timestamp commentDatePosted = rs.getTimestamp("COMMENT_POSTED_DATE");
		        	rowFound = true;
		        	if(postId != -1 && !postMade) {
			        	Post p = new Post(postId, postTitle, postText, picture, datePosted, likeCount, likedByUser);
			        	if(commentorName != "") {
			        		Comment c = new Comment(commentor, commentorName, commentText, commentDatePosted);
			        		p.addCOMMENT_LIST(c);
			        	}
			        	postListLocal.add(p);
		        	}
		        	else if(postId != -1 && postMade && commentorName != "") {
		        		Comment c = new Comment(commentor, commentorName, commentText,  commentDatePosted);
			        	postListLocal.get(postListLocal.size()-1).addCOMMENT_LIST(c);
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
	        List<Object> ret = new ArrayList<Object>();
		    ret.add(userName);
		    ret.add(firstName);
		    ret.add(lastName);
		    ret.add(siteName);
		    ret.add(profilePic);
		    ret.add(siteId);
		    ret.add(creatorId);
		    ret.add(postList);
		    ret.add(likeList);
		    return ret;
	    }
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			if(request.getParameter("site") == null){
				response.sendRedirect("/Error/");
				return;
			}
			List<Object> objs = getInfo(Integer.parseInt(request.getParameter("site")),request);
			String userName = (String) objs.get(0);
		    @SuppressWarnings("unused")
			String firstName = (String) objs.get(1);
		    @SuppressWarnings("unused")
			String lastName = (String) objs.get(2);
		    String siteName = (String) objs.get(3);
		    String profilePic = (String) objs.get(4);
		    @SuppressWarnings("unused")
			int siteId = (int) objs.get(5);
		    int creatorId = (int) objs.get(6);
		    List<Post> postList = null;
		    if (objs.get(7) != null)
		    	postList = (List<Post>) objs.get(7);
		    Map<Integer,Integer> likeList = (Map<Integer,Integer>) objs.get(8);
			
			
			boolean siteOwner = false;
			boolean loggedIn = false;
			if(request.getSession().getAttribute("userSiteId") != null &&  (int)request.getSession().getAttribute("userSiteId") == Integer.parseInt(request.getParameter("site"))) {
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
				tempMain = new MainTemplate(request);
			}
			else {
				tempMain = new MainTemplate();
			}
			
			Template temp = tempMain.getCurrentTemplate();
			CompoundElement cont = new CompoundElement("div");
			cont.setAttribute("style", "left:75px");//width:calc(~\"100%-75px\")");
			cont.addClass("container");
			cont.addClass("col-10");
			CompoundElement jumbo = new CompoundElement("div");
			jumbo.addClass("jumbotron");
			CompoundElement header = new CompoundElement("div");
			Element profilePicture = new Element("img");
			//profilePicture.addClass("col-4");
			String max = (loggedIn) ? "245":"200" ;
			profilePicture.setAttribute("style", "border:3px solid black;"
					+ "max-width:"+max+"px;max-height:"+max+"px;margin-left:50px;object-fit: cover;");
			CompoundElement row = new CompoundElement("div");
			row.addClass("row");
			if(profilePic != null) {
				profilePicture.setAttribute("src", "data:image/jpg;base64,"
				+ profilePic);
				
			}
			else {
				profilePicture.setAttribute("src", "Images/blank-profile-picture.png");
			
			}
			row.addElement(profilePicture);
			Element pSite = new Element("h1");
			pSite.setData("Welcome to: "+ siteName);
			
			Element pCreator = new Element("h3");
			pCreator.setData("Creator: "+ userName);
			CompoundElement pStore = new CompoundElement("h3");
			CompoundElement storeLink = new CompoundElement("a");
			storeLink.setData("Visit my Store!");
			storeLink.setAttribute("href", "Store?id="+creatorId);
			pStore.addElement(storeLink);
			Element pPosts = new Element("h5");
			pPosts.setData(userName + "'s posts: " + postList.size());
			header.addClasses("w-75 p-3 col-7");
			header.addElement(pSite);
			header.addElement(pCreator);
			header.addElement(pStore);
			header.addElement(pPosts);
			row.addElement(new Element("div"));
			row.addElement(header);
			if(siteOwner) {
			CompoundElement newPost = new CompoundElement("a", "Add New Post");
				newPost.addClasses("btn", "btn-primary", "button");
				newPost.setAttribute("href", "/BlogHost/NewPost");
				header.addElement(newPost);
				
				CompoundElement donationListButton = new CompoundElement("button", "View Donations");
				donationListButton.addClasses("btn", "btn-primary", "ml-5");
				donationListButton.setAttribute("data-toggle", "modal");
				donationListButton.setAttribute("data-target", "#donations");
				
				header.addElement(donationListButton);
			}
			else if(loggedIn) {
				CompoundElement donationButton = new CompoundElement("button", "Donate");
				donationButton.addClasses("btn", "btn-success", "text-white");
				donationButton.setAttribute("data-toggle", "modal");
				donationButton.setAttribute("data-target", "#donationModal");
				header.addElement(donationButton);
				
				List<BlogHostSubscriptions> subs = Model.getAll(BlogHostSubscriptions.class, 
						"subscriber_id=? and creator_id=?", UserUtils.getUserID(request), creatorId);
				
				CompoundElement subscribeButton;
				if(subs.isEmpty())
				{
					subscribeButton = new CompoundElement("button", "Subscribe");
					subscribeButton.addClasses("btn", "btn-info", "ml-3");
				}
				else
				{
					subscribeButton = new CompoundElement("button", "Subscribed");
					subscribeButton.addClasses("btn", "btn-outline-secondary", "ml-3");
				}
				subscribeButton.setAttribute("onclick", "subscribe(" + creatorId + ")");
				subscribeButton.setAttribute("id", "subscribeButton");
				header.addElement(subscribeButton);
				temp.getBody().addScript("js/subscriptions.js");
			}
			CompoundElement area  = new CompoundElement("div");
			area.addClass("row p-3");
			CompoundElement main  = new CompoundElement("div");
			main.addClass("col-12");
			CompoundElement list  = new CompoundElement("ul");
			list.addClasses("list-group", "p-3");
			int counter = 0;
			for (Post p : postList) {
				CompoundElement item  = new CompoundElement("li");
				item.addClass("list-group-item");
				if (counter%2==1)item.addClass("list-group-item-secondary");
				
				CompoundElement topLine  = new CompoundElement("div");
				topLine.addClass("row");
				Element title = new Element("h2");
				title.addClass("col-10 breakText");
				title.setData((p.getPOST_TITLE().length()>50) ? p.getPOST_TITLE().substring(0, 50)+"...": p.getPOST_TITLE());
				//title.addClasses("w-75", "p-3");
				Element text = new Element("h5");
				text.addClass("p-3 col-12 breakText");
				text.setData((p.getPOST_TEXT().length()>100) ?  p.getPOST_TEXT().substring(0, 100)+"...": p.getPOST_TEXT().replaceAll("\n", "<br />"));
				Element date = new Element("small");
				int time = (int)((new Timestamp(System.currentTimeMillis()).getTime() - p.getDATE_POSTED().getTime())/1000);
				String posted = time + " second(s) ago";
				if(time > 59) {
					time = time/60;
					posted = time + " minute(s) ago";
					if(time > 59) {
						time = time/60;
						posted = time + " hour(s) ago";
						if(time > 23) {
							time = time/24;
							posted = time + " day(s) ago";
						}
					}
				}
				
				date.setData(posted);
				date.addClass("col-2");
				date.setAttribute("style", "text-align:right");
				
				
				CompoundElement likes = new CompoundElement("div");
				likes.setAttribute("style", "line-height:auto;right:-15px");
				likes.addClass("col-12 row");
				Element biggerLikeBadge = new Element("h3");
				Element biggerLikeButton = new Element("h3");
				biggerLikeBadge.addClass("col-6");
				biggerLikeButton.addClass("col-6");
				Element likesSpan = new Element("span");
				likesSpan.setData("Likes: " + p.getLIKE_COUNT());
				likesSpan.setAttribute("name","likeCount"+p.getID());
				//likesSpan.setAttribute("style","display: inline-block;vertical-align: middle;line-height: normal;");
				likesSpan.addClass("badge badge-secondary badge-pill");
			
				Element likeButton = new Element("span");
				likeButton.setData((p.LIKED_BY_USER) ? "Unlike":"Like");
				likeButton.setAttribute("id","likeButton"+p.getID());
				likeButton.setAttribute("style","float:right;");
				String action = (p.LIKED_BY_USER) ? "0":"1";
				likeButton.setAttribute("onclick", 
						"like("+request.getSession().getAttribute("userId")+","+
						p.getID()+","+action+",'"+request.getContextPath()+"');");
				likeButton.addClasses("badge badge-primary");
				biggerLikeBadge.setData(likesSpan.getHTML());
				likes.addElement(biggerLikeBadge);
				
				if(loggedIn) {
					biggerLikeButton.setData(likeButton.getHTML());
					likes.addElement(biggerLikeButton);
				}
				List<Comment> comList = p.getCOMMENT_LIST();
				CompoundElement commentList = new CompoundElement("ul");
				commentList.addClass("list-group");
				commentList.setAttribute("style","list-style: none;");
				commentList.setAttribute("id", "commentList"+p.getID());
				int comCounter = 0;
				if (p.getCOMMENT_LIST().size() > 0) {
					Collections.reverse(comList);
					for(Comment c : comList) {
						CompoundElement commentItem = new CompoundElement("li");
						CompoundElement commentTop = new CompoundElement("div");
						commentTop.addClass("row col-12");
						if (comCounter % 2 == 1)
							commentItem.addClass("list-group-item list-group-item-secondary");
						else
							commentItem.addClass("list-group-item ");
						comCounter++;
						Element commentText = new Element("p");
						commentText.setData(c.getCOMMENT_TEXT());
						commentText.addClass("col-8 breakText");
						Element commentDate = new Element("small");
						int comTime = (int)((new Timestamp(System.currentTimeMillis()).getTime() - c.getCOMMENT_DATE_POSTED().getTime())/1000);
						String comPosted = comTime + " second(s) ago";
						if(comTime > 59) {
							comTime = comTime/60;
							comPosted = comTime + " minute(s) ago";
							if(comTime > 59) {
								comTime = comTime/60;
								comPosted = comTime + " hour(s) ago";
								if(comTime > 23) {
									comTime = comTime/24;
									comPosted = comTime + " day(s) ago";
								}
							}
						}
						commentDate.addClass("col-4");
						commentDate.setData(comPosted);
						commentDate.setAttribute("style", "text-align:right");
						
						Element commentAuthor = new Element("small");
						commentAuthor.setData("Comment by: " + c.getCOMMENTOR_NAME());
						commentTop.addElement(commentText);
						commentTop.addElement(commentDate);
						commentItem.addElement(commentTop);
						commentItem.addElement(commentAuthor);
						commentList.addElement(commentItem);
					}
				}
				
				CompoundElement comments = new CompoundElement("div");
				comments.setAttribute("style", "line-height:auto;right:-15px");
				comments.addClass("col-12 row");
				Element biggerCommentBadge = new Element("h3");
				Element commentInput = new Element("textarea");
				commentInput.setAttribute("name", "commentText"+p.getID());
				commentInput.setAttribute("id", "commentText"+p.getID());
				Element biggerCommentButton = new Element("h3");
				biggerCommentBadge.addClass("col-md-3");
				commentInput.addClass("col-md-6");
				biggerCommentButton.addClass("col-md-3");
				Element commentSpan = new Element("span");
				commentSpan.setData("Comments: " + comList.size());
				commentSpan.setAttribute("name","comCount"+p.getID());
				//likesSpan.setAttribute("style","display: inline-block;vertical-align: middle;line-height: normal;");
				commentSpan.addClass("badge badge-secondary badge-pill");
				biggerCommentBadge.setData(commentSpan.getHTML());
				comments.addElement(biggerCommentBadge);
				
				Element commentButton = new Element("span");
				commentButton.setData("Submit Comment");
				commentButton.setAttribute("style","float:right");
				commentButton.setAttribute("id","commentButton"+p.getID());
				commentButton.setAttribute("onclick", 
						"comment("+request.getSession().getAttribute("userId")+","+
						p.getID()+",'"+request.getContextPath()+"');");
				commentButton.addClasses("badge badge-primary");
				Form comForm = null;
				if(loggedIn) {
					biggerCommentButton.setData(commentButton.getHTML());
					comments.addElement(commentInput);
					comments.addElement(biggerCommentButton);
					comForm = new Form();
					comForm.setMethod("POST");
					CompoundElement submitButton = new CompoundElement("button", "Submit Comment");
					submitButton.addClasses("btn", "btn-danger");
					submitButton.setAttribute("style", "float:right");
					comForm.addElement(submitButton);
					Element postId = new Element("input");
					postId.setAttribute("type", "hidden");
					postId.setAttribute("name", "postId");
					postId.setAttribute("value", String.valueOf(p.getID()));						
					
				}
				
				
				Element img = new Element("img");
				if(p.getPICTURE() != null) {
					img.setAttribute("src", "data:image/jpg;base64," + p.getPICTURE());
					img.setAttribute("style","border:3px solid black;max-width:100%;display: block;"
							+ "margin-left: auto; margin-right: auto;");
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
				topLine.addElement(title);
				topLine.addElement(date);
				item.addElement(topLine);
				
				item.addElement(text);
				
				if(p.getPICTURE() != null) {
					item.addElement(img);
					item.addElement(new Element("br"));
				}
				
				item.addElement(biggerLikeBadge);
				item.addElement(biggerCommentBadge);
				item.addElement(form);
				
				
				list.addElement(item);
				
				
				CompoundElement cardButton = new CompoundElement("button", "Open Post");
				cardButton.addClasses("btn", "btn-primary");
				cardButton.setAttribute("data-toggle", "modal");
				cardButton.setAttribute("data-target", "#modal" + counter);
				cardButton.setAttribute("onclick", "readPost("+p.getID()+",'"+request.getContextPath()+"')");
				item.addElement(new Element("br"));
				item.addElement(cardButton);
				
				List<Element> modalFooter = new LinkedList<Element>();
			
				List<Element> modalBody = new LinkedList<Element>();
				Element titleModal = new Element("h2");
				titleModal.addClass("col-10 breakText");
				titleModal.setData(p.getPOST_TITLE());
				//title.addClasses("w-75", "p-3");
				Element textModal = new Element("h5");
				textModal.addClass("p-3 col-12 breakText");
				textModal.setData(p.getPOST_TEXT().replaceAll("\n", "<br />"));
				
				modalBody.add(textModal);
				if(p.getPICTURE() != null) {
					modalBody.add(img);
				}
				modalBody.add(likes);
				modalBody.add(comments);
				//modalBody.add(comForm);
				modalBody.add(commentList);
				
				CompoundElement modal = BootstrapTemplates.scrollableModal(titleModal.getHTML()+date.getHTML(), "modal" + counter, modalBody, modalFooter);
				
				List<Element> makeLargeList =  modal.getElementsByClass("modal-dialog");
				if(!makeLargeList.isEmpty()) {
					Element makeLarge = makeLargeList.get(0);
					makeLarge.addClass("modal-xl");
				}
				
				if(siteOwner)
				{
					List<Element> donationList = new LinkedList<Element>();
					CompoundElement listOfDonations = new CompoundElement("ul");
					listOfDonations.setAttribute("id", "donationList");
					listOfDonations.addClasses("list-group", "list-group-flush", "list-group-striped");
					
					List<BlogHostDonations> donations = Model.getAll(BlogHostDonations.class, "creator_id=?", page);
					BigDecimal total = BigDecimal.ZERO;
					for(BlogHostDonations donation : donations)
					{
						List<BlogHostCreators> creator = Model.getAll(BlogHostCreators.class, "id=?", donation.getSubscriberID());
						CompoundElement listItem = new CompoundElement("li", "Donation from: " + creator.get(0).getUserName());
						listItem.addClasses("list-group-item", "pb-4");
						CompoundElement amountSpan = new CompoundElement("span", "Amount: $" + donation.getAmount().toPlainString());
						amountSpan.addClass("float-right");
						
						listItem.addElement(amountSpan);
						listOfDonations.addElement(listItem);
						total = total.add(donation.getAmount());
					}
					
					if(donations.isEmpty())
					{
						CompoundElement noDonations = new CompoundElement("div");
						noDonations.addClasses("jumbtotron", "bg-light");
						CompoundElement noDonationsHeader = new CompoundElement("h1", "No Donations");
						noDonationsHeader.addClasses("display-4", "px-3");
						CompoundElement noDonationsLead = new CompoundElement("p", "You do not have any donations at this time. Don't worry, keep up the good work!");
						noDonationsLead.addClasses("lead", "px-3", "pb-3");
						
						noDonations.addElement(noDonationsHeader);
						noDonations.addElement(noDonationsLead);
						donationList.add(noDonations);
					}
					else
					{
						CompoundElement totalListItem = new CompoundElement("li");
						totalListItem.addClasses("list-group-item", "pb-4", "mt-1");
						CompoundElement totalRight = new CompoundElement("span", "Total: $" + total.toPlainString());
						totalRight.addClass("float-right");
						totalListItem.addElement(totalRight);
						listOfDonations.addElement(totalListItem);
						donationList.add(listOfDonations);
					}
					CompoundElement donationListModal = BootstrapTemplates.scrollableModal("Donations", "donations", donationList, new LinkedList<Element>());
					temp.getBody().addEndElement(donationListModal);
				}
				else if(loggedIn)
				{
					List<Element> donationBody = new LinkedList<Element>();
					forms.Number amount = new forms.Number();
					amount.addClass("form-control");
					amount.setLabelText("Amount to Donate  ");
					amount.setValue("1.00");
					amount.setStep(0.01);
					amount.setAttribute("id", "donationAmount");
					amount.setMin(Donation.MIN_DONATION);
					amount.setMax(Donation.MAX_DONATION);
					amount.setName("donationAmount");
					donationBody.add(amount.getLabel());
					donationBody.add(amount);
					
					List<Element> donationFooter = new LinkedList<Element>();
					CompoundElement donationButton = new CompoundElement("button", "Submit Donation");
					donationButton.addClasses("btn", "btn-success");
					donationButton.setAttribute("onclick", "donate(" + page + ")");
					donationButton.setAttribute("id", "donationButton");
					donationFooter.add(donationButton);
					
					CompoundElement donationModal = BootstrapTemplates.scrollableModal("Donation to " + userName, "donationModal", donationBody, donationFooter);
					donationModal.setAttribute("style", "z-index:9");
					temp.getBody().addEndElement(donationModal);
					temp.getBody().addScript("js/donation.js");
				}
				
				modal.getElementsByClass("close").get(0).setAttribute("hidden", "hidden");
				temp.getBody().addEndElement(modal);
				
				counter++;
			}
//			CompoundElement ad  = new CompoundElement("div");
//			ad.addClasses("col-2");
//			ad.setAttribute("display", "block");
			
			//ad.setData("test");
			main.addElement(list);
			area.addElement(main);
			//area.addElement(ad);
			jumbo.addElement(row);
			jumbo.addElement(area);
			//jumbo.addElement(ad);
			cont.addElement(jumbo);
			temp.getBody().addElement(cont);
			Element ad = new Element("img");
			Ad adString = new Ad();
			ad.setAttribute("src", adString.getAd());
			
			ad.setAttribute("style","display: block;margin: 0 auto; position: relative;left:75px");
			temp.getBody().addElement(ad);
			temp.getBody().addElement(new Element("br"));
			temp.getBody().addElement(new Element("br"));
			temp.getBody().addElement(new Element("br"));
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


