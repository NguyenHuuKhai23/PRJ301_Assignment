/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.HistoryDAO;
import dao.NotificationDAO;
import dao.ReadNotificationDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.History;
import model.Notification;
import model.ReadNotification;
import model.User;

/**
 *
 * @author thais
 */
public class NotificationServlet extends HttpServlet {

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
            out.println("<title>Servlet NotificationServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet NotificationServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            request.getRequestDispatcher("view/login.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action");
        ArrayList<Notification> n = NotificationDAO.getNotifications();

        if ("addNotification".equals(action)) {
            request.setAttribute("op", "addNotification");
            request.setAttribute("notification", n);
            request.getRequestDispatcher("view/notification.jsp").forward(request, response);
        } else if ("delete".equals(action)) {
            String notificationId_sv = request.getParameter("notificationID");
            int notificationID = Integer.parseInt(notificationId_sv);
            Notification notification = NotificationDAO.getNotificationById(notificationID);

            NotificationDAO.deleteNotification(notification);

            // Ghi vào History
            String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")" + " has deleted notification for " + notification.getUser().getFullName() + " (userID: " + notification.getUserID() + ")";
            History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
            HistoryDAO.insertHistory(history);

            request.setAttribute("alert", "Notification deleted successfully");
            request.setAttribute("notification", n);
            request.getRequestDispatcher("view/notification.jsp").forward(request, response);
        } else if ("view".equals(action)) {
            String notificationId_sv = request.getParameter("notificationID");
            int notificationID = Integer.parseInt(notificationId_sv);
            Notification notification = NotificationDAO.getNotificationById(notificationID);

            ReadNotification read = new ReadNotification(0, user.getUserID(), notification.getNotificationID(), "");
            ReadNotificationDAO.addReadNotification(read);
            request.setAttribute("alert", "Notification view successfully");
            
            // Ghi vào History
            String historyAction = user.getFullName()+ " (userID: " + user.getUserID()+ ")" + " has viewed notification for " + notification.getUser().getFullName() +" (userID: " + notification.getUserID()+")";
            History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
            HistoryDAO.insertHistory(history);

            request.setAttribute("notification", n);
            request.getRequestDispatcher("view/notification.jsp").forward(request, response);
        } else {
            request.setAttribute("notification", n);
            request.getRequestDispatcher("view/notification.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if ("submitNotification".equals(action)) {
            String content = request.getParameter("content");
            if (content == null || content.trim().isEmpty()) {
                request.setAttribute("error", "Content cannot be empty.");
                request.setAttribute("op", "addNotification");
                request.getRequestDispatcher("view/notification.jsp").forward(request, response);
                return;
            } else {
                Notification notification = new Notification();
                notification.setContent(content);
                notification.setUserID(user.getUserID());
                notification.setClubID(user.getMemberClub().getClubID());
                notification.setNotificationDate(getCurrentTime());
                if (user.getRole().equals("Admin")) {

                    NotificationDAO.addNotificationByAdmin(notification);
                } else {
                    NotificationDAO.addNotification(notification);
                }
                // Ghi vào History
                String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")" + " has add new notification";
                History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
                HistoryDAO.insertHistory(history);

                request.setAttribute("alert", "Notification added successfully.");
            }
        }

        ArrayList<Notification> n = NotificationDAO.getNotifications();
        request.setAttribute("notification", n);
        request.getRequestDispatcher("view/notification.jsp").forward(request, response);
    }
    // Hàm hỗ trợ lấy thời gian hiện tại

    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
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
