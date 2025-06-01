/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ClubDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import model.Club;
import model.User;

/**
 *
 * @author thais
 */
public class MyClubServlet extends HttpServlet {

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
            out.println("<title>Servlet MyClubServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MyClubServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        UserDAO udao = new UserDAO();
        ClubDAO cdao = new ClubDAO();

        if (user == null) {
            request.getRequestDispatcher("view/login.jsp").forward(request, response);
            return;
        }
        
        String action = request.getParameter("action");

        if (action == null) {
            ArrayList<Club> clubs = ClubDAO.getClubsByUserID(user.getUserID());

            session.setAttribute("user", user);
            request.setAttribute("clubs", clubs);
            request.setAttribute("user", user);
            request.getRequestDispatcher("view/myclubs.jsp").forward(request, response);
        } else if (action.equals("view")) {
            String clubID = request.getParameter("clubID");
            String userID = request.getParameter("userID");
            
            if (clubID != null && userID != null) {
                Club c = cdao.getClubByClubId(Integer.parseInt(clubID));
                User u = udao.getUserByUserIdNClubID(user.getUserID(), Integer.parseInt(clubID));
                
                
                session.setAttribute("selectedClub", c);
                session.setAttribute("user", u);
                
                // Chuyển hướng sang management với userID và clubID
                response.sendRedirect("management?userID=" + userID + "&clubID=" + clubID);
                return;
            }
        }
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}