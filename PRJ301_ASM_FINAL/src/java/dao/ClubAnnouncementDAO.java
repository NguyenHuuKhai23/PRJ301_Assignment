/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.ClubAnnouncement;

/**
 *
 * @author knguy
 */
public class ClubAnnouncementDAO {
     public static ArrayList<ClubAnnouncement> getClubAnnouncements() {
        DBContext db = DBContext.getInstance();

        ArrayList<ClubAnnouncement> clubAnnouncements = new ArrayList<>();

        try {
            String sql = """
                         select * from ClubAnnouncements
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ClubAnnouncement clubAnnouncement = new ClubAnnouncement(
                        rs.getInt("announcementID"),
                        rs.getInt("clubID"), 
                        rs.getString("title"), 
                        rs.getString("content"), 
                        rs.getString("announcementDate"));
                clubAnnouncements.add(clubAnnouncement);
            }
        } catch (Exception e) {
            return null;
        }
        return clubAnnouncements;
    }

    public static ArrayList<ClubAnnouncement> getClubAnnouncementsByClubId(int clubId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<ClubAnnouncement> clubAnnouncements = new ArrayList<ClubAnnouncement>();
        try {
            String sql = """
                            select * from ClubAnnouncements where clubID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, clubId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                ClubAnnouncement clubAnnouncement = new ClubAnnouncement(
                        rs.getInt("announcementID"),
                        rs.getInt("clubID"), 
                        rs.getString("title"), 
                        rs.getString("content"), 
                        rs.getString("announcementDate"));
                clubAnnouncements.add(clubAnnouncement);
            }
        } catch (Exception e) {
            return null;
        }
        return clubAnnouncements;
    }
    
    public static ClubAnnouncement getClubAnnouncementsByAnnouncementID(int announcementID) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<ClubAnnouncement> clubAnnouncements = new ArrayList<ClubAnnouncement>();
        try {
            String sql = """
                        select * from ClubAnnouncements where announcementID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, announcementID); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                ClubAnnouncement clubAnnouncement = new ClubAnnouncement(
                        rs.getInt("announcementID"),
                        rs.getInt("clubID"), 
                        rs.getString("title"), 
                        rs.getString("content"), 
                        rs.getString("announcementDate"));
                clubAnnouncements.add(clubAnnouncement);
            }
        } catch (Exception e) {
            return null;
        }
        return clubAnnouncements.get(0);
    }
    
    public static ClubAnnouncement addClub(ClubAnnouncement announcement) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            insert into ClubAnnouncements(clubID, title, content, announcementDate)
            values (?, ?, ?, ?)
            """; // (2)

            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, announcement.getClubID());
            statement.setString(2, announcement.getTitle());
            statement.setString(3, announcement.getContent());
            statement.setString(4, announcement.getAnnouncementDate());
            rs = statement.executeUpdate(); // (5)

        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return announcement;
        }
    }
     
    public static ClubAnnouncement deleteAnnouncement(ClubAnnouncement announcement) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            delete from ClubAnnouncements
            where announcementID = ?
            """; // (2)
            PreparedStatement statment = db.getConnection().prepareStatement(sql); // (3)
            statment.setInt(1, announcement.getAnnouncementID()); // (4)
            rs = statment.executeUpdate(); // (5)
        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return announcement;
        }
    }

    public static ClubAnnouncement updateAnnouncement(ClubAnnouncement announcement) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                         update ClubAnnouncements
                         set clubID = ?, title = ?, content = ?, announcementDate = ?
                         where announcementID = ?
                        """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, announcement.getClubID());
            statement.setString(2, announcement.getTitle());
            statement.setString(3, announcement.getContent());
            statement.setString(4, announcement.getAnnouncementDate());
            statement.setInt(5, announcement.getAnnouncementID());
            rs = statement.executeUpdate();
            
        } catch (Exception e) {
            return null;
        }
        System.out.println(rs);
        if (rs == 0) {
            return null;
        } else {
           return announcement;
        }
    }
}
