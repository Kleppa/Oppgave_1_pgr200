package innlevering;


import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {
    private static MysqlDataSource ds = new MysqlDataSource();
    private static InputStream inputStream;
    private static Properties props;

    public DBConnector() {

    }

	/**
	 * Sets up a connenction with config file user/name/databse  and returns a connenction
	 * @return new Connection();
	 */

	public Connection getNewConnection() {
        Connection connection=null;
        props= new Properties();
        readProperties();


        ds.setServerName(props.getProperty("host"));
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("password"));



        try (PreparedStatement ps = ds.getConnection()
                .prepareStatement("CREATE DATABASE IF NOT EXISTS " + props.getProperty("databasename") + ";")) {
            ps.execute();
        } catch (SQLException e) {
            System.out.println("Database is already created");
        }

        // Select created table
        ds.setDatabaseName(props.getProperty("databasename"));

        try {
            connection=ds.getConnection();
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Did not manage to open a connection -DBCONNENCTOR");
        return connection;

    }

	/**
	 * reads a config file and loads them to property object
	 *
	 */

	private void readProperties() {
        try {
            inputStream = new FileInputStream("conf.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (inputStream != null) {
            try {
                props.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}