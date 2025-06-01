/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import model.MemberClub;
import model.User;

/**
 *
 * @author knguy
 */
@MultipartConfig
public class EditMemberServlet extends HttpServlet {

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
            out.println("<title>Servlet EditMemberServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditMemberServlet at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        UserDAO userDAO = new UserDAO();

        String userID = request.getParameter("userID");
        String role = request.getParameter("role");
        String clubID_raw = request.getParameter("clubID");

        int clubID = 0;
        if (clubID_raw != null && !clubID_raw.trim().isEmpty()) {
            clubID = Integer.parseInt(clubID_raw);
        }

        User userEdit = userDAO.getUserByUserIdNClubID(Integer.parseInt(userID), user.getMemberClub().getClubID());

        ArrayList<User> userInClub = userDAO.getUsersByClubId(clubID);

        int viceChairmanCount = 0;
        int teamLeaderCount = 0;
        for (User member : userInClub) {
            if ("ViceChairman".equals(member.getMemberClub().getRole())) {
                viceChairmanCount++;
            } else if ("TeamLeader".equals(member.getMemberClub().getRole())) {
                teamLeaderCount++;
            }
        }

        boolean isRoleChangeValid = true;
        String errorMessage = null;

        if ("ViceChairman".equals(role) && !"ViceChairman".equals(userEdit.getRole()) && viceChairmanCount >= 2) {
            isRoleChangeValid = false;
            errorMessage = "This club has had 2 ViceChairman.";
        } else if ("TeamLeader".equals(role) && !"TeamLeader".equals(userEdit.getRole()) && teamLeaderCount >= 3) {
            isRoleChangeValid = false;
            errorMessage = "This club has had 3 TeamLeader.";
        }
        

        if (isRoleChangeValid) {
            MemberClub m = userEdit.getMemberClub();
            m.setRole(role);
            userEdit.setMemberClub(m);
            userEdit = userDAO.updateMemberInClub(userEdit);

            if (userEdit != null) {
                request.setAttribute("alert", "Update Role Successfully!");
                response.sendRedirect("management");
            } else {
                request.setAttribute("error", "Update Role Failed!");
                request.setAttribute("userEdit", userEdit);
                request.getRequestDispatcher("view/edituserinclub.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", errorMessage);
            request.setAttribute("userEdit", userEdit);
            request.getRequestDispatcher("view/edituserinclub.jsp").forward(request, response);
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

}
