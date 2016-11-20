package com.example.mona_.a_track;

import android.widget.DatePicker;

public class AssignmentInfo {
    int class_id;
    String  subject;
    String assignment;
    DatePicker assignmentdate;
    int assignment_id;

    // The Get methods to access these variables, and  the set methods to modify them
    public void  setClass_id(int class_id){
        this.class_id = class_id;
    }

    public Integer getClass_id(){return this.class_id;}

    public void setSubject(String subject){
        this.subject = subject;
    }
    public String getSubject(){
        return this.subject;
    }

    public void setAssignment(String assignment){
        this.assignment = assignment;
    }
    public String getAssignment(){
        return this.assignment;
    }

    public void setAssignment_id(int assignment_id){
        this.assignment_id = assignment_id;
    }
    public Integer getAssignment_id(){
        return this.assignment_id;
    }

    @Override
    public String toString() {
        return this.getAssignment();

    }
}
