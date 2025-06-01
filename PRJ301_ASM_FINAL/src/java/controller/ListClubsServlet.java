package controller;

import dao.ClubDAO;
import dao.ClubJoinApplicationDAO;
import dao.HistoryDAO;
import dao.UserDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import model.Club;
import model.User;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.ClubJoinApplication;
import model.History;

@MultipartConfig
public class ListClubsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        UserDAO udao = new UserDAO();
        ClubDAO cdao = new ClubDAO();
        if (user == null) {
            // Redirect to login if no user is found in session
            request.getRequestDispatcher("view/login.jsp").forward(request, response);
            return;
        }
        String action = request.getParameter("action");
        ArrayList<Club> clubs = ClubDAO.getClubs();
        request.setAttribute("club", clubs);
        request.setAttribute("user", user);
        if (action == null) {
            // Get list of clubs from ClubDAO
            request.getRequestDispatcher("view/listclubs.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            try {
                int clubID = Integer.parseInt(request.getParameter("clubId"));
                Club club = cdao.getClubByClubIdNoClubMember(clubID);
                club = cdao.deleteClub(club);
                
                // Ghi lịch sử xóa club
                String historyAction = user.getFullName() + " (userID" + user.getUserID() + ") "
                        + "has deleted club " + club.getClubName() + " (clubID: " + clubID + ")";
                History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
                HistoryDAO.insertHistory(history);

                session.setAttribute("user", UserDAO.getUserByUserId(user.getUserID()));
                request.setAttribute("op", null);
                request.setAttribute("action", null);
                request.setAttribute("club", clubs);
                request.setAttribute("alert", "Delete Club Successfully!");
                request.getRequestDispatcher("view/listclubs.jsp").forward(request, response);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } else if (action.equals("register")) {
            try {
                int clubID = Integer.parseInt(request.getParameter("clubId"));
                Club club = cdao.getClubByClubId(clubID);

                request.setAttribute("club", cdao.getClubByClubId(clubID));
                request.setAttribute("op", "register");
                request.getRequestDispatcher("view/listclubs.jsp").forward(request, response);
            } catch (Exception e) {
            }

        } else if (action.equals("addclub")) {
            request.setAttribute("op", "addclub");
            request.getRequestDispatcher("view/listclubs.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // For this case, POST can behave the same as GET
//        doGet(request, response);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        ClubDAO cdao = new ClubDAO();

        if (user == null) {
            // Redirect to login if no user is found in session
            request.getRequestDispatcher("view/home.jsp").forward(request, response);
            return;
        }
        String action = request.getParameter("action");
        ArrayList<Club> clubs = ClubDAO.getClubs();
        if (action == null) {

        } else if (action.equals("addclub")) {
            String clubName = request.getParameter("clubName");
            String description = request.getParameter("description");

            // Lấy ảnh từ form
            Part filePart = request.getPart("img"); // "image" là name của input file trong form

            // Check if any required field is empty or missing
            if (isEmpty(clubName) || isEmpty(description) || filePart == null || filePart.getSize() == 0) {
                String error = "All club information must be provided! (Name, Description, and Image are required)";
                request.setAttribute("user", user);
                request.setAttribute("op", "addclub");
                request.setAttribute("error", error);
                request.getRequestDispatcher("view/listclubs.jsp").forward(request, response);
                return;
            }

            // Check if club name already exists
            if (cdao.isClubNameDuplicate(clubName)) {
                String error = "Club name '" + clubName + "' already exists!";
                request.setAttribute("user", user);
                request.setAttribute("op", "addclub");
                request.setAttribute("error", error);
                request.getRequestDispatcher("view/listclubs.jsp").forward(request, response);
                return;
            }

            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uniqueFileName = "";

            // Thư mục lưu ảnh trong thư mục của project
            ServletContext context = getServletContext();
            String webPath = context.getRealPath("/"); // Lấy đường dẫn thư mục `web`
            File projectPath = new File(webPath).getParentFile().getParentFile(); // Lùi lên 2 cấp để về thư mục gốc
            String uploadPath = projectPath + "/web/img/club";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            String filePath = uploadPath + File.separator + uniqueFileName;
            filePart.write(filePath);

            Club club = new Club(0, clubName, description, uniqueFileName, "");
            club = ClubDAO.addClub(club);

            // add club to history
            String historyAction = user.getFullName() + " (userID" + user.getUserID() + ") "
                    + "has added new club: " + clubName;
            History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
            HistoryDAO.insertHistory(history);

            if (club != null) {
                request.setAttribute("op", null);
                request.setAttribute("action", null);
                request.setAttribute("club", clubs);
                request.setAttribute("alert", "Add Club Successfully!");
                request.getRequestDispatcher("view/listclubs.jsp").forward(request, response);
            }
        } else if (action.equals("registerclub")) {
            String clubId_raw = request.getParameter("clubId");
            try {
                int clubId = Integer.parseInt(clubId_raw);
                Club club = cdao.getClubByClubId(clubId);
                ClubJoinApplicationDAO applicationDAO = new ClubJoinApplicationDAO();
                ClubJoinApplication application = applicationDAO.addClubJoinApplication(new ClubJoinApplication(0, user.getUserID(), clubId, "", ""));

                // Ghi lịch sử xóa club
                String historyAction = user.getFullName() + " (userID" + user.getUserID() + ") "
                        + "has registed club " + club.getClubName() + " (clubID: " + clubId + ")";
                History history = new History(0, user.getUserID(), historyAction, getCurrentTime());
                HistoryDAO.insertHistory(history);

                if (application != null) {
                    request.setAttribute("alert", "Club registration successful. Please allow approval!");
                    request.getRequestDispatcher("view/listclubs.jsp").forward(request, response);
                }
            } catch (Exception e) {
            }
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Hàm hỗ trợ lấy thời gian hiện tại
    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    @Override
    public String getServletInfo() {
        return "Servlet to list all clubs";
    }
}
