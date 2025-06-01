/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.EventFeedback;

/**
 *
 * @author knguy
 */
public class EventFeedbackDAO {

    public static ArrayList<EventFeedback> getEventsFeedbacks() {
        DBContext db = DBContext.getInstance();

        ArrayList<EventFeedback> eventFeedbacks = new ArrayList<>();

        try {
            String sql = """
                         select * from EventFeedback
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                EventFeedback eventFeedback = new EventFeedback(
                        rs.getInt("feedbackID"),
                        rs.getInt("eventID"),
                        rs.getInt("userID"),
                        rs.getInt("rating"),
                        rs.getString("comments"),
                        rs.getString("feedbackDate"));
                eventFeedbacks.add(eventFeedback);
            }
        } catch (Exception e) {
            return null;
        }
        return eventFeedbacks;
    }

    public static EventFeedback getEventFeedbackByFeedbackId(int feedbackId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<EventFeedback> eventFeedbacks = new ArrayList<EventFeedback>();
        try {
            String sql = """
                        select * from EventFeedback where feedbackID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, feedbackId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                EventFeedback eventFeedback = new EventFeedback(
                        rs.getInt("feedbackID"),
                        rs.getInt("eventID"),
                        rs.getInt("userID"),
                        rs.getInt("rating"),
                        rs.getString("comments"),
                        rs.getString("feedbackDate"));
                eventFeedbacks.add(eventFeedback);
            }
        } catch (Exception e) {
            return null;
        }
        return eventFeedbacks.get(0);
    }

    public static ArrayList<EventFeedback> getEventsFeedbacksByEventId(int eventId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<EventFeedback> eventFeedbacks = new ArrayList<EventFeedback>();
        try {
            String sql = """
                        select * from EventFeedback where eventID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, eventId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                EventFeedback eventFeedback = new EventFeedback(
                        rs.getInt("feedbackID"),
                        rs.getInt("eventID"),
                        rs.getInt("userID"),
                        rs.getInt("rating"),
                        rs.getString("comments"),
                        rs.getString("feedbackDate"));
                eventFeedbacks.add(eventFeedback);
            }
        } catch (Exception e) {
            return null;
        }
        return eventFeedbacks;
    }

    public static ArrayList<EventFeedback> getEventsFeedbacksByUserId(int userId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<EventFeedback> eventFeedbacks = new ArrayList<EventFeedback>();
        try {
            String sql = """
                        select * from EventFeedback where userID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, userId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                EventFeedback eventFeedback = new EventFeedback(
                        rs.getInt("feedbackID"),
                        rs.getInt("eventID"),
                        rs.getInt("userID"),
                        rs.getInt("rating"),
                        rs.getString("comments"),
                        rs.getString("feedbackDate"));
                eventFeedbacks.add(eventFeedback);
            }
        } catch (Exception e) {
            return null;
        }
        return eventFeedbacks;
    }

    public static ArrayList<EventFeedback> getEventsFeedbacksByRating(int rating) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<EventFeedback> eventFeedbacks = new ArrayList<EventFeedback>();
        try {
            String sql = """
                        select * from EventFeedback where rating = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, rating); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                EventFeedback eventFeedback = new EventFeedback(
                        rs.getInt("feedbackID"),
                        rs.getInt("eventID"),
                        rs.getInt("userID"),
                        rs.getInt("rating"),
                        rs.getString("comments"),
                        rs.getString("feedbackDate"));
                eventFeedbacks.add(eventFeedback);
            }
        } catch (Exception e) {
            return null;
        }
        return eventFeedbacks;
    }

    public static EventFeedback addEventFeedback(EventFeedback eventFeedback) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
        insert into EventFeedback(eventID, userID, rating, comments, feedbackDate)
        values (?, ?, ?, ?, ?)
        """; // (2)

            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, eventFeedback.getEventID());
            statement.setInt(2, eventFeedback.getUserID());
            statement.setInt(3, eventFeedback.getRating());
            statement.setString(4, eventFeedback.getComments());
            statement.setString(5, eventFeedback.getFeedbackDate());
            rs = statement.executeUpdate(); // (5)

        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return eventFeedback;
        }
    }

    public static EventFeedback deleteEventFeedback(EventFeedback eventFeedback) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            delete from EventFeedback
            where feedbackID = ?
            """; // (2)
            PreparedStatement statment = db.getConnection().prepareStatement(sql); // (3)
            statment.setInt(1, eventFeedback.getFeedbackID()); // (4)
            rs = statment.executeUpdate(); // (5)
        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return eventFeedback;
        }
    }

    public static EventFeedback updateEventFeedback(EventFeedback eventFeedback) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                         update EventFeedback
                         set eventID = ?, userID = ?, rating = ?, comments = ?, feedbackDate = ?
                         where feedbackID = ?
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, eventFeedback.getEventID());
            statement.setInt(2, eventFeedback.getUserID());
            statement.setInt(3, eventFeedback.getRating());
            statement.setString(4, eventFeedback.getComments());
            statement.setString(5, eventFeedback.getFeedbackDate());
            statement.setInt(6, eventFeedback.getFeedbackID());
            rs = statement.executeUpdate();

        } catch (Exception e) {
            return null;
        }
        System.out.println(rs);
        if (rs == 0) {
            return null;
        } else {
            return eventFeedback;
        }
    }

    public static ArrayList<EventFeedback> getEventFeedbackByEventId(int eventId) {
        ArrayList<EventFeedback> feedbackList = new ArrayList<>();
        DBContext db = DBContext.getInstance();
        String sql = """
                     select ep.feedbackID, u.image, u.fullName, ep.rating, ep.comments, ep.feedbackDate from EventFeedback ep 
                     join Users u on u.userID = ep.userID where ep.eventID = ?
                     """;
        try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setInt(1, eventId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                EventFeedback feedback = new EventFeedback(
                        rs.getInt("feedbackID"),
                        rs.getInt("rating"),
                        rs.getString("comments"),
                        rs.getString("feedbackDate"),
                        rs.getString("image"),
                        rs.getString("fullName")
                );
                feedbackList.add(feedback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    public static boolean hasUserGivenFeedback(int userId, int eventId) {
        DBContext db = DBContext.getInstance();
        boolean exists = false;

        try {
            String sql = """
                     SELECT COUNT(*) as count FROM EventFeedback 
                     WHERE userID = ? AND eventID = ?
                     """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, eventId);
            ResultSet rs = statement.executeQuery();

            if (rs.next() && rs.getInt("count") > 0) {
                exists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }

    public static double getAverageRatingByEventId(int eventId) {
        DBContext db = DBContext.getInstance();
        double avgRating = 0.0;

        try {
            String sql = """
                     SELECT ROUND(AVG(rating * 1.0), 2) as avgRating 
                     FROM EventFeedback 
                     WHERE eventID = ?;
                     """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, eventId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                avgRating = rs.getDouble("avgRating"); // Lấy giá trị trung bình
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return avgRating;
    }

}
