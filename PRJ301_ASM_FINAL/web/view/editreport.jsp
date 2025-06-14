<%-- 
    Document   : editreport
    Created on : Mar 16, 2025, 2:56:02 PM
    Author     : knguy
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="model.User" %>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <title>Edit - report</title>
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

                <% User user = (User) session.getAttribute("user");  
                     
                    if (user == null) {
                        response.sendRedirect("login");
                        return;
                    }
                %>

                <!-- Khac nhau o day -->
                <!-- Form Start -->
                <div class="container-fluid pt-4 px-4">
                    <form action="report" method="post">
                        <div class="col-sm-12 col-xl-11">
                            <div class="bg-light rounded h-100 p-4">
                                <h1 style="color: #009cff;" class="mb-4">Report</h1>
                                <input type="hidden" name="action" value="edit">
                                <div class="row mb-3">
                                    <label for="text" class="col-sm-3 col-form-label">Report ID</label>
                                    <div class="col-sm-12">
                                        <input type="text" class="form-control" name="reportID" value="${report.getReportID()}" readonly="">
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label for="text" class="col-sm-3 col-form-label">Semester</label>
                                    <div class="col-sm-12">
                                        <input type="text" class="form-control" name="semester" value="${report.getSemester()}" readonly="">
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label for="textareaFullName" class="col-sm-3 col-form-label">Member changes</label>
                                    <div class="col-sm-12">
                                        <textarea id="textareaFullName" name="memberChanges" rows="3" class="form-control">${report.getMemberChanges()}</textarea>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label for="textareaFullName" class="col-sm-3 col-form-label">Event summary</label>
                                    <div class="col-sm-12">
                                        <textarea id="textareaFullName" name="eventSummary" rows="3" class="form-control">${report.getEventSummary()}</textarea>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label for="textareaFullName" class="col-sm-3 col-form-label">Participant status</label>
                                    <div class="col-sm-12">
                                        <textarea id="textareaFullName" name="participantStatus" rows="3" class="form-control">${report.getParticipationStatus()}</textarea>
                                    </div>
                                </div>
                                <h style="color: red;">${requestScope.error}</h>
                                <div><button type="submit" class="btn btn-primary">Save</button></div>
                            </div>
                        </div>
                    </form>
                   
                </div>
                
            </div>
        </div>

        <!-- Footer -->
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
                // Hiển thị alert
                var alertMessage = '<%= request.getAttribute("alert") %>';
                alert(alertMessage);

                window.location.href = './management';
            };
        </script>
        <% } %>

    </body>

</html>
