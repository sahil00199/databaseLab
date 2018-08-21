package Login;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import javax.servlet.http.*;
import Home.*;
/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//request
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession currentSession = request.getSession(false);
		String id = request.getParameter("name");
		String password = request.getParameter("password");
		//System.out.println(id);
		//System.out.println(password);
		if (currentSession == null)
		{
			if (id != null && password != null)
			{
				try
				( Connection conn = DriverManager.getConnection(
			    		Home.url, Home.name, Home.passwordSQL);
						PreparedStatement pstmt = conn.prepareStatement("select password from password where id = ?");)
				{
					//System.out.println("Came here");
					pstmt.setString(1, id);
					ResultSet rset = pstmt.executeQuery();
					if (rset.next() == false)
					{
						System.out.println("There is no such username in the database");
						response.sendRedirect("Login");
					}
					else 
					{
						String storedPassword = rset.getString("password");
						//System.out.println(storedPassword);
						if (storedPassword.equals(password))
						{
							currentSession = request.getSession(true);
							currentSession.setAttribute("id", id);
							currentSession.setAttribute("password", password);
							PreparedStatement pstmt2 = conn.prepareStatement("select * from student where id = ?");
							pstmt2.setString(1, id);
							ResultSet rset2 = pstmt2.executeQuery();
							if (rset2.next())
							{
								currentSession.setAttribute("type", "student");
							}
							else
							{
								currentSession.setAttribute("type", "instructor");
							}
							response.sendRedirect("Home");
						}
						else
						{
							System.out.println("Invalid Credentials");
							PrintWriter out = response.getWriter();
							out.println("<html> <head><title>Login Page</title></head>");
							out.println("<body><h2>The login credentials were invalid</h2><br>");
							out.println("<h3>Name: Sahil Shah, Preey Shah, Roll Number: 160050005, 160050008</h3><br>");
							out.println("<form action = \"Login\" method = \"get\">");
							out.println(" Enter your name: <input type=\"text\" name = \"name\">");
							out.println("<br>");
							out.println(" Enter your password: <input type=\"text\" name = \"password\">");
							out.println("<input type=\"submit\" value = \"Submit\"></form>" );
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
//				PrepareStatement pstmt = 
//				ResultSet rset = 
			}
			else
			{
				//System.out.println("Invalid Credentials");
				PrintWriter out = response.getWriter();
				out.println("<html> <head><title>Login Page</title></head>");
				out.println("<body>");
				out.println("<h3>Name: Sahil Shah, Roll Number: 160050005</h3><br>");
				out.println("<form action = \"Login\" method = \"get\">");
				out.println(" Enter your name: <input type=\"text\" name = \"name\">");
				out.println("<br>");
				out.println(" Enter your password: <input type=\"text\" name = \"password\">");
				out.println("<input type=\"submit\" value = \"Submit\"></form>" );
				out.println("</body></html>");
			}
		}
		else
		{
			response.sendRedirect("Home");
		}
	}

}