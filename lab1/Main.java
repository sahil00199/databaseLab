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
					+ " where ID=subpart.pID and subpart.spID = height.p"
					+ " and h < 100)"
					+ " select p,max(h) from height group by p"
					+ " order by max";
			ResultSet rs=stmt.executeQuery(query);
			Statement stmt5=conn.createStatement();
			stmt5.executeUpdate("create temporary  table cost ( ID varchar(50), cost "
					+ "int NOT NULL Default 0.0);\n");
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
					PreparedStatement pstmt3 = conn.prepareStatement
							("select cost from cost where ID = ?");
					pstmt3.setString(1, rs2.getString(1));
					ResultSet r= pstmt3.executeQuery();
					if(r.next())
					{
						sum=sum+ r.getInt(1)*(rs2.getInt(2));
					}
				//	sum=sum+(hm.get(rs2.getString(1)))*(rs2.getInt(2));
				}
				sum=sum+v;
				PreparedStatement pstmt4 = conn.prepareStatement
						("insert into cost values(?,?)");
				pstmt4.setString(1, s1);
				pstmt4.setInt(2, sum);
				pstmt4.executeUpdate();
				//hm.put(s1,sum);
				//System.out.println(s1+" "+sum);
			}
			Scanner sc = new Scanner(System.in);
			while(true) {
				String inp=sc.next();
				PreparedStatement pstmt5 = conn.prepareStatement
						("select cost from cost where ID = ?");
				pstmt5.setString(1, inp);
				ResultSet rset= pstmt5.executeQuery();
				if(rset.next())
				{
					System.out.println(inp+" "+rset.getInt(1));
				}
				else {
					System.out.println("Such a part with ID=" + inp +" does not exist");
				}
			}
		}
		catch (Exception sqle)
		{
		System.out.println("Exception : " + sqle);
		}
	}

}

