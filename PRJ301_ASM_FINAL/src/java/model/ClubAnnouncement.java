/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author knguy
 */
public class ClubAnnouncement {
    private int announcementID, clubID;
    private String title, content, announcementDate;

    public ClubAnnouncement() {
    }

    public ClubAnnouncement(int announcementID, int clubID, String title, String content, String announcementDate) {
        this.announcementID = announcementID;
        this.clubID = clubID;
        this.title = title;
        this.content = content;
        this.announcementDate = announcementDate;
    }

    public int getAnnouncementID() {
        return announcementID;
    }

    public void setAnnouncementID(int announcementID) {
        this.announcementID = announcementID;
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnnouncementDate() {
        return announcementDate;
    }

    public void setAnnouncementDate(String announcementDate) {
        this.announcementDate = announcementDate;
    }

    @Override
    public String toString() {
        return "ClubAnnouncement{" + "announcementID=" + announcementID + ", clubID=" + clubID + ", title=" + title + ", content=" + content + ", announcementDate=" + announcementDate + '}';
    }
    
    
}
