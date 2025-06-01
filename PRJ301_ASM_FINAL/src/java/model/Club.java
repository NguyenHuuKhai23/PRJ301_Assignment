/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


/**
 *
 * @author knguy
 */
public class Club {
    private int clubID;
    private String clubName, description, image;
    private String establishedDate;
    private MemberClub memberClub;
    
    public Club() {
    }

    public Club(int clubID, String clubName, String description, String image, String establishedDate, MemberClub memberClub) {
        this.clubID = clubID;
        this.clubName = clubName;
        this.description = description;
        this.image = image;
        this.establishedDate = establishedDate;
        this.memberClub = memberClub;
    }

    public Club(int clubID, String clubName, String description, String image, String establishedDate) {
        this.clubID = clubID;
        this.clubName = clubName;
        this.description = description;
        this.image = image;
        this.establishedDate = establishedDate;
    }
    
    

    public MemberClub getMemberClub() {
        return memberClub;
    }

    public void setMemberClub(MemberClub memberClub) {
        this.memberClub = memberClub;
    }

   
    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(String establishedDate) {
        this.establishedDate = establishedDate;
    }

    @Override
    public String toString() {
        return "Club{" + "clubID=" + clubID + ", clubName=" + clubName + ", description=" + description + ", image=" + image + ", establishedDate=" + establishedDate + '}';
    }

    
    
}
