/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author thais
 */
public class History {
    private int historyID, userID;
    private String action, changeDate;

    public History() {
    }

    public History(int historyID, int userID, String action, String changeDate) {
        this.historyID = historyID;
        this.userID = userID;
        this.action = action;
        this.changeDate = changeDate;
    }

    public int getHistoryID() {
        return historyID;
    }

    public void setHistoryID(int historyID) {
        this.historyID = historyID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    @Override
    public String toString() {
        return "History{" + "historyID=" + historyID + ", userID=" + userID + ", action=" + action + ", changeDate=" + changeDate + '}';
    }
    
    
}
