package main;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbConnection.DBConnection;
import templates.MainTemplate;
import util.Template;
import html.*;

@WebServlet("/HomePage")
public class HomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String URL = "/BlogHost/HomePage";

    
    public HomePage() 
    {

    }

    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		MainTemplate tempMain;
		if(request.getSession().getAttribute("userSiteId") != null){
			tempMain = new MainTemplate(request);
		}
		else {
			tempMain = new MainTemplate();
		}
		Template temp = tempMain.getCurrentTemplate();
		response.setContentType("text/html");
		CompoundElement container = tempMain.createContainer();
		container.setData("<center><h1>Welcome to BlogHost</h1></center>");
		temp.getBody().addElement(container);
		CompoundElement viewedCreators = tempMain.createContainer();
		CompoundElement header = new CompoundElement("h2", "Most Viewed Creators");
		
		CompoundElement userList = new CompoundElement("ul");
		userList.addClass("list-group");
		Connection connection = DBConnection.getDBConnection();
        String selectSQL = "SELECT c.id, c.user_name,c.profile_picture , s.SITE_NAME, s.id as Site_Id, Sum(ps.Views) FROM BlogHostPostStats as ps " + 
        		"left join BlogHostPosts as p on p.id = ps.Post_id " + 
        		"left join BlogHostSites as s on p.Site_id = s.id " + 
        		"left join BlogHostCreators as c on s.Creator_id = c.id " + 
        		"group by c.id, c.user_name, s.SITE_NAME, s.id " + 
        		"order by Sum(ps.Views) desc limit 5";

        PreparedStatement preparedStatement = null;
        try {
        	connection = DBConnection.getDBConnection();
	        preparedStatement = connection.prepareStatement(selectSQL);
	        ResultSet rs = preparedStatement.executeQuery();
	        int counter = 0;
	        boolean viewed = false;
	        while(rs.next()) {
	        	viewed = true;
	        	CompoundElement user = new CompoundElement("li");
	        	user.addClass("text-align:center");
	        	CompoundElement userDiv = new CompoundElement("div");
	        	userDiv.addClass("row");
	        	CompoundElement userSubDiv = new CompoundElement("div");
	        	CompoundElement userHeader = new CompoundElement("div","<h3>User: " + rs.getString("user_name")+"</h3>");
	        	if (counter % 2 == 1)
	        		user.addClass("list-group-item list-group-item-secondary");
				else
					user.addClass("list-group-item ");
	        	CompoundElement site = new CompoundElement("div","<h4>Site: <a href=\"Site?site=" + rs.getString("Site_Id")+"\">"+rs.getString("site_name")+"</a></h4>");
	        	
	        	
	        	Element profilePicture = new Element("img");
				profilePicture.setAttribute("style", "border:3px solid black;"
						+ "max-width:75px;max-height:75px;margin-left:10px;margin-right:10px;object-fit: cover;");
				if(rs.getBlob("profile_picture") != null) {
					profilePicture.setAttribute("src", "data:image/jpg;base64,"
					+ Base64.getEncoder().encodeToString(rs.getBlob("profile_picture").getBytes(1,(int)rs.getBlob("profile_picture").length())));
					
				}
				else {
					profilePicture.setAttribute("src", "Images/blank-profile-picture.png");
				
				}
				userDiv.addElement(profilePicture);
				userSubDiv.addElement(userHeader);
				userSubDiv.addElement(site);
				userDiv.addElement(userSubDiv);
				user.addElement(userDiv);
				userList.addElement(user);
	        	counter++;
	        }
	        viewedCreators.addElement(header);
	        viewedCreators.addElement(userList);
	        if (!viewed) {
	        	header.setData("No Creators Viewed");
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
		temp.getBody().addElement(viewedCreators);
		
		Element ad = new Element("img");
		Ad adString = new Ad();
		ad.setAttribute("src", adString.getAd());
		
		ad.setAttribute("style","display: block;margin-left: auto;margin-right: auto;");
		temp.getBody().addElement(ad);
		temp.getBody().addElement(new Element("br"));
		temp.getBody().addElement(new Element("br"));
		temp.getBody().addElement(new Element("br"));
		response.getWriter().println(temp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

}