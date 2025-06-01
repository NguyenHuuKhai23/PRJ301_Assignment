/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.ClubJoinApplication;
import model.MemberClub;
import model.User;

/**
 *
 * @author knguy
 */
public class ClubJoinApplicationDAO {
    
    //OK
    public static ArrayList<ClubJoinApplication> getClubJoinApplications() {
        DBContext db = DBContext.getInstance();

        ArrayList<ClubJoinApplication> clubJoinApplications = new ArrayList<>();

        try {
            String sql = """
                         select * from ClubJoinApplications
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ClubJoinApplication clubJoinApplication = new ClubJoinApplication(
                        rs.getInt("applicationID"),
                        rs.getInt("userID"),
                        rs.getInt("clubID"),
                        rs.getString("status"),
                        rs.getString("applicationDate"));
                clubJoinApplications.add(clubJoinApplication);
            }
        } catch (Exception e) {
            return null;
        }
        return clubJoinApplications;
    }

    public static ClubJoinApplication getClubJoinApplicationByApplicationId(int applicationId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<ClubJoinApplication> clubJoinApplications = new ArrayList<>();
        try {
            String sql = """
                            select * from ClubJoinApplications where applicationID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, applicationId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) {
                ClubJoinApplication clubJoinApplication = new ClubJoinApplication(
                        rs.getInt("applicationID"),
                        rs.getInt("userID"),
                        rs.getInt("clubID"),
                        rs.getString("status"),
                        rs.getString("applicationDate"));
                clubJoinApplications.add(clubJoinApplication);
            }
        } catch (Exception e) {
            return null;
        }
        return clubJoinApplications.get(0);
    }

    public static ArrayList<ClubJoinApplication> getClubJoinApplicationsWithUserName(User user) {
        DBContext db = DBContext.getInstance();

        ArrayList<ClubJoinApplication> clubJoinApplications = new ArrayList<>();

        try {
            String sql = """
                         select cj.applicationID, cj.userID, cj.clubID, u.fullName, cj.status, cj.applicationDate 
                         from ClubJoinApplications cj join Users u on cj.userID = u.userID join Clubs clb on clb.clubID = cj.clubID
                         where cj.clubID = ?
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, user.getMemberClub().getClubID());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ClubJoinApplication clubJoinApplication = new ClubJoinApplication(
                        rs.getInt("applicationID"),
                        rs.getInt("userID"),
                        user.getMemberClub().getClubID(),
                        rs.getString("status"),
                        rs.getString("applicationDate"),
                        rs.getString("fullName"));
                clubJoinApplications.add(clubJoinApplication);
            }
        } catch (Exception e) {
            return null;
        }
        return clubJoinApplications;
    }

    public static ClubJoinApplication addClubJoinApplication(ClubJoinApplication clubJoinApplication) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            INSERT INTO ClubJoinApplications (userID, clubID, status, applicationDate)
            VALUES (?, ?, 'Waiting', GETDATE())
            """; // (2)

            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, clubJoinApplication.getUserID());
            statement.setInt(2, clubJoinApplication.getClubID());
            rs = statement.executeUpdate(); // (5)

        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return clubJoinApplication;
        }
    }

    public static ClubJoinApplication updateClubJoinApplication(ClubJoinApplication clubJoinApplication) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                         update ClubJoinApplications
                         set status = ?
                         where applicationID = ?
                        """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setString(1, clubJoinApplication.getStatus());
            statement.setInt(2, clubJoinApplication.getApplicationID());
            rs = statement.executeUpdate();

        } catch (Exception e) {
            return null;
        }
        System.out.println(rs);
        if (rs == 0) {
            return null;
        } else {
            return clubJoinApplication;
        }
    }

    public static boolean isUserWaitingForApproval(int userID, int clubID) {
        DBContext db = DBContext.getInstance();
        try {
            String sql = """
                     SELECT COUNT(*) AS count FROM ClubJoinApplications 
                     WHERE userID = ? and clubID = ? AND status = 'Waiting'
                     """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, userID);
            statement.setInt(2, clubID);
            ResultSet rs = statement.executeQuery();
            if (rs.next() && rs.getInt("count") > 0) {
                return true; // Người dùng có ít nhất một đơn đang chờ duyệt
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Không có đơn nào đang chờ
    }
    
}
