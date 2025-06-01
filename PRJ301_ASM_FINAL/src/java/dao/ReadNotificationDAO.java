/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.ReadNotification;

/**
 *
 * @author thais
 */
public class ReadNotificationDAO {

    public static ArrayList<ReadNotification> getReadNotification() {
        DBContext db = DBContext.getInstance();

        ArrayList<ReadNotification> readNotification = new ArrayList<>();

        try {
            String sql = """
                         select * from ReadNotifications
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ReadNotification r = new ReadNotification(
                        rs.getInt("readNotificationID"),
                        rs.getInt("userID"),
                        rs.getInt("notificationID"),
                        rs.getString("status"));
                readNotification.add(r);
            }
        } catch (Exception e) {
            return null;
        }
        return readNotification;
    }

    public static ReadNotification addReadNotification(ReadNotification readNotification) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
            INSERT INTO ReadNotifications (userID, notificationID, status) VALUES
            (?, ?, ?)
            """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, readNotification.getUserID());
            statement.setInt(2, readNotification.getNotificationID());
            statement.setString(3, "Read");
            rs = statement.executeUpdate();
        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return readNotification;
        }
    }

    // Phương thức kiểm tra xem thông báo đã được đọc bởi người dùng chưa
    public static boolean isNotificationRead(int userID, int notificationID) {
        DBContext db = DBContext.getInstance();
        String sql = "SELECT COUNT(*) FROM ReadNotifications WHERE userID = ? AND notificationID = ?";
        try {
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, userID);
            statement.setInt(2, notificationID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu có bản ghi, false nếu không có
            }
        } catch (Exception e) {
        }
        return false;
    }

   

}
