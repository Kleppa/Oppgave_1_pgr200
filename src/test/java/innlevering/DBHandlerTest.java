package innlevering;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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

    @Test
    public void getSbTest(){
        String testVariable=null;
        assertNull(testVariable);
        testVariable=newDbHandler.getStringBuilderAsString();
        assertTrue(!(testVariable==null));

    }
	@Test
	public void testGetColoumns(){
		ArrayList<String>arr=newDbHandler.getColoumns(null);
		assertEquals(arr,null);ArrayList<String>
				arr2=newDbHandler.getColoumns("NotInDatabase");
		assertEquals(arr,null);

	}@Test
	public void testGetColoumnsNotNull(){
		ArrayList<String>arr=newDbHandler.getColoumns("Teacher");
		ArrayList<String>arr1=newDbHandler.getColoumns("Room");
		ArrayList<String>arr2=newDbHandler.getColoumns("Student");
		assertTrue(arr.size()>0);
		assertTrue(arr1.size()>0);
		assertTrue(arr2.size()>0);
	}


}