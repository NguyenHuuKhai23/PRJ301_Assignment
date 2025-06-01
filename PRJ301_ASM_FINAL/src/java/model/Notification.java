/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author thais
 */
public class Notification {
    private int notificationID;
    private int userID;
    private int clubID;
    private String content;
    private String notificationDate;
    private User user;
    
    public Notification() {
    }
    

    public Notification(int notificationID, int userID, int clubID, String content, String notificationDate, User user) {
        this.notificationID = notificationID;
        this.userID = userID;
        this.clubID = clubID;
        this.content = content;
        this.notificationDate = notificationDate;
        this.user = user;
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Notification{" + "notificationID=" + notificationID + ", userID=" + userID + ", clubID=" + clubID + ", content=" + content + ", notificationDate=" + notificationDate + ", user=" + user + '}';
    }

    
}
