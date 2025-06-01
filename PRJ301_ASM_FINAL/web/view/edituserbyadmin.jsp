<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="model.User" %>
<%@page import="model.Club" %>
<%@page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Edit Profile - Admin</title>
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
                    User user = (User) session.getAttribute("user");  
                    if (user == null) {
                        response.sendRedirect("login");
                        return;
                    }
                %>

                <!-- Form Start -->
                <div class="container-fluid pt-4 px-4">
                    <h1 style="color: #009cff;">Edit Profile</h1>
                    <form action="adminedit" method="post" enctype="multipart/form-data">
                        <div class="row g-0">
                            <div class="col-sm-12 col-xl-3">
                                <div class="bg-light rounded h-100 p-4">
                                    <div class="text-center">
                                        <div class="mb-3">
                                            <img class="rounded-circle me-lg-2 border border-2 border-dark" 
                                                 src="img/user/${userEdit.image}" 
                                                 style="width: 180px; height: 180px;"
                                                 alt="User Image">
                                        </div>        
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-12 col-xl-6">
                                <div class="bg-light rounded h-100 p-4">
                                    <h6 style="color: #009cff;" class="mb-4">Personal Information</h6>

                                    <div class="row mb-3">
                                        <label for="userID" class="col-sm-4 col-form-label">User ID</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="userID" name="userID" 
                                                   value="${userEdit.userID}" readonly>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="studentID" class="col-sm-4 col-form-label">Student ID</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="studentID" name="studentID" 
                                                   value="${userEdit.studentID}" readonly="">
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="fullName" class="col-sm-4 col-form-label">Full Name</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="fullName" name="fullName" 
                                                   value="${userEdit.fullName}">
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="email" class="col-sm-4 col-form-label">Email</label>
                                        <div class="col-sm-8">
                                            <input type="email" class="form-control" id="email" name="email" 
                                                   value="${userEdit.email}">
                                        </div>
                                    </div>                           

                                    <div class="row mb-3">
                                        <label for="role" class="col-sm-4 col-form-label">Role In Club</label>
                                        <div class="col-sm-8">
                                            <select name="role" class="form-select">
                                                <c:choose>
                                                    <c:when test="${userEdit.memberClub.role != null}">
                                                        <option value="Chairman" ${userEdit.memberClub.role == 'Chairman' ? 'selected' : ''}>Chairman</option>
                                                        <option value="ViceChairman" ${userEdit.memberClub.role == 'ViceChairman' ? 'selected' : ''}>ViceChairman</option>
                                                        <option value="TeamLeader" ${userEdit.memberClub.role == 'TeamLeader' ? 'selected' : ''}>TeamLeader</option>
                                                        <option value="Member" ${userEdit.memberClub.role == 'Member' ? 'selected' : ''}>Member</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="Admin" ${userEdit.role == 'Admin' ? 'selected' : ''}>Admin</option>
                                                        <option value="User" ${userEdit.role == 'User' ? 'selected' : ''}>User</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </select>
                                            <div>
                                                <p><i>If change Club ID, then role in club will set to MEMBER</i></p>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="clubID" class="col-sm-4 col-form-label">Club</label>
                                        <input type="hidden" name="old" value="${userEdit.memberClub.clubID}">
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="email" name="clubID" 
                                                   value="${userEdit.memberClub.clubID}" <c:if test="${userEdit.getRole() eq 'Admin'}">readonly=""</c:if>>
                                        </div>
                                    </div>

                                    <% if (request.getAttribute("error") != null) { %>
                                    <p style="color: red;"><%= request.getAttribute("error") %></p>
                                    <% } %>

                                    <button type="submit" class="btn btn-primary">Save</button>
                                    <a class="btn btn-secondary ms-2" href="./management">Cancel</a>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <jsp:include page="footer.jsp"/>

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
    </body>
</html>