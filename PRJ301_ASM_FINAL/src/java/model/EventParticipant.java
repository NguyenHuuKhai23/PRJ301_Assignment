/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author knguy
 */
public class EventParticipant {
    private int eventParticipantID, eventID, userID;
    private String status, fullName, role, studentID;

    public EventParticipant() {
    }
    
    public EventParticipant(int eventParticipantID, int eventID, int userID, String status) {
        this.eventParticipantID = eventParticipantID;
        this.eventID = eventID;
        this.userID = userID;
        this.status = status;
    }

    public EventParticipant(int eventParticipantID, int eventID, int userID, String status, String fullName, String role, String studentID) {
        this.eventParticipantID = eventParticipantID;
        this.eventID = eventID;
        this.userID = userID;
        this.status = status;
        this.fullName = fullName;
        this.role = role;
        this.studentID = studentID;
    }

    public int getEventParticipantID() {
        return eventParticipantID;
    }

    public void setEventParticipantID(int eventParticipantID) {
        this.eventParticipantID = eventParticipantID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    @Override
    public String toString() {
        return "EventParticipant{" + "eventParticipantID=" + eventParticipantID + ", eventID=" + eventID + ", userID=" + userID + ", status=" + status + ", fullName=" + fullName + ", role=" + role + ", studentID=" + studentID + '}';
    }  
}
