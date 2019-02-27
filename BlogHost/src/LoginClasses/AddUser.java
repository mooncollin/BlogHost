package LoginClasses;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddUser")
public class AddUser extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public AddUser() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String userName = request.getParameter("userName");
      String firstName = request.getParameter("firstName");
      String lastName = request.getParameter("lastName");
      String password = request.getParameter("userPass");
      String age = request.getParameter("age");
      String Picture = request.getParameter("pic");
      if (Picture == null) {
      }
      
      password = LoginUtil.makeHash(userName, password);
      Connection connection = null;
      String insertSql = " INSERT INTO BlogHostCreators (id, user_name, password, first_name, last_name, age, PROFILE_PICTURE) values (default, ?, ?, ?, ?, ?, ?)";

      try {
         DBConnection.getDBConnection();
         connection = DBConnection.connection;
         PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
         preparedStmt.setString(1, userName);
         preparedStmt.setString(2, password);
         preparedStmt.setString(3, firstName);
         preparedStmt.setString(4, lastName);
         preparedStmt.setString(5, age);
         preparedStmt.setString(6, Picture);
         preparedStmt.execute();
         connection.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

      // Set response content type
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Insert Data to DB table";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h2 align=\"center\">" + title + "</h2>\n" + //
            "<ul>\n" + //

            "  <li><b>User Name</b>: " + userName + "\n" + //
            "  <li><b>First Name</b>: " + firstName + "\n" + //
            "  <li><b>Last Name</b>: " + lastName + "\n" + //

            "</ul>\n");

      out.println("<a href=/BlogHost/login.html>login</a> <br>");
      out.println("</body></html>");
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
