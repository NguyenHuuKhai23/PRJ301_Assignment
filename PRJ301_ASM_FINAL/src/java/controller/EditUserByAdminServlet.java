package controller;

import dao.ClubDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.regex.Pattern;
import model.Club;
import model.MemberClub;
import model.User;

@MultipartConfig
public class EditUserByAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển hướng về trang chỉnh sửa với dữ liệu cần thiết
        UserDAO userDAO = new UserDAO();
        ClubDAO clubDAO = new ClubDAO();
        User userEdit;
        String userID = request.getParameter("userID");
        String clubID = request.getParameter("clubID");

        if (!clubID.equals("N/A")) {
            userEdit = userDAO.getUserByUserIdNClubID(Integer.parseInt(userID), Integer.parseInt(clubID));
        } else {
            userEdit = userDAO.getUserByUserId(Integer.parseInt(userID));
        }

        ArrayList<Club> clubs = clubDAO.getClubs();
        request.setAttribute("userEdit", userEdit);
        request.setAttribute("clubs", clubs);
        request.getRequestDispatcher("view/edituserbyadmin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User adminUser = (User) session.getAttribute("user");
        UserDAO userDAO = new UserDAO();
        ClubDAO clubDAO = new ClubDAO();
        ArrayList<Club> clubs = clubDAO.getClubs();

        // Lấy thông tin từ form
        String userID = request.getParameter("userID");
        String fullName = request.getParameter("fullName");
        String studentID = request.getParameter("studentID");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String clubIDRaw = request.getParameter("clubID");

        // Lấy thông tin user cần chỉnh sửa
        String old = request.getParameter("old");
        User userEdit;
        if (old.equals("0")) {
            userEdit = userDAO.getUserByUserId(Integer.parseInt(userID));
        } else {
            userEdit = userDAO.getUserByUserIdNClubID(Integer.parseInt(userID), Integer.parseInt(old));
        }
        if (userEdit == null) {
            request.setAttribute("error", "User not found!");
            prepareFormData(request, userEdit, clubs);
            request.getRequestDispatcher("view/edituserbyadmin.jsp").forward(request, response);
            return;
        }

        // Xử lý clubID
        int clubID;

        try {
            clubID = Integer.parseInt(clubIDRaw);
            if (clubID < 0 && clubs.stream().noneMatch(c -> c.getClubID() == clubID)) {
                request.setAttribute("error", "Invalid Club ID! ClubID: 0 is not in any club");
                prepareFormData(request, userEdit, clubs);
                request.getRequestDispatcher("view/edituserbyadmin.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Club ID format!");
            prepareFormData(request, userEdit, clubs);
            request.getRequestDispatcher("view/edituserbyadmin.jsp").forward(request, response);
            return;
        }

        // Kiểm tra dữ liệu
        if (!isValidEmail(email)) {
            request.setAttribute("error", "Invalid email format!");
            prepareFormData(request, userEdit, clubs);
            request.getRequestDispatcher("view/edituserbyadmin.jsp").forward(request, response);
            return;
        }

        if (!email.equals(userEdit.getEmail()) && checkExistEmail(email)) {
            request.setAttribute("error", "Email already exists!");
            prepareFormData(request, userEdit, clubs);
            request.getRequestDispatcher("view/edituserbyadmin.jsp").forward(request, response);
            return;
        }

        if (!studentID.equals(userEdit.getStudentID()) && checkExistStudentID(studentID)) {
            request.setAttribute("error", "Student ID already exists!");
            prepareFormData(request, userEdit, clubs);
            request.getRequestDispatcher("view/edituserbyadmin.jsp").forward(request, response);
            return;
        }

        boolean updated = false;

        if ("Admin".equals(userEdit.getRole())) {
            // Admin: Chỉ cập nhật fullName, email, role
            userEdit.setFullName(fullName);
            userEdit.setEmail(email);
            userEdit.setRole(role);
            updated = userDAO.updateUserByAdmin(userEdit) != null;
        } else {
            // Non-Admin: Cập nhật thông tin cơ bản
            userEdit.setFullName(fullName);
            userEdit.setEmail(email);

            MemberClub currentClub = userEdit.getMemberClub(); // Lấy thông tin club hiện tại
            int currentClubID = (currentClub != null) ? currentClub.getClubID() : -1; // ClubID hiện tại, -1 nếu không có

            if (clubID > 0) {
                if (currentClubID == clubID) {
                    // Đã ở trong club này, chỉ cập nhật role
                    if (currentClub != null) {
                        currentClub.setRole(role);
                        System.out.println(currentClub.toString());
                        userEdit.setMemberClub(currentClub);
                        updated = userDAO.updateUserByAdmin(userEdit) != null;
                        updated = userDAO.updateMemberInClub(userEdit) != null;
                    }
                } else {
                    // Chuyển sang club mới hoặc thêm vào club mới
                    if (currentClubID > 0) {
                        // Đang ở club cũ, xóa khỏi club cũ trước
                        userDAO.kickUser(userEdit);
                    }
                    // Thêm vào club mới với role mặc định "Member" hoặc role được chỉ định
                    MemberClub newClub = new MemberClub(userEdit.getUserID(), clubID, role != null ? role : "Member");
                    userEdit.setMemberClub(newClub);
                    updated = userDAO.addMemberClub(newClub) != null;
                }
            } else {
                // clubID = 0: Xóa khỏi club nếu đang ở trong club
                if (currentClub != null && currentClubID > 0) {
                    userDAO.kickUser(userEdit);
                    userEdit.setMemberClub(null);
                    updated = userDAO.updateUserByAdmin(userEdit) != null;
                } else {
                    // Không ở club nào, chỉ cập nhật role
                    userEdit.setRole(role);
                    updated = userDAO.updateUserByAdmin(userEdit) != null;
                }
            }
        }

        if (updated) {
            if (userEdit.getUserID() == adminUser.getUserID()) {
                session.setAttribute("user", userEdit); // Cập nhật session nếu là chính Admin đang chỉnh sửa
            }
            request.setAttribute("alert", "Profile updated successfully!");
        } else {
            request.setAttribute("alert", "Failed to update profile!");
        }

        prepareFormData(request, userEdit, clubs);
        request.getRequestDispatcher("view/edituserbyadmin.jsp").forward(request, response);
    }

    private void prepareFormData(HttpServletRequest request, User userEdit, ArrayList<Club> clubs) {
        request.setAttribute("userEdit", userEdit);
        request.setAttribute("clubs", clubs);
    }

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final UserDAO userDAO = new UserDAO();

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean checkExistEmail(String email) {
        return userDAO.getUsers().stream().anyMatch(u -> u.getEmail().equals(email));
    }

    public static boolean checkExistStudentID(String studentID) {
        return userDAO.getUsers().stream().anyMatch(u -> u.getStudentID().equals(studentID));
    }

    @Override
    public String getServletInfo() {
        return "Servlet for editing user profiles by Admin";
    }
}
