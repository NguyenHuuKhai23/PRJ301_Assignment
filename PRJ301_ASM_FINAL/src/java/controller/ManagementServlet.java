package controller;

import dao.ClubDAO;
import dao.ClubJoinApplicationDAO;
import dao.EventDAO;
import dao.HistoryDAO;
import dao.ReportDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.Club;
import model.ClubJoinApplication;
import model.Event;
import model.History;
import model.MemberClub;
import model.Report;
import model.User;

public class ManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // Khởi tạo các DAO
        UserDAO udao = new UserDAO();
        ClubDAO cdao = new ClubDAO();
        EventDAO edao = new EventDAO();
        ReportDAO rdao = new ReportDAO();
        ClubJoinApplicationDAO cjadao = new ClubJoinApplicationDAO();

        // Lấy tham số từ request
        String searchTerm = request.getParameter("searchTerm");
        String tab = request.getParameter("tab");
        String action = request.getParameter("action");
        String clubIDStr = request.getParameter("clubID");

        // Xử lý clubID từ request
        int clubId = (user.getMemberClub() != null) ? user.getMemberClub().getClubID() : 0;
        if (clubIDStr != null && !clubIDStr.isEmpty()) {
            clubId = Integer.parseInt(clubIDStr);
        }

        // Các danh sách dữ liệu
        ArrayList<User> users = null;
        ArrayList<User> userInClub = null;
        ArrayList<Club> clubs = cdao.getClubs();
        ArrayList<Event> events = edao.getEvents();
        ArrayList<Report> reports = rdao.getReports();
        ArrayList<ClubJoinApplication> joinApplications = cjadao.getClubJoinApplicationsWithUserName(user);

        // Xử lý theo vai trò
        if ("Admin".equals(user.getRole())) {
            // Xử lý cho Admin
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                users = udao.searchByStudentId(searchTerm.trim());
            } else {
                users = udao.getUsers();
            }

            if ("delete".equals(action)) {
                String userIDToDelete = request.getParameter("userID");
                if (userIDToDelete != null && !userIDToDelete.isEmpty()) {
                    User userToDelete = udao.getUserByUserId(Integer.parseInt(userIDToDelete));
                    if (userToDelete != null) {
                        udao.deleteUser(userToDelete);
                        String historyAction = "UserID " + user.getUserID() + " (" + user.getRole() + ")"
                                + " has deleted " + userToDelete.getFullName() + " (userID: " + userIDToDelete + ")";
                        HistoryDAO.insertHistory(new History(0, user.getUserID(), historyAction, getCurrentTime()));
                    }
                }
                String userPageStr = request.getParameter("userPage");
                int userPage = (userPageStr != null) ? Integer.parseInt(userPageStr) : 1;
                response.sendRedirect("management?tab=user&userPage=" + userPage);
                return;
            } else if ("deleteClub".equals(action)) {
                String clubID = request.getParameter("clubID");
                if (clubID != null && !clubID.isEmpty()) {
                    Club clubToDelete = cdao.getClubByClubId(Integer.parseInt(clubID));
                    if (clubToDelete != null) {
                        cdao.deleteClub(clubToDelete);
                    }
                }
                String clubPageStr = request.getParameter("clubPage");
                int clubPage = (clubPageStr != null) ? Integer.parseInt(clubPageStr) : 1;
                response.sendRedirect("management?tab=club&clubPage=" + clubPage);
                return;
            } else if ("deleteEvent".equals(action)) {
                String eventID = request.getParameter("eventID");
                if (eventID != null && !eventID.isEmpty()) {
                    Event eventToDelete = edao.getEventByEventId(Integer.parseInt(eventID));
                    if (eventToDelete != null) {
                        edao.deleteEvent(eventToDelete);
                    }
                    String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")"
                            + " has deleted event by " + eventToDelete.getEventName() + " (userID: " + eventToDelete.getEventID() + ")";
                    HistoryDAO.insertHistory(new History(0, user.getUserID(), historyAction, getCurrentTime()));
                    request.setAttribute("alert", "Delete report Successfully!");
                }
                String eventPageStr = request.getParameter("eventPage");
                int eventPage = (eventPageStr != null) ? Integer.parseInt(eventPageStr) : 1;
                response.sendRedirect("management?tab=event&eventPage=" + eventPage);
                return;
            } else if ("deletereport".equals(action)) {
                String reportID = request.getParameter("reportID");
                if (reportID != null && !reportID.trim().isEmpty()) {
                    Report report = rdao.getReportByReportId(Integer.parseInt(reportID));
                    Club c = cdao.getClubByClubId(report.getClubID());
                    report = rdao.deleteReport(report);
                    if (report != null) {
                        String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")"
                                + " has deleted report by " + c.getClubName() + " (userID: " + c.getClubID() + ")";
                        HistoryDAO.insertHistory(new History(0, user.getUserID(), historyAction, getCurrentTime()));
                        request.setAttribute("alert", "Delete report Successfully!");
                    } else {
                        request.setAttribute("alert", "Delete report failed!");
                    }
                }
                String reportPageStr = request.getParameter("reportPage");
                int reportPage = (reportPageStr != null) ? Integer.parseInt(reportPageStr) : 1;
                response.sendRedirect("management?tab=report&reportPage=" + reportPage);
                return;
            } else if ("editinall".equals(action)) {
                String userIDToEdit = request.getParameter("userID");
                String clubID = request.getParameter("clubID");
                User u;
                
                if (clubID == null) {
                    u = udao.getUserByUserId(Integer.parseInt(userIDToEdit));
                } else {
                    u = udao.getUserByUserIdNClubID(Integer.parseInt(userIDToEdit), Integer.parseInt(clubID));
                }

                String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")"
                        + " has edited information user " + u.getFullName() + " (userID: " + u.getUserID() + ")";
                History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
                HistoryDAO.insertHistory(history);

                session.setAttribute("user", user);
                request.setAttribute("userEdit", u);
                request.getRequestDispatcher("view/edituserbyadmin.jsp").forward(request, response);
                return;
            }
        } else {
            // Xử lý cho User (Chairman, ViceChairman, TeamLeader, Member)
            users = udao.getUsers();
            if (clubId != 0) {
                if ("clubmember".equals(tab) && searchTerm != null && !searchTerm.trim().isEmpty()) {
                    userInClub = udao.searchUsersInClubByStudentIdOrName(clubId, searchTerm.trim());
                } else {
                    userInClub = udao.getUsersByClubId(clubId);
                }
            } else {
                userInClub = new ArrayList<>();
            }

            String role = user.getMemberClub() != null ? user.getMemberClub().getRole() : "";
            if ("Chairman".equals(role) || "ViceChairman".equals(role)) {
                if ("acp".equals(action)) {
                    String applicationID_raw = request.getParameter("applicationID");
                    int applicationID = Integer.parseInt(applicationID_raw);
                    ClubJoinApplication application = cjadao.getClubJoinApplicationByApplicationId(applicationID);
                    User userA = udao.getUserByUserId(application.getUserID());
                    Club clubA = cdao.getClubByClubId(application.getClubID());
                    if (userA != null && clubA != null) {
                        MemberClub m = new MemberClub(userA.getUserID(), clubA.getClubID(), "");
                        m = udao.addMemberClub(m);
                        if (m != null) {
                            application.setStatus("Accept");
                            cjadao.updateClubJoinApplication(application);
                        }
                    }

                    String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")"
                            + " has accepted " + userA.getFullName() + " (userID: " + userA.getUserID() + ")";
                    History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
                    HistoryDAO.insertHistory(history);

                    response.sendRedirect("management?clubID=" + clubId);
                    return;
                } else if ("ref".equals(action)) {
                    String applicationID_raw = request.getParameter("applicationID");
                    int applicationID = Integer.parseInt(applicationID_raw);
                    ClubJoinApplication application = cjadao.getClubJoinApplicationByApplicationId(applicationID);
                    User userA = udao.getUserByUserId(application.getUserID());
                    application.setStatus("Refuse");
                    cjadao.updateClubJoinApplication(application);

                    String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")"
                            + " has refused for user " + userA.getFullName() + " (userID: " + userA.getUserID() + ")";
                    History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
                    HistoryDAO.insertHistory(history);

                    response.sendRedirect("management?clubID=" + clubId);
                    return;
                } else if ("kick".equals(action)) {
                    String userIDToKick = request.getParameter("userID");
                    String clubIDKick = request.getParameter("clubID");
                    User u = udao.getUserByUserIdNClubID(Integer.parseInt(userIDToKick), Integer.parseInt(clubIDKick));
                    if (u != null) {
                        udao.kickUser(u);
                    }

                    String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")"
                            + " has kick user " + u.getFullName() + " (userID: " + u.getUserID() + ")";
                    History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
                    HistoryDAO.insertHistory(history);

                    response.sendRedirect("management?clubID=" + clubId);
                    return;
                } else if ("editinclub".equals(action)) {
                    String userIDToEdit = request.getParameter("userID");
                    User u = udao.getUserByUserIdNClubID(Integer.parseInt(userIDToEdit), user.getMemberClub().getClubID());

                    String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")"
                            + " has edit information " + u.getFullName() + " (userID: " + u.getUserID() + ") in club";
                    History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
                    HistoryDAO.insertHistory(history);

                    session.setAttribute("user", user);
                    request.setAttribute("userEdit", u);
                    request.getRequestDispatcher("view/edituserinclub.jsp").forward(request, response);
                    return;
                } else if ("deletereport".equals(action)) {
                    String reportID = request.getParameter("reportID");
                    if (reportID != null && !reportID.trim().isEmpty()) {
                        Report report = rdao.getReportByReportId(Integer.parseInt(reportID));
                        Club c = cdao.getClubByClubId(report.getClubID());
                        report = rdao.deleteReport(report);
                        if (report != null) {

                            String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")"
                                    + " has deleted report by " + c.getClubName() + " (clubID: " + c.getClubID() + ")";
                            HistoryDAO.insertHistory(new History(0, user.getUserID(), historyAction, getCurrentTime()));

                            request.setAttribute("alert", "Delete report Successfully!");
                        } else {
                            request.setAttribute("alert", "Delete report failed!");
                        }
                    }
                    String reportPageChairmanStr = request.getParameter("reportPageChairman");
                    int reportPageChairman = (reportPageChairmanStr != null) ? Integer.parseInt(reportPageChairmanStr) : 1;
                    response.sendRedirect("management?tab=report&reportPageChairman=" + reportPageChairman + "&clubID=" + clubId);
                    return;
                } else if ("editreport".equals(action)) {
                    String reportID = request.getParameter("reportID");
                    Report report = rdao.getReportByReportId(Integer.parseInt(reportID));
                    Club c = cdao.getClubByClubId(report.getClubID());

                    String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")"
                            + " has edited report by " + c.getClubName() + " (clubID: " + c.getClubID() + ")";
                    HistoryDAO.insertHistory(new History(0, user.getUserID(), historyAction, getCurrentTime()));

                    session.setAttribute("user", user);
                    request.setAttribute("report", report);
                    request.getRequestDispatcher("view/editreport.jsp").forward(request, response);
                    return;
                }
            }
        }

        // Phân trang
        int pageSize = 10;
        int reportPageSize = 5;

        // Phân trang cho Users (Admin)
        if (users != null) {
            String userPageStr = request.getParameter("userPage");
            int userPage = (userPageStr != null) ? Integer.parseInt(userPageStr) : 1;
            int totalUsers = users.size();
            int totalUserPages = (int) Math.ceil((double) totalUsers / pageSize);
            int userStart = (userPage - 1) * pageSize;
            int userEnd = Math.min(userStart + pageSize, totalUsers);
            request.setAttribute("userList", new ArrayList<>(users.subList(userStart, userEnd)));
            request.setAttribute("currentUserPage", userPage);
            request.setAttribute("totalUserPages", totalUserPages);
            request.setAttribute("userOffset", userStart);
            request.setAttribute("totalUsers", totalUsers);
        }

        // Phân trang cho Clubs
        String clubPageStr = request.getParameter("clubPage");
        int clubPage = (clubPageStr != null) ? Integer.parseInt(clubPageStr) : 1;
        int totalClubs = clubs.size();
        int totalClubPages = (int) Math.ceil((double) totalClubs / pageSize);
        int clubStart = (clubPage - 1) * pageSize;
        int clubEnd = Math.min(clubStart + pageSize, totalClubs);
        request.setAttribute("clubList", new ArrayList<>(clubs.subList(clubStart, clubEnd)));
        request.setAttribute("currentClubPage", clubPage);
        request.setAttribute("totalClubPages", totalClubPages);
        request.setAttribute("clubOffset", clubStart);

        // Phân trang cho Events
        String eventPageStr = request.getParameter("eventPage");
        int eventPage = (eventPageStr != null) ? Integer.parseInt(eventPageStr) : 1;
        int totalEvents = events.size();
        int totalEventPages = (int) Math.ceil((double) totalEvents / pageSize);
        int eventStart = (eventPage - 1) * pageSize;
        int eventEnd = Math.min(eventStart + pageSize, totalEvents);
        request.setAttribute("eventList", new ArrayList<>(events.subList(eventStart, eventEnd)));
        request.setAttribute("currentEventPage", eventPage);
        request.setAttribute("totalEventPages", totalEventPages);
        request.setAttribute("eventOffset", eventStart);

        // Phân trang cho Club Members (Chairman/ViceChairman)
        if (userInClub != null) {
            String chairmanPageStr = request.getParameter("chairmanPage");
            int chairmanPage = (chairmanPageStr != null) ? Integer.parseInt(chairmanPageStr) : 1;
            int totalClubMembers = userInClub.size();
            int totalChairmanPages = (int) Math.ceil((double) totalClubMembers / pageSize);
            int chairmanStart = (chairmanPage - 1) * pageSize;
            int chairmanEnd = Math.min(chairmanStart + pageSize, totalClubMembers);
            request.setAttribute("clubMemberList", new ArrayList<>(userInClub.subList(chairmanStart, chairmanEnd)));
            request.setAttribute("currentChairmanPage", chairmanPage);
            request.setAttribute("totalChairmanPages", totalChairmanPages);
            request.setAttribute("chairmanOffset", chairmanStart);
        }

        // Phân trang cho Reports (Admin)
        String reportPageStr = request.getParameter("reportPage");
        int reportPage = (reportPageStr != null) ? Integer.parseInt(reportPageStr) : 1;
        int totalReports = reports.size();
        int totalReportPages = (int) Math.ceil((double) totalReports / reportPageSize);
        int reportStart = (reportPage - 1) * reportPageSize;
        int reportEnd = Math.min(reportStart + reportPageSize, totalReports);
        request.setAttribute("reports", new ArrayList<>(reports.subList(reportStart, reportEnd)));
        request.setAttribute("currentReportPage", reportPage);
        request.setAttribute("totalReportPages", totalReportPages);
        request.setAttribute("reportOffset", reportStart);

        // Phân trang cho Reports (Chairman/ViceChairman)
        ArrayList<Report> clubReports = new ArrayList<>();
        if (clubId != 0) {
            for (Report report : reports) {
                if (report.getClubID() == clubId) {
                    clubReports.add(report);
                }
            }
        }
        String reportPageChairmanStr = request.getParameter("reportPageChairman");
        int reportPageChairman = (reportPageChairmanStr != null) ? Integer.parseInt(reportPageChairmanStr) : 1;
        int totalClubReports = clubReports.size();
        int totalReportPagesChairman = (int) Math.ceil((double) totalClubReports / reportPageSize);
        int reportStartChairman = (reportPageChairman - 1) * reportPageSize;
        int reportEndChairman = Math.min(reportStartChairman + reportPageSize, totalClubReports);
        request.setAttribute("reportsChairman", new ArrayList<>(clubReports.subList(reportStartChairman, reportEndChairman)));
        request.setAttribute("currentReportPageChairman", reportPageChairman);
        request.setAttribute("totalReportPagesChairman", totalReportPagesChairman);
        request.setAttribute("reportOffsetChairman", reportStartChairman);

        // Thiết lập các thuộc tính chung
        session.setAttribute("user", user);
        request.setAttribute("user", user);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("clubJoinApplications", joinApplications);

        request.setAttribute("activeTab", (clubIDStr != null && !clubIDStr.isEmpty()) ? "clubmember"
                : (tab != null ? tab : ("Admin".equals(user.getRole()) ? "user" : "clubmember")));

        request.getRequestDispatcher("view/management.jsp").forward(request, response);
    }

    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for managing users, clubs, events, and reports";
    }
}
