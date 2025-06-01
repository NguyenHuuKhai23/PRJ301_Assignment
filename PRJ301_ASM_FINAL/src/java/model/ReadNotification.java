/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author thais
 */
public class ReadNotification {
    private int readNotificationID, userID, notificationID;
    private String status;

    public ReadNotification() {
    }

    public ReadNotification(int readNotificationID, int userID, int notificationID, String status) {
        this.readNotificationID = readNotificationID;
        this.userID = userID;
        this.notificationID = notificationID;
        this.status = status;
    }

    public int getReadNotificationID() {
        return readNotificationID;
    }

    public void setReadNotificationID(int readNotificationID) {
        this.readNotificationID = readNotificationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ReadNotifications{" + "readNotificationID=" + readNotificationID + ", userID=" + userID + ", notificationID=" + notificationID + ", status=" + status + '}';
    }   
}
