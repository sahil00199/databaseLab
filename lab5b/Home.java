package Home;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import javax.servlet.http.*;
import java.io.PrintWriter;
/**
 * Servlet implementation class Home
 */
@WebServlet("/Home")
public class Home extends HttpServlet {
	public static String url = "jdbc:postgresql://localhost:5080/postgres";
	public static String name = "preey";
	public static String passwordSQL = "";
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Home() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession s=request.getSession(false);
		if(s != null)
		{
			PrintWriter out = response.getWriter();
			try
			( Connection conn = DriverManager.getConnection(
		    		url, name, passwordSQL);)
			{
			String id=(String)(s.getAttribute("id"));
			out.println("<html> <head><title>Home Page</title></head>");
			out.println("<body>");
			out.println("<form action = \"createConversation\" method = \"get\">");
			out.println(" Enter the id of your friend!! <input type=\"text\" name = \"id\">");
			out.println("<br>");
			//out.println(" Enter your password: <input type=\"text\" name = \"password\">");
			out.println("<input type=\"submit\" value = \"createConversation\"></form>" );
			PreparedStatement pstmt=conn.prepareStatement("with q as (select timestamp,"
					+ "thread_id,text from posts as p where p.timestamp = (select max(timestamp) "
					+ "from posts as p2 where p2.thread_id=p.thread_ID group by thread_id))"
					+ " (select name,text,timestamp,q.thread_id as thread_id "
					+ "from conversations,q,users where "
					+ "conversations.thread_id = q.thread_id and uid2=? and uid1=uid) "
					+ "union (select name,text,timestamp,q.thread_id as thread_id from conversations,"
					+ "q,users where conversations.thread_id = q.thread_id and uid1=? and uid2=uid)"
					+ " union  "
					+ "(select name, null as text, null as timestamp "
+ ",thread_id from conversations as c,users where"
+ " uid2=? and uid1=uid and not exists (select * from posts as p"
+ " where p.thread_id=c.thread_id))"
+ " union (select name,null as text, null as timestamp,thread_id "
+ "from conversations as c,users where uid1=? and uid2=uid and not exists "
+ "(select * from posts as p where p.thread_id=c.thread_id))"
+ "  order by timestamp desc nulls last"
						);
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			pstmt.setString(3, id);
			pstmt.setString(4, id);
			ResultSet rs=pstmt.executeQuery();
			toHTML(rs,out,3);
			out.println("No messages were exchanged with those with a null in text and timestamp");
//			PreparedStatement pstmt2=conn.prepareStatement("(select name, null as text, null as timestamp "
//					+ ",thread_id from conversations as c,users where"
//					+ " uid2=? and uid1=uid and not exists (select * from posts as p where p.thread_id=c.thread_id))"
//					+ " union (select name,null as text, null as timestamp,thread_id "
//					+ "from conversations as c,users where uid1=? and uid2=uid and not exists "
//					+ "(select * from posts as p where p.thread_id=c.thread_id))");
//			pstmt2.setString(1, id);
//			pstmt2.setString(2, id);
//			ResultSet rs2=pstmt2.executeQuery();
//			toHTML(rs2,out,3);
			out.println("</body></html>");
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
		else
		{
			response.sendRedirect("Login");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	private static void toHTML(ResultSet rset,PrintWriter out,int x)
	{
		try
		{
			ResultSetMetaData rsmd = rset.getMetaData();
			int numColumns = rsmd.getColumnCount();
			numColumns = x;
			
			//print out the column names first
			out.println("<table>");
			//output is different from what is given in the example, but is what
			//is usually done in html (the correct syntax and tags)
			String currentLine = "\t<tr>";
			for (int i = 1 ; i <= numColumns ; i ++)
			{
				currentLine = currentLine + " <th>" + rsmd.getColumnName(i) + "</th>";
			}
			currentLine = currentLine + " <th>" + " Press to display Conversations " + "</th>";
			currentLine = currentLine + " </tr>";
		out.println(currentLine);
			while(rset.next())
			{
				currentLine = "\t<tr>";
				for (int i = 1 ; i <= numColumns ; i ++)
				{
					currentLine = currentLine + " <td>" + rset.getString(i) + "</td>";
				}
				currentLine = currentLine + " <td>" + "<form action=\"ConversationDetails\" method=\"GET\">\n" + 
						"  <input type=\"hidden\" name=\"threadid\" value=\""+rset.getString(numColumns+1) +
						"\" /> \n" +  
						"  <input type=\"submit\" value = \"DisplayConversation\"  /> \n" + 
						"</form>"+  "</td>";
				currentLine = currentLine + " </tr>";
				out.println(currentLine);
			}
			out.println("</table>");
		}
		catch (SQLException sqe)
		{
			out.println("There was an error in trying to read the result");
			out.println("There error was:");
			out.println(sqe);
		}
	}

}