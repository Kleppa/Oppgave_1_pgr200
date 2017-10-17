package innlevering;

import java.util.ArrayList;

/**
 * Created by Kleppa on 04/09/2017.
 */
public class Subject implements DatabaseContent {

    private String name;


    public ArrayList<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(ArrayList<String> subjectList) {
        this.subjectList = subjectList;
    }

    private ArrayList<String> subjectList=new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getCampusPrio() {
        return campusPrio;
    }

    public void setCampusPrio(String campusPrio) {
        this.campusPrio = campusPrio;
    }

    public String getEducationForm() {
        return educationForm;
    }

    public void setEducationForm(String educationForm) {
        this.educationForm = educationForm;
    }

    public String getSubjectProgram() {
        return subjectProgram;
    }

    public void setSubjectProgram(String subjectProgram) {
        this.subjectProgram = subjectProgram;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAmountOfHours() {
        return amountOfHours;
    }

    public void setAmountOfHours(String amountOfHours) {
        this.amountOfHours = amountOfHours;
    }

    public String getAmountOfStudents() {
        return amountOfStudents;
    }

    public void setAmountOfStudents(String amountOfStudents) {
        this.amountOfStudents = amountOfStudents;
    }

    private String subjectCode;
    private String campusPrio;
    private String educationForm;
    private String subjectProgram;
    private String duration;
    private String amountOfHours;
    private String amountOfStudents;

    public void setColsAndDataTypes(String colsAndDataTypes) {
        this.colsAndDataTypes = colsAndDataTypes;
    }

    private String colsAndDataTypes;

    @Override
    public String toString() {
        return "Subject{" +
                "\"name\":" + "\""+name + "\""+
               ",\" subjectCode\":" +"\""+ subjectCode+"\"" +
                ", \"campusPrio\":" +"\""+ campusPrio +"\""+
                ", \"educationForm\":" +"\""+ educationForm +"\""+
                ", \"subjectProgram\":" +"\""+ subjectProgram +"\""+
                ", \"duration\":" + duration +
                ", \"amountOfHours\":" + amountOfHours +
                ", \"amountOfStudents\":" +amountOfStudents +
                '}';
    }

    @Override
    public String getColsAndDataTypes() {
        return colsAndDataTypes;
    }
}