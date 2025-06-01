/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Event;
import model.EventParticipant;
import model.User;

/**
 *
 * @author knguy
 */
public class EventParticipantDAO {

    public static ArrayList<EventParticipant> getEventParticipants() {
        DBContext db = DBContext.getInstance();

        ArrayList<EventParticipant> eventParticipants = new ArrayList<>();

        try {
            String sql = """
                         select * from EventParticipants
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                EventParticipant eventParticipant = new EventParticipant(
                        rs.getInt("eventParticipantID"),
                        rs.getInt("eventID"),
                        rs.getInt("userID"),
                        rs.getString("status"));
                eventParticipants.add(eventParticipant);
            }
        } catch (Exception e) {
            return null;
        }
        return eventParticipants;
    }

    public static EventParticipant getEventParticipantsByEventParticipantId(int eventParticipantId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<EventParticipant> eventParticipants = new ArrayList<EventParticipant>();
        try {
            String sql = """
                        select * from EventParticipants where eventParticipantID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, eventParticipantId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                EventParticipant eventParticipant = new EventParticipant(
                        rs.getInt("eventParticipantID"),
                        rs.getInt("eventID"),
                        rs.getInt("userID"),
                        rs.getString("status"));
                eventParticipants.add(eventParticipant);
            }
        } catch (Exception e) {
            return null;
        }
        return eventParticipants.get(0);
    }

    public static ArrayList<EventParticipant> getEventParticipantsByEventId(int eventId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<EventParticipant> eventParticipants = new ArrayList<EventParticipant>();
        try {
            String sql = """
                        select * from EventParticipants where eventID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, eventId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                EventParticipant eventParticipant = new EventParticipant(
                        rs.getInt("eventParticipantID"),
                        rs.getInt("eventID"),
                        rs.getInt("userID"),
                        rs.getString("status"));
                eventParticipants.add(eventParticipant);
            }
        } catch (Exception e) {
            return null;
        }
        return eventParticipants;
    }

    public static ArrayList<EventParticipant> getEventParticipantsByUserId(int userId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<EventParticipant> eventParticipants = new ArrayList<EventParticipant>();
        try {
            String sql = """
                         select * from EventParticipants where userID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, userId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                EventParticipant eventParticipant = new EventParticipant(
                        rs.getInt("eventParticipantID"),
                        rs.getInt("eventID"),
                        rs.getInt("userID"),
                        rs.getString("status"));
                eventParticipants.add(eventParticipant);
            }
        } catch (Exception e) {
            return null;
        }
        return eventParticipants;
    }

    public static boolean isUserRegisteredForEvent(int userId, int eventId) {
        DBContext db = DBContext.getInstance(); // (1)
        boolean registered = false;
        try {
            String sql = """
                         select * from EventParticipants where eventId = ? and userID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, eventId);
            statement.setInt(2, userId);
            ResultSet rs = statement.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                registered = true;
            }
        } catch (Exception e) {
            return registered;
        }
        return registered;
    }

    public static ArrayList<EventParticipant> getEventParticipantsByStatus(int status) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<EventParticipant> eventParticipants = new ArrayList<EventParticipant>();
        try {
            String sql = """
                         select * from EventParticipants where status = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, status); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                EventParticipant eventParticipant = new EventParticipant(
                        rs.getInt("eventParticipantID"),
                        rs.getInt("eventID"),
                        rs.getInt("userID"),
                        rs.getString("status"));
                eventParticipants.add(eventParticipant);
            }
        } catch (Exception e) {
            return null;
        }
        return eventParticipants;
    }

    public static EventParticipant addEventParticipant(EventParticipant eventParticipant) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            insert into EventParticipants(eventID, userID, status)
            values (?, ?, ?)
            """; // (2)

            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, eventParticipant.getEventID());
            statement.setInt(2, eventParticipant.getUserID());
            statement.setString(3, "Registered");
            rs = statement.executeUpdate(); // (5)

        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return eventParticipant;
        }
    }

    public static EventParticipant deleteEventFeedback(EventParticipant eventParticipant) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            delete from EventParticipants
            where eventParticipantID = ?
            """; // (2)
            PreparedStatement statment = db.getConnection().prepareStatement(sql); // (3)
            statment.setInt(1, eventParticipant.getEventParticipantID()); // (4)
            rs = statment.executeUpdate(); // (5)
        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return eventParticipant;
        }
    }

    public static EventParticipant updateEventParticipant(EventParticipant eventParticipant) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                         update EventParticipants
                         set status = ?
                         where eventParticipantID = ?
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setString(1, eventParticipant.getStatus());
            statement.setInt(2, eventParticipant.getEventParticipantID());
            rs = statement.executeUpdate();

        } catch (Exception e) {
            return null;
        }
        System.out.println(rs);
        if (rs == 0) {
            return null;
        } else {
            return eventParticipant;
        }
    }

    public static ArrayList<EventParticipant> getUserEventParticipants(Event event) {
        DBContext db = DBContext.getInstance();
        ArrayList<EventParticipant> eventParticipants = new ArrayList<>();

        try {
            String sql = """
                     SELECT u.userID, u.studentID, u.fullName, u.role, ep.status, 
                            ep.eventParticipantID, ep.eventID 
                     FROM Users u 
                     JOIN EventParticipants ep ON u.userID = ep.userID 
                     WHERE ep.eventID = ? order by ep.status desc
                     """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, event.getEventID());
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                EventParticipant eventParticipant = new EventParticipant(
                        rs.getInt("eventParticipantID"),
                        rs.getInt("eventID"),
                        rs.getInt("userID"),
                        rs.getString("status"),
                        rs.getString("fullName"),
                        rs.getString("role"),
                        rs.getString("studentID")
                );
                eventParticipants.add(eventParticipant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventParticipants;
    }
}
