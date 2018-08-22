
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.io.PrintWriter;
import javax.servlet.http.*;

/**
 * Servlet implementation class createConversation
 */
@WebServlet("/createConversation")
public class createConversation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public createConversation() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private static int min(int a, int b) 
    {
    	if (a < b) return a;
    	return b;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession(false) == null)
		{
			System.out.println("The user is not logged in");
			response.sendRedirect("Login");
		}
		else
		{
			PrintWriter out = response.getWriter();
			HttpSession currentSession = request.getSession(false);
			String user1 = (String) currentSession.getAttribute("id");
			String user2 = request.getParameter("id");
			int i = 0;
			for (i = 0 ; i < min(user1.length(), user2.length()); i ++)
			{
				char c1 = user1.charAt(i);
				char c2 = user2.charAt(i);
				if (c1 > c2)
				{
					String temp = user1;
					user1 = user2;
					user2 = temp;
				}
				if (c2 > c1)
				{
					break;
				}
			}
			if (i == min(user1.length(), user2.length()) && user1.length() > user2.length())
			{
				String temp = user1;
				user1 = user2;
				user2 = temp;
			}
			//ensured that user 1 < user 2
			
			try
			( Connection conn = DriverManager.getConnection(
		    		Home.url, Home.name, Home.passwordSQL);
			  PreparedStatement pstmt1 = conn.prepareStatement("select * from users where uid = ? ;");
			  PreparedStatement pstmt2 = conn.prepareStatement("select * from users where uid = ? ;");
			  PreparedStatement pstmt3 = conn.prepareStatement("select * from conversations where uid1 = ? and uid2 = ? ;");
			  PreparedStatement pstmt4 = conn.prepareStatement("insert into conversations values (?, ?) ;");
			)
			{
				pstmt1.setString(1, user1);
				pstmt2.setString(1, user2);
				pstmt3.setString(1, user1);
				pstmt3.setString(2, user2);
				pstmt4.setString(1, user1);
				pstmt4.setString(2, user2);
				
				//PrintWriter out = response.getWriter();
				//check if both the users exist
				ResultSet rset1 = pstmt1.executeQuery();
				ResultSet rset2 = pstmt2.executeQuery();
				if ((! rset1.next()) || (! rset2.next()))
				{
					out.println("<html>\n" + 
							"    <head>\n" + 
							"        <title>\n" + 
							"            Error page\n" + 
							"        </title>\n" + 
							"    </head>\n" + 
							"    <body>\n" + 
							"        <h3> One of the users involved in the chat does not exist in the database </h3>\n" + 
							"        <br>\n" + 
							"        <a href = \"Home\"> Home </a>\n" + 
							"        <br>\n" + 
							"        <a href = \"Logout\"> Logout </a>\n" + 
							"    </body>\n" + 
							"</html>\n");
					return;
				}
				
				//check if a chat already exists or not
				ResultSet rset3 = pstmt3.executeQuery();
				if (rset3.next())
				{
					out.println("<html>\n" + 
							"    <head>\n" + 
							"        <title>\n" + 
							"            Error page\n" + 
							"        </title>\n" + 
							"    </head>\n" + 
							"    <body>\n" + 
							"        <h3> A chat already exists between the given users! </h3>\n" + 
							"        <br>\n" + 
							"        <a href = \"Home\"> Home </a>\n" + 
							"        <br>\n" + 
							"        <a href = \"Logout\"> Logout </a>\n" + 
							"    </body>\n" + 
							"</html>\n");
					return;
				}
				
				if (user1.equalsIgnoreCase(user2))
				{
					out.println("<html>\n" + 
							"    <head>\n" + 
							"        <title>\n" + 
							"            Error page\n" + 
							"        </title>\n" + 
							"    </head>\n" + 
							"    <body>\n" + 
							"        <h3> A chat already exists between the given users! </h3>\n" + 
							"        <br>\n" + 
							"        <a href = \"Home\"> Home </a>\n" + 
							"        <br>\n" + 
							"        <a href = \"Logout\"> Logout </a>\n" + 
							"    </body>\n" + 
							"</html>\n");
					return;
				}
				
				//we are now good to go ahead and create a new chat
				int returnStatus = pstmt4.executeUpdate();
				if (returnStatus == 1)
				{
					out.println("<html>\n" + 
							"    <head>\n" + 
							"        <title>\n" + 
							"            Confirmation Page\n" + 
							"        </title>\n" + 
							"    </head>\n" + 
							"    <body>\n"
							+ "<h1>WhatASap</h1><br>" + 
							"        <h3> Successfully created a new chat :D  </h3>\n" + 
							"        <br>\n" + 
							"        <a href = \"Home\"> Home </a>\n" + 
							"        <br>\n" + 
							"        <a href = \"Logout\"> Logout </a>\n" + 
							"    </body>\n" + 
							"</html>");
				}
				else
				{
					out.println("<html>\n" + 
							"    <head>\n" + 
							"        <title>\n" + 
							"            Error page\n" + 
							"        </title>\n" + 
							"    </head>\n" + 
							"    <body>\n" + 
							"        <h3>There was an error in inserting the chat into the table because the number"
							+ " of lines updated is " + Integer.toString(returnStatus) + "</h3>\n" + 
							"        <br>\n" + 
							"        <a href = \"Home\"> Home </a>\n" + 
							"        <br>\n" + 
							"        <a href = \"Logout\"> Logout </a>\n" + 
							"    </body>\n" + 
							"</html>\n");
				}
			}
			catch(SQLException sqex)
			{
				System.out.println("there was an error in trying to open a connection with the database");
				try {
					throw sqex;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}