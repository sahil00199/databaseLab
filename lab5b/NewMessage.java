package NewMessage;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import javax.servlet.http.*;
import java.io.PrintWriter;
import Home.*;
/**
 * Servlet implementation class NewMessage
 */
@WebServlet("/NewMessage")
public class NewMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession(false) == null)
		{
			System.out.println("The user was not logged in");
			response.sendRedirect("Login");
		}
		else
		{
			String threadid = (String) request.getParameter("threadid");
			
			int threadid2= Integer.parseInt(threadid);
			String message = (String) request.getParameter("message");
			String user = (String) request.getSession().getAttribute("id");
			try
			( Connection conn = DriverManager.getConnection(
		    		Home.url, Home.name, Home.passwordSQL);
			  PreparedStatement pstmt1 = conn.prepareStatement("select * from conversations where thread_id = ? and (uid1 = ? or uid2 = ?) ;");
			  PreparedStatement pstmt2 = conn.prepareStatement("insert into posts(thread_id,uid,timestamp,text)values("
			  		+ " ?, ?, now(), ?)");
			)
			{
				PrintWriter out = response.getWriter();
				pstmt1.setInt(1, threadid2);
				pstmt1.setString(2, user);
				pstmt1.setString(3, user);
				//first check if such a conversation exists
				ResultSet rset1 = pstmt1.executeQuery();
				if (!rset1.next())
				{
					out.println("<html>\n" + 
							"    <head>\n" + 
							"        <title>\n" + 
							"            Error Page\n" + 
							"        </title>\n" + 
							"    </head>\n" + 
							"    <body>\n" + 
							"        <h3>The conversation that you are trying to post into does not exist in the database</h3>\n" + 
							"        <br>\n" + 
							"        <a href = \"Home\"> Home </a>\n" + 
							"        <br>\n" + 
							"        <a href = \"Logout\"> Logout </a>\n" + 
							"    </body>\n" + 
							"</html>");
					return;
				}
				else
				{
					pstmt2.setInt(1,  threadid2);
					pstmt2.setString(2, user);
					pstmt2.setString(3, message);
					int returnStatus = pstmt2.executeUpdate();
					if (returnStatus != 1)
					{
						out.println("<html>\n" + 
								"    <head>\n" + 
								"        <title>\n" + 
								"            Error page\n" + 
								"        </title>\n" + 
								"    </head>\n" + 
								"    <body>\n" + 
								"        <h3>There was an error in inserting the message into the conversation because the number"
								+ " of lines updated is " + Integer.toString(returnStatus) + "</h3>\n" + 
								"        <br>\n" + 
								"        <a href = \"Home\"> Home </a>\n" + 
								"        <br>\n" + 
								"        <a href = \"Logout\"> Logout </a>\n" + 
								"    </body>\n" + 
								"</html>\n");
					}
					else
					{
						response.sendRedirect("ConversationDetails?threadid=" + threadid);
					}
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

}