/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.ClubJoinApplication;
import model.Event;

/**
 *
 * @author knguy
 */
public class EventDAO {

    public static ArrayList<Event> getEvents() {
        DBContext db = DBContext.getInstance();

        ArrayList<Event> events = new ArrayList<>();

        try {
            String sql = """
                         select * from Events
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Event event = new Event(rs.getInt("eventID"),
                        rs.getInt("clubID"),
                        rs.getString("eventName"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("eventDate"),
                        rs.getString("image"));
                events.add(event);
            }
        } catch (Exception e) {
            return null;
        }
        return events;
    }

    public static Event getEventByEventId(int eventID) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<Event> events = new ArrayList<Event>();
        try {
            String sql = """
                        select * from Events where eventID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, eventID); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                Event event = new Event(rs.getInt("eventID"),
                        rs.getInt("clubID"),
                        rs.getString("eventName"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("eventDate"),
                        rs.getString("image"));
                events.add(event);
            }
        } catch (Exception e) {
            return null;
        }
        return events.get(0);
    }

    public static ArrayList<Event> getEventByClubId(int clubId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<Event> events = new ArrayList<Event>();
        try {
            String sql = """
                        select * from Events where clubID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, clubId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                Event event = new Event(rs.getInt("eventID"),
                        rs.getInt("clubID"),
                        rs.getString("eventName"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("eventDate"),
                        rs.getString("image"));
                events.add(event);
            }
        } catch (Exception e) {
            return null;
        }
        return events;
    }

    public static Event addEvent(Event event) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
            INSERT INTO Events(eventName, description, eventDate, location, clubID, image)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setString(1, event.getEventName());
            statement.setString(2, event.getDescription());
            // Giả sử eventDate là String từ form hoặc cần convert từ định dạng khác
            statement.setString(3, event.getEventDate()); // Cần lấy từ form hoặc đặt giá trị mặc định
            statement.setString(4, event.getLocation());
            statement.setInt(5, event.getClubID());
            statement.setString(6, event.getImage());
            rs = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(); // Hoặc dùng logger để ghi lỗi
            return null;
        }
        return rs > 0 ? event : null;
    }

    public static Event deleteEvent(Event event) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            delete from Events
            where eventID = ?
            """; // (2)
            PreparedStatement statment = db.getConnection().prepareStatement(sql); // (3)
            statment.setInt(1, event.getEventID()); // (4)
            rs = statment.executeUpdate(); // (5)
        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return event;
        }
    }

    public static Event updateEvent(Event event) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                         update Events
                         set eventName = ?, description = ?, eventDate = ?, location = ?, clubID
                         where eventID = ?
                        """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setString(1, event.getEventName());
            statement.setString(2, event.getDescription());
            statement.setString(3, event.getEventDate());
            statement.setString(4, event.getLocation());
            statement.setInt(5, event.getClubID());
            statement.setInt(6, event.getEventID());
            statement.setString(7, event.getImage());
            rs = statement.executeUpdate();

        } catch (Exception e) {
            return null;
        }
        System.out.println(rs);
        if (rs == 0) {
            return null;
        } else {
            return event;
        }
    }

    public static ArrayList<Event> getLatestEvents() {
        DBContext db = DBContext.getInstance();
        ArrayList<Event> events = new ArrayList<>();

        try {
            String sql = """
                     SELECT TOP 3 * FROM Events 
                     ORDER BY eventDate DESC 
                     """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("eventID"),
                        rs.getInt("clubID"),
                        rs.getString("eventName"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("eventDate"),
                        rs.getString("image")
                );
                events.add(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return events;
    }
}
