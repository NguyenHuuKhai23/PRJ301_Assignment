/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.EventDAO;
import dao.EventFeedbackDAO;
import dao.EventParticipantDAO;
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
import java.util.ArrayList;
import model.Event;
import model.EventFeedback;
import model.EventParticipant;
import model.History;
import model.User;

/**
 *
 * @author thais
 */
@MultipartConfig
public class EventServlet extends HttpServlet {

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
            out.println("<title>Servlet EventServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EventServlet at " + request.getContextPath() + "</h1>");
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
        EventDAO ed = new EventDAO();
        UserDAO udao = new UserDAO();
        EventParticipantDAO epd = new EventParticipantDAO();
        EventFeedbackDAO efd = new EventFeedbackDAO();

        if (user == null) {
            // Redirect to login if no user is found in session
            request.getRequestDispatcher("view/login.jsp").forward(request, response);
            return;
        }
        String action = request.getParameter("action");
        ArrayList<Event> events = ed.getEvents();
        request.setAttribute("events", events);
        request.setAttribute("user", user);
        if (action == null) {
            // Get list of event
            request.getRequestDispatcher("view/event.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            try {
                int eventId = Integer.parseInt(request.getParameter("eventId"));
                Event event = ed.getEventByEventId(eventId);
                event = ed.deleteEvent(event);

                // Ghi vào History
                String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")" + " has deleted event "
                        + event.getEventName() + " (eventID: " + event.getEventID() + ")";
                History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
                HistoryDAO.insertHistory(history);

                request.setAttribute("op", null);
                request.setAttribute("action", null);
                request.setAttribute("event", events);
                request.setAttribute("alert", "Delete Event Successfully!");
                request.getRequestDispatcher("view/event.jsp").forward(request, response);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } else if (action.equals("register")) {
            try {
                int eventId = Integer.parseInt(request.getParameter("eventId"));
                request.setAttribute("event", ed.getEventByEventId(eventId));
                Event event = ed.getEventByEventId(eventId);
                String eventID = request.getParameter("eventId");
                epd.addEventParticipant(new EventParticipant(0, Integer.parseInt(eventID), user.getUserID(), ""));

                // Ghi vào History
                String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")" + " has registed event "
                        + event.getEventName() + " (eventID: " + event.getEventID() + ")";
                History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
                HistoryDAO.insertHistory(history);

                request.setAttribute("alert", "Register Event Successfully!");
                request.getRequestDispatcher("view/event.jsp").forward(request, response);
            } catch (Exception e) {
            }
        } else if (action.equals("addEvent")) {

            // Ghi vào History
            String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")" + " has added new event";
            History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
            HistoryDAO.insertHistory(history);

            request.setAttribute("op", "addevent");
            request.getRequestDispatcher("view/event.jsp").forward(request, response);

        } else if (action.equals("rollcall")) {
            int eventId = Integer.parseInt(request.getParameter("eventId"));
            Event event = ed.getEventByEventId(eventId);
            ArrayList<EventParticipant> eventParticipant = epd.getUserEventParticipants(event);

            request.setAttribute("event", event);
            request.setAttribute("eventParticipant", eventParticipant);
            request.setAttribute("op", "rollcall");
            request.getRequestDispatcher("view/event.jsp").forward(request, response);
        } else if (action.equals("absent")) {
            String eventParticipantID_sv = request.getParameter("eventParticipantID");
            String eventIdStr = request.getParameter("eventId"); // Lấy eventId từ request
            int eventId = Integer.parseInt(request.getParameter("eventId"));
            Event event = ed.getEventByEventId(eventId);
            int eventParticipantID = Integer.parseInt(eventParticipantID_sv);
            EventParticipant eventParticipant = epd.getEventParticipantsByEventParticipantId(eventParticipantID);
            eventParticipant.setStatus("Absent");
            epd.updateEventParticipant(eventParticipant);

            // Ghi vào History
            String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")" + " has marked the eventID "
                    + event.getEventID() + ") absent for UserID " + eventParticipant.getUserID();
            History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
            HistoryDAO.insertHistory(history);

            // Redirect về trang rollcall với eventId tương ứng
            response.sendRedirect("event?action=rollcall&eventId=" + eventIdStr);
            return;
        } else if (action.equals("attended")) {
            int eventId = Integer.parseInt(request.getParameter("eventId"));
            Event event = ed.getEventByEventId(eventId);
            String eventParticipantID_sv = request.getParameter("eventParticipantID");
            String eventIdStr = request.getParameter("eventId"); // Lấy eventId từ request
            int eventParticipantID = Integer.parseInt(eventParticipantID_sv);
            EventParticipant eventParticipant = epd.getEventParticipantsByEventParticipantId(eventParticipantID);
            eventParticipant.setStatus("Attended");
            epd.updateEventParticipant(eventParticipant);

            // Ghi vào History
            String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")" + " has marked the eventID "
                    + event.getEventID() + ") attended for UserID " + eventParticipant.getUserID();
            History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
            HistoryDAO.insertHistory(history);

            // Redirect về trang rollcall với eventId tương ứng
            response.sendRedirect("event?action=rollcall&eventId=" + eventIdStr);
            return;
        } else if (action.equals("feedback")) {
            int eventId = Integer.parseInt(request.getParameter("eventId"));
            Event event = ed.getEventByEventId(eventId);

            request.setAttribute("event", event);
            request.setAttribute("op", "feedback");
            request.getRequestDispatcher("view/event.jsp").forward(request, response);
        } else if (action.equals("listfeedback")) {
            int eventId = Integer.parseInt(request.getParameter("eventId"));
            ArrayList<EventFeedback> eventFeedback = efd.getEventFeedbackByEventId(eventId);
            Event event = EventDAO.getEventByEventId(eventId);
            request.setAttribute("eventFeedback", eventFeedback);
            request.setAttribute("event", event);
            request.setAttribute("op", "listfeedback");
            request.getRequestDispatcher("view/event.jsp").forward(request, response);
        } else if (action.equals("deleteFb")) {
            int feedbackID = Integer.parseInt(request.getParameter("feedbackID"));
            EventFeedback eventFeedback = efd.getEventFeedbackByFeedbackId(feedbackID);

            // Ghi vào History
            String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")" + " has deleted feedback by UserID "
                    + eventFeedback.getUserID() + " in eventID " + eventFeedback.getEventID();
            History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
            HistoryDAO.insertHistory(history);

            eventFeedback = efd.deleteEventFeedback(eventFeedback);
            if (eventFeedback == null) {
                request.setAttribute("alert", "Delete Feedback Failed!");
            } else {
                request.setAttribute("alert", "Delete Feedback Successfully!");
            }
            request.setAttribute("op", "listfeedback");
            request.getRequestDispatcher("view/event.jsp").forward(request, response);
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
        EventDAO ed = new EventDAO();
        EventParticipantDAO epd = new EventParticipantDAO();
        EventFeedbackDAO efd = new EventFeedbackDAO();

        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        String action = request.getParameter("action");
        if ("addevent".equals(action)) {
            String eventName = request.getParameter("eventName");
            String description = request.getParameter("description");
            String location = request.getParameter("location");
            String eventDate = request.getParameter("eventDate");

            LocalDateTime dateTime = LocalDateTime.parse(eventDate);

            // Chuyển sang định dạng mong muốn
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateTime.format(formatter);

            // Lấy ảnh từ form
            Part filePart = request.getPart("img"); // "image" là name của input file trong form
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uniqueFileName = "";

            if (filePart.getSize() == 0) {
                uniqueFileName = "event_image.png";
            } else {
                // Thư mục lưu ảnh trong thư mục của project
                ServletContext context = getServletContext();
                String webPath = context.getRealPath("/"); // Lấy đường dẫn thư mục `web`
                File projectPath = new File(webPath).getParentFile().getParentFile(); // Lùi lên 2 cấp để về thư mục gốc
                String uploadPath = projectPath + "/web/img/event";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                uniqueFileName = System.currentTimeMillis() + "_" + fileName;
                String filePath = uploadPath + File.separator + uniqueFileName;
                filePart.write(filePath);
            }

            if (eventName == null || eventName.trim().isEmpty()
                    || description == null || description.trim().isEmpty()
                    || location == null || location.trim().isEmpty()
                    || formattedDate == null || formattedDate.trim().isEmpty()) {
                request.setAttribute("error", "Event information can't be empty!");
                request.setAttribute("op", "addclub");
                request.getRequestDispatcher("view/event.jsp").forward(request, response);
                return;
            }

            Event event = new Event(0, user.getMemberClub().getClubID(), eventName, description, location, formattedDate, uniqueFileName);
            event = ed.addEvent(event);
            if (event != null) {
                ArrayList<Event> events = ed.getEvents();
                request.setAttribute("events", events);
                request.setAttribute("alert", "Add Event Successfully!");
                request.getRequestDispatcher("view/event.jsp").forward(request, response);
            } else {
                request.setAttribute("alert", "Failed to add event!");
                request.setAttribute("op", "addclub");
                request.getRequestDispatcher("view/event.jsp").forward(request, response);
            }
        }

        if ("submitFeedback".equals(action)) {
            String ratingStr = request.getParameter("rating");
            String comments = request.getParameter("description");
            int eventId = Integer.parseInt(request.getParameter("eventId"));
            Event event = ed.getEventByEventId(eventId);

            // Kiểm tra rating
            if (isEmpty(ratingStr) || "0".equals(ratingStr)) {
                request.setAttribute("error", "Please select a rating!");
                request.setAttribute("event", ed.getEventByEventId(eventId)); // Đặt lại event để hiển thị form
                request.setAttribute("op", "feedback");
                request.getRequestDispatcher("view/event.jsp").forward(request, response);
                return;
            }
            int rating = Integer.parseInt(ratingStr);

            // Kiểm tra comment
            if (isEmpty(comments)) {
                request.setAttribute("error", "Please enter a comment!");
                request.setAttribute("event", ed.getEventByEventId(eventId));
                request.setAttribute("op", "feedback");
                request.getRequestDispatcher("view/event.jsp").forward(request, response);
                return;
            }

            // Tạo feedback với thời gian hiện tại
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String feedbackDate = now.format(formatter);

            EventFeedback eventFeedback = new EventFeedback(0, eventId, user.getUserID(), rating, comments, feedbackDate);
            eventFeedback = efd.addEventFeedback(eventFeedback);

            // Ghi vào History
            String historyAction = user.getFullName() + " (userID: " + user.getUserID() + ")" + " has feedbacked the event "
                    + event.getEventName() + " (eventID: " + event.getEventID() + ")";
            History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
            HistoryDAO.insertHistory(history);

            request.setAttribute("alert", "Feedback submitted successfully!");
            request.getRequestDispatcher("view/event.jsp").forward(request, response);
        }
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
    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
