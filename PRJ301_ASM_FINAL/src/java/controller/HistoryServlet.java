/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.HistoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import model.History;
import model.User;

/**
 *
 * @author thais
 */
public class HistoryServlet extends HttpServlet {

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
            out.println("<title>Servlet HistoryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet HistoryServlet at " + request.getContextPath() + "</h1>");
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
        if ("delete".equals(action)) {
            try {
                int historyID = Integer.parseInt(request.getParameter("historyID"));
                History history = new History();
                history.setHistoryID(historyID);
                HistoryDAO.deleteHistory(history);
            } catch (NumberFormatException e) {
            }
        }

        int pageSize = 10; 
        int currentHistoryPage = 1;

        try {
            String pageParam = request.getParameter("historyPage");
            if (pageParam != null) {
                currentHistoryPage = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            currentHistoryPage = 1;
        }

        ArrayList<History> allHistory = HistoryDAO.getHistorys();
        int totalHistory = allHistory.size();
        int totalHistoryPages = (totalHistory == 0) ? 1 : (int) Math.ceil((double) totalHistory / pageSize);

        if (currentHistoryPage < 1) {
            currentHistoryPage = 1;
        }
        if (totalHistory > 0 && currentHistoryPage > totalHistoryPages) {
            currentHistoryPage = totalHistoryPages;
        }

        int historyOffset = (currentHistoryPage - 1) * pageSize;
        ArrayList<History> history;

        if (totalHistory == 0) {
            history = new ArrayList<>();
        } else {
            int fromIndex = Math.max(0, historyOffset); // Ensure non-negative
            int endIndex = Math.min(fromIndex + pageSize, totalHistory);
            history = new ArrayList<>(allHistory.subList(fromIndex, endIndex));
        }

        request.setAttribute("history", history);
        request.setAttribute("currentHistoryPage", currentHistoryPage);
        request.setAttribute("totalHistoryPages", totalHistoryPages);
        request.setAttribute("historyOffset", historyOffset);

        request.getRequestDispatcher("view/history.jsp").forward(request, response);
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
        doGet(request, response);
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
