package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.MemberClub;
import model.Notification;
import model.User;

public class NotificationDAO {

    public static ArrayList<Notification> getNotifications() {
        DBContext db = DBContext.getInstance();
        ArrayList<Notification> notifications = new ArrayList<>();

        try {
            String sql = """
                     SELECT DISTINCT n.notificationID, n.userID, n.clubID, n.content, n.notificationDate, 
                     u.studentID, u.fullName, u.email, u.password, u.image, u.role, m.roleClub
                     FROM Notifications n 
                     LEFT JOIN Users u ON n.userID = u.userID 
                     LEFT JOIN MemberClubs m on u.userID = m.userID
                     ORDER BY n.notificationDate DESC
                     """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                // Create Notification object
                Notification n = new Notification(
                        rs.getInt("notificationID"),
                        rs.getInt("userID"),
                        rs.getInt("clubID"),
                        rs.getString("content"),
                        rs.getString("notificationDate"),
                        new User(rs.getInt("userID"),
                                rs.getString("studentID"),
                                rs.getString("fullName"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("image"),
                                rs.getString("role"),
                                new MemberClub(rs.getInt("userID"),
                                        rs.getInt("clubID"),
                                        rs.getString("roleClub"))));
                notifications.add(n);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        }
        return notifications;
    }



    public static Notification getNotificationById(int notificationID) {
        DBContext db = DBContext.getInstance();
        try {
            String sql = """
                    SELECT n.notificationID, n.userID, n.clubID, n.content, n.notificationDate, 
                                         u.studentID, u.fullName, u.email, u.password, u.image, u.role, m.roleClub
                                         FROM Notifications n 
                                         LEFT JOIN Users u ON n.userID = u.userID 
                                         LEFT JOIN MemberClubs m on u.userID = m.userID
                    WHERE notificationID = ?
                    """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, notificationID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setFullName(rs.getString("fullName"));
                MemberClub memberClub = new MemberClub();
                memberClub.setClubID(rs.getInt("clubID"));
                Notification n = new Notification(
                        rs.getInt("notificationID"),
                        rs.getInt("userID"),
                        rs.getInt("clubID"),
                        rs.getString("content"),
                        rs.getString("notificationDate"),
                        new User(rs.getInt("userID"),
                                rs.getString("studentID"),
                                rs.getString("fullName"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("image"),
                                rs.getString("role"),
                                new MemberClub(rs.getInt("userID"),
                                        rs.getInt("clubID"),
                                        rs.getString("roleClub"))));
                return n;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static Notification deleteNotification(Notification notification) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = "delete from Notifications where notificationID = ?";
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, notification.getNotificationID());
            rs = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return (rs > 0) ? notification : null;
    }

    public static Notification addNotification(Notification notification) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
            INSERT INTO Notifications (userID, clubID, content, notificationDate)
            VALUES (?, ?, ?, ?)
            """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, notification.getUserID());
            statement.setInt(2, notification.getClubID());
            statement.setString(3, notification.getContent());
            statement.setString(4, notification.getNotificationDate());
            rs = statement.executeUpdate();
        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return notification;
        }
    }

    public static Notification addNotificationByAdmin(Notification notification) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
            INSERT INTO Notifications (userID, content, notificationDate)
            VALUES (?, ?, ?)
            """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, notification.getUserID());
            statement.setString(2, notification.getContent());
            statement.setString(3, notification.getNotificationDate());
            rs = statement.executeUpdate();
        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return notification;
        }
    }

}
