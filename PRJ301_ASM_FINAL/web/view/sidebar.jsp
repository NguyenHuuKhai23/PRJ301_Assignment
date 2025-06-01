<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="model.User" %>
<!DOCTYPE html>
<!-- Sidebar Start -->
<div class="sidebar pe-4 pb-3">
    <nav class="navbar bg-light navbar-light">
        <a href="index.jsp" class="navbar-brand mx-4 mb-3">
            <h3 class="text-primary">FPT_CLUB</h3>
        </a>
        <div class="navbar-nav w-100">
            <% 
                String pageParam = request.getParameter("page"); // Lấy tham số page từ URL
                String actionParam = request.getParameter("action"); // Lấy tham số action từ URL
                String servletPath = request.getServletPath(); // Lấy đường dẫn servlet
                String requestURI = request.getRequestURI(); // Lấy toàn bộ URI

                // Xác định currentPage
                String currentPage;
                if (pageParam != null && !pageParam.isEmpty()) {
                    currentPage = pageParam;
                } else if (requestURI != null && !requestURI.isEmpty()) {
                    if (requestURI.contains("/event")) {
                        currentPage = "events";
                    } else if (requestURI.contains("/listClubs") || requestURI.contains("/view/listclubs.jsp")) {
                        currentPage = "clubs";
                    } else if (requestURI.contains("/history") || requestURI.contains("/view/history.jsp")) {
                        currentPage = "history";
                    } else {
                        currentPage = "home";
                    }
                } else if (servletPath != null && !servletPath.isEmpty()) {
                    if (servletPath.equals("/event")) {
                        currentPage = "events";
                    } else if (servletPath.equals("/listClubs") || servletPath.equals("/view/listclubs.jsp")) {
                        currentPage = "clubs";
                    } else if (servletPath.equals("/history") || servletPath.equals("/view/history.jsp")) {
                        currentPage = "history";
                    } else {
                        currentPage = "home";
                    }
                } else {
                    currentPage = "home";
                }
            %>
            <a href="index.jsp?page=home" class="nav-item nav-link <%= currentPage.equals("home") ? "active" : "" %>"><i class="fa fa-home me-2"></i>Home</a>
            <a href="./listClubs?page=clubs" class="nav-item nav-link <%= currentPage.equals("clubs") ? "active" : "" %>"><i class="fa fa-users me-2"></i>Clubs</a>
            <a href="./event?page=events" class="nav-item nav-link <%= currentPage.equals("events") ? "active" : "" %>"><i class="fa fa-calendar me-2"></i>Events</a>
            <c:if test="${user != null && user.getRole().equals('Admin')}">
                <a href="./history?page=history" class="nav-item nav-link <%= currentPage.equals("history") ? "active" : "" %>"><i class="fa fa-list me-2"></i>History</a>
            </c:if>
        </div>
    </nav>
</div>
<!-- Sidebar End -->