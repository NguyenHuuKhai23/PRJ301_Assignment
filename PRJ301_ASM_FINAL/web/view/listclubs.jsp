<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.User" %>
<%@ page import="model.Club" %>
<%@ page import="model.ClubJoinApplication" %>
<%@ page import="dao.ClubJoinApplicationDAO" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Clubs</title>
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
        <link href="css/fix.css" rel="stylesheet"/>
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

                <% String op = (String)request.getAttribute("op");
                User user = (User) session.getAttribute("user"); 
                if (op == null) { %>
                <!-- List Clubs -->
                <div>
                    <h1 style="margin: 10px 30px 0 30px; background: #009cff;color: white; border-radius: 15px; padding: 5px 10px">List Clubs</h1>
                </div>
                <div class="container-fluid pt-4 px-4" style="text-align: end">
                    <% if ("Admin".equals(user.getRole())) {%>
                    <a href="./listClubs?action=addclub" class="btn btn-primary ms-2">Add Club</a>
                    <% } %>
                </div>

                <% ArrayList<Club> clubs = (ArrayList<Club>) request.getAttribute("club"); 
                if (user == null) {
                    response.sendRedirect("login");
                    return;
                }%>
                <% if (clubs == null || clubs.isEmpty()) { %>
                <div class="container-fluid pt-4 px-4">
                    <p>No club yet!</p>
                </div>
                <% } else { %>
                <% for (Club club : clubs) { %>
                <div class="container-fluid pt-4 px-4">
                    <div class="row g-4">
                        <div class="col-sm-12 col-xl-12">
                            <div class="bg-light rounded d-flex align-items-center justify-content-between p-4">
                                <div class="d-flex align-items-center">
                                    <img class="rounded-circle me-lg-2" src="img/club/<%= club.getImage() %>" style="width: 100px; height: 100px;">
                                    <div>
                                        <h3 class="mb-0 ms-3 text-primary"><%= club.getClubName() %></h3>
                                        <p class="mb-0 ms-3"><%= club.getDescription() %></p>
                                        <small class="ms-3">Established: <%= club.getEstablishedDate() %></small>
                                    </div>
                                </div>
                                <div class="option_club">

                                    <% if ("Admin".equals(user.getRole())) { %>
                                    <a href="./listClubs?action=delete&clubId=<%= club.getClubID()%>" class="btn btn-danger ms-2">Delete</a>
                                    <% } else { 
                                        if (!UserDAO.isUserInClub(user.getUserID(), club.getClubID())) {
                                    %>
                                    <% if (!ClubJoinApplicationDAO.isUserWaitingForApproval(user.getUserID(), club.getClubID())) { %>
                                    <a href="./listClubs?action=register&clubId=<%= club.getClubID()%>" onclick="confirmRegister('<%= club.getClubID() %>')" class="btn btn-primary ms-2">Register</a>
                                    <% } else if (ClubJoinApplicationDAO.isUserWaitingForApproval(user.getUserID(), club.getClubID())) { %>
                                    <a href="#" class="btn btn-secondary ms-2 disabled">Waiting</a>
                                    <% } %>
                                    <% } else {%>
                                    <a href="#" class="btn btn-secondary ms-2 disabled">Registered</a>
                                    <% } %>
                                    <% } %>
                                    <% if ("Chairman".equals(user.getMemberClub().getRole()) && user.getMemberClub().getClubID() == club.getClubID()) { %>
                                    <a href="./listClubs?action=delete&clubId=<%= club.getClubID()%>" class="btn btn-danger ms-2">Delete</a>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <% } %>
                <% } %>

                <%} else if ("register".equals(op)) { 
                Club club = (Club) request.getAttribute("club"); %>
                <div class="container-fluid pt-4 px-4">
                    <form id="clubRegisterForm" action="listClubs" method="POST" class="justify-content-center">
                        <h4 style="color: #009cff">Are you sure you want to register this club?</h4>
                        <input type="hidden" name="action" value="registerclub">
                        <input type="hidden" id="clubIdInput" name="clubId" value="<%= club.getClubID()%>">
                        <button class="btn btn-primary" type="submit" onclick="return confirmRegister()">Register</button>
                    </form>
                </div>
                <%} else if ("addclub".equals(op)) { %>
                <div class="container-fluid pt-4 px-4">
                    <form action="listClubs" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="addclub">
                        <div class="row g-0 justify-content-center">
                            <div class="col-sm-12 col-xl-3">
                                <div class="bg-light rounded h-100 p-4">
                                    <div class="text-center">
                                        <div class="mb-3">
                                            <img id="clubImage" class="rounded-circle me-lg-2 border border-2 border-dark" src="" style="width: 200px; height: 200px;">
                                        </div>
                                        <div class="mb-3">
                                            <label for="formFileSm" class="btn btn-warning">Upload image</label>
                                            <input class="form-control form-control-sm" id="formFileSm" name="img" type="file" accept="image/*" style="display: none;">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-12 col-xl-6">
                                <div class="bg-light rounded h-100 p-4">
                                    <h6 style="color: #009cff;" class="mb-4">Club information</h6>
                                    <div class="row mb-3">
                                        <label for="inputclubname" class="col-sm-3 col-form-label">Club Name</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="inputclubname" name="clubName">
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label for="textareaFullName" class="col-sm-3 col-form-label">Description</label>
                                        <div class="col-sm-8">
                                            <textarea id="textareaFullName" name="description" rows="5" class="form-control"></textarea>
                                        </div>
                                    </div>
                                    <h style="color: red;">${requestScope.error}</h>
                                    <div><button type="submit" class="btn btn-primary">Save</button></div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <% } %>
            </div>
            <!-- Content End -->
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
                                var alertMessage = '<%= request.getAttribute("alert") %>';
                                alert(alertMessage);
                                window.location.href = './listClubs';
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