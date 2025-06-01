/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ClubDAO;
import dao.HistoryDAO;
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
import model.Club;
import model.History;
import model.User;

/**
 *
 * @author knguy
 */
@MultipartConfig
public class EditClubServlet extends HttpServlet {

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
            out.println("<title>Servlet EditClubServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditClubServlet at " + request.getContextPath() + "</h1>");
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
        String clubID = request.getParameter("clubID");
        String clubName = request.getParameter("clubName");
        String description = request.getParameter("description");

        // Check if required fields are empty or contain only whitespace
        if (clubID == null || clubID.trim().isEmpty()
                || clubName == null || clubName.trim().isEmpty()
                || description == null || description.trim().isEmpty()) {
            request.setAttribute("alert", "Edit Club Information Failed! All fields must be filled!");
            request.getRequestDispatcher("view/management.jsp").forward(request, response);
            return;
        }

        String imageFileName = null;
        Club club = ClubDAO.getClubByClubId(Integer.parseInt(clubID));

        // Handle image upload
        Part filePart = request.getPart("img");
        if (filePart != null && filePart.getSize() > 0) { // Check if a new image is uploaded
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            System.out.println(fileName);

            ServletContext context = getServletContext();
            String webPath = context.getRealPath("/");
            File projectPath = new File(webPath).getParentFile().getParentFile();

            String uploadPath = projectPath + "/web/img/club";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            String filePath = uploadPath + File.separator + uniqueFileName;
            filePart.write(filePath);
            imageFileName = uniqueFileName;
        } else {
            // Use existing image if no new image is uploaded
            imageFileName = club.getImage();
        }

        if (!ClubDAO.isClubNameDuplicate(clubName.trim(), Integer.parseInt(clubID))) {
            club.setClubID(Integer.parseInt(clubID));
            club.setClubName(clubName.trim());
            club.setDescription(description.trim());
            club.setImage(imageFileName);
            club = ClubDAO.updateClub(club);

            if (club != null) {
                request.setAttribute("alert", "Edit Club Information Successfully!");

                // Record to History
                String historyAction = "UserID " + user.getUserID() + " (" + user.getRole() + ")"
                        + " has edited information club " + clubName.trim() + " (clubID: " + clubID + ")";
                History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
                HistoryDAO.insertHistory(history);
            } else {
                request.setAttribute("alert", "Edit Club Information Failed!");
            }
        } else {
            request.setAttribute("alert", "Edit Club Information Failed! Club Name is Duplicate!");
        }

        request.getRequestDispatcher("view/management.jsp").forward(request, response);
    }

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
