/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.ArrayList;
import model.Club;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import model.MemberClub;

/**
 *
 * @author knguy
 */
public class ClubDAO {

    public static ArrayList<Club> getClubs() {
        DBContext db = DBContext.getInstance();

        ArrayList<Club> clubs = new ArrayList<>();

        try {
            String sql = """
                         select * from Clubs
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Club club = new Club(rs.getInt("clubID"),
                        rs.getString("clubName"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getString("establishedDate")
                );
                clubs.add(club);
            }
        } catch (Exception e) {
            return null;
        }
        return clubs;
    }

    public static ArrayList<Club> getClubsByUserID(int userID) {
        DBContext db = DBContext.getInstance();

        ArrayList<Club> clubs = new ArrayList<>();

        try {
            String sql = """ 
                         select m.clubID, c.clubName, c.description, c.image, c.establishedDate, 
                         m.userID, m.roleClub from  MemberClubs m 
                            join Clubs c on m.clubID = c.clubID where userID = ?
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Club club = new Club(rs.getInt("clubID"),
                        rs.getString("clubName"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getString("establishedDate"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                clubs.add(club);
            }
        } catch (Exception e) {
            return null;
        }
        return clubs;
    }

    public static Club getClubByClubId(int clubId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<Club> clubs = new ArrayList<Club>();
        try {
            String sql = """
                          select m.clubID, c.clubName, c.description, c.image, c.establishedDate, 
                                                     m.userID, m.roleClub from  MemberClubs m 
                                                        join Clubs c on m.clubID = c.clubID where c.clubID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, clubId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                Club club = new Club(rs.getInt("clubID"),
                        rs.getString("clubName"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getString("establishedDate"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                clubs.add(club);
            }
        } catch (Exception e) {
            return null;
        }
        return clubs.get(0);
    }
    
    public static Club getClubByClubIdNoClubMember(int clubId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<Club> clubs = new ArrayList<Club>();
        try {
            String sql = """
                         select * from Clubs where clubID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, clubId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) { // (6)
                Club club = new Club(rs.getInt("clubID"),
                        rs.getString("clubName"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getString("establishedDate"),
                        new MemberClub());
                clubs.add(club);
            }
        } catch (Exception e) {
            return null;
        }
        return clubs.get(0);
    }

    public static Club addClub(Club club) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            insert into Clubs(clubName, description, establishedDate, image)
            values (?, ?, ?, ?)
            """; // (2)

            LocalDate today = LocalDate.now();

            // Định dạng ngày theo "yyyy-MM-dd"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = today.format(formatter);

            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setString(1, club.getClubName());
            statement.setString(2, club.getDescription());
            statement.setString(3, formattedDate);
            statement.setString(4, club.getImage());
            rs = statement.executeUpdate(); // (5)

        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return club;
        }
    }
    

    public static Club deleteClub(Club club) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            delete from Clubs
            where clubID = ?
            """; // (2)
            PreparedStatement statment = db.getConnection().prepareStatement(sql); // (3)
            statment.setInt(1, club.getClubID()); // (4)
            rs = statment.executeUpdate(); // (5)
        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return club;
        }
    }

    public static Club updateClub(Club club) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                         update Clubs
                         set clubName = ?, description = ?, establishedDate = ?, image = ?
                         where clubID = ?
                        """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setString(1, club.getClubName());
            statement.setString(2, club.getDescription());
            statement.setString(3, club.getEstablishedDate());
            statement.setString(4, club.getImage());
            statement.setInt(5, club.getClubID());
            rs = statement.executeUpdate();

        } catch (Exception e) {
            return null;
        }
        System.out.println(rs);
        if (rs == 0) {
            return null;
        } else {
            return club;
        }
    }

    public static boolean isClubNameDuplicate(String clubName, int clubId) {
        DBContext db = DBContext.getInstance();
        try {
            String sql = "SELECT COUNT(*) FROM Clubs WHERE clubName = ? AND clubID != ?";
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setNString(1, clubName);
            statement.setInt(2, clubId);
            ResultSet rs = statement.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Tên câu lạc bộ đã bị trùng
            }
        } catch (Exception e) {
            return true; // Trả về true nếu có lỗi để tránh trùng lặp dữ liệu
        }
        return false; // Không bị trùng
    }

    public static boolean isClubNameDuplicate(String clubName) {
        DBContext db = DBContext.getInstance();

        try {
            // Use LOWER() for case-insensitive comparison
            String sql = "SELECT COUNT(*) FROM Clubs WHERE LOWER(clubName) = LOWER(?)";
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setNString(1, clubName);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // True if there’s at least one duplicate
            }
        } catch (Exception e) {
        } 
        return false; // No duplicates found
    }
}
