/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.EmailService;

/**
 *
 * @author thais
 */
public class ChangePasswordServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ChangePasswordServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChangePasswordServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("view/changepassword.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");

    String email_sv = request.getParameter("email");
    String verificationCode = request.getParameter("verificationCode"); // Mã xác minh từ form
    String pass1 = request.getParameter("pass1");
    String pass2 = request.getParameter("pass2");

    HttpSession session = request.getSession();
    User user = (User) session.getAttribute("user");

    // Bước 1: Nhập email để nhận mã xác minh
    if (pass1 == null && pass2 == null && verificationCode == null) {
        if (email_sv == null || email_sv.trim().isEmpty()) {
            request.setAttribute("error", "Email is required!");
            request.getRequestDispatcher("view/changepassword.jsp").forward(request, response);
        } else {
            try {
                user = UserDAO.getUsersByEmail(email_sv);
                if (user == null) {
                    request.setAttribute("email_sv", email_sv);
                    request.setAttribute("error", "Email does not exist!");
                    request.getRequestDispatcher("view/changepassword.jsp").forward(request, response);
                } else {
                    // Tạo mã xác minh ngẫu nhiên (ví dụ: 6 chữ số)
                    int ran = (int) (Math.random() * 900000) + 100000; // Tạo số ngẫu nhiên từ 100000 đến 999999
                    session.setAttribute("verificationCode", String.valueOf(ran)); // Lưu mã vào session
                    session.setAttribute("user", user); // Lưu user vào session
                    session.setAttribute("email_sv", email_sv); // Lưu email vào session

                    // Gửi email chứa mã xác minh
                    EmailService.sendEmail(email_sv, "Xác minh đổi mật khẩu", "Mã xác minh của bạn là: " + ran);
                    request.setAttribute("email_sv", email_sv);
                    request.getRequestDispatcher("view/checkverificationcode.jsp").forward(request, response);
                }
            } catch (Exception e) {
                request.setAttribute("error", "An error occurred while processing your request!");
                request.getRequestDispatcher("view/changepassword.jsp").forward(request, response);
            }
        }
    } 
    // Bước 2: Kiểm tra mã xác minh
    else if (verificationCode != null && pass1 == null && pass2 == null) {
        String storedCode = (String) session.getAttribute("verificationCode");
        if (verificationCode.equals(storedCode)) {
            // Mã đúng, chuyển sang form nhập mật khẩu
            request.setAttribute("user", user);
            request.getRequestDispatcher("view/changepassword.jsp").forward(request, response);
        } else {
            request.setAttribute("email_sv", email_sv);
            request.setAttribute("error", "Invalid verification code!");
            request.getRequestDispatcher("view/checkverificationcode.jsp").forward(request, response);
        }
    } 
    // Bước 3: Xử lý đổi mật khẩu
    else if (pass1 != null && pass2 != null) {
        if (pass1.trim().isEmpty() || pass2.trim().isEmpty()) {
            request.setAttribute("user", user);
            request.setAttribute("error", "Passwords are required!");
            request.getRequestDispatcher("view/changepassword.jsp").forward(request, response);
        } else if (!pass1.equals(pass2)) {
            request.setAttribute("user", user);
            request.setAttribute("error", "Passwords do not match!");
            request.getRequestDispatcher("view/changepassword.jsp").forward(request, response);
        } else {
            // Cập nhật mật khẩu
            user.setPassword(pass1);
            User updatedUser = UserDAO.updatePassByEmail(user);
            session.removeAttribute("user");
            session.removeAttribute("verificationCode");
            session.removeAttribute("email_sv");
            request.setAttribute("alert", "Password changed successfully!");
            request.getRequestDispatcher("view/login.jsp").forward(request, response);
        }
    }
}

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public boolean checkBlank(String email, String pass1, String pass2) {
        return email == null || email.trim().isEmpty()
                || pass1 == null || pass1.trim().isEmpty()
                || pass2 == null || pass2.trim().isEmpty();
        
    }
}
