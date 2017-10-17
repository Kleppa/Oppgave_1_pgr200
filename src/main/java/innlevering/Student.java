package innlevering;

import java.util.ArrayList;

/**
 * Created by Kleppa on 04/09/2017.
 */
public class Student implements DatabaseContent {
    private String studentSubjects;
    public void setColsAndDataTypes(String colsAndDataTypes) {
        this.colsAndDataTypes = colsAndDataTypes;
    }

    private String colsAndDataTypes;
    public ArrayList<String> getSubjectList() {
        return subjectList;
    }
    ArrayList<String> subjectList=new ArrayList<String>();
    @Override
    public String getColsAndDataTypes() {
        return colsAndDataTypes;
    }
    @Override
    public String toString() {
        return "Student{" +
                "\"studentName\":" + "\""+ studenName + "\""+",\"studentSubjects\":"+"\""+studentSubjects+"\""+
                '}';
    }
    public void addToList(String s){
        subjectList.add(s);
    }



    public String getStudenName() {
        return studenName;
    }

    public void setStudenName(String studenName) {
        this.studenName = studenName;
    }



    private String studenName;


    public String getStudentSubjects() {
        return studentSubjects;
    }

    public void setStudentSubjects(String studentSubjects) {
        this.studentSubjects = studentSubjects;
    }
}
