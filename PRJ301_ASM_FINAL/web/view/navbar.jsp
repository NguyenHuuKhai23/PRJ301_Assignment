<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dao.NotificationDAO"%>
<%@page import="dao.ReadNotificationDAO"%>
<%@page import="model.Notification"%>
<%@page import="model.User"%>
<%@page import="java.util.ArrayList"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<header>
    <link href="css/fix.css" rel="stylesheet">
</header>
<!-- Navbar Start -->
<nav class="navbar navbar-expand bg-light navbar-light sticky-top px-4 py-0">
    <a href="index.jsp" class="navbar-brand d-flex d-lg-none me-1">
        <h2 class="text-primary mb-0"></h2>
    </a>
    <a href="#" class="sidebar-toggler flex-shrink-0">
        <i class="fa fa-bars"></i>
    </a>

    <div class="navbar-nav align-items-center ms-auto">   
        <% 
            User user = (User) session.getAttribute("user");
            NotificationDAO notificationDAO = new NotificationDAO();
            ReadNotificationDAO readNotificationDAO = new ReadNotificationDAO();
            ArrayList<Notification> notifications = notificationDAO.getNotifications();
            int unreadCount = 0;
            
            if (user != null) {
                // Đếm số thông báo chưa đọc với điều kiện tương tự phần notification
                for (Notification n : notifications) {
                    if ("Admin".equals(user.getRole()) || 
                        (user.getMemberClub() != null && user.getMemberClub().getClubID() == n.getClubID()) || 
                        "Admin".equals(n.getUser().getRole())) {
                        if (!readNotificationDAO.isNotificationRead(user.getUserID(), n.getNotificationID())) {
                            unreadCount++;
                        }
                    }
                }
                // Đặt notifications vào page scope để JSTL có thể truy cập
                pageContext.setAttribute("notifications", notifications);
            }
        %>
        <div class="nav-item dropdown">
            <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown" style="position: relative;">
                <i class="fa fa-bell me-lg-2"></i>
                <!-- Display unread notification count -->
                <span class="badge bg-danger rounded-circle number" style="<%= unreadCount > 0 ? "" : "display: none;" %>">
                    <%= unreadCount %>
                </span>
                <span class="d-none d-lg-inline-flex"></span>
            </a>
            <div class="dropdown-menu dropdown-menu-end bg-light border-0 rounded-0 rounded-bottom m-0">
                <% 
                    if (user != null) {
                        int displayedCount = 0;
                %>
                <c:forEach var="n" items="${notifications}">
                    <% 
                        Notification currentNotification = (Notification) pageContext.getAttribute("n");
                        if ("Admin".equals(user.getRole()) || 
                            (user.getMemberClub() != null && user.getMemberClub().getClubID() == currentNotification.getClubID()) || 
                            "Admin".equals(currentNotification.getUser().getRole())) {
                    %>
                    <a href="notification?page=notification" class="dropdown-item">
                        <h6 class="fw-normal mb-0">${n.user.fullName} has added a new notification</h6>
                        <small>${n.notificationDate}</small>
                    </a>
                    <% 
                            displayedCount++;
                            if (displayedCount >= 3) break; 
                        }
                    %>
                </c:forEach>
                <% 
                    }
                %>
                <hr class="dropdown-divider">
                <a href="./notification" class="dropdown-item text-center">See all notifications</a>
            </div>
        </div>

        <% if (user == null) { %>
        <div class="nav-item dropdown">
            <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">
                <i class="fa fa-user me-lg-2"></i>
                <span class="d-none d-lg-inline-flex"></span>
            </a>
            <div class="dropdown-menu dropdown-menu-end bg-light border-0 rounded-0 rounded-bottom m-0">                                
                <a href="./login" class="dropdown-item">Login</a>
                <hr class="dropdown-divider">
                <a href="./signup" class="dropdown-item">Sign Up</a>
            </div>
        </div>
        <% } else { 
            String img = "img/user/" + user.getImage();
        %>
        <div class="nav-item dropdown">
            <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">
                <img class="rounded-circle me-lg-2" src="<%= img %>" alt="" style="width: 40px; height: 40px;">
                <span class="d-none d-lg-inline-flex"><%= user.getFullName() %> (<%= user.getRole() %>)</span>
            </a>
            <div class="dropdown-menu dropdown-menu-end bg-light border-0 rounded-0 rounded-bottom m-0">
                <a href="./profile" class="dropdown-item">My Profile</a>
                <% 
                    String memberClubRole = user.getMemberClub() != null ? user.getMemberClub().getRole() : "";
                    if ("Admin".equals(user.getRole())) { %>
                <a href="./management" class="dropdown-item">Management</a>
                <% } else if("User".equals(user.getRole()) && (user.getMemberClub().getRole() != null)) { %>
                <a href="./myclub" class="dropdown-item">My Clubs</a>
                <% } %>
                <a href="./message" class="dropdown-item">Send message</a>
                <a href="./logout" class="dropdown-item">Log Out</a>
            </div>
        </div>
        <% } %>
    </div>
</nav>
<!-- Navbar End -->