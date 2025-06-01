package controller;

import dao.MessageDAO;
import dao.UserDAO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import model.Message;
import model.User;

/**
 * Servlet handling message-related requests: list, view, reply, send, mark as
 * seen, delete.
 */
public class MessageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String action = request.getParameter("action");
        MessageDAO md = new MessageDAO();

        if (action == null) {
            ArrayList<Message> mess = md.getMessages();
            request.setAttribute("mess", mess);
            request.getRequestDispatcher("view/messagelist.jsp").forward(request, response);
        } else if (action.equals("view")) {
            String messID = request.getParameter("messageID");
            Message m = md.getMessageById(Integer.parseInt(messID));
            request.setAttribute("message", m);
            request.getRequestDispatcher("view/messagelist.jsp").forward(request, response);
        } else if (action.equals("reply")) {
            String messID = request.getParameter("messageID");
            Message originalMessage = md.getMessageById(Integer.parseInt(messID));
            request.setAttribute("originalMessage", originalMessage);
            request.getRequestDispatcher("view/messagelist.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            String messID = request.getParameter("messageID");
            Message messageToDelete = md.getMessageById(Integer.parseInt(messID));
            Message deletedMessage = md.deleteMessage(messageToDelete);
            session.setAttribute("alert", "Message deleted successfully!");
            response.sendRedirect("message");
        } else if (action.equals("mess")) {
            // Chuyển tiếp đến JSP để hiển thị form New Question
            request.getRequestDispatcher("view/messagelist.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        MessageDAO md = new MessageDAO();

        if ("reply".equals(action)) {
            String messageID = request.getParameter("messageID");
            Message originalMessage = md.getMessageById(Integer.parseInt(messageID));
            String content = request.getParameter("content");

            if (content == null || content.trim().isEmpty()) {
                session.setAttribute("error", "Reply content cannot be empty!");
                response.sendRedirect("message?action=reply&messageID=" + messageID);
                return;
            }

            String sentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new java.util.Date());

            Message reply = new Message(0, user.getUserID(), originalMessage.getSenderID(),
                    originalMessage.getSubject(), content, sentDate, "sent");

            md.markMessageAsSeen(Integer.parseInt(messageID));

            Message result = md.addMessage(reply);

            session.setAttribute("alert", "Reply sent successfully!");

            response.sendRedirect("message");
        } else if ("mess".equals(action)) {
            String senderID = request.getParameter("senderID");
            String receiverID = request.getParameter("receiverID");
            String subject = request.getParameter("subject");
            String content = request.getParameter("content");

            if (subject == null || subject.trim().isEmpty()
                    || content == null || content.trim().isEmpty()) {
                session.setAttribute("error", "Subject and content cannot be empty!");
                response.sendRedirect("message?action=mess&senderID=" + senderID);
                return;
            }
            
            // Lấy danh sách tất cả user
            ArrayList<User> users = UserDAO.getUsers();
            boolean receiverExists = false;
            for (User u : users) {
                if (u.getUserID() == Integer.parseInt(receiverID)) {
                    receiverExists = true;
                    break;
                }
            }

            // Nếu receiverID không tồn tại, trả về lỗi
            if (!receiverExists) {
                session.setAttribute("error", "Receiver ID does not exist!");
                response.sendRedirect("message?action=mess&senderID=" + senderID);
                return;
            }
            String sentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new java.util.Date());

            Message newMessage = new Message(0, Integer.parseInt(senderID),
                    Integer.parseInt(receiverID),
                    subject, content, sentDate, "sent");

            Message result = md.addMessage(newMessage);

            session.setAttribute("alert", "Question sent successfully!");
            response.sendRedirect("message");
        }
    }

    @Override
    public String getServletInfo() {
        return "Message management servlet";
    }
}
