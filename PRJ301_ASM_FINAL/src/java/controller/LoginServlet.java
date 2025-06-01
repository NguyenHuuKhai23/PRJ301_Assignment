/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author thais
 */
public class LoginServlet extends HttpServlet {

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
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
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
        Cookie[] cookies = request.getCookies();
        String email = "";
        String pass = "";
        String remember = ""; // Đổi thành "" thay vì null để tránh lỗi
        
        String action = request.getParameter("action");
        if ("signup".equals(action)) {
            // Xóa cookie nếu chuyển đến Sign Up
            clearCookies(response);
            // Chuyển hướng hoặc forward đến trang Sign Up
            response.sendRedirect("signup.jsp"); // Thay "signup.jsp" bằng đường dẫn thực tế
            return;
        }
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("email".equals(cookie.getName())) {
                    email = cookie.getValue();
                } else if ("password".equals(cookie.getName())) {
                    pass = cookie.getValue();
                } else if ("remember".equals(cookie.getName())) {
                    remember = cookie.getValue();
                }
            }
        }

        // Gán giá trị cho request attributes
        request.setAttribute("email_sv", email);
        request.setAttribute("pass_sv", pass);
        // Nếu remember là "true", gán "checked", nếu không thì để trống
        request.setAttribute("remember", "true".equals(remember) ? "checked" : "");

        request.getRequestDispatcher("view/login.jsp").forward(request, response);
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
        PrintWriter out = response.getWriter();

        String email_sv = request.getParameter("email");
        String pass_sv = request.getParameter("pass");
        String remember = request.getParameter("remember"); // "on" hoặc null

        User user = UserDAO.getUsersByEmailAndPassword(email_sv, pass_sv);

        if (user == null) {
            request.setAttribute("email_sv", email_sv);
            request.setAttribute("pass_sv", pass_sv);
            request.setAttribute("remember", "on".equals(remember) ? "checked" : ""); // Giữ trạng thái checkbox
            String error = "Not match username and password!";
            request.setAttribute("error", error);
            request.getRequestDispatcher("view/login.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            if ("on".equals(remember)) {
                // Lưu cookie khi chọn "Remember me"
                Cookie emailCookie = new Cookie("email", email_sv);
                Cookie passCookie = new Cookie("password", pass_sv);
                Cookie remCookie = new Cookie("remember", "true");

                emailCookie.setMaxAge(24 * 60 * 60);
                passCookie.setMaxAge(24 * 60 * 60);
                remCookie.setMaxAge(24 * 60 * 60);

                response.addCookie(emailCookie);
                response.addCookie(passCookie);
                response.addCookie(remCookie);
            } else {
                // Xóa cookie khi không chọn "Remember me"
                clearCookies(response);
            }

            response.sendRedirect("home");
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
    
    // Phương thức để xóa tất cả cookie liên quan
    private void clearCookies(HttpServletResponse response) {
        Cookie emailCookie = new Cookie("email", "");
        Cookie passCookie = new Cookie("password", "");
        Cookie remCookie = new Cookie("remember", "");

        emailCookie.setMaxAge(0);
        passCookie.setMaxAge(0);
        remCookie.setMaxAge(0);

        response.addCookie(emailCookie);
        response.addCookie(passCookie);
        response.addCookie(remCookie);
    }
}
