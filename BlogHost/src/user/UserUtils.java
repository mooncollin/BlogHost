package user;

import javax.servlet.http.HttpServletRequest;

public class UserUtils
{
	public static final String USER_SITE_ID_KEY = "userSiteId";
	public static final String USER_NAME_KEY = "userName";
	
	public static Integer getUserSiteID(HttpServletRequest request)
	{
		Object id = request.getSession().getAttribute(USER_SITE_ID_KEY);
		if(id == null)
		{
			return null;
		}
		
		return (Integer) id;
	}
	
	public static String getUserName(HttpServletRequest request)
	{
		Object userName = request.getSession().getAttribute(USER_NAME_KEY);
		if(userName == null)
		{
			return null;
		}
		
		return (String) userName;
	}
}
