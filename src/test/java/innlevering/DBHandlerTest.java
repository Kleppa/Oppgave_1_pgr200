package innlevering;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Kleppa on 16/10/2017.
 */
public class DBHandlerTest {
    DBHandler newDbHandler =new DBHandler();
    @Before
    public void setUp() throws Exception {
        DBHandler newDbHandler =new DBHandler();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void readPropertyPathAndSendToInputHandler() throws Exception {

    }

    @Test
    public void createTables() throws Exception {

    }

    @Test
    public void addColoumns() throws Exception {

    }
    @Test
    public void getSbTest(){
        String testVariable=null;
        assertNull(testVariable);
        testVariable=newDbHandler.getSb();
        assertTrue(!(testVariable==null));
    }

    @Test
    public void fillTablesWithRowContent() throws Exception {

    }

    @Test
    public void get() throws Exception {

    }

    @Test
    public void get1() throws Exception {

    }

    @Test
    public void dropFromDatabase() throws Exception {

    }

    @Test
    public void dropTable() throws Exception {

    }

    @Test
    public void createTable() throws Exception {

    }

}