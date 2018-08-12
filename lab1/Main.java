package question;
import java.sql.*;
import java.util.*;


public class Main {
	
	public static void main(String[] args) {
		try(Connection conn = DriverManager.getConnection(
	    		"jdbc:postgresql://localhost:5080/postgres",
	    		"preey", "");
				Statement stmt = conn.createStatement();)
		{
			String query="with recursive height(p, h) as "
					+ "(select ID,1 from part as pa where"
					+ " not exists (select * from subpart"
					+ " where subpart.pID = pa.ID) union "
					+ "select ID, h+1 from part,subpart,height"
					+ " where ID=subpart.pID and subpart.spID = height.p)"
					+ " select p,max(h) from height group by p"
					+ " order by max";
			ResultSet rs=stmt.executeQuery(query);
			Map< String,Integer> hm = 
                    new HashMap< String,Integer>();
			while(rs.next()) {
				String s1=rs.getString(1);
				PreparedStatement pstmt2 = conn.prepareStatement
						("select cost from part where ID = ?");
				pstmt2.setString(1,s1);
				ResultSet rs3= pstmt2.executeQuery();
				rs3.next();
				int v=rs3.getInt(1);
				PreparedStatement pstmt = conn.prepareStatement
						("select spID,number from subpart where pID = ?");
				pstmt.setString(1, s1);
				ResultSet rs2=pstmt.executeQuery();
				int sum=0;
				while(rs2.next())
				{
					sum=sum+(hm.get(rs2.getString(1)))*(rs2.getInt(2));
				}
				sum=sum+v;
				hm.put(s1,sum);
				System.out.println(s1+" "+sum);
			}
		}
		catch (Exception sqle)
		{
		System.out.println("Exception : " + sqle);
		}
	}

}

