package controller;

import dao.HistoryDAO;
import dao.UserDAO;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.History;
import model.User;

@MultipartConfig
public class ProfileServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProfileServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProfileServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if ("edit".equals(action)) {
            request.setAttribute("isEditing", true);
        } else {
            request.setAttribute("isEditing", false);
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("view/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        UserDAO userDAO = new UserDAO();

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy thông tin từ form
        String fullName = request.getParameter("fullName");
        String pass1 = request.getParameter("pass1");
        String pass2 = request.getParameter("pass2");

        // Xử lý ảnh
        Part filePart = request.getPart("img");
        String fileName = filePart != null ? Paths.get(filePart.getSubmittedFileName()).getFileName().toString() : "";

        // Chỉ cập nhật ảnh nếu người dùng tải lên ảnh mới
        if (filePart != null && filePart.getSize() > 0) { // Kiểm tra nếu có file được upload
            ServletContext context = getServletContext();
            String webPath = context.getRealPath("/");
            File projectPath = new File(webPath).getParentFile().getParentFile();
            String uploadPath = projectPath + "/web/img/user";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            String filePath = uploadPath + File.separator + uniqueFileName;
            filePart.write(filePath);
            user.setImage(uniqueFileName); // Chỉ cập nhật ảnh nếu có file mới
        } // Nếu không có file mới, giữ nguyên ảnh cũ (không cần setImage)

        // Xử lý mật khẩu
        if ((pass1 != null && !pass1.trim().isEmpty()) || (pass2 != null && !pass2.trim().isEmpty())) {
            if (!checkConfirmPassword(pass1, pass2)) {
                request.setAttribute("error", "Passwords do not match!");
                request.setAttribute("isEditing", true);
                request.getRequestDispatcher("view/profile.jsp").forward(request, response);
                return;
            } else if (!checkPasswordLength(pass1)) { // Thêm kiểm tra độ dài mật khẩu
                request.setAttribute("error", "Password must be more than 5 characters!");
                request.setAttribute("isEditing", true);
                request.getRequestDispatcher("view/profile.jsp").forward(request, response);
                return;
            } else {
                user.setPassword(pass1);
            }
        }

        user.setFullName(fullName);
        session.setAttribute("user", user);
        userDAO.updateUser(user);

        // Ghi lịch sử cập nhật profile
        String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ") "
                + "has changed personal information";
        History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
        HistoryDAO.insertHistory(history);

        request.setAttribute("alert", "Update Profile Successfully!");
        request.setAttribute("isEditing", false);
        request.getRequestDispatcher("view/profile.jsp").forward(request, response);
    }

    // Hàm hỗ trợ lấy thời gian hiện tại
    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for managing user profile";
    }

    boolean checkConfirmPassword(String password1, String password2) {
        if (password1 == null || password2 == null) {
            return false;
        }
        return password1.equals(password2);
    }

    private boolean checkPasswordLength(String password) {
        return password != null && password.length() > 5;
    }

}
