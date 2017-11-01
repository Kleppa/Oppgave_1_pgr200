package innlevering;

import com.mysql.jdbc.exceptions.MySQLNonTransientConnectionException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Connection;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class DBConnectorTest {
    DBConnector dbcon = new DBConnector();

    @Test
    public void connectionNotNull() throws Exception {

        Connection newCon = dbcon.getNewConnection();
        assertNotNull(newCon);


    }
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void tooManyConnectionsError() {

            Connection newCon;
            for (int i = 0; i < 200; i++)
                newCon = dbcon.getNewConnection();
        exception.expect(MySQLNonTransientConnectionException.class);
    }

}