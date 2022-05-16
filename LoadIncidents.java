import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * This program loads incident data from a .csv
 * 
 * Use from cmd line, either pass the path as an argument or after it prompts you.
 * 
 * The connection code is mostly copied from the MySQL documentation
 * 
 * Code is in a lot of strongly coupled methods mostly so that each method only
 * takes in what it needs.
 * 
 * @author Donovan Pate
 * 
 * @see https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-connect-drivermanager.html
 * @see https://stackoverflow.com/questions/2839321/connect-java-to-a-mysql-database
 */

public class LoadIncidents {
	/**
	 * The main method that runs and controls this console app
	 * 
	 * @param args the arguments passed at the command line. Not designed for any.
	 */
	public static void main (String[] args) throws SQLException{
		
		Connection conn = connectToDB();
		try{
			Statement stmt = conn.createStatement();
			System.out.println("Selecting incidents database. Will create it not exists");
			stmt.execute("CREATE DATABASE IF NOT EXISTS `incidents`;");
			stmt.execute("USE `incidents`;");
			if(!stmt.executeQuery("SHOW TABLES").next()){
				System.out.println("No Tables found in incidents database. Creating table");
				createIncidentTable(stmt);
			}
		} catch (Exception e){
			System.out.println(e.toString());
		}
		
		Scanner console = new Scanner(System.in);
		System.out.println("What is the path to Load incidents from? (enter 'exit' to close)");
		String csvPath = console.nextLine();
		
		while(!csvPath.equals("exit")){
			try{
				loadFromCSV(csvPath, conn);
			} catch (Exception e){
				System.out.println(e.toString());
				e.printStackTrace();
			}
			System.out.println("What is the path to Load incidents from? (enter 'exit' to close)");
			csvPath = console.nextLine();
		}
		console.close();
		conn.close();
		System.out.println("Safely closed connection");
	}
	
	/**
	 * This method establishes the connection to the database if it is
	 * configured per the installation instructions (My work one will 
	 * use kerberos authentication, but that's something I can figure out
	 * later)
	 * 
	 * @return the connection variable for the successful connection
	 * @see https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-connect-drivermanager.html
	 */
	public static Connection connectToDB(){
		Connection conn = null;
		try{
			String user = "root";
			String password = "ThisPasswordIsSecureISwear";
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/?",user,password);
			
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception e){
			System.out.println("Exception: " + e.toString());
		}
		
		if(conn == null){
			System.out.println("No Connection was able to be created. Can you verify your root password matches the installation instructions?");
			return null;
		} else {
			System.out.println("Connection Secured!");
			return conn;
		}
	}
	
	/**
	 * Method which stores the code for generating the incidents table if it doesnt exist
	 * The definitions for each column are taken to match exactly the source of the incident data in max
	 * length and structure. Dates are stored as strings. 
	 * 
	 * Some dummy data dates seem to have been put into a weird format by Excel.
	 * 
	 * The actual data Dates are all nicely readable strings.
	 * 
	 * @param stmt a Statement for the connection on which to create the table
	 * @see https://dev.mysql.com/doc/refman/8.0/en/creating-tables.html
	 */
	public static void createIncidentTable(Statement stmt){
		String createTable = "CREATE TABLE `incident_table` (\n" +
							"`ID` int(22) NOT NULL AUTO_INCREMENT,\n" + //IME, best practice to not have the primary key based on the primary from another system
							"`created` varchar(40) DEFAULT NULL,\n" + 
							"`number` varchar(40) NOT NULL,\n" + 
							"`state` varchar(40) DEFAULT NULL,\n" + 
							"`short_description` varchar(200) DEFAULT NULL,\n" + 
							"`caller_email` varchar(200) DEFAULT NULL,\n" + 
							"`comments` text(4000) DEFAULT NULL,\n" + 
							"`category` varchar(100) DEFAULT NULL,\n" + 
							"`city` varchar(100) DEFAULT NULL,\n" + 
							"`close_code` varchar(40) DEFAULT NULL,\n" + 
							"`close_notes` varchar(4000) DEFAULT NULL,\n" + 
							"`closed_at` varchar(40) DEFAULT NULL,\n" + 
							"`comments_and_work_notes` text(4000) DEFAULT NULL,\n" + 
							"`description` text(4000) DEFAULT NULL,\n" + 
							"`made_sla` tinyint(1) DEFAULT 1,\n" + 
							"`resolved_date` varchar(40) DEFAULT NULL,\n" + 
							"`location_state` varchar(40) DEFAULT NULL,\n" + 
							"`subcategory` varchar(100) DEFAULT NULL,\n" + 
							"`last_updated` varchar(40) DEFAULT NULL,\n" + 
							"`update_count` int DEFAULT 0,\n" + 
							"`updated_by` varchar(200) DEFAULT NULL,\n" + 
							"`work_notes` text(4000) DEFAULT NULL,\n" + 
							"`watch_list` varchar(200) DEFAULT NULL,\n" + 
							"`assigned_email` varchar(200) DEFAULT NULL,\n" + 
							"`assignment_group` varchar(80) DEFAULT NULL,\n" + 
							"`closer_email` varchar(200) DEFAULT NULL,\n" + 
							"`configuration_item` varchar(200) DEFAULT NULL,\n" + 
							"`opener_email` varchar(200) DEFAULT NULL,\n" + 
							"`resolver_email` varchar(40) DEFAULT NULL,\n" + 
							"`keywords` text(18000) DEFAULT NULL,\n" + 
							"`attachments_list` text(4000) DEFAULT NULL,\n" + 
							"PRIMARY KEY (`ID`)\n" + 
							");";
		try{
			stmt.execute(createTable);
			System.out.println("Table Created!");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return;
	}
	
	private static void loadFromCSV(String csvPath, Connection conn) throws Exception{
		String columns = "";
		String incidentRow = "";
		File incidentCsv = new File(csvPath);
		
		if(!incidentCsv.exists()){
			return;
		}
		
		Scanner csvScan = new Scanner(incidentCsv);
		columns = csvScan.nextLine();
		columns = columns.substring(4,columns.length()-1);
		
		// see https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
		// This helped me figure out 
		
		csvScan.useDelimiter("\"\n\"");
		while(csvScan.hasNext()){
			incidentRow = csvScan.next();
			incidentRow = incidentRow.substring(1,incidentRow.length()-1);
			Incident current = new Incident(incidentRow, columns);
			try{
				Statement stmt = conn.createStatement();
				stmt.execute(current.getSQLInsert());
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		csvScan.close();
	}
	
}

