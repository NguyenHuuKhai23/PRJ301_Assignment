/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author knguy
 */
public class Report {
    private int reportID, clubID;
    private String semester, memberChanges, eventSummary, participationStatus;
    private String createdDate;

    public Report() {
    }

    public Report(int reportID, int clubID, String semester, String memberChanges, String eventSummary, String participationStatus, String createdDate) {
        this.reportID = reportID;
        this.clubID = clubID;
        this.semester = semester;
        this.memberChanges = memberChanges;
        this.eventSummary = eventSummary;
        this.participationStatus = participationStatus;
        this.createdDate = createdDate;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getMemberChanges() {
        return memberChanges;
    }

    public void setMemberChanges(String memberChanges) {
        this.memberChanges = memberChanges;
    }

    public String getEventSummary() {
        return eventSummary;
    }

    public void setEventSummary(String eventSummary) {
        this.eventSummary = eventSummary;
    }

    public String getParticipationStatus() {
        return participationStatus;
    }

    public void setParticipationStatus(String participationStatus) {
        this.participationStatus = participationStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Report{" + "reportID=" + reportID + ", clubID=" + clubID + ", semester=" + semester + ", memberChanges=" + memberChanges + ", eventSummary=" + eventSummary + ", participationStats=" + participationStatus + ", createdDate=" + createdDate + '}';
    }
    
    
}
