package innlevering;

import java.util.ArrayList;

/**
 * Created by Kleppa on 04/09/2017.
 */
public class Teacher implements DatabaseContent {
    private String name;
    private String age;
    private String contactInfo;
    private String availability;

    public void setColsAndDataTypes(String colsAndDataTypes) {
        this.colsAndDataTypes = colsAndDataTypes;
    }

    private String  colsAndDataTypes;
    private ArrayList<String> subjectList=new ArrayList<String>();

    public int getSizeList(){
        return subjectList.size();
    }

    public String getColsAndDataTypes(){
        return colsAndDataTypes;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "\"name\":" + "\""+ name + "\""+
                ", \"age\":" + "\""+ age + "\""+
                ", \"contactInfo\":" + "\""+ contactInfo + "\""+
                ",\" availability\":" + "\""+ availability + "\""+
                ", \"subjectList\":" + subjectList +
                '}';
    }

    public int getlistSize(){
        return subjectList.size();
    }
    public void addToList(String subject){
        subjectList.add(subject);
    }
    public String getFromList(int i){
        return subjectList.get(i);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
