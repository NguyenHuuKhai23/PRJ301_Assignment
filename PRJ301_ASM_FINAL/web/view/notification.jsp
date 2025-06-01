<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="model.User" %>
<%@page import="model.MemberClub" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Notification</title>
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
                <% 
                    String op = (String) request.getAttribute("op");
                    User user = (User) session.getAttribute("user");
                    if (op == null) { 
                %>
                <div>
                    <h1 style="margin: 10px 30px 0 30px; background: #009cff; color: white; border-radius: 15px; padding: 5px 10px">List Notification</h1>
                </div>
                
                <div class="container-fluid pt-4 px-4" style="text-align: end">
                    <a href="./management" class="btn btn-warning ms-2">Management</a>
                    <% if ("Admin".equals(user.getRole()) || "Chairman".equals(user.getMemberClub().getRole()) || "ViceChairman".equals(user.getMemberClub().getRole())) { %>
                    <a href="notification?action=addNotification" class="btn btn-primary ms-2">Add Notification</a>
                    <% } %>
                </div>
                <c:forEach items="${notification}" var="n">
                    <c:if test="${user.role == 'Admin' || (user.memberClub.clubID == n.clubID) || n.user.role == 'Admin'}">                       
                        <div class="container-fluid pt-4 px-4">
                            <div class="row g-4">
                                <div class="col-sm-12 col-xl-12">
                                    <div class="bg-light rounded d-flex align-items-center justify-content-between p-4">
                                        <div class="d-flex align-items-center">
                                            <div>                                   
                                                <p class="mb-0">Date: ${n.notificationDate}</p>
                                                <p class="mb-0">Posted by: ${n.user.fullName} (${n.user.memberClub.role})</p>
                                                <p class="mb-4">ClubID: ${n.clubID}</p>
                                                <p class="mb-0">Content:</p>
                                                <p class="mb-0">${n.content}</p>
                                            </div>
                                        </div>
                                        <div class="option_club">
                                            <% 
                                                boolean canDelete = false;
                                                if ("Admin".equals(user.getRole())) {
                                                    // Admin có thể xóa tất cả thông báo
                                                    canDelete = true;
                                                } else if ("Chairman".equals(user.getMemberClub().getRole()) || "ViceChairman".equals(user.getMemberClub().getRole())) {
                                                    // Chairman và ViceChairman chỉ xóa được thông báo trong club của họ
                                                    int userClubID = user.getMemberClub().getClubID();
                                                    int notificationClubID = ((model.Notification) pageContext.getAttribute("n")).getClubID();
                                                    canDelete = (userClubID == notificationClubID);
                                                }
                                            %>
                                            <% if (canDelete) { %>
                                            <a href="notification?action=delete&notificationID=${n.notificationID}" 
                                               class="btn btn-danger ms-2" 
                                               onclick="return confirm('Are you sure you want to delete this notification?');">Delete</a>
                                            <% } %>
                                            <% 
                                                int currentUserID = user.getUserID();
                                                int currentNotificationID = ((model.Notification) pageContext.getAttribute("n")).getNotificationID();
                                                boolean isViewed = dao.ReadNotificationDAO.isNotificationRead(currentUserID, currentNotificationID);
                                            %>
                                            <% if (!isViewed) { %>
                                            <a href="notification?action=view&notificationID=${n.notificationID}" class="btn btn-primary ms-2">View</a>
                                            <% } else { %>
                                            <span class="btn btn-secondary ms-2 disabled">Viewed</span>
                                            <% } %>                 
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
                <% } else if ("addNotification".equals(op)) { %>
                <div class="container-fluid pt-4 px-4">
                    <div class="col-sm-12 col-xl-8">
                        <div class="bg-light rounded h-100 p-4">
                            <h6 style="color: #009cff;" class="mb-4">Add New Notification</h6>
                            <form action="notification" method="POST">
                                <input type="hidden" name="action" value="submitNotification">
                                <!-- Notification Content -->
                                <div class="row mb-3">
                                    <label for="inputContent" class="col-sm-3 col-form-label">Content</label>
                                    <div class="col-sm-8">
                                        <textarea id="inputContent" name="content" rows="5" class="form-control" placeholder="Enter notification content..."></textarea>
                                    </div>
                                </div>
                                <!-- Display error message from servlet -->
                                <% String error = (String) request.getAttribute("error"); %>
                                <% if (error != null) { %>
                                <div class="text-danger mb-3"><%= error %></div>
                                <% } %>
                                <div>
                                    <input type="submit" value="Save" class="btn btn-primary">
                                    <a href="./notification" class="btn btn-secondary">Cancel</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
            <!-- Content End -->
        </div>
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
                                                       window.location.href = './notification';
                                                   };
        </script>
        <% } %>
    </body>
</html>