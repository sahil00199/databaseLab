

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
	public static String url = "jdbc:postgresql://localhost:5050/postgres";
	public static String name = "sahilshah";
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
		if (request.getSession(false) == null)
		{
			System.out.println("the user is not logged in");
			response.sendRedirect("Login");
		}
		else
		{
			PrintWriter out = response.getWriter();
			try
			( Connection conn = DriverManager.getConnection(
		    		url, name, passwordSQL);
		    )
			{
				HttpSession currentSession = request.getSession();
				String type = (String) currentSession.getAttribute("type");
				String id = (String) currentSession.getAttribute("id");
				if (type.equals("instructor"))
				{
					PreparedStatement pstmt = conn.prepareStatement("select name, dept_name from instructor where id = ?");
					pstmt.setString(1, id);
					ResultSet rset = pstmt.executeQuery();
					if (! rset.next())
					{
						System.out.println("There is no such user");
						response.sendRedirect("Login");
					}
					else
					{
						String name = rset.getString("name");
						String dept = rset.getString("dept_name");
						out.println("<html><head><title>Home</title></head><body>"
								+ "Name: " + name + "<br>"
								+ "Dept: " + dept);
						out.println("<br><a href=\"Logout\"> Logout</a>");
						out.println("</body></html>");
					}
				}
				else
				{
					PreparedStatement pstmt = conn.prepareStatement("select name, dept_name from student where id = ?");
					pstmt.setString(1, id);
					ResultSet rset = pstmt.executeQuery();
					if (! rset.next())
					{
						System.out.println("There is no such user");
						response.sendRedirect("Login");
					}
					else
					{
						String name = rset.getString("name");
						String dept = rset.getString("dept_name");
						out.println("<html><head><title>Home</title></head><body>"
								+ "Name: " + name + "<br>"
								+ "Dept: " + dept + "<br> Type: " + type);
//						+ "<form action=\"Logout\" method=\"post\""
//								+ "<input type=\"submit\" value = \"Logout\">"
//								+ "</form></body></html>");
						out.println("<br><a href=\"Logout\"> Logout</a>");
						out.println("<br><a href=\"displayGrades\"> View Grades</a>");
						out.println("</body></html>");
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
