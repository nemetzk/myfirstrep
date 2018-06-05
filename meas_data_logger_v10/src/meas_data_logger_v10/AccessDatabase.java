package meas_data_logger_v10;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

 
public class AccessDatabase {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    
    String myTable = null;
    
	public AccessDatabase(String msAccDB) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        }
        catch(ClassNotFoundException cnfex) {
 
            System.out.println("Problem in loading or "
                    + "registering MS Access JDBC driver");
            cnfex.printStackTrace();
        }
 
        // Step 2: Opening database connection
        try {	 
        	String dbURL = "jdbc:ucanaccess://" + msAccDB; 
            // Step 2.A: Create and get connection using DriverManager class
            connection = DriverManager.getConnection(dbURL); 
            // Step 2.B: Creating JDBC Statement 
            statement = connection.createStatement();
            // Step 2.C: Executing SQL &amp; retrieve data into ResultSet
            //System.out.println("ID\tName\t\t\tAge\tMatches");
            //System.out.println("==\t================\t===\t=======");  
         // Step 2.D: Adding data to database
            //statement.execute("INSERT INTO tblEmployees (strLastName,strFirstName,strTitle)  VALUES  ('Nemet', 'Krisztian','engineer')");     
            // processing returned data and printing into console
            //resultSet = statement.executeQuery("SELECT * FROM tblEmployees");
            /*
            while(resultSet.next()) {
                System.out.println(resultSet.getInt(1) + "\t" + 
                		resultSet.getString(2) + "\t" + 
                        resultSet.getString(3) + "\t" +
                        resultSet.getString(4));
            }*/
        }
        catch(SQLException sqlex){
            sqlex.printStackTrace();
        }
        finally { 
            // Step 3: Closing database connection
            /*
        	try {
                if(null != connection) {
                    // cleanup resources, once after processing
                    resultSet.close();
                    statement.close();
                    // and then finally close connection
                    connection.close();
                }
            }
            catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }*/
        }
    } //constructor
	
	public AccessDatabase(String msAccDB, String myTableFunHead) {
		this(msAccDB);
		myTable=myTableFunHead;
		
	}
	
	
	
	public void runStatement(String statementText)
	//example statementText: "INSERT INTO tblEmployees (strLastName,strFirstName,strTitle)  VALUES  ('Nemet', 'Krisztian','engineer')" 
	{
	
		try {
			statement.execute(statementText);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} //runStatement
	
	public int[] requestRowByIdx(String tableName, int rowIdxFunHead) {
		int[] myInnerDataArray = new int[254];
		
		try {
			//figyelem hiba itt kell megkeresni a helyes sql kifejezest es tombbe pakolast!
			resultSet = statement.executeQuery("SELECT * FROM "+tableName+ " WHERE measId ="+String.valueOf(rowIdxFunHead) );
			int i = 0;
			while(resultSet.next()) {
				if (i>0) {
					myInnerDataArray[i]=resultSet.getInt(i);
				}
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return myInnerDataArray;
	}
	
	public void insertRow(int databaseRowIdFunHead,String fieldArrayFunHead[], int valueArrayFunHead[]) {
		//Example insertRow({strLastName,strFirstName,strTitle},{'Nemet', 'Krisztian','engineer'});
		String fieldString="";
		String valueString="";
		String commandString = "INSERT INTO " + myTable;
		int max_data_idx = 0;
		int i;
		
		if (valueArrayFunHead.length>250) {//@@ ez 254 volt
			max_data_idx=250;
			
		}
		
		for (i=3;i<max_data_idx;i++) {
			if (i!=3) fieldString+=",";
			fieldString+= fieldArrayFunHead[i]; 
		}
		
		for (i=3;i<max_data_idx;i++) {
			
			if (i!=3) {
				valueString+=",";
				valueString += String.valueOf(valueArrayFunHead[i]);
			}
			else {
				valueString=String.valueOf(databaseRowIdFunHead);
			}
			 
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String myDate = dateFormat.format(date);
		
		DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
		String myTime = dateFormatTime.format(date);
		
		 System.out.println(commandString+" (" + "datum," + "ido," + fieldString + ") " + "VALUES" + " (#" + myDate+"#,#"+myTime+"#,"+ valueString + ")");
		       runStatement(commandString+" (" + "datum," + "ido," + fieldString + ") " + "VALUES" + " (#" + myDate+"#,#"+myTime+"#,"+ valueString + ")");//@@ ide csempesztem be a datumot meg az idot
	}
	
	
	public ResultSet runStatementResultSet(String statementText)
	// "SELECT * FROM tblEmployees"
	{
	
		try {
			resultSet = statement.executeQuery(statementText);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	} //runStatementResultSet
	
	public void printResultSet(ResultSet inputResultSet) 
	{
		try {
			while(inputResultSet.next()) {
			    System.out.println(inputResultSet.getInt(1) + "\t" + 
			    		inputResultSet.getString(2) + "\t" + 
			    		inputResultSet.getString(3) + "\t" +
			    		inputResultSet.getString(4));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

} //class


