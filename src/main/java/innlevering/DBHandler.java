package innlevering;

import com.sun.org.apache.regexp.internal.RE;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;


/**
 * Created by Kleppa on 12/09/2017.
 * Handles interaction with database
 */
public class DBHandler {
    private ArrayList<DatabaseContent> contentArrayList;

    public String getSb() {
        return sb.toString();
    }

    public void setSb(StringBuilder sb) {
        this.sb = sb;
    }

    private StringBuilder sb= new StringBuilder();
    private Scanner scanner=new Scanner(System.in);
    private InputHandler input;
    private DBConnector dbCon;
    Properties newProp = new Properties();
    private FileInputStream inputStream;

    public void readPropertyPathAndSendToInputHandler() {
        try {
            inputStream = new FileInputStream("conf.properties");
            System.out.println(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        if (inputStream != null) {
            try {
                newProp.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            input = new InputHandler(newProp.getProperty("dbcontent"));
        }
    }

    public DBHandler() {
        dbCon = new DBConnector();
        readPropertyPathAndSendToInputHandler();
        contentArrayList = input.getObjectList();
        createTables();
        addColoumns();
        fillTablesWithRowContent();
    }

    public void createTables() {


        String tableName = "Something went wrong";
        int i = 0;


        try (
                Connection con = dbCon.getNewConnection()) {

            DatabaseMetaData md = con.getMetaData();
            try (

                    ResultSet rs = md.getTables(null, null, tableName, null)
            ) {
                con.setCatalog(newProp.getProperty("databasename"));

                while (i < contentArrayList.size()) {
                    tableName = contentArrayList.get(i).getClass().toString();
                    tableName = tableName.substring(tableName.lastIndexOf(".") + 1);

                    if (!rs.next()) {
                        try (PreparedStatement preparedStatement = con.prepareStatement("CREATE TABLE  " + tableName + " (id int AUTO_INCREMENT, CONSTRAINT PK_" + tableName + " PRIMARY KEY (id));")) {
                            preparedStatement.execute();
                        } catch (SQLException e) {
                            System.out.println("Table is already found in Database ");
                        }
                        i++;
                    }
                }
            }
            System.out.println("Tables have been made");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addColoumns() {
        try (Connection con = dbCon.getNewConnection()) {
            PreparedStatement gettingColCount = null;

            String tableName = "Something went wrong";
            int i = 0;

            for (DatabaseContent dbc : contentArrayList) {

                tableName = contentArrayList.get(i).getClass().toString();
                tableName = tableName.substring(tableName.lastIndexOf(".") + 1);

                try {


                    DatabaseMetaData md = con.getMetaData();


                    ResultSet rs = md.getTables(null, null, tableName, null);

                    {


                        String querySql = "SELECT COUNT(*) AS cols\n" +
                                "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                                "WHERE table_schema = '" +newProp.getProperty("databasename")+"'\n" +
                                "  AND table_name = '" + tableName + "';";


                        gettingColCount = con.prepareStatement(querySql);

                        ResultSet rsCol = gettingColCount.executeQuery();
                        rsCol.next();
                        int colsFromDb = rsCol.getInt("cols");


                        if (rs.next() && colsFromDb < 2) {

                            PreparedStatement preparedStatement = con.prepareStatement("ALTER TABLE " + tableName + " ADD(" + (contentArrayList.get(i).getColsAndDataTypes() + ");"));


                            preparedStatement.execute();
                        } else {
                            System.out.println("Table is not found in Database or cant add cols bcus you are trying to duplicating columns ");
                        }
                        i++;


                    }

                } catch (SQLException e) {
                    System.out.println("Catched an sql error");

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void fillTablesWithRowContent() {
        int objectListSize = input.getObjectList().size();


        String tableName = "If this shows Something went wrong";
        for (int i = 0; i < objectListSize; i++) {

            tableName = contentArrayList.get(i).getClass().toString();
            tableName = tableName.substring(tableName.lastIndexOf(".") + 1);

            JSONObject tmpobj = new JSONObject(contentArrayList.get(i));
            String objectToStringedFromObjectList = input.getObjectList().get(i).toString();
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
                    Connection con = dbCon.getNewConnection()) {
                try (
                        PreparedStatement ps = con.prepareStatement("INSERT INTO " + tableName + "(" + coloumnsnames + ") VALUES (" + questionMarks + "); ")) {

                    for (int l = 0; l < values.length; l++) {

                        ps.setString(l + 1, values[l]);
                    }


                    ps.execute();


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
        if (identifier.isEmpty() || coloumnToIdentifyRowWith.isEmpty()) {
            get(tableName, sql);
            return;
            // TODO: 10/10/2017 FIX SPECIFIC GET METHOD
        }

        Properties prop = new Properties();
        String tableNameWithAddedPrefix = prop.getProperty("databasename");
        tableNameWithAddedPrefix += tableName;

        try (Connection con = dbCon.getNewConnection(); PreparedStatement ps = con.prepareStatement("SELECT " + sql.trim() + " FROM " + tableNameWithAddedPrefix.trim() + " WHERE " + coloumnToIdentifyRowWith + "  = " + "?" + " ;")) {


            ps.setString(1, identifier.trim());

            if (sql.toLowerCase().contains("drop") || sql.toLowerCase().contains(";--") || tableNameWithAddedPrefix.toLowerCase().contains("drop") || tableNameWithAddedPrefix.toLowerCase().contains(";--")) {
                System.out.println("wtf are you doing");
            } else {


                try (ResultSet rs = ps.executeQuery()) {

                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    if (columnCount == 1) {
                        String row = "";
                        if (rs.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                row += rsmd.getColumnName(i) + " : " + rs.getString(i) + ", ";

                            }
                            sb.append(row);
                            System.out.println(row);
                        }
                    } else {


                        while (rs.next()) {
                            String row = "";
                            for (int i = 1; i <= columnCount; i++) {
                                row += rsmd.getColumnName(i) + " : " + rs.getString(i) + ", ";

                            }
                            sb.append(row);
                            System.out.println(row);
                        }

                    }

                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param tableName String from what query are you querying
     * @param sql       String sql statement to send with the query
     */
    public ResultSet get(String tableName, String sql) {


        String newName = "";
        if (!sql.equals("information_schema.tables")) {
            newName = "Westerdals_Schedual_Maker.";
        }

        newName += tableName;

        try (
                Connection con = dbCon.getNewConnection()

        ) {
            if (!sql.equals("information_schema.tables")) {
                try (PreparedStatement ps = con.prepareStatement("SELECT " + sql + " FROM " +newName  + " ;")) {

                    try (ResultSet rs = ps.executeQuery()) {
                        printContent(rs);
                        return rs;
                    }
                }
            } else {
                try (PreparedStatement ps = con.prepareStatement("SELECT " + newName + " FROM " + sql + "  WHERE TABLE_SCHEMA='Westerdals_Schedual_Maker';");ResultSet rs = ps.executeQuery()) {

                    printContent(rs);
                    return rs;
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private StringBuilder printContent(ResultSet rs) {
         sb= new StringBuilder();
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
                    sb.append(rsmd.getColumnName(i) + " : " + columnValue+"\n");

                    System.out.print(rsmd.getColumnName(i) + " : " + columnValue);

                    counter++;
                }
                System.out.println("\n----------------------------------------------------------------------------------------------------------");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb;
    }

    /**
     * Takes userinput and builds a query to delete someth
     */
    public void dropFromDatabase() {
        //  DELETE FROM table_name WHERE some_column IS NULL;


        try (Connection con = dbCon.getNewConnection();
             PreparedStatement ps = con.prepareStatement("SELECT table_name FROM information_schema.tables  WHERE TABLE_SCHEMA='Westerdals_Schedual_Maker';");
             ResultSet rs = ps.executeQuery(); PreparedStatement ps2 = con.prepareStatement("SELECT count(table_name) FROM information_schema.tables WHERE TABLE_SCHEMA='Westerdals_Schedual_Maker';"); ResultSet rs2 = ps2.executeQuery()) {

            String[] table = null;


            ResultSetMetaData rsmd = ps.getMetaData();
            if (rs2.next())
                System.out.println(rs2.getInt(1));
            table = new String[Integer.parseInt(rs2.getString(1))];

            int count = 0;

            while (rs.next()) {


                System.out.println();
                table[count] = rs.getString(1);
                System.out.println(rs.getString(1));
                count++;
            }
            //SELECT table_name FROM information_schema.tables;
            if (table != null) {

                Scanner sc = new Scanner(System.in);

                int x = 0;
                for (String s : table
                        ) {
                    System.out.println(1 + x + " " + table[x++]);

                }
                System.out.println("From what table do you want to delete? please enter a choice");
                String userInput = sc.nextLine();
                int userChoose = Integer.parseInt(userInput);
                System.out.println("What do you want to delete? Coloumn name");
                String userChooseRow = sc.nextLine();
                System.out.println("What do you want to delete? row value");
                String rowValue = sc.nextLine();

                if (!table[userChoose].isEmpty()) {

                    try (PreparedStatement prepStat2 = con.prepareStatement("DELETE FROM " + " Westerdals_Schedual_Maker." + table[userChoose] + " WHERE " + userChooseRow + " = " + rowValue + " LIMIT 1 ;")) {
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


        ResultSet resultSet = get("table_name", "information_schema.tables");
        System.out.println("What table do you want to delete, use its name.");
        String userChoice = scanner.nextLine();
        try(Connection con = dbCon.getNewConnection();PreparedStatement preparedStatement=con.prepareStatement("DROP TABLE "+userChoice+";")){


            preparedStatement.execute();
            System.out.println("Table -  "+userChoice+" has been deleted from the database");
        } catch (SQLException e) {
            System.out.println("The table you tried to find does not exsist or you tried something that is not allowed");
        }

    }

    /**
     * createTable creates a table in current db scheme from userinput.
     */
    public void createTable(){
        System.out.println("What do you want your table to be named?");
        String userinput = scanner.nextLine();
        try (Connection con = dbCon.getNewConnection(); PreparedStatement ps = con.prepareStatement("CREATE TABLE "+userinput+";")){
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
// TODO: 09/10/2017 Properties fil, bygg med maven #viktig
