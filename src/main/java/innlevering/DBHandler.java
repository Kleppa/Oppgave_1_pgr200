package innlevering;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by Kleppa on 12/09/2017.
 * Handles interaction with database
 */
public class DBHandler {
	private boolean hackToFixaBugThatSuddenlyAppereadBeforeDeadline = false;

	public void setSbNull() {
		stringBuilder.delete(0, stringBuilder.length());
	}

	private int rowCount = 0;
	private ArrayList<DatabaseContent> contentArrayList;

	public String getStringBuilderAsString() {
		return stringBuilder.toString();
	}

	public void setStringBuilder(StringBuilder stringBuilder) {
		this.stringBuilder = stringBuilder;
	}

	private StringBuilder stringBuilder = new StringBuilder();
	private Scanner scanner = new Scanner(System.in);
	private InputHandler inputHandler;
	private DBConnector dbConnector;
	private Properties properties = new Properties();
	private FileInputStream inputStream;

	public void readPropertyPathAndSendToInputHandler() {
		try {
			inputStream = new FileInputStream("conf.properties");
		} catch (FileNotFoundException e) {
			System.out.println("We did not manage to find the config file");
		}

		loadProperties();

		inputHandler = new InputHandler(properties.getProperty("dbcontent"));
	}

	private void loadProperties() {
		if (inputStream != null) {
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				System.out.println("Could not load properties from inputStream, see loadproperties()");
			}
		}
	}

	/**
	 * Runs the setup
	 */
	public DBHandler() {
		dbConnector = new DBConnector();
		readPropertyPathAndSendToInputHandler();
		contentArrayList = inputHandler.getObjectList();
		fillDataBaseWithTablesFromContentArray();
		addColoumns();
		fillTablesWithRowContent();
		System.out.println("Backend is ready without any issues");
	}

	/**
	 * Creates tables from contentArrayList
	 */
	private void fillDataBaseWithTablesFromContentArray() {
		String tableName = "Something went wrong";
		int i = 0;
		try (
				Connection connection = dbConnector.getNewConnection()) {
			connection.setCatalog(properties.getProperty("databasename"));
			while (i < contentArrayList.size()) {
				tableName = contentArrayList.get(i).getClass().toString();
				tableName = tableName.substring(tableName.lastIndexOf(".") + 1);
				createTable(tableName);
				i++;
			}
		} catch (SQLException e) {
			System.out.println("Could not open a connenction");
		}
	}

	/**
	 * Creates coloumns from contentArrayList
	 */

	public void addColoumns() {
		boolean dontSpamMsg = false;
		try (Connection con = dbConnector.getNewConnection()) {
			PreparedStatement gettingColCount = null;
			String tableName = "Something went wrong";

			for (int i = 0; i < contentArrayList.size(); i++) {

				tableName = contentArrayList.get(i).getClass().toString().substring(tableName.lastIndexOf(".") + 1);
				System.out.println(contentArrayList.size());
				if (tableName.contains("class innlevering.")) {
					tableName = tableName.substring(tableName.lastIndexOf(".") + 1);

				}
				try {

					{
						System.out.println(tableName + "THIS IS THE TABLENAME");


						if (getTableNames().contains(tableName)) {

							PreparedStatement preparedStatement = con.prepareStatement(
									"ALTER TABLE " + tableName + " ADD(" + (contentArrayList.get(i).getColsAndDataTypes() + ");"));

							preparedStatement.execute();

						} else {
							if (!dontSpamMsg) {
								System.out.println("Tried to duplicate columns");
								dontSpamMsg = true;
							}
						}

					}
				} catch (SQLException e) {
					System.out.println("Catched an sql error");
				}
			}
		} catch (SQLException e) {
			System.out.println("There was an issue establish a connection to db");
		}
	}

	/**
	 * Fills database with content from the contentArrayList
	 */
	public void fillTablesWithRowContent() {
		int objectListSize = inputHandler.getObjectList().size();


		String tableName = "If this shows Something went wrong";
		for (int i = 0; i < objectListSize; i++) {

			tableName = contentArrayList.get(i).getClass().toString();
			tableName = tableName.substring(tableName.lastIndexOf(".") + 1);

			JSONObject tmpobj;
			String objectToStringedFromObjectList = inputHandler.getObjectList().get(i).toString();
			//Ugliest String Ive ever made, but it gets the job done, it makes sure that the text only starts from where the needed json object is.
			String splitOutJsonObj = (objectToStringedFromObjectList.substring(objectToStringedFromObjectList.lastIndexOf(tableName) + tableName.length()));
			tmpobj = new JSONObject(splitOutJsonObj);
			String coloumnsnames = "";

			String questionMarks = "";
			String[] values = new String[tmpobj.names().length()];
			boolean arrayused = false;
			for (int x = 0; x < values.length; x++) {
				if (x < values.length - 1) {
					coloumnsnames += tmpobj.names().getString(x) + ",";
				} else {
					coloumnsnames += tmpobj.names().getString(x);
				}

				if (tmpobj.has("subjectList") && !tmpobj.isNull("subjectList") && !arrayused) {
					JSONArray jsAr = tmpobj.getJSONArray("subjectList");
					values[x] = "Subjects";
					arrayused = true;
					if (x < values.length - 1) {
						questionMarks += "?,";
					} else {
						questionMarks += "?";
					}

				} else {
					values[x] = tmpobj.get(tmpobj.names().getString(x)).toString();

					if (x < values.length - 1) {
						questionMarks += "?,";
					} else {
						questionMarks += "?";
					}
				}
			}
			try (
					Connection connection = dbConnector.getNewConnection()) {
				try (
						PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + tableName + "(" + coloumnsnames + ") VALUES (" + questionMarks + "); ")) {

					for (int l = 0; l < values.length; l++) {
						preparedStatement.setString(l + 1, values[l]);
					}
					System.out.println(preparedStatement);

					preparedStatement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param tableName                String from what query are you querying
	 * @param sql                      String sql statement to send with the query
	 * @param coloumnToIdentifyRowWith String sql coloumns u want
	 * @param identifier               String for identifying specific rows.
	 *                                 sql injection vulnerability
	 */

	public void get(String tableName, String sql, String coloumnToIdentifyRowWith, String identifier) {
		if (isIdentifiersEmpty(identifier, coloumnToIdentifyRowWith)) {
			System.out.println("All variables not filled in, therefor sending task to get(tableName,Sql)");
			get(tableName, sql);
		}
		String tableNameWithAddedPrefix = properties.getProperty("databasename") + "." + tableName;
		try (Connection connection = dbConnector.getNewConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + sql.trim() + " FROM " + tableNameWithAddedPrefix.trim() + " WHERE " + coloumnToIdentifyRowWith + "  = " + "?" + " ;")) {
			preparedStatement.setString(1, identifier.trim());
			if (isPartOfTable(coloumnToIdentifyRowWith, tableName)) {
				if (sql.toLowerCase().contains("drop") || sql.toLowerCase().contains(";--") || tableNameWithAddedPrefix.toLowerCase().contains("drop") || tableNameWithAddedPrefix.toLowerCase().contains(";--")) {
					System.out.println("wtf are you doing");
				} else {

					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						//System.out.println(resultSet.getString(0));
						ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
						if (getColoumnCount(resultSet) == 1) {
							String row = "";
							if (resultSet.next()) {
								for (int i = 1; i <= getColoumnCount(resultSet); i++)
									row += resultSetMetaData.getColumnName(i) + " : " + resultSet.getString(i) + ", ";

								stringBuilder.append(row);
								rowCount++;
								System.out.println(row);
							}
							stringBuilder.append("done");
						} else {
							while (resultSet.next()) {
								String row = "";
								for (int i = 1; i <= getColoumnCount(resultSet); i++) {
									row += resultSetMetaData.getColumnName(i) + " : " + resultSet.getString(i) + ", ";
								}
								rowCount++;
								stringBuilder.append(row);
								System.out.println(row);
							}
							stringBuilder.append("done");
						}
					}
				}
			} else {
				System.out.println("Your identifier could not be found in the table.");
			}
		} catch (SQLException e) {
			System.out.println("We had an issue getting your sql query.");
		}
	}

	/**
	 * Checks if coloumn is part of table
	 * @param targetName
	 * @param table
	 * @return
	 */
	private boolean isPartOfTable(String targetName, String table) {
		if (getColoumns(table).contains(targetName))
			return true;

		return false;
	}

	/**
	 * Checks if the identifiers is empty
	 * @param identifier
	 * @param coloumnToIdentifyRowWith
	 * @return
	 */
	private boolean isIdentifiersEmpty(String identifier, String coloumnToIdentifyRowWith) {
		if (identifier.isEmpty() || coloumnToIdentifyRowWith.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 *  Gets and returns coloumn count for a table
	 * @param resultSet
	 * @return number of cols
	 * @throws SQLException
	 */
	private int getColoumnCount(ResultSet resultSet) throws SQLException {
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		return resultSetMetaData.getColumnCount();
	}

	/**
	 * Get from databse
	 * @param tableName String from what query are you querying
	 * @param sql       String sql statement to send with the query
	 */
	public ResultSet get(String tableName, String sql) {

		String newName = tableName;
		try (
				Connection con = dbConnector.getNewConnection()

		) {
			if (!getTableNames().contains(tableName)) {
				System.out.println("We do not have a table with that name");
				return null;
			}

			if (!sql.equals("information_schema.tables")) {
				try (PreparedStatement ps = con.prepareStatement("SELECT " + sql + " FROM " + newName + " ;")) {

					try (ResultSet rs = ps.executeQuery()) {
						printContent(rs);
						return rs;
					}
				}
			} else {
				try (PreparedStatement ps = con.prepareStatement("SELECT " + newName + " FROM " + sql + "  WHERE TABLE_SCHEMA='Westerdals_Schedual_Maker';"); ResultSet rs = ps.executeQuery()) {

					printContent(rs);
					return rs;
				} catch (SQLException e) {
					System.out.println("Your sql query is not valid sql");
				}
			}
		} catch (SQLException e) {
			System.out.println("We could not find your coloumn in db");
		}
		return null;
	}

	/**
	 *
	 * @return an Arraylst with table names
	 */
	public ArrayList<String> getTableNames() {
		ArrayList<String> tableNameList = new ArrayList<>();
		try (Connection con = dbConnector.getNewConnection(); PreparedStatement ps =
				con.prepareStatement("SELECT TABLE_NAME FROM information_schema.tables WHERE TABLE_SCHEMA='Westerdals_Schedual_Maker' ;")) {
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next())
				tableNameList.add(resultSet.getString(1));
		} catch (SQLException e) {
			System.out.println("Did not manage to open a connenction");
		}
		return tableNameList;
	}

	/**
	 * prints the result of a query.
	 * @param rs Resultset
	 * @return
	 */
	private StringBuilder printContent(ResultSet rs) {
		stringBuilder = new StringBuilder();
		ResultSetMetaData rsmd = null;
		try {
			rsmd = rs.getMetaData();

			int columnsNumber = rsmd.getColumnCount();
			int counter = 1;

			while (rs.next()) {
				System.out.println("-------------------------------------------- ROW NUMBER " + counter + " : ---------------------------------------------\n");
				for (int i = 1; i <= columnsNumber; i++) {

					if (i > 1) System.out.print(",  ");
					String columnValue = rs.getString(i);
					stringBuilder.append(rsmd.getColumnName(i) + " : " + columnValue +", ");

					System.out.print(rsmd.getColumnName(i) + " : " + columnValue);
					counter++;
				}
				System.out.println("\n----------------------------------------------------------------------------------------------------------");
			}
			stringBuilder.append("done");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stringBuilder;
	}

	/**
	 * Returns an arraylist of coloumn names from chosen table
	 *
	 * @param table specify what table you want coloums from
	 * @return ArrayList with coloumn names
	 */
	public ArrayList<String> getColoumns(String table) {
		if (getTableNames().contains(table)) {
			ArrayList<String> coloumnList = new ArrayList<>();
			try (Connection con = dbConnector.getNewConnection(); PreparedStatement ps =
					con.prepareStatement("select Column_name \n" +
							"from Information_schema.columns \n" +
							"where Table_name like '" + table + "'" +
							";")) {
				ResultSet resultSet = ps.executeQuery();
				while (resultSet.next())
					coloumnList.add(resultSet.getString(1));


			} catch (SQLException e) {
				System.out.println("Did not find that coloumn in table");
			}
			return coloumnList;
		}

		return null;
	}

	/**
	 * Takes userinput and builds a query to delete someth
	 */
	public void dropFromDatabase() {


		try (Connection con = dbConnector.getNewConnection(); PreparedStatement ps = con.prepareStatement("SELECT table_name FROM information_schema.tables  WHERE TABLE_SCHEMA='Westerdals_Schedual_Maker';"); PreparedStatement ps2 = con.prepareStatement("SELECT count(table_name) FROM information_schema.tables WHERE TABLE_SCHEMA='Westerdals_Schedual_Maker';"); ResultSet rs2 = ps2.executeQuery()) {
			ps.executeQuery();
			ArrayList<String> tableNames = getTableNames();
			if (tableNames != null) {
				Scanner sc = new Scanner(System.in);
				getTableNames().forEach(System.out::println);
				System.out.println("From what table do you want to delete? please enter a choice");
				String userInput = sc.nextLine();

				getColoumns(userInput).forEach(System.out::println);
				System.out.println("What do you want to delete? Coloumn name");
				String userChooseRow = sc.nextLine();
				System.out.println("What do you want to delete? Cell value");
				String rowValue = sc.nextLine();

				String value = tableNames.stream().filter(rn -> rn.equals(userInput))
						.findAny().get();

				if (tableNames.contains(userInput)) {

					try (PreparedStatement prepStat2 = con.prepareStatement("DELETE FROM " + " Westerdals_Schedual_Maker." + value + " WHERE " + userChooseRow + " = " + rowValue + " LIMIT 1 ;")) {
						prepStat2.execute();

					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("user choice is empty");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Drops a table that the user chooses.
	 */
	public void dropTable() {

		getTableNames().forEach(System.out::println);
		System.out.println("What table do you want to delete?");
		String userChoice = scanner.nextLine();
		try (Connection con = dbConnector.getNewConnection(); PreparedStatement preparedStatement = con.prepareStatement("DROP TABLE " + userChoice + ";")) {

			preparedStatement.execute();
			System.out.println("Table -  " + userChoice + " has been deleted from the database");
		} catch (SQLException e) {
			System.out.println("The table you tried to find does not exsist or you tried something that is not allowed");
		}
	}

	/**
	 * createTable creates a table in current db scheme from userinput.
	 *
	 * @param tableName
	 */
	public void createTable(String tableName) {
		try (Connection con = dbConnector.getNewConnection(); PreparedStatement preparedStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS  " + tableName + " (id int AUTO_INCREMENT, CONSTRAINT PK_" + tableName + " PRIMARY KEY (id));")) {
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Could not connenct to database");
		}
	}

	//Just made a duplicate for the Server- client version.
	public void dropFromDatabase_Assignement2(String table, String sql, String col) {


		try (Connection con = dbConnector.getNewConnection(); PreparedStatement ps = con.prepareStatement("SELECT table_name FROM information_schema.tables  WHERE TABLE_SCHEMA='Westerdals_Schedual_Maker';"); PreparedStatement ps2 = con.prepareStatement("SELECT count(table_name) FROM information_schema.tables WHERE TABLE_SCHEMA='Westerdals_Schedual_Maker';"); ResultSet rs2 = ps2.executeQuery()) {
			ps.executeQuery();
			ArrayList<String> tableNames = getTableNames();
			if (tableNames != null) {

				String userInput = table;
				String userChooseRow = sql;
				String rowValue = col;
				String value = tableNames.stream().filter(rn -> rn.equals(userInput))
						.findAny().get();

				if (tableNames.contains(userInput)) {

					try (PreparedStatement prepStat2 = con.prepareStatement("DELETE FROM " + " Westerdals_Schedual_Maker." + value + " WHERE " + userChooseRow + " = " + rowValue + " LIMIT 1 ;")) {
						prepStat2.execute();

					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("We could not use your query, please try again");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void userAddsCols() {
		System.out.println("What table do you want to alter?");
		String tableName;
		getTableNames().stream().forEach(System.out::println);
		if (getTableNames().contains(tableName = scanner.nextLine())) {
			try (Connection con = dbConnector.getNewConnection()) {

				System.out.println("What coloumns do you want to add to the table, remember to add sql data type, typeexit to finish adding");
				String userInput = scanner.nextLine();


				StringBuilder colsToAdd = new StringBuilder();

				while (!userInput.equalsIgnoreCase("exit")) {
					System.out.println("Col added");
					colsToAdd.append(userInput);
					userInput = scanner.nextLine();
					if (userInput.equalsIgnoreCase("exit")) {
						continue;
					}
					colsToAdd.append(", ");
				}

				PreparedStatement preparedStatement = con.prepareStatement(
						"ALTER TABLE " + tableName + " ADD(" + (colsToAdd.toString() + ");"));
				System.out.println(preparedStatement);
				preparedStatement.executeUpdate();
				stringBuilder = null;
			} catch (SQLException ex) {
				System.out.println("could not execute your sql");
			}
		} else {
			System.out.println("Could not find table");
		}

	}

	/**
	 * inserts new row to database
	 */
	public void addRow() {
		System.out.println("what table do you want to add rows too");
		getTableNames().forEach(System.out::println);
		String table = scanner.nextLine();

		ArrayList<StringBuilder> sqlList = createColsAndVals(table);
		StringBuilder cols = sqlList.get(0);
		StringBuilder val = sqlList.get(1);


		try (Connection con = dbConnector.getNewConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO " + table + "(" + cols.toString() + ") VALUES (" + val.toString() + ");");
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Could not use your sql query, please try again");
		}
	}

	/**
	 * Preps an ArrayList with cols and values to later add to insert from user
	 * @param tablename tablename to prep cols and valus too
	 * @return returns an Arraylist  with size 2 to index 0 is col names to add and 1 is values to add
	 */
	private ArrayList<StringBuilder> createColsAndVals(String tablename) {
		ArrayList<StringBuilder> stringArrayList = new ArrayList<>();
		getTableNames().forEach(System.out::println);

		if (getTableNames().contains(tablename)) {

			String userinput;
			StringBuilder colsToAdd = new StringBuilder();
			System.out.println("These are the coloumn for the current table");
			getColoumns(tablename).forEach(System.out::println);

			System.out.println("What coloumns do you want to use fill in your new row,  type exit to finish , type continue to start adding values to cols");
			StringBuilder valuesToAdd = new StringBuilder();
			userinput = scanner.nextLine();
			colLoop:
			while (!(userinput).equalsIgnoreCase("continue")) {
				System.out.println("Col added to query");
				colsToAdd.append(userinput);
				userinput = scanner.nextLine();

				if (!userinput.equalsIgnoreCase("continue")) {
					colsToAdd.append(", ");
				} else {
					continue;
				}
			}
			System.out.println("What Value do you want to add, one at the time");
			userinput = scanner.nextLine();
			valueLoop:
			while (!userinput.equalsIgnoreCase("exit")) {
				System.out.println("Val added to query");
				valuesToAdd.append("'"+userinput+"'");
				userinput = scanner.nextLine();
				if (!userinput.equalsIgnoreCase("exit")) {
					valuesToAdd.append(", ");
				} else {
					continue;
				}

			}
			stringArrayList.add(colsToAdd);
			stringArrayList.add(valuesToAdd);
			return stringArrayList;
		}
		return null;
	}
}
// TODO: bygg med maven #viktig