<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.Cookie" %>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <title>Login</title>
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

        <style>
            body {
                background: url('img/website/CLB-FPTU-DN.png') no-repeat center center fixed;
                background-size: cover;
            }
        </style>
    </head>

    <body>
        <!-- Spinner Start -->
        <div id="spinner" class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
            <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
                <span class="sr-only">Loading...</span>
            </div>
        </div>
        <!-- Spinner End -->


        <!-- Sign In Start -->
        <div class="container-fluid">

            <div class="row h-100 align-items-center justify-content-center" style="min-height: 100vh;">
                <div class="col-12 col-sm-8 col-md-6 col-lg-5 col-xl-4">
                    <form action="login" method="post">
                        <div class="bg-light rounded p-4 p-sm-5 my-4 mx-3">
                            <div class="d-flex align-items-center justify-content-between mb-3">
                                <a href="index.jsp" class="">
                                    <h3 class="text-primary">FPT_CLUB</h3>
                                </a>
                                <h3>Log In</h3>
                            </div>
                            <div class="form-floating mb-2">
                                <input type="email" class="form-control" id="floatingInput" placeholder="Email" name="email" value="${requestScope.email_sv}">
                                <label for="floatingInput">Email address</label>
                            </div>
                            <div class="form-floating mb-2">
                                <input type="password" class="form-control" id="floatingPassword" placeholder="Password" name="pass" value="${requestScope.pass_sv}">
                                <label for="floatingPassword">Password</label>
                            </div>
                            <div>
                                <h style="color: red;">&nbsp;&nbsp;${requestScope.error}</h>
                            </div>
                            <div class="d-flex align-items-center justify-content-between mb-4">
                                <div class="form-check">
                                    <input type="checkbox" class="form-check-input" id="rememberMe" name="remember" ${requestScope.remember}>
                                    <label class="form-check-label" for="rememberMe">Remember me</label>
                                </div>
                                <a href="./changepassword">Forgot Password</a>
                            </div>                            
                            <input type="submit" value="Log In" class="btn btn-primary py-3 w-100 mb-4">
                        </form>
                        <p class="text-center mb-0">Don't have an Account? <a href="./signup">Sign Up</a></p>
                    </div>
                </div>
            </div>
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
                alert('<%= request.getAttribute("alert") %>');
                window.location.href = './login'; // Chuyển hướng sau khi đóng alert
            };
        </script>
        <% } %>
    </body>

</html>