import java.sql.*;
import java.util.Scanner;

class ResultSetOutput
{
	public static void toHTML(ResultSet rset)
	{
		try
		{
			ResultSetMetaData rsmd = rset.getMetaData();
			int numColumns = rsmd.getColumnCount();
			
			//print out the column names first
			System.out.println("<table>");
			//output is different from what is given in the example, but is what
			//is usually done in html (the correct syntax and tags)
			String currentLine = "\t<tr>";
			for (int i = 1 ; i <= numColumns ; i ++)
			{
				currentLine = currentLine + " <th>" + rsmd.getColumnName(i) + "</td>";
			}
			currentLine = currentLine + " </tr>";
			System.out.println(currentLine);
			while(rset.next())
			{
				currentLine = "\t<tr>";
				for (int i = 1 ; i <= numColumns ; i ++)
				{
					currentLine = currentLine + " <td>" + rset.getString(i) + "</td>";
				}
				currentLine = currentLine + " </tr>";
				System.out.println(currentLine);
			}
			System.out.println("</table>");
		}
		catch (SQLException sqe)
		{
			System.out.println("There was an error in trying to read the result");
			System.out.println("There error was:");
			System.out.println(sqe);
		}
	}
	
	public static void toJSON(ResultSet rset)
	{
		System.out.println("Came here");
		try
		{
			String totalLines = "";
			ResultSetMetaData rsmd = rset.getMetaData();
			int numColumns = rsmd.getColumnCount();
			
			String[] columnNames = new String[numColumns];
			int[] columnTypes = new int[numColumns];
			String currentLine = "{header: [";
			for (int i = 1 ; i <= numColumns ; i ++)
			{
				currentLine = currentLine + "\"" + rsmd.getColumnName(i) + "\"";
				if (i < numColumns)
				{
					currentLine = currentLine + ",";
				}
				columnNames[i - 1] = rsmd.getColumnName(i);
				columnTypes[i - 1] = rsmd.getColumnType(i);
			}
			currentLine = currentLine + "],";
			System.out.println(currentLine);
			currentLine = "data: [";
			
			while(rset.next())
			{
				if (currentLine.equals(""))
				{
					currentLine = currentLine + ",\n\t";
				}
				currentLine = currentLine + "{";
				for (int i = 1 ; i <= numColumns ; i ++)
				{
					currentLine = currentLine + columnNames[i - 1] + ":";
					if (columnTypes[i-1] == -1 || columnTypes[i-1] == 1 || columnTypes[i-1] == 12)
					{
						currentLine = currentLine + "\"";
					}
					currentLine = currentLine + rset.getString(i);
					if (columnTypes[i-1] == -1 || columnTypes[i-1] == 1 || columnTypes[i-1] == 12)
					{
						currentLine = currentLine + "\"";
					}
					if (i < numColumns)
					{
						currentLine = currentLine + ", ";
					}
				}
				currentLine = currentLine + " }";
				//System.out.println(currentLine);
				totalLines = totalLines + currentLine;
				currentLine = "";
			}
			System.out.println(totalLines);
			System.out.println("\t]\n}");
		}
		catch (SQLException sqe)
		{
			System.out.println("There was an error in trying to read the result");
			System.out.println("There error was:");
			System.out.println(sqe);
		}
	}
}


public class lab4b_part1 {
	public static void main(String[] args) {
		 
		try (
		    Connection conn = DriverManager.getConnection(
		    		"jdbc:postgresql://localhost:5050/postgres", "sahil", "");
		    Statement stmt = conn.createStatement();
		)
		{
			ResultSetOutput rso = new ResultSetOutput();
			Scanner input = new Scanner(System.in);
			String query = "";//input.nextLine();
			query = "select * from student";
			ResultSet rset;
			try
			{
				rset = stmt.executeQuery(query);
				//rso.toHTML(rset);
				rso.toJSON(rset);
			}
			catch( SQLException sqle )
			{
				System.out.println("The query could not be executed.\n The error was as follows:");
				System.out.println(sqle);
			}
		}
		catch (Exception sqle)
		{
		System.out.println("Exception : " + sqle);
		}
	}
}



