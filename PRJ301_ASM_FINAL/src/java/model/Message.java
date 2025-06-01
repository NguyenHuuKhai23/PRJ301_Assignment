/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author knguy
 */
public class Message {

    private int messageID;
    private int senderID;
    private int receiverID;
    private String subject;
    private String content;
    private String sentDate;
    private String status;

    public Message(int messageID, int senderID, int receiverID, String subject, String content, String sentDate, String status) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.subject = subject;
        this.content = content;
        this.sentDate = sentDate;
        this.status = status;
    }

    // Getters and Setters
    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
