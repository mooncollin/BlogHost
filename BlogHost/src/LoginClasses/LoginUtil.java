package LoginClasses;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpSession;

import BlogHostHome.UserAccount;

public class LoginUtil {
	public static void storeUser(HttpSession session, UserAccount user) {
		session.setAttribute("currentUser", user);
	}
	
	public static UserAccount getCurrentUser(HttpSession session) {
		UserAccount user = (UserAccount) session.getAttribute("currentUser");
		return user;
	}
	
	public static String makeHash(String nameIn, String passIn) {
		int num = 7;
		char[] name = nameIn.toCharArray();
		for(int i = 0; i < name.length; i++) {
			num *=  6217;
			num %= 6047;
		}
		String pass = passIn + String.valueOf(num);
		System.out.println(pass);
		MessageDigest md = null;
		byte[] bytesOfMessage = null;
		try {
			bytesOfMessage = pass.getBytes("UTF-8");
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (md != null && bytesOfMessage != null) {
		byte[] thedigest = md.digest(bytesOfMessage);
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < thedigest.length; ++i) {
          sb.append(Integer.toHexString((thedigest[i] & 0xFF) | 0x100).substring(1,3));
       }
        return sb.toString().substring(1, 31);
		}
		return null;
		
	}
}


