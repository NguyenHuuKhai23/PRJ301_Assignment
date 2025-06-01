/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author knguy
 */
public class MemberClub {
    private int userID, clubID;
    private String roleClub;
    
    public MemberClub() {
    }

    public MemberClub(int userID, int clubID, String roleClub) {
        this.userID = userID;
        this.clubID = clubID;
        this.roleClub = roleClub;
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

    public String getRole() {
        return roleClub;
    }

    public void setRole(String roleClub) {
        this.roleClub = roleClub;
    }

    @Override
    public String toString() {
        return "MemberClub{" + "userID=" + userID + ", clubID=" + clubID + ", roleClub=" + roleClub + '}';
    }
    
}
