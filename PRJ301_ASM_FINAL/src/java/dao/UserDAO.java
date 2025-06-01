/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.MemberClub;
import model.User;

/**
 *
 * @author knguy
 */
public class UserDAO {

    //OK
    public static ArrayList<User> getUsers() {
        DBContext db = DBContext.getInstance();

        ArrayList<User> users = new ArrayList<>();

        try {
            String sql = """
                         SELECT u.userID, u.studentID, u.fullName, u.email, u.password, u.role, u.image, m.clubID, m.roleClub
                         FROM Users u 
                         LEFT JOIN MemberClubs m ON u.userID = m.userID
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("userID"), rs.getString("studentID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getString("role"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                users.add(user);
            }
        } catch (Exception e) {
            return null;
        }
        return users;
    }

    //OK
    public static User getUserByUserId(int userId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<User> users = new ArrayList<>();
        try {
            String sql = """
                         select u.userID, studentID, fullName, email, password, role, image, clubID, roleClub
                         from Users u left join MemberClubs m on u.userID = m.userID where u.userID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, userId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) {
                User user = new User(rs.getInt("userID"), rs.getString("studentID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getString("role"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                users.add(user);
            }
        } catch (Exception e) {
            return null;
        }
        return users.get(0);
    }

    //OK
    public static User getUserByUserIdNClubID(int userId, int clubId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<User> users = new ArrayList<>();
        try {
            String sql = """
                         select u.userID, studentID, fullName, email, password, role, image, clubID, roleClub
                         from Users u left join MemberClubs m on u.userID = m.userID where u.userID = ? and m.clubID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, userId); // (4)
            statement.setInt(2, clubId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) {
                User user = new User(rs.getInt("userID"), rs.getString("studentID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getString("role"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                users.add(user);
            }
        } catch (Exception e) {
            return null;
        }
        return users.get(0);
    }

    //OK
    public static User getUsersByStudentId(String studentId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<User> users = new ArrayList<>();
        try {
            String sql = """
                         select u.userID, studentID, fullName, email, password, role, image, clubID, roleClub
                         from Users u left join MemberClubs m on u.userID = m.userID where studentID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setString(1, studentId); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) {
                User user = new User(rs.getInt("userID"), rs.getString("studentID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getString("role"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                users.add(user);
            }
        } catch (Exception e) {
            return null;
        }
        return users.get(0);
    }

    //OK
    public static ArrayList<User> getUsersByClubId(int clubId) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<User> users = new ArrayList<>();
        try {
            String sql = """
                         SELECT u.userID, studentID, fullName, email, password, role, image, clubID, roleClub
                         FROM Users u
                         LEFT JOIN MemberClubs m ON u.userID = m.userID
                         WHERE clubID = ?
                         ORDER BY 
                             CASE roleClub
                                 WHEN ? THEN 1
                                 WHEN ? THEN 2
                                 WHEN ? THEN 3
                                 WHEN ? THEN 4
                                 ELSE 5
                             END;
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, clubId); // (4)
            statement.setString(2, "Chairman");
            statement.setString(3, "ViceChairman");
            statement.setString(4, "TeamLeader");
            statement.setString(5, "Member");
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) {
                User user = new User(rs.getInt("userID"), rs.getString("studentID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getString("role"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                users.add(user);
            }
        } catch (Exception e) {
            return null;
        }
        return users;
    }

    //OK
    public static User getUsersByEmailAndPassword(String email, String password) {
        DBContext db = DBContext.getInstance();
        User user = null; // Chỉ cần 1 user, không cần danh sách

        try {
            String sql = """
                         select u.userID, studentID, fullName, email, password, role, image, clubID, roleClub
                         from Users u left join MemberClubs m on u.userID = m.userID where email = ? AND password = ?
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) { // Nếu có kết quả, tạo User object
                user = new User(rs.getInt("userID"), rs.getString("studentID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getString("role"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
            }

            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console để debug
        }

        return user; // Trả về null nếu không tìm thấy user
    }

    //OK
    public static ArrayList<User> getUsersByRole(String role) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<User> users = new ArrayList<>();
        try {
            String sql = """
                         select u.userID, studentID, fullName, email, password, role, image, clubID, roleClub
                         from Users u left join MemberClubs m on u.userID = m.userID where role = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setString(1, role); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) {
                User user = new User(rs.getInt("userID"), rs.getString("studentID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getString("role"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                users.add(user);
            }
        } catch (Exception e) {
            return null;
        }
        return users;
    }

    //OK
    public static ArrayList<User> getUsersByRoleClub(String roleClub) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<User> users = new ArrayList<>();
        try {
            String sql = """
                         select u.userID, studentID, fullName, email, password, role, image, clubID, roleClub
                         from Users u left join MemberClubs m on u.userID = m.userID where roleClub = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setString(1, roleClub); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) {
                User user = new User(rs.getInt("userID"), rs.getString("studentID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getString("role"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                users.add(user);
            }
        } catch (Exception e) {
            return null;
        }
        return users;
    }

    //OK
    public static ArrayList<User> getUsersByRoleClubNClubID(String roleClub, int clubID) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<User> users = new ArrayList<>();
        try {
            String sql = """
                        select u.userID, studentID, fullName, email, password, role, image, clubID, roleClub
                        from Users u left join MemberClubs m on u.userID = m.userID where roleClub = ? and clubID = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setString(1, roleClub); // (4)
            statement.setInt(2, clubID); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) {
                User user = new User(rs.getInt("userID"), rs.getString("studentID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getString("role"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                users.add(user);
            }
        } catch (Exception e) {
            return null;
        }
        return users;
    }

    //OK
    public static User addUser(User user) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            insert into Users(studentID, fullName, email, password, image)
            values (?, ?, ?, ?, ?)
            """; // (2)

            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setString(1, user.getStudentID());
            statement.setNString(2, user.getFullName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setNString(5, user.getImage());
            rs = statement.executeUpdate(); // (5)

        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return user;
        }
    }

    //OK
    public static MemberClub addMemberClub(MemberClub memberClub) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            insert into MemberClubs(userID, clubID, roleClub)
            values (?, ?, ?)
            """; // (2)

            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, memberClub.getUserID());
            statement.setInt(2, memberClub.getClubID());
            statement.setString(3, "Member");
            rs = statement.executeUpdate(); // (5)

        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return memberClub;
        }
    }
    
    //OK
    public static MemberClub addMemberClubInChange(MemberClub memberClub) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            insert into MemberClubs(userID, clubID, roleClub)
            values (?, ?, ?)
            """; // (2)

            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setInt(1, memberClub.getUserID());
            statement.setInt(2, memberClub.getClubID());
            statement.setString(3, memberClub.getRole());
            rs = statement.executeUpdate(); // (5)

        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return memberClub;
        }
    }

    //OK
    public static User deleteUser(User user) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            delete from Users
            where userID = ?
            """; // (2)
            PreparedStatement statment = db.getConnection().prepareStatement(sql); // (3)
            statment.setInt(1, user.getUserID()); // (4)
            rs = statment.executeUpdate(); // (5)
        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return user;
        }
    }

    public static User updateUser(User user) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                         update Users
                         set fullName = ?, password = ?, image = ?
                         where userID = ?
                        """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setNString(1, user.getFullName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getImage());
            statement.setInt(4, user.getUserID());
            rs = statement.executeUpdate();

        } catch (Exception e) {
            return null;
        }
        System.out.println(rs);
        if (rs == 0) {
            return null;
        } else {
            return user;
        }
    }

    public static User updateUserByAdmin(User user) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                         update Users
                         set fullName = ?, email = ?, role = ?
                         where userID = ?
                        """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setNString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRole());
            statement.setInt(4, user.getUserID());

            rs = statement.executeUpdate();

        } catch (Exception e) {
            return null;
        }
        System.out.println(rs);
        if (rs == 0) {
            return null;
        } else {
            return user;
        }
    }

    public static User updateMemberInClub(User user) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                         update MemberClubs
                         set roleClub = ?
                         where userID = ? and clubID = ?
                        """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setString(1, user.getMemberClub().getRole());
            statement.setInt(2, user.getUserID());
            statement.setInt(3, user.getMemberClub().getClubID());
            rs = statement.executeUpdate();

        } catch (Exception e) {
            return null;
        }
        System.out.println(rs);
        if (rs == 0) {
            return null;
        } else {
            return user;
        }
    }

    public static MemberClub updateMemberClubID(MemberClub memberClub) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                         update MemberClubs
                         set clubID = ?, roleClub ?
                         where userID = ?
                        """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, memberClub.getClubID());
            statement.setString(2, "Member");
            statement.setInt(3, memberClub.getUserID());

            rs = statement.executeUpdate();

        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return memberClub;
        }
    }

    //OK
    public static User getUsersByEmail(String email) {
        DBContext db = DBContext.getInstance(); // (1)
        ArrayList<User> users = new ArrayList<>();
        try {
            String sql = """
                        select u.userID, studentID, fullName, email, password, role, image, clubID, roleClub
                        from Users u left join MemberClubs m on u.userID = m.userID where email = ?
                        """; // (2)
            PreparedStatement statement = db.getConnection().prepareStatement(sql); // (3)
            statement.setString(1, email); // (4)
            ResultSet rs = statement.executeQuery(); // (5)
            while (rs.next()) {
                User user = new User(rs.getInt("userID"), rs.getString("studentID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getString("role"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                users.add(user);
            }
        } catch (Exception e) {
            return null;
        }
        return users.get(0);
    }

    //OK
    public static User updatePassByEmail(User user) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                         update Users 
                         set password = ? 
                         where email = ?
                        """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getEmail());
            rs = statement.executeUpdate();

        } catch (Exception e) {
            return null;
        }
        System.out.println(rs);
        if (rs == 0) {
            return null;
        } else {
            return user;
        }
    }

    public static User kickUser(User user) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
                     delete from MemberClubs
                     where userID = ? and clubID = ?
                     """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, user.getUserID());
            statement.setInt(2, user.getMemberClub().getClubID());
            rs = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        System.out.println(rs);
        if (rs == 0) {
            return null;
        } else {
            return user;
        }
    }

    public static ArrayList<User> searchByStudentId(String searchTerm) {
        DBContext db = DBContext.getInstance();
        ArrayList<User> users = new ArrayList<>();

        try {
            String sql = """
                     select u.userID, studentID, fullName, email, password, role, image, clubID, roleClub
                     from Users u left join MemberClubs m on u.userID = m.userID 
                     WHERE studentID LIKE ? 
                     OR fullName LIKE ? 
                     OR email LIKE ? 
                     OR role LIKE ?
                     """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            // Gán giá trị tìm kiếm cho tất cả các cột
            statement.setString(1, "%" + searchTerm + "%");
            statement.setString(2, "%" + searchTerm + "%");
            statement.setString(3, "%" + searchTerm + "%");
            statement.setString(4, "%" + searchTerm + "%");

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("userID"), rs.getString("studentID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getString("role"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public static ArrayList<User> searchUsersInClubByStudentIdOrName(int clubID, String searchTerm) {
        DBContext db = DBContext.getInstance();
        ArrayList<User> users = new ArrayList<>();

        try {
            String sql = """
                     select u.userID, studentID, fullName, email, password, role, image, clubID, roleClub
                     from Users u left join MemberClubs m on u.userID = m.userID
                         WHERE clubID = ? AND 
                         (studentID LIKE ? OR fullName LIKE ? OR email LIKE ? OR role LIKE ?)
                     """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            // Gán giá trị tìm kiếm cho tất cả các cột
            statement.setInt(1, clubID);
            statement.setString(2, "%" + searchTerm + "%");
            statement.setString(3, "%" + searchTerm + "%");
            statement.setString(4, "%" + searchTerm + "%");
            statement.setString(5, "%" + searchTerm + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("userID"), rs.getString("studentID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getString("role"),
                        new MemberClub(rs.getInt("userID"),
                                rs.getInt("clubID"),
                                rs.getString("roleClub")));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    // Kiểm tra xem user có trong club không
    public static boolean isUserInClub(int userId, int clubId) {
        DBContext db = DBContext.getInstance();
        boolean isInClub = false;

        try {
            String sql = """
                     SELECT COUNT(*) 
                     FROM MemberClubs 
                     WHERE userID = ? AND clubID = ?
                     """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, clubId);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1); // Lấy giá trị đếm từ cột đầu tiên
                isInClub = (count > 0);  // Nếu count > 0, user có trong club
            }

            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }

        return isInClub;
    }

    // Lấy role của user trong club, trả về null nếu user không có trong club
    public static String getUserRoleInClub(int userId, int clubId) {
        DBContext db = DBContext.getInstance();
        String roleClub = null;

        try {
            String sql = """
                    SELECT roleClub 
                    FROM MemberClubs 
                    WHERE userID = ? AND clubID = ?
                    """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, clubId);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                roleClub = rs.getString("roleClub"); // Lấy giá trị roleClub nếu tồn tại
            }

            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi
        }

        return roleClub; // Trả về roleClub hoặc null nếu user không trong club
    }

    // Lấy vai trò cao nhất của user trong tất cả các club
    public static String getHighestRoleInClubs(int userId) {
        DBContext db = DBContext.getInstance();
        String highestRole = null;

        try {
            String sql = """
                    SELECT TOP 1 roleClub 
                    FROM MemberClubs 
                    WHERE userID = ?
                    ORDER BY 
                        CASE roleClub
                            WHEN 'Chairman' THEN 1
                            WHEN 'ViceChairman' THEN 2
                            WHEN 'TeamLeader' THEN 3
                            WHEN 'Member' THEN 4
                            ELSE 5
                        END
                    """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                highestRole = rs.getString("roleClub"); // Lấy vai trò cao nhất
            }

            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi
        }

        return highestRole; // Trả về vai trò cao nhất hoặc null nếu user không trong club nào
    }

}
