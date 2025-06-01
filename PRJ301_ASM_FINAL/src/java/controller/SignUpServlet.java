/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.regex.Pattern;
import model.User;

/**
 *
 * @author knguy
 */
public class SignUpServlet extends HttpServlet {

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
            out.println("<title>Servlet SignUpServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SignUpServlet at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");
        if ("signup".equals(action)) {
            clearCookies(response);
        }

        RequestDispatcher rs = request.getRequestDispatcher("view/signup.jsp");
        rs.forward(request, response);
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
        String studentID = request.getParameter("studentID");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        String check = request.getParameter("check");
        String error = "";

        if (checkBlank(studentID, fullName, email, password1, password2, check)) {
            error += "Registration information cannot be empty!";
            request.setAttribute("error", error);
            request.getRequestDispatcher("view/signup.jsp").forward(request, response);
        } else {
            if (!checkRollNumber(studentID)) {
                error += "Invalid student ID!";
                request.setAttribute("error", error);
                request.getRequestDispatcher("view/signup.jsp").forward(request, response);
            } else {
                if (!checkPasswordLength(password1)) { // Thêm kiểm tra độ dài mật khẩu
                    error += "Password must be more than 5 characters!";
                    request.setAttribute("error", error);
                    request.getRequestDispatcher("view/signup.jsp").forward(request, response);
                } else if (!checkConfirmPassword(password1, password2)) {
                    error += "Passwords do not match!";
                    request.setAttribute("error", error);
                    request.getRequestDispatcher("view/signup.jsp").forward(request, response);
                } else {
                    if (checkExistStudentID(studentID)) {
                        error += "Student already exists!";
                        request.setAttribute("error", error);
                        request.getRequestDispatcher("view/signup.jsp").forward(request, response);
                    } else {
                        if (isValidEmail(email)) {
                            if (checkExistEmail(email)) {
                                error += "Email already exists!";
                                request.setAttribute("error", error);
                                request.getRequestDispatcher("view/signup.jsp").forward(request, response);
                            } else {
                                User user = new User(0, studentID, fullName, email, password2, "no_acc.png", error, null);
                                user = userDAO.addUser(user);
                                if (user != null) {
                                    request.setAttribute("alert", "Successfully registered account!");
                                    request.getRequestDispatcher("view/login.jsp").forward(request, response);
                                } else {
                                    error += "Failed to register account!";
                                    request.setAttribute("error", error);
                                    request.getRequestDispatcher("view/signup.jsp").forward(request, response);
                                }
                            }
                        } else {
                            error += "It is not a valid email!";
                            request.setAttribute("error", error);
                            request.getRequestDispatcher("view/signup.jsp").forward(request, response);
                        }
                    }
                }
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

    boolean checkRollNumber(String rnum) {
        return rnum.matches("^(HA|HE|HS|ha|he|hs)(17|18|19|20)\\d{4}$");
    }

    boolean checkConfirmPassword(String password1, String password2) {
        return password1.equals(password2);
    }

    public boolean checkBlank(String studentID, String fullName, String email, String password1, String password2, String check) {
        return studentID == null || studentID.trim().isEmpty()
                || fullName == null || fullName.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password1 == null || password1.trim().isEmpty()
                || password2 == null || password2.trim().isEmpty()
                || check == null || check.trim().isEmpty();
    }

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static UserDAO userDAO = new UserDAO();

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean checkExistEmail(String email) {
        return UserDAO.getUsers().stream().anyMatch(u -> u.getEmail().equals(email));
    }

    public static boolean checkExistStudentID(String studentID) {
        return UserDAO.getUsers().stream().anyMatch(u -> u.getStudentID().equals(studentID));
    }

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

    private boolean checkPasswordLength(String password) {
        return password != null && password.length() > 5;
    }
}
