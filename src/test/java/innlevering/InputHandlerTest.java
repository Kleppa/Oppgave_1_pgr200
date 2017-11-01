package innlevering;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class InputHandlerTest {
    InputHandler newip =new InputHandler();


    @Test
    public void getObjectList() throws Exception {

        ArrayList<innlevering.DatabaseContent> al=newip.getObjectList();
        assertTrue(al!=null);
    }

    @Test
    public void readContent() throws Exception {

        //Relative path does not work.
        newip.readContent("/Users/Kleppa/Documents/Oppgave_1_pgr200/src/test/java/innlevering/testFile.json");
        assertTrue(newip.getObjArrRoom().length()>0);
        assertTrue(newip.getObjArrTeacher().length()>0);
        assertTrue(newip.getObjArrStudentGroups().length()>0);
        assertTrue(newip.getObjArrSubjects().length()>0);

    }
    @Test (expected = NullPointerException.class)
    public void nullPointerNofile(){
        newip.readContent("");

    }
    @Test(expected=NullPointerException.class)
    public void fileNullPointerConstructorTest(){
        InputHandler newip =new InputHandler("testFile.json");
    }
    @Test(expected = NullPointerException.class)
    public void fileNullPointerMethod(){
        newip.readContent("testFile.json");

    }


}