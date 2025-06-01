/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author knguy
 */
public class ClubJoinApplication {
    private int applicationID, userID, clubID;
    private String status, applicationDate, UserName;

    public ClubJoinApplication() {
    }

    public ClubJoinApplication(int applicationID, int userID, int clubID, String status, String applicationDate) {
        this.applicationID = applicationID;
        this.userID = userID;
        this.clubID = clubID;
        this.status = status;
        this.applicationDate = applicationDate;
    }

    public ClubJoinApplication(int applicationID, int userID, int clubID, String status, String applicationDate, String UserName) {
        this.applicationID = applicationID;
        this.userID = userID;
        this.clubID = clubID;
        this.status = status;
        this.applicationDate = applicationDate;
        this.UserName = UserName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
    
    public int getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    @Override
    public String toString() {
        return "ClubJoinApplication{" + "applicationID=" + applicationID + ", userID=" + userID + ", clubID=" + clubID + ", status=" + status + ", applicationDate=" + applicationDate + '}';
    }
           
    
}
