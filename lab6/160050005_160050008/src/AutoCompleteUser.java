

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.io.PrintWriter;

/**
 * Servlet implementation class AutoCompleteUser
 */
@WebServlet("/AutoCompleteUser")
public class AutoCompleteUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoCompleteUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession(false) == null)
		{
			response.sendRedirect("LoginServlet");
		}
		else
		{
			String partial = (String) request.getParameter("partial");
			String location = (String) request.getParameter("location");
			String uid = (String) request.getSession().getAttribute("id");
//			try
//			(
//					Connection conn = DriverManager.getConnection(Config.url, Config.user, Config.password);
//					PreparedStatement pstmt = conn.prepareStatement("select uid from users where name like '%?%' or phone like '%?%' or uid like '%?%'");
//					//PreparedStatement pstmt2 = conn.prepareStatement("select count(*) from users where name like '%?%' or phone like '%?%' or uid like '%?%'");
//			)
//			{
//				pstmt2.setString(1, partial);
//				pstmt2.setString(2, partial);
//				pstmt2.setString(3, partial);
//				ResultSet rset2 = pstmt2.executeQuery();
//				
//				rset2.next();
////				int numUsers = rset2.getInt(1);
////				
//				pstmt.setString(1, partial);
//				pstmt.setString(2, partial);
//				pstmt.setString(3, partial);
//				ResultSet rset = pstmt.executeQuery();
//			}
//			catch (SQLException sqe)
//			{
//				System.out.println("The was an error in trying to service the database request in AutoCompleteUser");
//				throw sqe;
//			}
			
			partial = partial + "%";
			String query = "";
			System.out.println(location + " " + partial + " " +	 uid);
			if (location.equals("bottom"))
			{
				query = "select uid as label, uid as value from ((select uid  from users where name like ? or phone like ? or uid like ?) except"
						+ "((select distinct uid1 from conversations, users where uid2 = ?) union (select distinct uid2 from conversations, users where uid1 = ?))) as whatever(uid); ";
			}
			else if (location.equals("top"))
			{
				query = "select uid as label, uid as value from ((select uid from users where name like ? or phone like ? or uid like ?) intersect "
						+ "((select distinct uid1 from conversations, users where uid2 = ?) union (select distinct uid2 from conversations, users where uid1 = ?))) as whatever(uid); ";
			}
			String res = DbHelper.executeQueryJson(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING, 
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING}, 
					new String[] {partial, partial, partial, uid, uid});
			
			PrintWriter out = response.getWriter();
			out.print(res);
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
