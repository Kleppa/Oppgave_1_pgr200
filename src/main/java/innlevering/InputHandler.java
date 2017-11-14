package innlevering;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.management.AttributeNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Kleppa on 04/09/2017.
 * Class takes a file and creates objects from it.
 */
public class InputHandler {

    private JSONObject obj;

    public JSONArray getObjArrSubjects() {
        return objArrSubjects;
    }

    public JSONArray getObjArrTeacher() {
        return objArrTeacher;
    }

    public JSONArray getObjArrRoom() {
        return objArrRoom;
    }

    public JSONArray getObjArrStudentGroups() {
        return objArrStudentGroups;
    }

    private JSONArray objArrSubjects;
    private JSONArray objArrTeacher;
    private JSONArray objArrRoom;
    private JSONArray objArrStudentGroups;
    private ArrayList<DatabaseContent> objectList = new ArrayList<DatabaseContent>();

    public ArrayList<DatabaseContent> getObjectList() {
        return objectList;
    }

    public InputHandler(String filename) {
        readContent(filename);

    }
    public InputHandler(){

    }

    /**
     * Takes a jsonfile and adds content to correct types of json arrays
     *
     * @param filename path to filename
     */
    public void readContent(String filename) {
        File file;
        Scanner sc = null;

        try {
            file = new File(filename);
            sc = new Scanner(file);

        } catch (FileNotFoundException e) {
            System.out.println(" We could not find the file you wanted to use");
        } catch (NullPointerException e) {
            System.out.println(" Nullpointer Exception is thrown.");

        }
        String fileContent = null;

        if (sc != null) {
            fileContent = sc.nextLine();
            while (sc.hasNext()) {
                fileContent += sc.nextLine();
            }
        }

        if (fileContent != null) {
            obj = new JSONObject(fileContent);
        }


        objArrSubjects = obj.getJSONArray("subjects".trim());
        objArrTeacher = obj.getJSONArray("teachers".trim());
        objArrRoom = obj.getJSONArray("rooms".trim());
        objArrStudentGroups = obj.getJSONArray("students".trim());


        createObjectsFromParsedJsonFile();
    }

    /**
     * Takes jsonarrays and creates objects from it
     */

    public void createObjectsFromParsedJsonFile() {
        createTeachers();
        createRooms();
        createStudent();
        createSubjects();

    }

    /**
     * Creates objects of type teachers from input file
     */

    private void createTeachers() {
        
        JSONObject tmpObj;


        for (int i = 0; i < objArrTeacher.length(); i++) {

            tmpObj = objArrTeacher.getJSONObject(i);


            DatabaseContent tmpTeacher = new Teacher();


            for (Object key : tmpObj.keySet()) {

                //key types
                String keyStr = (String) key;
                Object keyvalue = tmpObj.get(keyStr);
                //Print key and value


                if (keyStr.equalsIgnoreCase("name")) {

                    ((Teacher) tmpTeacher).setName(keyvalue.toString());
                } else if (keyStr.equalsIgnoreCase("availability")) {


                    ((Teacher) tmpTeacher).setAvailability(keyvalue.toString());
                } else if (keyStr.equalsIgnoreCase("ColsAndDataTypes")) {
                    ((Teacher) tmpTeacher).setColsAndDataTypes(keyvalue.toString());
                } else if (keyStr.equalsIgnoreCase("contact_info")) {

                    ((Teacher) tmpTeacher).setContactInfo(keyvalue.toString());
                } else if (keyStr.equalsIgnoreCase("age")) {
                    ((Teacher) tmpTeacher).setAge(keyvalue.toString());
                } else if (keyStr.equalsIgnoreCase("subject")) {

                    for (Object o : ((JSONArray) keyvalue)) {
                        ((Teacher) tmpTeacher).addToList(o.toString());
                    }
                } else {
                    System.out.println("You are attempting to add attributes that Teacher do not have");
                }
            }
            objectList.add(tmpTeacher);
        }
    }

    /**
     * Creates objects of type room from input file
     */

    private void createRooms() {
        
        JSONObject tmpObj;


        for (int i = 0; i < objArrRoom.length(); i++) {

            tmpObj = objArrRoom.getJSONObject(i);


            DatabaseContent tmpRoom = new Room();


            for (Object key : tmpObj.keySet()) {

                //based on you key types
                String keyStr = (String) key;
                Object keyvalue = tmpObj.get(keyStr);
                //Print key and value
				/**
				 roomCode varchar(5),facilitiesSupports varchar(55),max-capasity tinyint(10), roomSize varchar(20));
				 */

                if (keyStr.equalsIgnoreCase("roomCode")) {

                    ((Room) tmpRoom).setRoomCode((keyvalue.toString()));
                } else if (keyStr.equalsIgnoreCase("ColsAndDataTypes")) {
                    ((Room) tmpRoom).setColsAndDataTypes(keyvalue.toString());
                } else if (keyStr.equalsIgnoreCase("roomSize")) {
                    ((Room) tmpRoom).setRoomSize(keyvalue.toString());
                } else if (keyStr.equalsIgnoreCase("max_capasity")) {
                    ((Room) tmpRoom).setMaxCapasity(Integer.parseInt(keyvalue.toString()));
                } else if (keyStr.equalsIgnoreCase("facilitiesSupports")) {
                    ((Room) tmpRoom).setFacilitiesSupports(keyvalue.toString());
                } else {

                    try {
                        throw new AttributeNotFoundException();
                    } catch (AttributeNotFoundException e) {
                        System.out.println("Json attribute not in defined in class");
                        e.printStackTrace();
                    }
                }
            }
            // just testing if  everything is added nicely....

            objectList.add(tmpRoom);


        }

    }

    /**
     * Creates objects of type students from input file
     */
    private void createStudent() {
        
        JSONObject tmpObj;


        for (int i = 0; i < objArrStudentGroups.length(); i++) {
            tmpObj = objArrStudentGroups.getJSONObject(i);
            DatabaseContent tmpStu = new Student();

            for (Object key : tmpObj.keySet()) {
                //based on you key types
                String keyStr = (String) key;
                Object keyvalue = tmpObj.get(keyStr);
                //Print key and value
                if (keyStr.equalsIgnoreCase("ColsAndDataTypes")) {
                    ((Student) tmpStu).setColsAndDataTypes(keyvalue.toString());
                }else if(keyStr.equalsIgnoreCase("studentname")){
                    ((Student) tmpStu).setStudenName(keyvalue.toString());

                }else{
                    ((Student) tmpStu).setStudentSubjects(keyvalue.toString());
                }
            }
            // just testing if  everything is added nicely....
            objectList.add(tmpStu);
        }
    }


    /**
     * Creates objects of type subjects from input file
     */
    private void createSubjects() {
        
        JSONObject tmpObj;

        for (int i = 0; i < objArrSubjects.length(); i++) {

            tmpObj = objArrSubjects.getJSONObject(i);
            DatabaseContent tmpSub = new Subject();

            for (Object key : tmpObj.keySet()) {

                String keyStr = (String) key;
                Object keyvalue = tmpObj.get(keyStr);
                // TODO: 01/11/2017 Say why i put them in objects 
                if (keyStr.equalsIgnoreCase("name")) {
                    ((Subject) tmpSub).setName((keyvalue.toString()));
                } else if (keyStr.equalsIgnoreCase("campus_priority")) {
                    ((Subject) tmpSub).setCampusPrio(keyvalue.toString());
                } else if (keyStr.equalsIgnoreCase("colsanddatatypes")) {
                    ((Subject) tmpSub).setColsAndDataTypes(keyvalue.toString());
                } else if (keyStr.equalsIgnoreCase("educationForm")) {
                    ((Subject) tmpSub).setEducationForm(keyvalue.toString());
                } else if (keyStr.equalsIgnoreCase("subjectProgram")) {
                    ((Subject) tmpSub).setSubjectProgram(keyvalue.toString());
                } else if (keyStr.equalsIgnoreCase("subject_code")) {
                    ((Subject) tmpSub).setSubjectCode(keyvalue.toString());
                } else if (keyStr.equalsIgnoreCase("duration")) {
                    ((Subject) tmpSub).setDuration((keyvalue.toString()));
                } else if (keyStr.equalsIgnoreCase("amountOfHours")) {
                    ((Subject) tmpSub).setAmountOfHours((keyvalue.toString()));
                } else if (keyStr.equalsIgnoreCase("amountOfStudents")) {
                    ((Subject) tmpSub).setAmountOfStudents(keyvalue.toString());
                } else {

                    try {
                        throw new AttributeNotFoundException();
                    } catch (AttributeNotFoundException e) {
                        System.out.println("Json attribute not defined in class");
                        e.printStackTrace();
                    }
                }
            }


            objectList.add(tmpSub);

        }
    }
    /**
     * Prints the content of objectList
     */
    public void print() {
        objectList.forEach(System.out::println);
    }
}
