/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ReportDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.Report;
import model.User;

/**
 *
 * @author thais
 */
public class ReportServlet extends HttpServlet {

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
            out.println("<title>Servlet ReportServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReportServlet at " + request.getContextPath() + "</h1>");
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
//        processRequest(request, response);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        UserDAO udao = new UserDAO();

        if (user == null) {
            // Redirect to login if no user is found in session
            request.getRequestDispatcher("view/login.jsp").forward(request, response);
            return;
        }
        String action = request.getParameter("action");
        session.setAttribute("user", user);
        if (action == null) {
            request.getRequestDispatcher("view/report.jsp").forward(request, response);
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ReportDAO redao = new ReportDAO();
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        String action = request.getParameter("action");

        if (action == null) {
            if (redao.hasReportForSemester(user.getMemberClub().getClubID(), getSemesterCurently())) {
                request.setAttribute("alert", "This time your club has written a report!");
            } else {
                String memberChanges = request.getParameter("memberChanges");
                String eventSummary = request.getParameter("eventSummary");
                String participantStatus = request.getParameter("participantStatus");

                if (memberChanges.trim().isEmpty() || eventSummary.trim().isEmpty() || participantStatus.trim().isEmpty()) {
                    request.setAttribute("error", "Feedback information can't empty!");
                } else {
                    Report report = new Report(0, user.getMemberClub().getClubID(), getSemesterCurently(), memberChanges, eventSummary, participantStatus, getDateCurently());
                    report = redao.addReport(report);
                    if (report != null) {
                        request.setAttribute("alert", "Add Report Successfully!");
                    } else {
                        request.setAttribute("alert", "Add Report Failed!");
                    }
                }
            }
            request.getRequestDispatcher("/view/report.jsp").forward(request, response);
        } else {
            String reportID = request.getParameter("reportID");
            String semester = request.getParameter("semester");
            String memberChanges = request.getParameter("memberChanges");
            String eventSummary = request.getParameter("eventSummary");
            String participantStatus = request.getParameter("participantStatus");

            if (reportID != null && !reportID.trim().isEmpty()) {
                Report report = redao.getReportByReportId(Integer.parseInt(reportID));
                if (report != null) {
                    report.setMemberChanges(memberChanges);
                    report.setParticipationStatus(participantStatus);
                    report.setCreatedDate(getDateCurently());
                    report.setEventSummary(eventSummary);
                    report = redao.updateReport(report);
                    if (report != null) {
                        request.setAttribute("alert", "Update Report Successfully!");
                    } else {
                        request.setAttribute("alert", "Update Report Failed!");
                    }
                } else {
                    request.setAttribute("alert", "Update Report Failed!");
                }
                request.getRequestDispatcher("view/editreport.jsp").forward(request, response);
            }
        }
    }

    public String getSemesterCurently() {
        LocalDate today = LocalDate.now(); // Lấy ngày hiện tại
        int month = today.getMonthValue();
        int year = today.getYear();
        String semester = "";
        System.out.println(month);
        if (month <= 4) {
            semester = "Học kì 1 - " + year;
        } else if (month <= 8 && month > 4) {
            semester = "Học kì 2 - " + year;
        } else {
            semester = "Học kì 3 - " + year;
        }
        return semester;
    }

    public String getDateCurently() {
        // Lấy thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Định dạng ngày giờ theo yêu cầu
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        // Chuyển đổi thành chuỗi
        String formattedDate = now.format(formatter);
        return formattedDate;
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
