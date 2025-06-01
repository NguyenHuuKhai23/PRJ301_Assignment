/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Message;
import model.User;

/**
 *
 * @author knguy
 */
public class MessageDAO {

    // Get all messages (for admin overview, if needed)
    public static ArrayList<Message> getMessages() {
        DBContext db = DBContext.getInstance();
        ArrayList<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM Messages";
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                        rs.getInt("messageID"),
                        rs.getInt("senderID"),
                        rs.getInt("receiverID"),
                        rs.getString("subject"),
                        rs.getString("content"),
                        rs.getString("sentDate"),
                        rs.getString("status")
                );
                messages.add(message);
            }
        } catch (Exception e) {
            return null;
        }
        return messages;
    }

    // Get a specific message by messageID
    public static Message getMessageById(int messageId) {
        DBContext db = DBContext.getInstance();
        try {
            String sql = "SELECT * FROM Messages WHERE messageID = ?";
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, messageId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Message(
                        rs.getInt("messageID"),
                        rs.getInt("senderID"),
                        rs.getInt("receiverID"),
                        rs.getString("subject"),
                        rs.getString("content"),
                        rs.getString("sentDate"),
                        rs.getString("status")
                );
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    // Get all messages for a specific user (inbox and sent messages)
    public static ArrayList<Message> getMessagesByUserId(int userId) {
        DBContext db = DBContext.getInstance();
        ArrayList<Message> messages = new ArrayList<>();
        try {
            String sql = """
                        SELECT * FROM Messages 
                        WHERE senderID = ? OR receiverID = ?
                        ORDER BY sentDate DESC
                        """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                        rs.getInt("messageID"),
                        rs.getInt("senderID"),
                        rs.getInt("receiverID"),
                        rs.getString("subject"),
                        rs.getString("content"),
                        rs.getString("sentDate"),
                        rs.getString("status")
                );
                messages.add(message);
            }
        } catch (Exception e) {
            return null;
        }
        return messages;
    }

    // Add a new message (with admin-user restriction)
    public static Message addMessage(Message message) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            // Check if sender or receiver is an admin (enforce admin-user communication)
            String checkRoleSql = """
                                 SELECT role FROM Users WHERE userID IN (?, ?)
                                 """;
            PreparedStatement checkStmt = db.getConnection().prepareStatement(checkRoleSql);
            checkStmt.setInt(1, message.getSenderID());
            checkStmt.setInt(2, message.getReceiverID());
            ResultSet roleRs = checkStmt.executeQuery();

            String senderRole = null, receiverRole = null;
            if (roleRs.next()) {
                senderRole = roleRs.getString("role");
                if (roleRs.next()) {
                    receiverRole = roleRs.getString("role");
                }
            }

            // Enforce: one must be Admin, the other must be User
            if (!((senderRole.equals("Admin") && receiverRole.equals("User"))
                    || (senderRole.equals("User") && receiverRole.equals("Admin"))
                    || (senderRole.equals("Admin") && receiverRole.equals("Admin")))) {
                return null; // Reject if not admin-to-user or user-to-admin
            }

            String sql = """
                        INSERT INTO Messages (senderID, receiverID, subject, content, sentDate, status)
                        VALUES (?, ?, ?, ?, ?, ?)
                        """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, message.getSenderID());
            statement.setInt(2, message.getReceiverID());
            statement.setString(3, message.getSubject());
            statement.setString(4, message.getContent());
            statement.setString(5, message.getSentDate());
            statement.setString(6, message.getStatus());
            rs = statement.executeUpdate();

            if (rs > 0) {
                return message;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    // Delete a message by messageID
    public static Message deleteMessage(Message message) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = "DELETE FROM Messages WHERE messageID = ?";
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, message.getMessageID());
            rs = statement.executeUpdate();
        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return message;
        }
    }

    // Mark a message as 'Seen'
    public static boolean markMessageAsSeen(int messageId) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = "UPDATE Messages SET status = 'Seen' WHERE messageID = ?";
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, messageId);
            rs = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rs > 0;
    }

    // Check if a user is an admin (helper method)
    private static boolean isAdmin(int userId) {
        DBContext db = DBContext.getInstance();
        try {
            String sql = "SELECT role FROM Users WHERE userID = ?";
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("role").equals("Admin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
