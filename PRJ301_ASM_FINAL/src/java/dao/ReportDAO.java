/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Report;

/**
 *
 * @author knguy
 */
public class ReportDAO {

    public static ArrayList<Report> getReports() {
        DBContext db = DBContext.getInstance();

        ArrayList<Report> reports = new ArrayList<>();

        try {
            String sql = """
                         select * from Reports
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Report report = new Report(
                        rs.getInt("reportID"),
                        rs.getInt("clubID"),
                        rs.getString("semester"),
                        rs.getString("memberChanges"),
                        rs.getString("eventSummary"),
                        rs.getString("participationStatus"),
                        rs.getString("createdDate"));
                reports.add(report);
            }
        } catch (Exception e) {
            return null;
        }
        return reports;
    }

    public static Report getReportByReportId(int reportId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<Report> reports = new ArrayList<>();

        try {
            String sql = """
                        select * from Reports where reportID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, reportId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                Report report = new Report(
                        rs.getInt("reportID"),
                        rs.getInt("clubID"),
                        rs.getString("semester"),
                        rs.getString("memberChanges"),
                        rs.getString("eventSummary"),
                        rs.getString("participationStatus"),
                        rs.getString("createdDate"));
                reports.add(report);
            }
        } catch (Exception e) {
            return null;
        }
        return reports.get(0);
    }

    public static ArrayList<Report> getReportsByClubId(int clubId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<Report> reports = new ArrayList<>();
        try {
            String sql = """
                        select * from Reports where clubID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, clubId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                Report report = new Report(
                        rs.getInt("reportID"),
                        rs.getInt("clubID"),
                        rs.getString("semester"),
                        rs.getString("memberChanges"),
                        rs.getString("eventSummary"),
                        rs.getString("participationStatus"),
                        rs.getString("createdDate"));
                reports.add(report);
            }
        } catch (Exception e) {
            return null;
        }
        return reports;
    }

    public static Report addReport(Report report) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            insert into Reports(clubID, semester, memberChanges, eventSummary, participationStatus, createdDate)
            values (?, ?, ?, ?, ?, ?)
            """; // (2)

            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, report.getClubID());
            statement.setString(2, report.getSemester());
            statement.setString(3, report.getMemberChanges());
            statement.setString(4, report.getEventSummary());
            statement.setString(5, report.getParticipationStatus());
            statement.setString(6, report.getCreatedDate());
            rs = statement.executeUpdate(); // (5)

        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return report;
        }
    }

    public static Report deleteReport(Report report) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            delete from Reports
            where reportID = ?
            """; // (2)
            PreparedStatement statment = db.getConnection().prepareStatement(sql); // (3)
            statment.setInt(1, report.getReportID()); // (4)
            rs = statment.executeUpdate(); // (5)
        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return report;
        }
    }

    public static Report updateReport(Report report) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                         update Reports
                         set clubID = ?, semester = ?, memberChanges = ?, eventSummary = ?, participationStatus = ?, createdDate = ?
                         where reportID = ?
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, report.getClubID());
            statement.setString(2, report.getSemester());
            statement.setString(3, report.getMemberChanges());
            statement.setString(4, report.getEventSummary());
            statement.setString(5, report.getParticipationStatus());
            statement.setString(6, report.getCreatedDate());
            statement.setInt(7, report.getReportID());
            rs = statement.executeUpdate();

        } catch (Exception e) {
            return null;
        }
        System.out.println(rs);
        if (rs == 0) {
            return null;
        } else {
            return report;
        }
    }

    public static boolean hasReportForSemester(int clubId, String semester) {
        DBContext db = DBContext.getInstance();
        boolean exists = false;
        try {
            String sql = """
                     SELECT COUNT(*) FROM Reports WHERE clubID = ? AND semester = ?
                     """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, clubId);
            statement.setString(2, semester);
            ResultSet rs = statement.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }
    
}
