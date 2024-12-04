package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;

public class JDBCConnection {
	/*
	 * Program to calculate the actual join size, estimated join size and estimation error between two tables
	 * @param args accepts three command line arguments from the user: 1. Database connection URL, 2. Table 1, 3. Table 2
	 */
	public static void main(String[] args) {
		//  Check if the correct number of command line arguments are provided.
		if (args.length != 3) {
            System.out.println("Insufficient number of arguments");
            System.exit(0);     // Exit the program if the arguments are incorrect.
        }
		// Assign the command line arguments to the corresponding variables 
        String url = args[0]; // Database connection url
        String table1 = args[1]; // Name of table 1
        String table2 = args[2]; // Name of table 2
        try {
            Connection connection = DriverManager.getConnection(url);  // Establish a connection to the database, specified by the url
            DatabaseMetaData metaData = connection.getMetaData(); // Create a DatabaseMetaData object to query and obtain information about the database's structure and properties
            Statement statement = connection.createStatement(); // Create a Statement object to execute SQL statements, against the database associated with the connection 
            int estimatedJoinSize = calculateEstimatedJoinSize(statement, metaData, table1, table2); // Invoke the method to compute the estimated join size
            int actualJoinSize = calculateActualJoinSize(statement, table1, table2); // Invoke the method to compute the actual join size
            int estimationError = calculateEstimationError(actualJoinSize, estimatedJoinSize); // Invoke the method to compute the estimation error
            System.out.println("Estimated the size of the natural join between " + table1 + " and " + table2 + " is: " + estimatedJoinSize); // Print the estimated join size
            System.out.println("Actual the size of the natural join between " + table1 + " and " + table2 + " is: " + actualJoinSize); // Print the actual join size
            System.out.println("Estimation error is: " + estimationError); // Print the estimation error
            statement.close(); // Close the Statement object
            connection.close(); // Close the database connection
        }
		// Catches any exception that is caused while trying to open db connection
        catch(SQLException e) {
        	System.out.println(e);
        }
	}
	/*
	 * Method to compute actual join size 
	 * @param statement, instance of an Statement object to execute the SQL queries
	 * @param table1 the name of table 1
	 * @param table2 the name of table 2
	 * @return the actual join size
	 */
	public static int calculateActualJoinSize(Statement statement, String table1, String table2) throws SQLException{
		String query = "select count(*) from " + table1 + " natural join " + table2; // Query to perform join operation between table 1 and table 2
		ResultSet result = statement.executeQuery(query); // Executes the SQL query
		int actualJoinSize = 0; 
		// Fetching the count of records from the resultant of the above query
		if(result != null) {
			result.next();
			actualJoinSize = result.getInt(1);
		}
		result.close(); // Close the ResultSet object
		return actualJoinSize;
	}
	/*
	 * Method to compute estimated join size 
	 * @param statement, instance of an Statement to execute the SQL queries
	 * @param metaData, instance of DatabaseMetaData class to query and obtain information about the database's structure and properties
	 * @param table1  the name of table 1
	 * @param table2 the name of table 2
	 * @return the estimated join size based on calculations
	 */
	public static int calculateEstimatedJoinSize(Statement statement, DatabaseMetaData metaData, String table1, String table2) throws SQLException{
		int table1_size = getTableSize(statement, table1); // Invoke the method to compute the number of records in table 1
		ArrayList<String> table1_columns = getColumns(metaData, table1); // Invoke the method to fetch the column names in table 1
		int table2_size = getTableSize(statement, table2); // Invoke the method to compute the number of records in table 2
		ArrayList<String> table2_columns = getColumns(metaData, table2); // Invoke the method to fetch the column names in table 2
		ArrayList<String> common_columns = new ArrayList<>(); 
		int estimatedJoinSize = 0;
		// Finding the common column names between table 1 and table 2
        for (String columnName : table1_columns) {
            if (table2_columns.contains(columnName)) {
                common_columns.add(columnName);
            }
        }
		// Calculating estimated join size, if there are columns in common.
        if(common_columns.size() != 0) {
        	// Concatenate the common column names to an empty string
        	String columns = "";
        	for (int i = 0; i < common_columns.size(); i++) {
        	    String columnName = common_columns.get(i);
        	    columns += columnName;
        	    if (i < common_columns.size() - 1) {
        	        columns += ", ";
        	    }
        	}   	
	        String query1 = "select count(*) from (select distinct " + columns + " from " + table1 + ")"; // Query to select the distinct number of tuples from table 1     
			ResultSet result1 = statement.executeQuery(query1); // Executes the SQL query using the Statement object 
			int table1_commonColumnsSize = 0;
			// Fetch the count value from the resultant of the above query
			if(result1 != null) {
				result1.next();
				table1_commonColumnsSize = result1.getInt(1);
			}
			result1.close();

			String query2 = "select count(*) from (select distinct " + columns + " from " + table2 + ")"; // Query to select the distinct number of tuples from table 2
			ResultSet result2 = statement.executeQuery(query2); // Executes the SQL query using the Statement object 
			int table2_commonColumnsSize = 0;
			// Fetch the count value from the resultant of the above query
			if(result2 != null) {
				result2.next();
				table2_commonColumnsSize = result2.getInt(1);
			}
			result2.close();
			// From the two tables, identify the table with larger size.
			int maxCount = table1_commonColumnsSize > table2_commonColumnsSize ? table1_commonColumnsSize : table2_commonColumnsSize;
			estimatedJoinSize = (table1_size * table2_size)/maxCount;
        }
		// Return table1_size * table2_size, if there are no common columns 
        else {
        	estimatedJoinSize = (table1_size * table2_size);
        }
		return estimatedJoinSize ;
		
	}
	/*
	 * Method to fetch the number of records in a table
	 * @param statement, instance of an Statement to execute the SQL queries
	 * @param table  the name of table
	 * @return the number of records in the table
	 */
	public static int getTableSize(Statement statement, String table) throws SQLException{
		String query = "select count(*) from " + table; // Query to count the number of records in a table
		ResultSet result = statement.executeQuery(query); // Executes the SQL query using the Statement object
		int tableSize = 0;
		if(result != null) {
			result.next();
			tableSize = result.getInt(1);
		}
		result.close(); // Close the ResultSet object
		return tableSize; // //Return the table size
	}
	/*
	 * Method to fetch the column names using the table meta data
	 * @param metaData, instance of DatabaseMetaData class to query and obtain information about the database's structure and properties
	 * @param table  the name of table
	 * @return the column names of a table as an ArrayList
	 */
	public static ArrayList<String> getColumns(DatabaseMetaData metaData, String table) throws SQLException {
        ArrayList<String> columns = new ArrayList<String>();
    	ResultSet result = metaData.getColumns(null, null, table, null); //Fetches the columns in a table using the DatabaseMetaData object 
        while (result.next()) {
            String column = result.getString("COLUMN_NAME");
            columns.add(column);
        }
        return columns; //Returning the obtained column names
    }
	/*
	 * Method to compute estimation error
	 * @param actualJoinSize, the actual join size of the tables
	 * @param estimatedJoinSize, the estimates join size of the tables
	 * @return the estimation error
	 */
	public static int calculateEstimationError(int actualJoinSize, int estimatedJoinSize) {
		return estimatedJoinSize - actualJoinSize; // Estimation error is the difference between the estimation join size and actual join size
	}
}
