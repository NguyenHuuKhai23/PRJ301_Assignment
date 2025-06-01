<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="model.User" %>
<%@page import="model.Event" %>
<%@page import="dao.EventDAO" %>
<%@page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <title>Home</title>
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

                <!-- Khac nhau o day -->
                <!-- Sale & Revenue Start -->
                <div class="container-fluid pt-4 px-4">
                    <div class="row g-4">
                        <img src="img/website/banner.png" alt="alt" class="mb-4"/>
                    </div>
                    <div class="row g-4">
                        <div class="col-sm-9 col-xl-9">

                            <div class="col-sm-12 col-xl-12">
                                <div class="bg-light rounded h-100 p-4">
                                    <div class="bg-light rounded d-flex align-items-center mb-3">
                                        <img class="rounded-circle me-lg-2" src="img/website/admin.png" style="width: 35px; height: 35px;">
                                        <h3 class="mb-0 text-primary">Admin</h3>
                                    </div>  
                                    <h4 class="mb-4">[Thông báo] Hệ thống quản lí câu lạc bộ chính thức ra mắt!</h4>
                                    <p>Phần mềm quản lý câu lạc bộ trong trường đại học nhằm hỗ trợ ban quản trị trong
                                        việc quản lý hoạt động, thành viên và báo cáo định kỳ. Ứng dụng cung cấp nền
                                        tảng giúp tăng hiệu quả, minh bạch và tiện lợi trong việc vận hành các câu lạc bộ,
                                        đồng thời khuyến khích sinh viên tham gia tích cực hơn.</p>
                                    <div class="bg-light rounded d-flex align-items-center mb-3">
                                        <i class="fa fa-map-pin">Website:</i><a style="color: #757575" href="./home">&nbsp;FPT_Club</a>
                                    </div>
                                    <div class="bg-light rounded d-flex align-items-center mb-3">
                                        <i class="fa fa-map-pin">Group:</i><a style="color: #757575" href="https://www.facebook.com/DaihocFPTHaNoi/?locale=vi_VN" target="_blank">&nbsp;Facebook.com</a>
                                    </div>
                                    <div class="bg-light rounded d-flex align-items-center mb-3">
                                        <h5>Những điều mà website FPT_Club có thể đem lại cho bạn</h5>
                                    </div>

                                    <ul>
                                        <li>Đăng Kí Tham gia câu lạc bộ</li>
                                        <li>Theo đuổi đam mê cũng như là tìm ra sở thích mới</li>
                                        <li>Theo dõi các sự kiện của trường một cách nhanh nhất</li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <div class="col-sm-3 col-xl-3">
                            <div class="col-sm-12 col-xl-12">
                                <div class="bg-light rounded h-100 p-4">
                                    <h3 class="mb-4">Recent Events</h3>
                                    <% ArrayList<Event> event = EventDAO.getLatestEvents(); 
                                    request.setAttribute("event", event);%>
                                    <c:forEach items="${event}" var="e">
                                        <div class="mb-3" style="display: flex;">
                                            <img src="img/event/${e.image}" style="width: 30%; height: 50px; margin-right: 5px;"/>
                                            <a href="./event?page=events"><p style="color: #757575; text-decoration: underline;">${e.eventName}</p></a>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>

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