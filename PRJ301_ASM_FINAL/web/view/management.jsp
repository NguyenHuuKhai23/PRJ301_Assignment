<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="model.User" %>
<%@ page import="model.ClubJoinApplication" %>
<%@ page import="model.Club" %>
<%@ page import="model.Report" %>
<%@ page import="dao.ReportDAO" %>
<%@ page import="model.Event" %>
<%@ page import="dao.ClubDAO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Management</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">

        <!-- Favicon -->
        <link href="img/favicon.ico" rel="icon">

        <!-- Google Web Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

        <!-- Icon Font Stylesheet -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Libraries Stylesheet -->
        <link href="lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
        <link href="lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet" />

        <!-- Customized Bootstrap Stylesheet -->
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <!-- Template Stylesheet -->
        <link href="css/style.css" rel="stylesheet">
    </head>

    <body>
        <div class="container-xxl position-relative bg-white d-flex p-0">
            <!-- Spinner -->
            <jsp:include page="spinner.jsp"/>

            <!-- Sidebar -->
            <jsp:include page="sidebar.jsp"/>

            <!-- Content Start -->
            <div class="content">
                <!-- Navbar -->
                <jsp:include page="navbar.jsp"/>
                <div>
                    <h1 style="margin: 10px 30px 0 30px; background: #009cff; color: white; border-radius: 15px; padding: 5px 10px">Management</h1>
                </div>
                <% User user = (User) session.getAttribute("user"); 
                if (user == null) {
                        response.sendRedirect("login");
                        return;
                    }%>       

                <div class="container-fluid pt-4 px-4" style="text-align: end">
                    
                    <c:if test="${!user.getRole().equals('Admin')}">
                        <a href="./myclub" class="btn btn-primary ms-1">My Clubs</a>
                    </c:if>
                    <c:if test="${user.getMemberClub() != null && user.getMemberClub().getRole().equals('Chairman')}">
                        <a href="./report" class="btn btn-success ms-1">Write Report</a>
                    </c:if>
                    <a href="./notification" class="btn btn-warning ms-1">Notification</a>

                </div>
                <% if (user.getRole().equals("Admin")) { %>
                <div class="container-fluid pt-4 px-4">
                    <div class="col-sm-12 col-xl-12">
                        <div class="bg-light rounded h-100 p-4">
                            <div class="d-flex justify-content-between align-items-center flex-wrap">
                                <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "user".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-user-tab" data-bs-toggle="pill" data-bs-target="#pills-user" 
                                                type="button" role="tab" aria-controls="pills-user" aria-selected="true">Users</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "club".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-club-tab" data-bs-toggle="pill" data-bs-target="#pills-club" 
                                                type="button" role="tab" aria-controls="pills-club" aria-selected="false">Clubs</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "event".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-event-tab" data-bs-toggle="pill" data-bs-target="#pills-event" 
                                                type="button" role="tab" aria-controls="pills-event" aria-selected="false">Events</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "report".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-report-tab" data-bs-toggle="pill" data-bs-target="#pills-report" 
                                                type="button" role="tab" aria-controls="pills-report" aria-selected="false">Reports</button>
                                    </li>
                                </ul>
                                <form action="management" method="get" class="d-none d-md-flex ms-4 align-items-center">
                                    <input type="hidden" name="tab" value="user">
                                    <input class="form-control border-0" type="search" name="searchTerm" 
                                           placeholder="Search users" style="width: 400px;">
                                    <input class="btn btn-primary ms-2" type="submit" value="Search">
                                </form>
                            </div>
                            <div class="tab-content" id="pills-tabContent">
                                <!-- Tab Users -->
                                <div class="tab-pane fade <%= "user".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-user" role="tabpanel" aria-labelledby="pills-user-tab">
                                    <div class="col-sm-12 col-xl-12">
                                        <% 
                                            String searchTerm = (String) request.getAttribute("searchTerm");
                                            Integer totalUsers = (Integer) request.getAttribute("totalUsers");
                                            List<User> userList = (List<User>) request.getAttribute("userList");
                                            if (userList == null || userList.isEmpty()) { 
                                        %>
                                        <p>No matching results found.</p>
                                        <% } else { %>
                                        <table class="table table-striped">
                                            <thead>
                                                <tr>
                                                    <th scope="col">#</th>
                                                    <th scope="col">Student ID</th>
                                                    <th scope="col">Full Name</th>
                                                    <th scope="col">Email</th>
                                                    <th scope="col">Role</th>
                                                    <th scope="col">Role In Club</th>
                                                    <th scope="col">Club</th>
                                                    <th scope="col" class="text-center">Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% 
                                                    Integer userOffset = (Integer) request.getAttribute("userOffset");
                                                    int userIndex = 1;
                                                    for (User u : userList) {
                                                %>
                                                <tr>
                                                    <td><%= userOffset + userIndex++ %></td>
                                                    <td><%= u.getStudentID() %></td>
                                                    <td><%= u.getFullName() %></td>
                                                    <td><%= u.getEmail() %></td>
                                                    <td><%= u.getRole() %></td>
                                                    <td><%= u.getMemberClub().getRole() != null ? u.getMemberClub().getRole() : "N/A" %></td>
                                                    <td><%= u.getMemberClub().getRole() != null ? u.getMemberClub().getClubID() : "N/A" %></td>
                                                    <td class="text-center">
                                                        <% if (u.getRole().equals("Admin") || u.getMemberClub().getRole() == null) { %>
                                                        <a href="management?action=editinall&userID=<%= u.getUserID() %>&tab=user&userPage=<%= request.getAttribute("currentUserPage") %><%= searchTerm != null ? "&searchTerm=" + searchTerm : "" %>" 
                                                           class="btn btn-primary ms-2">Edit</a>
                                                        <a href="management?action=delete&userID=<%= u.getUserID() %>&tab=user&userPage=<%= request.getAttribute("currentUserPage") %><%= searchTerm != null ? "&searchTerm=" + searchTerm : "" %>" 
                                                           class="btn btn-danger ms-2" 
                                                           onclick="return confirm('Are you sure you want to delete this user?');">Delete</a>
                                                        <% } else { %>
                                                        <a href="management?action=editinall&userID=<%= u.getUserID() %>&clubID=<%=u.getMemberClub().getClubID()%>&tab=user&userPage=<%= request.getAttribute("currentUserPage") %><%= searchTerm != null ? "&searchTerm=" + searchTerm : "" %>" 
                                                           class="btn btn-primary ms-2">Edit</a>
                                                        <a href="management?action=delete&userID=<%= u.getUserID() %>&tab=user&userPage=<%= request.getAttribute("currentUserPage") %><%= searchTerm != null ? "&searchTerm=" + searchTerm : "" %>" 
                                                           class="btn btn-danger ms-2" 
                                                           onclick="return confirm('Are you sure you want to delete this user?');">Delete</a>
                                                        <% } %>
                                                    </td>
                                                </tr>
                                                <% } %>
                                            </tbody>
                                        </table>
                                        <% if (searchTerm != null && !searchTerm.trim().isEmpty() && totalUsers != null) { %>
                                        <p class="mt-3">Search results for "<%= searchTerm %>": 
                                            <%= totalUsers %> results found.</p>
                                            <% } %>
                                            <% } %>
                                            <%
                                                int currentUserPage = (request.getAttribute("currentUserPage") != null) ? (Integer) request.getAttribute("currentUserPage") : 1;
                                                int totalUserPages = (request.getAttribute("totalUserPages") != null) ? (Integer) request.getAttribute("totalUserPages") : 1;
                                            %>
                                        <nav>
                                            <ul class="pagination justify-content-center">
                                                <li class="page-item <%= (currentUserPage == 1) ? "disabled" : "" %>">
                                                    <a class="page-link" href="management?tab=user&userPage=<%= currentUserPage - 1 %><%= searchTerm != null ? "&searchTerm=" + searchTerm : "" %>">Previous</a>
                                                </li>
                                                <% for (int i = 1; i <= totalUserPages; i++) { %>
                                                <li class="page-item <%= (i == currentUserPage) ? "active" : "" %>">
                                                    <a class="page-link" href="management?tab=user&userPage=<%= i %><%= searchTerm != null ? "&searchTerm=" + searchTerm : "" %>"><%= i %></a>
                                                </li>
                                                <% } %>
                                                <li class="page-item <%= (currentUserPage == totalUserPages) ? "disabled" : "" %>">
                                                    <a class="page-link" href="management?tab=user&userPage=<%= currentUserPage + 1 %><%= searchTerm != null ? "&searchTerm=" + searchTerm : "" %>">Next</a>
                                                </li>
                                            </ul>
                                        </nav>
                                    </div>
                                </div>

                                <!-- Tab Clubs -->
                                <div class="tab-pane fade <%= "club".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-club" role="tabpanel" aria-labelledby="pills-club-tab">
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th scope="col">#</th>
                                                <th scope="col">Club ID</th>
                                                <th scope="col">Club Name</th>
                                                <th scope="col">Description</th>
                                                <th scope="col">Established Date</th>
                                                <th scope="col" class="text-center">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% 
                                            int clubIndex = 1;
                                            List<Club> clubList = (List<Club>) request.getAttribute("clubList");
                                            Integer clubOffset = (Integer) request.getAttribute("clubOffset");
                                            if (clubList != null) {
                                                for (Club c : clubList) {
                                            %>
                                            <tr>
                                                <td><%= clubOffset + clubIndex++ %></td>
                                                <td><%= c.getClubID() %></td>
                                                <td><%= c.getClubName() %></td>
                                                <td><%= c.getDescription() %></td>
                                                <td><%= c.getEstablishedDate() != null ? c.getEstablishedDate() : "N/A" %></td>
                                                <td class="text-center">
                                                    <a href="management?action=deleteClub&clubID=<%= c.getClubID() %>&tab=club&clubPage=<%= request.getAttribute("currentClubPage") %>" 
                                                       class="btn btn-danger ms-2" 
                                                       onclick="return confirm('Are you sure you want to delete this club?');">Delete</a>
                                                </td>
                                            </tr>
                                            <% } %>
                                            <% } %>
                                        </tbody>
                                    </table>
                                    <%
                                    int currentClubPage = (request.getAttribute("currentClubPage") != null) ? (Integer) request.getAttribute("currentClubPage") : 1;
                                    int totalClubPages = (request.getAttribute("totalClubPages") != null) ? (Integer) request.getAttribute("totalClubPages") : 1;
                                    %>
                                    <nav>
                                        <ul class="pagination justify-content-center">
                                            <li class="page-item <%= (currentClubPage == 1) ? "disabled" : "" %>">
                                                <a class="page-link" href="management?tab=club&clubPage=<%= currentClubPage - 1 %>">Previous</a>
                                            </li>
                                            <% for (int i = 1; i <= totalClubPages; i++) { %>
                                            <li class="page-item <%= (i == currentClubPage) ? "active" : "" %>">
                                                <a class="page-link" href="management?tab=club&clubPage=<%= i %>"><%= i %></a>
                                            </li>
                                            <% } %>
                                            <li class="page-item <%= (currentClubPage == totalClubPages) ? "disabled" : "" %>">
                                                <a class="page-link" href="management?tab=club&clubPage=<%= currentClubPage + 1 %>">Next</a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>

                                <!-- Tab Events -->
                                <div class="tab-pane fade <%= "event".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-event" role="tabpanel" aria-labelledby="pills-event-tab">
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th scope="col">#</th>
                                                <th scope="col">Event ID</th>
                                                <th scope="col">Event Name</th>
                                                <th scope="col">Date</th>
                                                <th scope="col">Location</th>
                                                <th scope="col">Club ID</th>
                                                <th scope="col" class="text-center">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% 
                                            int eventIndex = 1;
                                            List<Event> eventList = (List<Event>) request.getAttribute("eventList");
                                            Integer eventOffset = (Integer) request.getAttribute("eventOffset");
                                            if (eventList != null) {
                                                for (Event e : eventList) {
                                            %>
                                            <tr>
                                                <td><%= eventOffset + eventIndex++ %></td>
                                                <td><%= e.getEventID() %></td>
                                                <td><%= e.getEventName() %></td>
                                                <td><%= e.getEventDate() %></td>
                                                <td><%= e.getLocation() %></td>
                                                <td><%= e.getClubID() %></td>
                                                <td class="text-center">
                                                    <a href="management?action=deleteEvent&eventID=<%= e.getEventID() %>&tab=event&eventPage=<%= request.getAttribute("currentEventPage") %>" 
                                                       class="btn btn-danger ms-2" 
                                                       onclick="return confirm('Are you sure you want to delete this event?');">Delete</a>
                                                </td>
                                            </tr>
                                            <% } %>
                                            <% } %>
                                        </tbody>
                                    </table>
                                    <%
                                    int currentEventPage = (request.getAttribute("currentEventPage") != null) ? (Integer) request.getAttribute("currentEventPage") : 1;
                                    int totalEventPages = (request.getAttribute("totalEventPages") != null) ? (Integer) request.getAttribute("totalEventPages") : 1;
                                    %>
                                    <nav>
                                        <ul class="pagination justify-content-center">
                                            <li class="page-item <%= (currentEventPage == 1) ? "disabled" : "" %>">
                                                <a class="page-link" href="management?tab=event&eventPage=<%= currentEventPage - 1 %>">Previous</a>
                                            </li>
                                            <% for (int i = 1; i <= totalEventPages; i++) { %>
                                            <li class="page-item <%= (i == currentEventPage) ? "active" : "" %>">
                                                <a class="page-link" href="management?tab=event&eventPage=<%= i %>"><%= i %></a>
                                            </li>
                                            <% } %>
                                            <li class="page-item <%= (currentEventPage == totalEventPages) ? "disabled" : "" %>">
                                                <a class="page-link" href="management?tab=event&eventPage=<%= currentEventPage + 1 %>">Next</a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>

                                <!-- Tab Reports -->
                                <div class="tab-pane fade <%= "report".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-report" role="tabpanel" aria-labelledby="pills-report-tab">
                                    <c:if test="${reports!=null}">
                                        <table class="table table-striped">
                                            <thead>
                                                <tr>
                                                    <th scope="col">#</th>
                                                    <th scope="col">Report ID</th>
                                                    <th scope="col">Club ID</th>
                                                    <th scope="col">Semester</th>
                                                    <th scope="col">Member Changes</th>
                                                    <th scope="col">Event Summary</th>
                                                    <th scope="col">Participant Status</th>
                                                    <th scope="col">Create Date</th>
                                                    <th scope="col" class="text-center">Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% 
                                                    int reportIndex = 1;
                                                    Integer reportOffset = (Integer) request.getAttribute("reportOffset");
                                                %>
                                                <c:forEach items="${reports}" var="report">
                                                    <tr>
                                                        <td><%= reportOffset + reportIndex++ %></td>
                                                        <td scope="col">${report.getReportID()}</td>
                                                        <td scope="col">${report.getClubID()}</td>
                                                        <td scope="col">${report.getSemester()}</td>
                                                        <td scope="col">${report.getMemberChanges()}</td>
                                                        <td scope="col">${report.getEventSummary()}</td>
                                                        <td scope="col">${report.getParticipationStatus()}</td>
                                                        <td scope="col">${report.getCreatedDate()}</td>
                                                        <td scope="col" class="text-center">
                                                            <a href="management?action=deletereport&reportID=${report.getReportID()}&tab=report&reportPage=<%= request.getAttribute("currentReportPage") %>" 
                                                               class="btn btn-danger ms-2"
                                                               onclick="return confirm('Are you sure you want to delete this report?');">Delete</a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                        <% 
                                            int currentReportPage = (request.getAttribute("currentReportPage") != null) ? (Integer) request.getAttribute("currentReportPage") : 1;
                                            int totalReportPages = (request.getAttribute("totalReportPages") != null) ? (Integer) request.getAttribute("totalReportPages") : 1;
                                        %>
                                        <nav>
                                            <ul class="pagination justify-content-center">
                                                <li class="page-item <%= (currentReportPage == 1) ? "disabled" : "" %>">
                                                    <a class="page-link" href="management?tab=report&reportPage=<%= currentReportPage - 1 %>">Previous</a>
                                                </li>
                                                <% for (int i = 1; i <= totalReportPages; i++) { %>
                                                <li class="page-item <%= (i == currentReportPage) ? "active" : "" %>">
                                                    <a class="page-link" href="management?tab=report&reportPage=<%= i %>"><%= i %></a>
                                                </li>
                                                <% } %>
                                                <li class="page-item <%= (currentReportPage == totalReportPages) ? "disabled" : "" %>">
                                                    <a class="page-link" href="management?tab=report&reportPage=<%= currentReportPage + 1 %>">Next</a>
                                                </li>
                                            </ul>
                                        </nav>
                                    </c:if>
                                    <c:if test="${reports==null}">
                                        <p>No reports found</p>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>


                <% } else { 
            if (user.getMemberClub() != null && user.getMemberClub().getRole().equals("Chairman")) { %>           
                <div class="container-fluid pt-4 px-4">
                    <div class="col-sm-12 col-xl-12">
                        <div class="bg-light rounded h-100 p-4">
                            <div class="d-flex justify-content-between align-items-center flex-wrap">
                                <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "clubmember".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-clubmember-tab" data-bs-toggle="pill" data-bs-target="#pills-clubmember" 
                                                type="button" role="tab" aria-controls="pills-clubmember" aria-selected="true">Club Members</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "joinapplication".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-joinapplication-tab" data-bs-toggle="pill" data-bs-target="#pills-joinapplication" 
                                                type="button" role="tab" aria-controls="pills-joinapplication" aria-selected="false">Join Applications</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "clubinfor".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-clubinfor-tab" data-bs-toggle="pill" data-bs-target="#pills-clubinfor" 
                                                type="button" role="tab" aria-controls="pills-clubinfor" aria-selected="false">Club Information</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "report".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-report-tab" data-bs-toggle="pill" data-bs-target="#pills-report" 
                                                type="button" role="tab" aria-controls="pills-report" aria-selected="false">Report</button>
                                    </li>
                                </ul>
                                <form action="management" method="get" class="d-none d-md-flex ms-4 align-items-center">
                                    <input type="hidden" name="tab" value="clubmember">
                                    <input class="form-control border-0" type="search" name="searchTerm" 
                                           placeholder="Search members" style="width: 300px;">
                                    <input class="btn btn-primary ms-2" type="submit" value="Search">
                                </form>
                            </div>

                            <div class="tab-content" id="pills-tabContent">
                                <!-- Tab Club Members -->
                                <div class="tab-pane fade <%= "clubmember".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-clubmember" role="tabpanel" aria-labelledby="pills-clubmember-tab">
                                    <% 
                                        String searchTermChairman = (String) request.getAttribute("searchTerm");
                                        List<User> clubMemberList = (List<User>) request.getAttribute("clubMemberList");
                                        if (clubMemberList == null || clubMemberList.isEmpty()) { 
                                    %>
                                    <p>No matching members found.</p>
                                    <% } else { %>
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th scope="col">#</th>
                                                <th scope="col">Student ID</th>
                                                <th scope="col">Full Name</th>
                                                <th scope="col">Email</th>
                                                <th scope="col">Role In Club</th>
                                                <th scope="col" class="text-center">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% 
                                            int chairmanIndex = 1;
                                            Integer chairmanOffset = (Integer) request.getAttribute("chairmanOffset");
                                            for (User u : clubMemberList) {
                                            %>
                                            <tr>
                                                <td><%= chairmanOffset + chairmanIndex++ %></td>
                                                <td><%= u.getStudentID() %></td>
                                                <td><%= u.getFullName() %></td>
                                                <td><%= u.getEmail() %></td>
                                                <td><%= u.getMemberClub().getRole() %></td>
                                                <td class="text-center">
                                                    <% if (!u.getMemberClub().getRole().equals("Chairman")) { %>
                                                    <a href="management?action=editinclub&userID=<%= u.getUserID() %>&clubID=<%= u.getMemberClub().getClubID() %>&tab=clubmember&chairmanPage=<%= request.getAttribute("currentChairmanPage") %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>" 
                                                       class="btn btn-primary ms-2">Edit</a>
                                                    <a href="management?action=kick&userID=<%= u.getUserID() %>&clubID=<%= u.getMemberClub().getClubID() %>&tab=clubmember&chairmanPage=<%= request.getAttribute("currentChairmanPage") %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>" 
                                                       class="btn btn-danger ms-2" 
                                                       onclick="return confirm('Are you sure you want to kick this member?');">Kick</a>
                                                    <% } %>

                                                </td>
                                            </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                    <% if (searchTermChairman != null && !searchTermChairman.trim().isEmpty()) { %>
                                    <p class="mt-3">Search results for "<%= searchTermChairman %>": 
                                        <%= clubMemberList.size() %> results found.</p>
                                        <% } %>
                                        <% } %>
                                        <%
                                        int currentChairmanPage = (request.getAttribute("currentChairmanPage") != null) ? (Integer) request.getAttribute("currentChairmanPage") : 1;
                                        int totalChairmanPages = (request.getAttribute("totalChairmanPages") != null) ? (Integer) request.getAttribute("totalChairmanPages") : 1;
                                        %>
                                    <nav>
                                        <ul class="pagination justify-content-center">
                                            <li class="page-item <%= (currentChairmanPage == 1) ? "disabled" : "" %>">
                                                <a class="page-link" href="management?tab=clubmember&chairmanPage=<%= currentChairmanPage - 1 %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>">Previous</a>
                                            </li>
                                            <% for (int i = 1; i <= totalChairmanPages; i++) { %>
                                            <li class="page-item <%= (i == currentChairmanPage) ? "active" : "" %>">
                                                <a class="page-link" href="management?tab=clubmember&chairmanPage=<%= i %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>"><%= i %></a>
                                            </li>
                                            <% } %>
                                            <li class="page-item <%= (currentChairmanPage == totalChairmanPages) ? "disabled" : "" %>">
                                                <a class="page-link" href="management?tab=clubmember&chairmanPage=<%= currentChairmanPage + 1 %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>">Next</a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>

                                <!-- Tab Join Applications -->
                                <div class="tab-pane fade <%= "joinapplication".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-joinapplication" role="tabpanel" aria-labelledby="pills-joinapplication-tab">
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th scope="col">Application ID</th>
                                                <th scope="col">User ID</th>
                                                <th scope="col">User Name</th>
                                                <th scope="col">Club ID</th>
                                                <th scope="col">Status</th>
                                                <th scope="col">Application Date</th>
                                                <th scope="col" class="text-center">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>                                           
                                            <% List<ClubJoinApplication> clubJoinApplications = (List<ClubJoinApplication>) request.getAttribute("clubJoinApplications");
                                            if (clubJoinApplications != null) {
                                                for (ClubJoinApplication a : clubJoinApplications) {
                                            %>
                                            <tr>
                                                <td><%= a.getApplicationID() %></td>
                                                <td><%= a.getUserID() %></td>
                                                <td><%= a.getUserName() %></td>
                                                <td><%= a.getClubID() %></td>
                                                <td><%= a.getStatus() %></td>
                                                <td><%= a.getApplicationDate() %></td>
                                                <td class="text-center">
                                                    <% if (a.getStatus().equals("Waiting")) { %>
                                                    <a href="management?action=acp&applicationID=<%= a.getApplicationID() %>&tab=joinapplication" class="btn btn-primary ms-2"
                                                       onclick="return confirm('Are you sure you want to accept this member?');">Accept</a>
                                                    <a href="management?action=ref&applicationID=<%= a.getApplicationID() %>&tab=joinapplication" class="btn btn-danger ms-2"
                                                       onclick="return confirm('Are you sure you want to refuse this member?');">Refuse</a>
                                                    <% } %>
                                                </td>
                                            </tr>
                                            <% } %>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Tab Club Information -->
                                <div class="tab-pane fade <%= "clubinfor".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-clubinfor" role="tabpanel" aria-labelledby="pills-clubinfor-tab">
                                    <% Club clubEdit = (Club) ClubDAO.getClubByClubId(user.getMemberClub().getClubID());
                                    request.setAttribute("clubEdit", clubEdit); %>
                                    <form action="editClub" method="post" enctype="multipart/form-data" style="border-top: 1px solid #757575">
                                        <div class="row g-0 justify-content-center">
                                            <div class="col-sm-12 col-xl-5">
                                                <div class="bg-light rounded h-100 p-4">
                                                    <div class="text-center">
                                                        <div class="mb-3 ">
                                                            <img id="clubImage" class="rounded-circle me-lg-2 border border-2 border-dark" src="img/club/${clubEdit.getImage()}" style="width: 300px; height: 300px;">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="formFileSm" class="btn btn-warning">Upload image</label>
                                                            <input class="form-control form-control-sm" id="formFileSm" name="img" type="file" accept="image/*" style="display: none;">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-12 col-xl-7">
                                                <div class="bg-light rounded h-100 p-4">
                                                    <h6 style="color: #009cff;" class="mb-4">Club Information</h6>
                                                    <div class="row mb-3">
                                                        <label for="inputclubname" class="col-sm-3 col-form-label">Club ID</label>
                                                        <div class="col-sm-8">
                                                            <input type="text" class="form-control" id="inputclubname" name="clubID" value="${clubEdit.getClubID()}" readonly="">
                                                        </div>
                                                    </div>
                                                    <div class="row mb-3">
                                                        <label for="inputclubname" class="col-sm-3 col-form-label">Club Name</label>
                                                        <div class="col-sm-8">
                                                            <input type="text" class="form-control" id="inputclubname" name="clubName" value="${clubEdit.getClubName()}">
                                                        </div>
                                                    </div>
                                                    <div class="row mb-3">
                                                        <label for="textareaFullName" class="col-sm-3 col-form-label">Description</label>
                                                        <div class="col-sm-8">
                                                            <textarea id="textareaFullName" name="description" rows="5" class="form-control">${clubEdit.getDescription()}</textarea>
                                                        </div>
                                                    </div>
                                                    <div class="row mb-3">
                                                        <label for="inputclubname" class="col-sm-3 col-form-label">Established Date</label>
                                                        <div class="col-sm-8">
                                                            <input type="text" class="form-control" id="inputclubname" name="date" value="${clubEdit.getEstablishedDate()}" readonly="">
                                                        </div>
                                                    </div>
                                                    <div><button type="submit" class="btn btn-primary">Save</button></div>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>

                                <div class="tab-pane fade <%= "report".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-report" role="tabpanel" aria-labelledby="pills-report-tab">
                                    <c:if test="${reportsChairman!=null}">
                                        <table class="table table-striped">
                                            <thead>
                                                <tr>
                                                    <th scope="col">#</th>
                                                    <th scope="col">Report ID</th>
                                                    <th scope="col">Club ID</th>
                                                    <th scope="col">Semester</th>
                                                    <th scope="col">Member Changes</th>
                                                    <th scope="col">Event Summary</th>
                                                    <th scope="col">Participant Status</th>
                                                    <th scope="col">Create Date</th>
                                                    <th scope="col" class="text-center">Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% 
                                                    int reportIndex = 1;
                                                    Integer reportOffsetChairman = (Integer) request.getAttribute("reportOffsetChairman");
                                                %>
                                                <c:forEach items="${reportsChairman}" var="report">
                                                    <c:if test="${report.getClubID() == user.getMemberClub().getClubID()}">
                                                        <tr>
                                                            <td><%= reportOffsetChairman + reportIndex++ %></td>
                                                            <td scope="col">${report.getReportID()}</td>
                                                            <td scope="col">${report.getClubID()}</td>
                                                            <td scope="col">${report.getSemester()}</td>
                                                            <td scope="col">${report.getMemberChanges()}</td>
                                                            <td scope="col">${report.getEventSummary()}</td>
                                                            <td scope="col">${report.getParticipationStatus()}</td>
                                                            <td scope="col">${report.getCreatedDate()}</td>
                                                            <td scope="col" class="text-center">
                                                                <a href="management?action=deletereport&reportID=${report.getReportID()}&tab=report&reportPageChairman=<%= request.getAttribute("currentReportPageChairman") %>" 
                                                                   class="btn btn-danger ms-2"
                                                                   onclick="return confirm('Are you sure you want to delete this report?');">Delete</a>
                                                                <a href="management?action=editreport&reportID=${report.getReportID()}&tab=report&reportPageChairman=<%= request.getAttribute("currentReportPageChairman") %>" 
                                                                   class="btn btn-primary ms-2">Edit</a>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                        <% 
                                            int currentReportPageChairman = (request.getAttribute("currentReportPageChairman") != null) ? (Integer) request.getAttribute("currentReportPageChairman") : 1;
                                            int totalReportPagesChairman = (request.getAttribute("totalReportPagesChairman") != null) ? (Integer) request.getAttribute("totalReportPagesChairman") : 1;
                                        %>
                                        <nav>
                                            <ul class="pagination justify-content-center">
                                                <li class="page-item <%= (currentReportPageChairman == 1) ? "disabled" : "" %>">
                                                    <a class="page-link" href="management?tab=report&reportPageChairman=<%= currentReportPageChairman - 1 %>">Previous</a>
                                                </li>
                                                <% for (int i = 1; i <= totalReportPagesChairman; i++) { %>
                                                <li class="page-item <%= (i == currentReportPageChairman) ? "active" : "" %>">
                                                    <a class="page-link" href="management?tab=report&reportPageChairman=<%= i %>"><%= i %></a>
                                                </li>
                                                <% } %>
                                                <li class="page-item <%= (currentReportPageChairman == totalReportPagesChairman) ? "disabled" : "" %>">
                                                    <a class="page-link" href="management?tab=report&reportPageChairman=<%= currentReportPageChairman + 1 %>">Next</a>
                                                </li>
                                            </ul>
                                        </nav>
                                    </c:if>
                                    <c:if test="${reportsChairman==null}">
                                        <p>No reports found</p>
                                    </c:if>
                                </div>    
                            </div>      
                        </div>
                    </div>
                </div>

                <% } else if(user.getMemberClub() != null && user.getMemberClub().getRole().equals("ViceChairman")) { %>
                <div class="container-fluid pt-4 px-4">
                    <div class="col-sm-12 col-xl-12">
                        <div class="bg-light rounded h-100 p-4">
                            <div class="d-flex justify-content-between align-items-center flex-wrap">
                                <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "clubmember".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-clubmember-tab" data-bs-toggle="pill" data-bs-target="#pills-clubmember" 
                                                type="button" role="tab" aria-controls="pills-clubmember" aria-selected="true">Club Members</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "joinapplication".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-joinapplication-tab" data-bs-toggle="pill" data-bs-target="#pills-joinapplication" 
                                                type="button" role="tab" aria-controls="pills-joinapplication" aria-selected="false">Join Applications</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "clubinfor".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-clubinfor-tab" data-bs-toggle="pill" data-bs-target="#pills-clubinfor" 
                                                type="button" role="tab" aria-controls="pills-clubinfor" aria-selected="false">Club Information</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "report".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-report-tab" data-bs-toggle="pill" data-bs-target="#pills-report" 
                                                type="button" role="tab" aria-controls="pills-report" aria-selected="false">Report</button>
                                    </li>
                                </ul>
                                <form action="management" method="get" class="d-none d-md-flex ms-4 align-items-center">
                                    <input type="hidden" name="tab" value="clubmember">
                                    <input class="form-control border-0" type="search" name="searchTerm" 
                                           placeholder="Search members" style="width: 300px;">
                                    <input class="btn btn-primary ms-2" type="submit" value="Search">
                                </form>
                            </div>

                            <div class="tab-content" id="pills-tabContent">
                                <!-- Tab Club Members -->
                                <div class="tab-pane fade <%= "clubmember".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-clubmember" role="tabpanel" aria-labelledby="pills-clubmember-tab">
                                    <% 
                                        String searchTermChairman = (String) request.getAttribute("searchTerm");
                                        List<User> clubMemberList = (List<User>) request.getAttribute("clubMemberList");
                                        if (clubMemberList == null || clubMemberList.isEmpty()) { 
                                    %>
                                    <p>No matching members found.</p>
                                    <% } else { %>
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th scope="col">#</th>
                                                <th scope="col">Student ID</th>
                                                <th scope="col">Full Name</th>
                                                <th scope="col">Email</th>
                                                <th scope="col">Role In Club</th>
                                                <th scope="col" class="text-center">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% 
                                            int chairmanIndex = 1;
                                            Integer chairmanOffset = (Integer) request.getAttribute("chairmanOffset");
                                            for (User u : clubMemberList) {
                                            %>
                                            <tr>
                                                <td><%= chairmanOffset + chairmanIndex++ %></td>
                                                <td><%= u.getStudentID() %></td>
                                                <td><%= u.getFullName() %></td>
                                                <td><%= u.getEmail() %></td>
                                                <td><%= u.getMemberClub().getRole() %></td>
                                                <td class="text-center">
                                                    <% if (!u.getMemberClub().getRole().equals("Chairman") && !u.getMemberClub().getRole().equals("ViceChairman")) { %>
                                                    <a href="management?action=editinclub&userID=<%= u.getUserID() %>&clubID=<%= u.getMemberClub().getClubID() %>&tab=clubmember&chairmanPage=<%= request.getAttribute("currentChairmanPage") %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>" 
                                                       class="btn btn-primary ms-2">Edit</a>
                                                    <a href="management?action=kick&userID=<%= u.getUserID() %>&clubID=<%= u.getMemberClub().getClubID() %>&tab=clubmember&chairmanPage=<%= request.getAttribute("currentChairmanPage") %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>" 
                                                       class="btn btn-danger ms-2" 
                                                       onclick="return confirm('Are you sure you want to kick this member?');">Kick</a>
                                                    <% } %>
                                                </td>
                                            </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                    <% if (searchTermChairman != null && !searchTermChairman.trim().isEmpty()) { %>
                                    <p class="mt-3">Search results for "<%= searchTermChairman %>": 
                                        <%= clubMemberList.size() %> results found.</p>
                                        <% } %>
                                        <% } %>
                                        <%
                                        int currentChairmanPage = (request.getAttribute("currentChairmanPage") != null) ? (Integer) request.getAttribute("currentChairmanPage") : 1;
                                        int totalChairmanPages = (request.getAttribute("totalChairmanPages") != null) ? (Integer) request.getAttribute("totalChairmanPages") : 1;
                                        %>
                                    <nav>
                                        <ul class="pagination justify-content-center">
                                            <li class="page-item <%= (currentChairmanPage == 1) ? "disabled" : "" %>">
                                                <a class="page-link" href="management?tab=clubmember&chairmanPage=<%= currentChairmanPage - 1 %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>">Previous</a>
                                            </li>
                                            <% for (int i = 1; i <= totalChairmanPages; i++) { %>
                                            <li class="page-item <%= (i == currentChairmanPage) ? "active" : "" %>">
                                                <a class="page-link" href="management?tab=clubmember&chairmanPage=<%= i %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>"><%= i %></a>
                                            </li>
                                            <% } %>
                                            <li class="page-item <%= (currentChairmanPage == totalChairmanPages) ? "disabled" : "" %>">
                                                <a class="page-link" href="management?tab=clubmember&chairmanPage=<%= currentChairmanPage + 1 %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>">Next</a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>

                                <!-- Tab Join Applications -->
                                <div class="tab-pane fade <%= "joinapplication".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-joinapplication" role="tabpanel" aria-labelledby="pills-joinapplication-tab">
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th scope="col">Application ID</th>
                                                <th scope="col">User ID</th>
                                                <th scope="col">User Name</th>
                                                <th scope="col">Club ID</th>
                                                <th scope="col">Status</th>
                                                <th scope="col">Application Date</th>
                                                <th scope="col" class="text-center">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>                                           
                                            <% List<ClubJoinApplication> clubJoinApplications = (List<ClubJoinApplication>) request.getAttribute("clubJoinApplications");
                                            if (clubJoinApplications != null) {
                                                for (ClubJoinApplication a : clubJoinApplications) {
                                            %>
                                            <tr>
                                                <td><%= a.getApplicationID() %></td>
                                                <td><%= a.getUserID() %></td>
                                                <td><%= a.getUserName() %></td>
                                                <td><%= a.getClubID() %></td>
                                                <td><%= a.getStatus() %></td>
                                                <td><%= a.getApplicationDate() %></td>
                                                <td class="text-center">
                                                    <% if (a.getStatus().equals("Waiting")) { %>
                                                    <a href="management?action=acp&applicationID=<%= a.getApplicationID() %>&tab=joinapplication" class="btn btn-primary ms-2"
                                                       onclick="return confirm('Are you sure you want to accept this member?');">Accept</a>
                                                    <a href="management?action=ref&applicationID=<%= a.getApplicationID() %>&tab=joinapplication" class="btn btn-danger ms-2"
                                                       onclick="return confirm('Are you sure you want to refuse this member?');">Refuse</a>
                                                    <% } %>
                                                </td>
                                            </tr>
                                            <% } %>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Tab Club Information -->
                                <div class="tab-pane fade <%= "clubinfor".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-clubinfor" role="tabpanel" aria-labelledby="pills-clubinfor-tab">
                                    <% Club clubEdit = (Club) ClubDAO.getClubByClubId(user.getMemberClub().getClubID());
                                    request.setAttribute("clubEdit", clubEdit); %>
                                    <form action="editClub" method="post" enctype="multipart/form-data" style="border-top: 1px solid #757575">
                                        <div class="row g-0 justify-content-center">
                                            <div class="col-sm-12 col-xl-5">
                                                <div class="bg-light rounded h-100 p-4">
                                                    <div class="text-center">
                                                        <div class="mb-3 ">
                                                            <img id="clubImage" class="rounded-circle me-lg-2 border border-2 border-dark" src="img/club/${clubEdit.getImage()}" style="width: 300px; height: 300px;">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="formFileSm" class="btn btn-warning">Upload image</label>
                                                            <input class="form-control form-control-sm" id="formFileSm" name="img" type="file" accept="image/*" style="display: none;">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-12 col-xl-7">
                                                <div class="bg-light rounded h-100 p-4">
                                                    <h6 style="color: #009cff;" class="mb-4">Club Information</h6>
                                                    <div class="row mb-3">
                                                        <label for="inputclubname" class="col-sm-3 col-form-label">Club ID</label>
                                                        <div class="col-sm-8">
                                                            <input type="text" class="form-control" id="inputclubname" name="clubID" value="${clubEdit.getClubID()}" readonly="">
                                                        </div>
                                                    </div>
                                                    <div class="row mb-3">
                                                        <label for="inputclubname" class="col-sm-3 col-form-label">Club Name</label>
                                                        <div class="col-sm-8">
                                                            <input type="text" class="form-control" id="inputclubname" name="clubName" value="${clubEdit.getClubName()}">
                                                        </div>
                                                    </div>
                                                    <div class="row mb-3">
                                                        <label for="textareaFullName" class="col-sm-3 col-form-label">Description</label>
                                                        <div class="col-sm-8">
                                                            <textarea id="textareaFullName" name="description" rows="5" class="form-control">${clubEdit.getDescription()}</textarea>
                                                        </div>
                                                    </div>
                                                    <div class="row mb-3">
                                                        <label for="inputclubname" class="col-sm-3 col-form-label">Established Date</label>
                                                        <div class="col-sm-8">
                                                            <input type="text" class="form-control" id="inputclubname" name="date" value="${clubEdit.getEstablishedDate()}" readonly="">
                                                        </div>
                                                    </div>
                                                    <div><button type="submit" class="btn btn-primary">Save</button></div>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>

                                <div class="tab-pane fade <%= "report".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-report" role="tabpanel" aria-labelledby="pills-report-tab">
                                    <c:if test="${reportsChairman!=null}">
                                        <table class="table table-striped">
                                            <thead>
                                                <tr>
                                                    <th scope="col">#</th>
                                                    <th scope="col">Report ID</th>
                                                    <th scope="col">Club ID</th>
                                                    <th scope="col">Semester</th>
                                                    <th scope="col">Member Changes</th>
                                                    <th scope="col">Event Summary</th>
                                                    <th scope="col">Participant Status</th>
                                                    <th scope="col">Create Date</th>
                                                    <th scope="col" class="text-center">Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% 
                                                    int reportIndex = 1;
                                                    Integer reportOffsetChairman = (Integer) request.getAttribute("reportOffsetChairman");
                                                %>
                                                <c:forEach items="${reportsChairman}" var="report">
                                                    <c:if test="${report.getClubID() == user.getMemberClub().getClubID()}">
                                                        <tr>
                                                            <td><%= reportOffsetChairman + reportIndex++ %></td>
                                                            <td scope="col">${report.getReportID()}</td>
                                                            <td scope="col">${report.getClubID()}</td>
                                                            <td scope="col">${report.getSemester()}</td>
                                                            <td scope="col">${report.getMemberChanges()}</td>
                                                            <td scope="col">${report.getEventSummary()}</td>
                                                            <td scope="col">${report.getParticipationStatus()}</td>
                                                            <td scope="col">${report.getCreatedDate()}</td>
                                                            <td scope="col" class="text-center">
                                                                <a href="management?action=deletereport&reportID=${report.getReportID()}&tab=report&reportPageChairman=<%= request.getAttribute("currentReportPageChairman") %>" 
                                                                   class="btn btn-danger ms-2"
                                                                   onclick="return confirm('Are you sure you want to delete this report?');">Delete</a>
                                                                <a href="management?action=editreport&reportID=${report.getReportID()}&tab=report&reportPageChairman=<%= request.getAttribute("currentReportPageChairman") %>" 
                                                                   class="btn btn-primary ms-2">Edit</a>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                        <% 
                                            int currentReportPageChairman = (request.getAttribute("currentReportPageChairman") != null) ? (Integer) request.getAttribute("currentReportPageChairman") : 1;
                                            int totalReportPagesChairman = (request.getAttribute("totalReportPagesChairman") != null) ? (Integer) request.getAttribute("totalReportPagesChairman") : 1;
                                        %>
                                        <nav>
                                            <ul class="pagination justify-content-center">
                                                <li class="page-item <%= (currentReportPageChairman == 1) ? "disabled" : "" %>">
                                                    <a class="page-link" href="management?tab=report&reportPageChairman=<%= currentReportPageChairman - 1 %>">Previous</a>
                                                </li>
                                                <% for (int i = 1; i <= totalReportPagesChairman; i++) { %>
                                                <li class="page-item <%= (i == currentReportPageChairman) ? "active" : "" %>">
                                                    <a class="page-link" href="management?tab=report&reportPageChairman=<%= i %>"><%= i %></a>
                                                </li>
                                                <% } %>
                                                <li class="page-item <%= (currentReportPageChairman == totalReportPagesChairman) ? "disabled" : "" %>">
                                                    <a class="page-link" href="management?tab=report&reportPageChairman=<%= currentReportPageChairman + 1 %>">Next</a>
                                                </li>
                                            </ul>
                                        </nav>
                                    </c:if>
                                    <c:if test="${reportsChairman==null}">
                                        <p>No reports found</p>
                                    </c:if>
                                </div>    
                            </div>      
                        </div>
                    </div>
                </div>

                <% } else if(user.getMemberClub() != null && (user.getMemberClub().getRole().equals("TeamLeader") 
                    || user.getMemberClub().getRole().equals("Member"))) { %>            
                <div class="container-fluid pt-4 px-4">
                    <div class="col-sm-12 col-xl-12">
                        <div class="bg-light rounded h-100 p-4">
                            <div class="d-flex justify-content-between align-items-center flex-wrap">
                                <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "clubmember".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-clubmember-tab" data-bs-toggle="pill" data-bs-target="#pills-clubmember" 
                                                type="button" role="tab" aria-controls="pills-clubmember" aria-selected="true">Club Members</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "joinapplication".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-joinapplication-tab" data-bs-toggle="pill" data-bs-target="#pills-joinapplication" 
                                                type="button" role="tab" aria-controls="pills-joinapplication" aria-selected="false">Join Applications</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "clubinfor".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-clubinfor-tab" data-bs-toggle="pill" data-bs-target="#pills-clubinfor" 
                                                type="button" role="tab" aria-controls="pills-clubinfor" aria-selected="false">Club Information</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "report".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-report-tab" data-bs-toggle="pill" data-bs-target="#pills-report" 
                                                type="button" role="tab" aria-controls="pills-report" aria-selected="false">Report</button>
                                    </li>
                                </ul>
                                <form action="management" method="get" class="d-none d-md-flex ms-4 align-items-center">
                                    <input type="hidden" name="tab" value="clubmember">
                                    <input class="form-control border-0" type="search" name="searchTerm" 
                                           placeholder="Search members" style="width: 300px;">
                                    <input class="btn btn-primary ms-2" type="submit" value="Search">
                                </form>
                            </div>

                            <div class="tab-content" id="pills-tabContent">
                                <!-- Tab Club Members -->
                                <div class="tab-pane fade <%= "clubmember".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-clubmember" role="tabpanel" aria-labelledby="pills-clubmember-tab">
                                    <% 
                                        String searchTermChairman = (String) request.getAttribute("searchTerm");
                                        List<User> clubMemberList = (List<User>) request.getAttribute("clubMemberList");
                                        if (clubMemberList == null || clubMemberList.isEmpty()) { 
                                    %>
                                    <p>No matching members found.</p>
                                    <% } else { %>
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th scope="col">#</th>
                                                <th scope="col">Student ID</th>
                                                <th scope="col">Full Name</th>
                                                <th scope="col">Email</th>
                                                <th scope="col">Role In Club</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% 
                                            int chairmanIndex = 1;
                                            Integer chairmanOffset = (Integer) request.getAttribute("chairmanOffset");
                                            for (User u : clubMemberList) {
                                            %>
                                            <tr>
                                                <td><%= chairmanOffset + chairmanIndex++ %></td>
                                                <td><%= u.getStudentID() %></td>
                                                <td><%= u.getFullName() %></td>
                                                <td><%= u.getEmail() %></td>
                                                <td><%= u.getMemberClub().getRole() %></td>
                                            </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                    <% if (searchTermChairman != null && !searchTermChairman.trim().isEmpty()) { %>
                                    <p class="mt-3">Search results for "<%= searchTermChairman %>": 
                                        <%= clubMemberList.size() %> results found.</p>
                                        <% } %>
                                        <% } %>
                                        <%
                                        int currentChairmanPage = (request.getAttribute("currentChairmanPage") != null) ? (Integer) request.getAttribute("currentChairmanPage") : 1;
                                        int totalChairmanPages = (request.getAttribute("totalChairmanPages") != null) ? (Integer) request.getAttribute("totalChairmanPages") : 1;
                                        %>
                                    <nav>
                                        <ul class="pagination justify-content-center">
                                            <li class="page-item <%= (currentChairmanPage == 1) ? "disabled" : "" %>">
                                                <a class="page-link" href="management?tab=clubmember&chairmanPage=<%= currentChairmanPage - 1 %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>">Previous</a>
                                            </li>
                                            <% for (int i = 1; i <= totalChairmanPages; i++) { %>
                                            <li class="page-item <%= (i == currentChairmanPage) ? "active" : "" %>">
                                                <a class="page-link" href="management?tab=clubmember&chairmanPage=<%= i %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>"><%= i %></a>
                                            </li>
                                            <% } %>
                                            <li class="page-item <%= (currentChairmanPage == totalChairmanPages) ? "disabled" : "" %>">
                                                <a class="page-link" href="management?tab=clubmember&chairmanPage=<%= currentChairmanPage + 1 %><%= searchTermChairman != null ? "&searchTerm=" + searchTermChairman : "" %>">Next</a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>

                                <!-- Tab Join Applications -->
                                <div class="tab-pane fade <%= "joinapplication".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-joinapplication" role="tabpanel" aria-labelledby="pills-joinapplication-tab">
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th scope="col">Application ID</th>
                                                <th scope="col">User ID</th>
                                                <th scope="col">User Name</th>
                                                <th scope="col">Club ID</th>
                                                <th scope="col">Status</th>
                                                <th scope="col">Application Date</th>
                                            </tr>
                                        </thead>
                                        <tbody>                                           
                                            <% List<ClubJoinApplication> clubJoinApplications = (List<ClubJoinApplication>) request.getAttribute("clubJoinApplications");
                                            if (clubJoinApplications != null) {
                                                for (ClubJoinApplication a : clubJoinApplications) {
                                            %>
                                            <tr>
                                                <td><%= a.getApplicationID() %></td>
                                                <td><%= a.getUserID() %></td>
                                                <td><%= a.getUserName() %></td>
                                                <td><%= a.getClubID() %></td>
                                                <td><%= a.getStatus() %></td>
                                                <td><%= a.getApplicationDate() %></td>
                                            </tr>
                                            <% } %>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Tab Club Information -->
                                <div class="tab-pane fade <%= "clubinfor".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-clubinfor" role="tabpanel" aria-labelledby="pills-clubinfor-tab">
                                    <% Club clubEdit = (Club) ClubDAO.getClubByClubId(user.getMemberClub().getClubID());
                                    request.setAttribute("clubEdit", clubEdit); %>
                                    <form action="editClub" method="post" enctype="multipart/form-data" style="border-top: 1px solid #757575">
                                        <div class="row g-0 justify-content-center">
                                            <div class="col-sm-12 col-xl-5">
                                                <div class="bg-light rounded h-100 p-4">
                                                    <div class="text-center">
                                                        <div class="mb-3 ">
                                                            <img id="clubImage" class="rounded-circle me-lg-2 border border-2 border-dark" src="img/club/${clubEdit.getImage()}" style="width: 300px; height: 300px;">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-12 col-xl-7">
                                                <div class="bg-light rounded h-100 p-4">
                                                    <h6 style="color: #009cff;" class="mb-4">Club Information</h6>
                                                    <div class="row mb-3">
                                                        <label for="inputclubname" class="col-sm-3 col-form-label">Club ID</label>
                                                        <div class="col-sm-8">
                                                            <input type="text" class="form-control" id="inputclubname" name="clubID" value="${clubEdit.getClubID()}" readonly="">
                                                        </div>
                                                    </div>
                                                    <div class="row mb-3">
                                                        <label for="inputclubname" class="col-sm-3 col-form-label">Club Name</label>
                                                        <div class="col-sm-8">
                                                            <input type="text" class="form-control" id="inputclubname" name="clubName" value="${clubEdit.getClubName()}">
                                                        </div>
                                                    </div>
                                                    <div class="row mb-3">
                                                        <label for="textareaFullName" class="col-sm-3 col-form-label">Description</label>
                                                        <div class="col-sm-8">
                                                            <textarea id="textareaFullName" name="description" rows="5" class="form-control">${clubEdit.getDescription()}</textarea>
                                                        </div>
                                                    </div>
                                                    <div class="row mb-3">
                                                        <label for="inputclubname" class="col-sm-3 col-form-label">Established Date</label>
                                                        <div class="col-sm-8">
                                                            <input type="text" class="form-control" id="inputclubname" name="date" value="${clubEdit.getEstablishedDate()}" readonly="">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>

                                <div class="tab-pane fade <%= "report".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-report" role="tabpanel" aria-labelledby="pills-report-tab">
                                    <c:if test="${reportsChairman!=null}">
                                        <table class="table table-striped">
                                            <thead>
                                                <tr>
                                                    <th scope="col">#</th>
                                                    <th scope="col">Report ID</th>
                                                    <th scope="col">Club ID</th>
                                                    <th scope="col">Semester</th>
                                                    <th scope="col">Member Changes</th>
                                                    <th scope="col">Event Summary</th>
                                                    <th scope="col">Participant Status</th>
                                                    <th scope="col">Create Date</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% 
                                                    int reportIndex = 1;
                                                    Integer reportOffsetChairman = (Integer) request.getAttribute("reportOffsetChairman");
                                                %>
                                                <c:forEach items="${reportsChairman}" var="report">
                                                    <c:if test="${report.getClubID() == user.getMemberClub().getClubID()}">
                                                        <tr>
                                                            <td><%= reportOffsetChairman + reportIndex++ %></td>
                                                            <td scope="col">${report.getReportID()}</td>
                                                            <td scope="col">${report.getClubID()}</td>
                                                            <td scope="col">${report.getSemester()}</td>
                                                            <td scope="col">${report.getMemberChanges()}</td>
                                                            <td scope="col">${report.getEventSummary()}</td>
                                                            <td scope="col">${report.getParticipationStatus()}</td>
                                                            <td scope="col">${report.getCreatedDate()}</td>

                                                        </tr>
                                                    </c:if>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                        <% 
                                            int currentReportPageChairman = (request.getAttribute("currentReportPageChairman") != null) ? (Integer) request.getAttribute("currentReportPageChairman") : 1;
                                            int totalReportPagesChairman = (request.getAttribute("totalReportPagesChairman") != null) ? (Integer) request.getAttribute("totalReportPagesChairman") : 1;
                                        %>
                                        <nav>
                                            <ul class="pagination justify-content-center">
                                                <li class="page-item <%= (currentReportPageChairman == 1) ? "disabled" : "" %>">
                                                    <a class="page-link" href="management?tab=report&reportPageChairman=<%= currentReportPageChairman - 1 %>">Previous</a>
                                                </li>
                                                <% for (int i = 1; i <= totalReportPagesChairman; i++) { %>
                                                <li class="page-item <%= (i == currentReportPageChairman) ? "active" : "" %>">
                                                    <a class="page-link" href="management?tab=report&reportPageChairman=<%= i %>"><%= i %></a>
                                                </li>
                                                <% } %>
                                                <li class="page-item <%= (currentReportPageChairman == totalReportPagesChairman) ? "disabled" : "" %>">
                                                    <a class="page-link" href="management?tab=report&reportPageChairman=<%= currentReportPageChairman + 1 %>">Next</a>
                                                </li>
                                            </ul>
                                        </nav>
                                    </c:if>
                                    <c:if test="${reportsChairman==null}">
                                        <p>No reports found</p>
                                    </c:if>
                                </div>    
                            </div>      
                        </div>
                    </div>
                    <% } %>    
                </div>
            </div>
        </div>

        <% } %>

        <div class="container-xxl position-relative bg-white d-flex p-0">
            <!-- Footer -->
            <jsp:include page="footer.jsp"/>
        </div>

        <!-- JavaScript Libraries -->
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="lib/chart/chart.min.js"></script>
        <script src="lib/easing/easing.min.js"></script>
        <script src="lib/waypoints/waypoints.min.js"></script>
        <script src="lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="lib/tempusdominus/js/moment.min.js"></script>
        <script src="lib/tempusdominus/js/moment-timezone.min.js"></script>
        <script src="lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js"></script>

        <!-- Template Javascript -->
        <script src="js/main.js"></script>
        <% if (request.getAttribute("alert") != null) { %>
        <script type="text/javascript">
                                                                       window.onload = function () {
                                                                           var alertMessage = '<%= request.getAttribute("alert") %>';
                                                                           alert(alertMessage);
                                                                           window.location.href = './management';
                                                                       };
        </script>
        <% } %>

        <script type="text/javascript">
            function previewImage(event) {
                var reader = new FileReader();
                var imageField = document.getElementById("clubImage");
                reader.onload = function () {
                    if (reader.readyState == 2) {
                        imageField.src = reader.result;
                    }
                }
                reader.readAsDataURL(event.target.files[0]);
            }
            document.getElementById("formFileSm").addEventListener("change", previewImage);
        </script>
    </body>
</html>