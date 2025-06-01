<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="model.User" %>
<%@page import="model.Event" %>
<%@page import="dao.EventDAO" %>
<%@page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
    <!-- Head section remains unchanged -->
    <head>
        <meta charset="utf-8">
        <title>History</title>
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
        <link href="css/fix.css" rel="stylesheet">
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

                <!-- Modified content section -->
                <div class="container-fluid pt-4 px-4">
                    <c:choose>
                        <c:when test="${history == null || empty history}">
                            <div class="text-center py-4">
                                <p>No History</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">UserID</th>
                                        <th scope="col" class="text-center">Action Done</th>
                                        <th scope="col">Change Date</th>
                                        <th scope="col" class="text-center">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${history}" var="h" varStatus="loop">
                                        <tr>
                                            <td>${historyOffset + loop.count}</td>
                                            <td>${h.userID}</td>
                                            <td class="text-center">${h.action}</td>
                                            <td>${h.changeDate}</td>
                                            <td class="text-center">
                                                <a href="history?action=delete&historyID=${h.historyID}&historyPage=${currentHistoryPage}" 
                                                   class="btn btn-danger ms-2" 
                                                   onclick="return confirm('Are you sure you want to delete this history?');">Delete</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>

                            <!-- Pagination -->
                            <nav>
                                <ul class="pagination justify-content-center">
                                    <li class="page-item ${currentHistoryPage == 1 ? 'disabled' : ''}">
                                        <a class="page-link" href="history?historyPage=${currentHistoryPage - 1}">Previous</a>
                                    </li>
                                    <c:forEach begin="1" end="${totalHistoryPages}" var="i">
                                        <li class="page-item ${i == currentHistoryPage ? 'active' : ''}">
                                            <a class="page-link" href="history?historyPage=${i}">${i}</a>
                                        </li>
                                    </c:forEach>
                                    <li class="page-item ${currentHistoryPage == totalHistoryPages ? 'disabled' : ''}">
                                        <a class="page-link" href="history?historyPage=${currentHistoryPage + 1}">Next</a>
                                    </li>
                                </ul>
                            </nav>
                        </c:otherwise>
                    </c:choose>
                </div>
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
    </body>
</html>