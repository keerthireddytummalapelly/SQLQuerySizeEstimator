# SQLQuerySizeEstimator

### Project Overview

This program connects to a PostgreSQL database and performs the following operations to analyze the size of a natural join between two specified tables:

### 1. Estimated Join Size
- The program estimates the size of the natural join between the two tables based on metadata and heuristics.
- It uses database statistics, such as the number of rows and attributes, to calculate the estimated join size.
- This estimation provides an approximation of the number of rows resulting from the natural join.


### 2. Actual Join Size
- The program executes a SQL query to perform the natural join between the two tables.
- It calculates the actual size of the resulting table by counting the rows in the join result.
- This represents the true size of the join based on the data in the tables.


### 3. Estimation Error
- The program calculates the difference between the Estimated Join Size and the Actual Join Size:
  \[
  \text{Estimation Error} = \text{Estimated Join Size} - \text{Actual Join Size}
  \]
- Positive Error: Indicates that the estimated size is larger than the actual size (overestimation).
- Negative Error: Indicates that the estimated size is smaller than the actual size (underestimation).


### Key Outputs
The program prints the following results:
1. Estimated Join Size: An approximate number of rows from the join.
2. Actual Join Size: The precise number of rows in the natural join.
3. Estimation Error: A numerical value indicating the accuracy of the estimation.

This program demonstrates practical database operations, including metadata usage, SQL execution, and error analysis, and provides insights into the accuracy of size estimations for join operations.

## Prerequisites

1. Eclipse IDE: Install Eclipse IDE for Java Developers.
2. Java Runtime Environment (JRE): Ensure JRE is installed and configured in Eclipse.
3. PostgreSQL Database: Ensure a PostgreSQL database is running and accessible.
4. PostgreSQL JDBC Driver: The `postgresql-42.6.0.jar` file (included in the repository).

## Steps to Run the Project

### 1. Import the Project into Eclipse

1. Open Eclipse.
2. Go to File > Import.
3. Select Existing Projects into Workspace under General.
4. Click Next.
5. Browse to the folder containing the attached Java project and select it.
6. Click Finish to import the project.

### 2. Add the PostgreSQL JDBC Driver

1. Right-click on the imported project folder in the Package Explorer.
2. Select Build Path > Configure Build Path.
3. Go to the Libraries tab.
4. Click Add External JARs.
5. Navigate to the provided `postgresql-42.6.0.jar` file and select it.
6. Click Apply and Close.

### 3. Run the Program

1. Locate the `JDBCConnection.java` file in the project.
2. Right-click on `JDBCConnection.java`.
3. Select Run As > Run Configurations.
4. In the Run Configurations window:
   - Select the program under Java Application.
   - Click the Arguments tab.
   - Add the program arguments in the Program Arguments section in the following format:
     ```
     <URL> <TABLENAME1> <TABLENAME2>
     ```

#### Argument Format

- <URL>: Replace `<PORTNUMBER>`, `<DBNAME>`, `<USERNAME>`, and `<PASSWORD>` with your PostgreSQL database details:

  ```
  jdbc:postgresql://localhost:<PORTNUMBER>/<DBNAME>?user=<USERNAME>&password=<PASSWORD>
  ```

- Example:

  ```
  jdbc:postgresql://localhost:5433/uni_KK85?user=postgres&password=<PASSWORD>
  ```

- <TABLENAME1> and <TABLENAME2>: Replace these with the names of the tables in your database.

5. Click Apply and then Run to execute the program.

#### Example Test Cases

![](/TestCase1.png)
![](/TestCase2.png)
![](/TestCase3.png)
![](/TestCase4.png)

Database used: https://www.db-book.com/university-lab-dir/sample_tables-dir/

## Troubleshooting

- Database Connectivity: Ensure the PostgreSQL server is running and accessible.
- JAR File Issues: Verify the `postgresql-42.6.0.jar` is correctly added to the build path.
- Arguments: Double-check the format and content of the program arguments.
