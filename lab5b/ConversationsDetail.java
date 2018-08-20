

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.http.*;

/**
 * Servlet implementation class ConversationsDetail
 */
@WebServlet("/ConversationsDetail")
public class ConversationsDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConversationsDetail() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    private static void toHTML(ResultSet rset, PrintWriter out)
    {
    	try
		{
			ResultSetMetaData rsmd = rset.getMetaData();
			int numColumns = rsmd.getColumnCount();
			
			//print out the column names first
			out.println("<table>");
			//output is different from what is given in the example, but is what
			//is usually done in html (the correct syntax and tags)
			String currentLine = "\t<tr>";
			for (int i = 1 ; i <= numColumns ; i ++)
			{
				currentLine = currentLine + " <th>" + rsmd.getColumnName(i) + "</td>";
			}
			currentLine = currentLine + " </tr>";
			out.println(currentLine);
			while(rset.next())
			{
				currentLine = "\t<tr>";
				for (int i = 1 ; i <= numColumns ; i ++)
				{
					currentLine = currentLine + " <td>" + rset.getString(i) + "</td>";
				}
				currentLine = currentLine + " </tr>";
				out.println(currentLine);
			}
			out.println("</table>");
		}
		catch (SQLException sqe)
		{
			System.out.println("There was an error in trying to read the result");
			System.out.println("There error was:");
			System.out.println(sqe);
		}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession(false) == null)
		{
			System.out.println("The user is not currently logged in");
			response.sendRedirect("Login");
		}
		else
		{
			PrintWriter out = response.getWriter();
			String threadid = request.getParameter("threadid");
			try
			( Connection conn = DriverManager.getConnection(
		    		Home.url, Home.name, Home.passwordSQL);
			  PreparedStatement pstmt1 = conn.prepareStatement("select * from conversations where thread_id = ? ;");
			  PreparedStatement pstmt2 = conn.prepareStatement("select uid, timestamp, text from posts"
			  		+ "where thread_id = ? order by timestamp desc ;");
			)
			{
				pstmt1.setString(1, threadid);
				ResultSet rset1 = pstmt1.executeQuery();
				if (! rset1.next())
				{
					out.println("<html>\n" + 
							"    <head>\n" + 
							"        <title>\n" + 
							"            Error Page\n" + 
							"        </title>\n" + 
							"    </head>\n" + 
							"    <body>\n" + 
							"        <h3> The chat you are asking for does not exist in the database!! </h3>\n" + 
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
					pstmt2.setString(1, threadid);
					ResultSet rset2 = pstmt2.executeQuery();
					out.println("<html><head><title><Conversation View></title></head><body>");
					toHTML(rset2, out);
					
					//now print the form for a new message
					out.println("<form action=\"NewMessage\" method=\"post\">\n" + 
							"            <input id=\"threadid\" value=" + threadid + " type=\"hidden\">\n" + 
							"            New Message: <input type=\"text\" id=\"message\" name=\"message\" >\n" + 
							"            <input type=\"submit\" value=\"Post\">\n" + 
							"        </form>\n" + 
							"    </body>\n" + 
							"</html>");
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
//			
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
