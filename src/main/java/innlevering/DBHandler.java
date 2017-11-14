package innlevering;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Kleppa on 12/09/2017.
 * Handles interaction with database
 */
public class DBHandler {
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

    public DBHandler() {
        dbConnector = new DBConnector();
        readPropertyPathAndSendToInputHandler();
        contentArrayList = inputHandler.getObjectList();
        fillDataBaseWithTablesFromContentArray();
        addColoumns();
        fillTablesWithRowContent();
        //Room.OrmUsage();
        System.out.println("Backend is ready without any issues");
    }

    public void fillDataBaseWithTablesFromContentArray() {
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
            int i = 0;

            for (DatabaseContent dbc : contentArrayList) {

                tableName = contentArrayList.get(i).getClass().toString().substring(tableName.lastIndexOf(".") + 1);
                try {
                    DatabaseMetaData md = con.getMetaData();
                    ResultSet rs = md.getTables(null, null, tableName, null);

                    {
                        String querySql = "SELECT COUNT(*) AS cols\n" +
                                "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                                "WHERE table_schema = '" + properties.getProperty("databasename") + "'\n" +
                                "  AND table_name = '" + tableName + "';";
                        gettingColCount = con.prepareStatement(querySql);
						System.out.println(gettingColCount);
						ResultSet rsCol = gettingColCount.executeQuery();
						rsCol.next();

                        if (rs.next()) {

                            PreparedStatement preparedStatement = con.prepareStatement(
                                    "ALTER TABLE " + tableName + " ADD(" + (contentArrayList.get(i).getColsAndDataTypes() + ");"));

							System.out.println("@@@@@@@@@@@@@@@@@@@@@"+preparedStatement);
							preparedStatement.execute();
                        } else {
                            if (!dontSpamMsg) {
                                System.out.println("Tried to duplicate columns");
                                dontSpamMsg = true;
                            }
                        }
                        i++;
                    }
                } catch (SQLException e) {
                    System.out.println("Catched an sql error");
                }
            }
        } catch (SQLException e) {
            System.out.println("There was an issue establish a connection to db");
        }
    }

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
            if (isPartOfTable(coloumnToIdentifyRowWith,tableName)) {
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
                        }
                    }
                }
            }else{
                System.out.println("Your identifier could not be found in the table.");
            }
        } catch (SQLException e) {
            System.out.println("We had an issue getting your sql query.");
        }
    }

    private boolean isPartOfTable(String targetName,String table) {
        if (getColoumns(table).contains(targetName))
            return true;

        return false;
    }

    private boolean isIdentifiersEmpty(String identifier, String coloumnToIdentifyRowWith) {
        if (identifier.isEmpty() || coloumnToIdentifyRowWith.isEmpty()) {
            return true;
        }
        return false;
    }

    private int getColoumnCount(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        return resultSetMetaData.getColumnCount();
    }

    /**
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
            System.out.println("We could not establish a connection");
        }
        return null;
    }

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
                    stringBuilder.append(rsmd.getColumnName(i) + " : " + columnValue + "\n");

                    System.out.print(rsmd.getColumnName(i) + " : " + columnValue);
                    counter++;
                }
                System.out.println("\n----------------------------------------------------------------------------------------------------------");
            }
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
				System.out.println("Did not manage to open a connenction");
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
                int userChoose = Integer.parseInt(userInput);
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
				String userChooseRow =sql;
				String rowValue =col;
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

}
// TODO: bygg med maven #viktig