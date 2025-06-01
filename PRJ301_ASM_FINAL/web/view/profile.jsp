<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="model.User" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Profile</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">
        <link href="img/favicon.ico" rel="icon">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
        <link href="lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
        <link href="lib/tempusdominus/css/tempusdominus-bootstrap-4.min.css" rel="stylesheet" />
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
    </head>

    <body>
        <div class="container-xxl position-relative bg-white d-flex p-0">
            <jsp:include page="spinner.jsp"/>
            <jsp:include page="sidebar.jsp"/>
            <div class="content">
                <jsp:include page="navbar.jsp"/>
                <div class="container-fluid pt-4 px-4">
                    <% 
                        User user = (User) session.getAttribute("user");
                        if (user == null) {
                            response.sendRedirect("login");
                            return;
                        }
                        String img = "img/user/" + user.getImage();
                        Boolean isEditing = (Boolean) request.getAttribute("isEditing");
                        if (isEditing == null || !isEditing) { 
                    %>
                    <h1 style="color: #009cff;">User Profile</h1>
                    <form action="profile" method="get">
                        <input type="hidden" name="action" value="edit">
                        <div class="row g-0">
                            <div class="col-sm-12 col-xl-3">
                                <div class="bg-light rounded h-100 p-4">
                                    <div class="text-center">
                                        <div class="mb-3">
                                            <img class="rounded-circle me-lg-2 border border-2 border-dark" src="<%= img %>" style="width: 180px; height: 180px;">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-12 col-xl-6">
                                <div class="bg-light rounded h-100 p-4">
                                    <h6 style="color: #009cff;" class="mb-4">Personal information</h6>
                                    <div class="row mb-3">
                                        <label for="inputStudentID" class="col-sm-3 col-form-label">Student ID</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="inputStudentID" name="studentID" value="<%=user.getStudentID()%>" disabled>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label for="inputFullName" class="col-sm-3 col-form-label">Full Name</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="inputFullName" name="fullName" value="<%=user.getFullName()%>" disabled>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label for="inputEmail" class="col-sm-3 col-form-label">Email</label>
                                        <div class="col-sm-8">
                                            <input type="email" class="form-control" id="inputEmail" name="email" value="<%=user.getEmail()%>" disabled>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label for="inputPassword" class="col-sm-3 col-form-label">Password</label>
                                        <div class="col-sm-8">
                                            <input type="password" class="form-control" id="inputPassword" name="pass1" value="<%=user.getPassword()%>" disabled>
                                        </div>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Change Information</button>
                                </div>
                            </div>
                        </div>
                    </form>
                    <% } else { %>
                    <h1 style="color: #009cff;">Edit Profile</h1>
                    <form action="profile" method="post" enctype="multipart/form-data">
                        <div class="row g-0">
                            <div class="col-sm-12 col-xl-3">
                                <div class="bg-light rounded h-100 p-4">
                                    <div class="text-center">
                                        <div class="mb-3">
                                            <img id="profileImage" class="rounded-circle me-lg-2 border border-2 border-dark" src="<%= img %>?t=<%= System.currentTimeMillis() %>" style="width: 180px; height: 180px;">
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
                                    <h6 style="color: #009cff;" class="mb-4">Personal information</h6>
                                    <div class="row mb-3">
                                        <label for="inputStudentID" class="col-sm-4 col-form-label">Student ID</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="inputStudentID" name="studentID" value="<%=user.getStudentID()%>" disabled>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label for="inputFullName" class="col-sm-4 col-form-label">Full Name</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="inputFullName" name="fullName" value="<%=user.getFullName()%>">
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label for="inputEmail" class="col-sm-4 col-form-label">Email</label>
                                        <div class="col-sm-8">
                                            <input type="email" class="form-control" id="inputEmail" name="email" value="<%=user.getEmail()%>" disabled>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label for="inputPassword1" class="col-sm-4 col-form-label">New Password</label>
                                        <div class="col-sm-8">
                                            <input type="password" class="form-control" id="inputPassword1" name="pass1">
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label for="inputPassword2" class="col-sm-4 col-form-label">Confirm Password</label>
                                        <div class="col-sm-8">
                                            <input type="password" class="form-control" id="inputPassword2" name="pass2">
                                        </div>
                                    </div>
                                    <% if (request.getAttribute("error") != null) { %>
                                    <p style="color: red;"><%= request.getAttribute("error") %></p>
                                    <% } %>
                                    <button type="submit" class="btn btn-primary">Save</button>
                                </div>
                            </div>
                        </div>
                    </form>
                    <% } %>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp"/>
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="lib/chart/chart.min.js"></script>
        <script src="lib/easing/easing.min.js"></script>
        <script src="lib/waypoints/waypoints.min.js"></script>
        <script src="lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="lib/tempusdominus/js/moment.min.js"></script>
        <script src="lib/tempusdominus/js/moment-timezone.min.js"></script>
        <script src="lib/tempusdominus/js/tempusdominus-bootstrap-4.min.js"></script>
        <script src="js/main.js"></script>
        <% if (request.getAttribute("alert") != null) { %>
        <script type="text/javascript">
            window.onload = function () {
                var alertMessage = '<%= request.getAttribute("alert") %>';
                alert(alertMessage);
                window.location.href = './profile';
            };
        </script>
        <% } %>
        <script type="text/javascript">
            function previewImage(event) {
                var reader = new FileReader();
                var imageField = document.getElementById("profileImage");
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