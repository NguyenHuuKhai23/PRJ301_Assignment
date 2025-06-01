/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author knguy
 */
public class EventFeedback {
    private int feedbackID, eventID, userID, rating;
    private String comments, feedbackDate, image, fullName;

    public EventFeedback() {
    }

    public EventFeedback(int feedbackID, int eventID, int userID, int rating, String comments, String feedbackDate) {
        this.feedbackID = feedbackID;
        this.eventID = eventID;
        this.userID = userID;
        this.rating = rating;
        this.comments = comments;
        this.feedbackDate = feedbackDate;
    }
    
    public EventFeedback(int feedbackID, int rating, String comments, String feedbackDate, String image, String fullName) {
        this.feedbackID = feedbackID;
        this.rating = rating;
        this.comments = comments;
        this.feedbackDate = feedbackDate;
        this.image = image;
        this.fullName = fullName;
    }
    
    

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(String feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    

    @Override
    public String toString() {
        return "EventFeedback{" + "feedbackID=" + feedbackID + ", eventID=" + eventID + ", userID=" + userID + ", rating=" + rating + ", comments=" + comments + ", feedbackDate=" + feedbackDate + '}';
    }
    
    
}
