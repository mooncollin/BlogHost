package main;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.mysql.cj.util.StringUtils;

import dbConnection.DBConnection;
import html.CompoundElement;
import html.Element;
import templates.MainTemplate;
import util.Template;

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
    protected List<Post> postList = new ArrayList<Post>();
    //protected Connection connection;
    
    
    protected void getSiteInfo(int id) {
    	Connection connection = null;
	    	try {
				connection = DBConnection.getDBConnection();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        String selectSQL = "Select * from BlogHostSites as s join BlogHostCreators as c on s.id = c.id where s.id = ?";
        PreparedStatement preparedStatement = null;
        try {
        	connection = DBConnection.getDBConnection();
	        preparedStatement = connection.prepareStatement(selectSQL);
	        preparedStatement.setInt(1, id);
	        ResultSet rs = preparedStatement.executeQuery();
	        if(rs.first()) {
	        	siteName = rs.getString(4);
	        	userName = rs.getString(7);
	        	firstName = rs.getString(8);
	        	lastName = rs.getString(9);
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
                	DBConnection.closeDBConnection();
            } catch (Exception se) {
                se.printStackTrace();
             }
         }
    }
    protected void getSitePosts(int id) {
    	Connection connection = null;
    	try {
			connection = DBConnection.getDBConnection();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	String selectSQL = "Select * from BlogHostPosts where site_id = ?";
        PreparedStatement preparedStatement = null;
        try {
        	connection = DBConnection.getDBConnection();
	        preparedStatement = connection.prepareStatement(selectSQL);
	        preparedStatement.setInt(1, id);
	        ResultSet rs = preparedStatement.executeQuery();
	       
	        while(rs.next()) {
	        	Post post = new Post();
	        	post.setID(rs.getInt("SITE_ID"));
	        	post.setPOST_TITLE(rs.getString("POST_TITLE"));
	        	post.setPOST_TEXT(rs.getString("POST_TEXT"));
	        	postList.add(post);
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
                	DBConnection.closeDBConnection();
             } catch (Exception se) {
                se.printStackTrace();
             }
         }
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		postList.clear();
		String page = request.getParameter("site");
		if(page == null || page == "" ) {//|| !StringUtils.isStrictlyNumeric(page)) {
			response.sendRedirect("/Error/");
			return;
		}
		getSiteInfo(Integer.parseInt(request.getParameter("site")));
		if(siteName == "NoRow") {
			response.sendRedirect("/Error/");
			return;
		}
		getSitePosts(Integer.parseInt(request.getParameter("site")));
		MainTemplate.createTmp();
		Template temp = MainTemplate.basicTemplate();
		CompoundElement header = new CompoundElement("div");
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
		CompoundElement list  = new CompoundElement("ul");
		list.addClasses("list-group", "w-75", "p-3");
		for (Post p : postList) {
			CompoundElement item  = new CompoundElement("li");
			item.addClass("list-group-item");
			
			
			Element title = new Element("h4");
			title.setData("Title: "+ p.POST_TITLE);
			//title.addClasses("w-75", "p-3");
			Element text = new Element("p");
			text.setData(p.POST_TEXT);
			item.addElement(title);
			item.addElement(text);
			list.addElement(item);
		}
		temp.getBody().addElement(header);
		temp.getBody().addElement(list);
		
		
		response.setContentType("text/html");
		response.getWriter().println(temp);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		doGet(request, response);
	}

}
