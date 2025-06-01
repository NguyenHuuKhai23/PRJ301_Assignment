<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="model.User" %>
<%@ page import="model.Event" %>
<%@ page import="model.EventFeedback" %>
<%@ page import="model.EventParticipant" %>
<%@ page import="dao.EventParticipantDAO" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="dao.EventFeedbackDAO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Event</title>
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
                    <h1 style="margin: 10px 30px 0 30px; background: #009cff; color: white; border-radius: 15px; padding: 5px 10px">Events</h1>
                </div>

                <% ArrayList<Event> events = (ArrayList<Event>) request.getAttribute("events");
                    if (user == null) {
                        response.sendRedirect("login");
                        return;
                    }
                %>

                <!-- Add Event button -->
                <div class="container-fluid pt-4 px-4" style="text-align: end">
                    <% String highestRole = UserDAO.getHighestRoleInClubs(user.getUserID());
                    if (user != null && user.getMemberClub().getClubID() != 0 && "Chairman".equals(highestRole)) { %>
                    <a href="./event?action=addEvent" class="btn btn-primary ms-2">Add Event</a>
                    <% } %>
                </div>

                <!-- Event List -->
                <% if (events == null || events.isEmpty()) { %>
                <div class="container-fluid pt-4 px-4">
                    <p>No events yet!</p>
                </div>
                <% } else { %>
                <div class="container-fluid pt-4 px-4">
                    <div class="row g-4">
                        <% for (Event event : events) { %>
                        <div class="col-sm-12 col-md-6 col-xl-4">
                            <div class="bg-light rounded d-flex flex-column align-items-center p-4" style="height: 420px">
                                <div class="image-container">
                                    <img class="cropped-image" src="img/event/<%= event.getImage() %>">
                                </div>
                                <div class="w-100 mt-2">
                                    <h4><%= event.getEventName() %></h4>
                                    <p>Description: <%= event.getDescription() %></p>
                                    <p>Location: <%= event.getLocation() %></p>
                                    <small>Event Date: <%= event.getEventDate() %></small>
                                </div>
                                <div class="mt-2">
                                    <% 
                                    java.util.Date currentDate = new java.util.Date(); // Ngày hiện tại
                                    String eventDateStr = event.getEventDate();        // Lấy chuỗi ngày từ event
                                    java.util.Date eventDate = null;                   // Khai báo eventDate
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày của chuỗi
                                        eventDate = sdf.parse(eventDateStr);                   // Chuyển chuỗi thành Date
                                    } catch (Exception e) {
                                        e.printStackTrace(); // Xử lý lỗi nếu chuỗi không đúng định dạng
                                    }
                                    if (eventDate != null && currentDate.compareTo(eventDate) <= 0) {
                                    // Nếu ngày hiện tại <= ngày sự kiện
                                        ArrayList<EventParticipant> eps = (ArrayList<EventParticipant>)EventParticipantDAO.getEventParticipantsByEventId(event.getEventID());
                                        boolean isRegistered = EventParticipantDAO.isUserRegisteredForEvent(user.getUserID(), event.getEventID());
                                            if (isRegistered) {
                                    %>
                                    <a href="#" class="btn btn-secondary ms-2 disabled">Registered</a>
                                    <%
                                            } else {
                                    %>
                                    <a href="./event?action=register&eventId=<%= event.getEventID() %>" class="btn btn-primary ms-2">Register</a>
                                    <%
                                            }
                                    %>
                                    <%} else { 
                                        boolean isFeedBacked = EventFeedbackDAO.hasUserGivenFeedback(user.getUserID(), event.getEventID());
                                        request.setAttribute("isFeedBacked", isFeedBacked);
                                    %>
                                    <div class="mb-1">
                                        <c:if test="${not isFeedBacked}">
                                            <a href="./event?action=feedback&eventId=<%= event.getEventID() %>" class="btn btn-primary ms-2">Feedback</a>
                                        </c:if>
                                        <c:if test="${isFeedBacked}">
                                            <a href="#" class="btn btn-secondary ms-2 disabled">Feedbacked</a>
                                        </c:if>
                                        <a href="./event?action=listfeedback&eventId=<%= event.getEventID() %>" class="btn btn-success ms-2">List Feedback</a>
                                    </div>

                                    <% } %>
                                    <% if (user != null && "Admin".equals(user.getRole())) { %>
                                    <a href="./event?action=delete&eventId=<%= event.getEventID() %>" class="btn btn-danger ms-2">Delete</a>
                                    <% } %>
                                    <% if ((user.getMemberClub().getClubID() != 0) 
                                    && UserDAO.isUserInClub(user.getUserID(), event.getClubID())
                                    && (( UserDAO.getUserRoleInClub(user.getUserID(), event.getClubID()).equals("Chairman") 
                                           || UserDAO.getUserRoleInClub(user.getUserID(), event.getClubID()).equals("ViceChairman") 
                                                || UserDAO.getUserRoleInClub(user.getUserID(), event.getClubID()).equals("TeamLeader")))) { %>
                                    <a href="./event?action=rollcall&eventId=<%= event.getEventID() %>" class="btn btn-primary ms-2">Roll Call</a>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                        <% } %>
                    </div>
                </div>
                <% } %>

                <% } else if ("register".equals(op)) {
                    Event event = (Event) request.getAttribute("event");
                %>
                <form id="eventRegisterForm" action="event" method="POST">
                    <h4>Are you sure you want to join this event?</h4>
                    <input type="hidden" name="action" value="registerEvent">
                    <input type="hidden" id="eventIdInput" name="eventId" value="<%= event.getEventID() %>">
                    <button type="submit" onclick="return confirm('Are you sure you want to register?')">Join Event</button>
                </form>

                <% } else if ("addevent".equals(op)) { %>
                <div class="container-fluid pt-4 px-4">
                    <form action="event" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="addevent">
                        <div class="row g-0 justify-content-center">
                            <div class="col-sm-12 col-xl-3">
                                <div class="bg-light rounded h-100 p-4 text-center">
                                    <div class="mb-3">
                                        <!-- Thêm id="eventImage" vào đây -->
                                        <img id="eventImage" class="rounded-circle me-lg-2 border border-2 border-dark" src="" style="width: 200px; height: 200px;">
                                    </div>
                                    <div class="mb-3">
                                        <label for="formFileSm" class="btn btn-warning">Upload image</label>
                                        <input class="form-control form-control-sm" id="formFileSm" name="img" type="file" accept="image/*" style="display: none;">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-12 col-xl-6">
                                <div class="bg-light rounded h-100 p-4">
                                    <h6 style="color: #009cff;" class="mb-4">Event Information</h6>
                                    <div class="row mb-3">
                                        <label for="inputEventName" class="col-sm-3 col-form-label">Event Name</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="inputEventName" name="eventName">
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label for="inputDescription" class="col-sm-3 col-form-label">Description</label>
                                        <div class="col-sm-8">
                                            <textarea id="inputDescription" name="description" rows="5" class="form-control"></textarea>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label for="inputEventDate" class="col-sm-3 col-form-label">Event Date</label>
                                        <div class="col-sm-8">
                                            <input type="datetime-local" class="form-control" id="inputEventDate" name="eventDate">
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label for="inputLocation" class="col-sm-3 col-form-label">Location</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="inputLocation" name="location">
                                        </div>
                                    </div>
                                    <h style="color: red;">${requestScope.error}</h>
                                    <div><button type="submit" class="btn btn-primary">Save</button></div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>

                <% } else if ("rollcall".equals(op)) { %>
                <div class="container-fluid pt-4 px-4">
                    <% 
                        List<EventParticipant> eventParticipant = (List<EventParticipant>) request.getAttribute("eventParticipant");
                        String eventIdFromRequest = request.getParameter("eventId");
                        Event event = (Event) request.getAttribute("event"); // Assuming event is passed from servlet
                        if (eventParticipant == null || eventParticipant.isEmpty()) { 
                    %>
                    <div class="bg-light rounded h-100 p-4">
                        <p>No registered participants for this event!</p>
                    </div>
                    <% } else { %>
                    <div class="bg-light rounded h-100 p-4">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th scope="col">#</th>
                                    <th scope="col">UserID</th>
                                    <th scope="col">StudentID</th>
                                    <th scope="col">Full Name</th>
                                    <th scope="col">Role</th>
                                    <th scope="col">Status</th>
                                    <th scope="col" class="text-center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                    int index = 1;
                                    for (EventParticipant u : eventParticipant) {
                                %>
                                <tr>
                                    <td><%= index++ %></td>
                                    <td><%= u.getUserID() %></td>
                                    <td><%= u.getStudentID() %></td>
                                    <td><%= u.getFullName() %></td>
                                    <td><%= u.getRole() %></td>
                                    <td><%= u.getStatus() %></td>
                                    <td class="text-center">
                                        <% 
                                            String status = u.getStatus();
                                            if (!status.equals("Absent") && !status.equals("Attended")) {
                                        %>
                                        <a href="./event?action=absent&eventParticipantID=<%= u.getEventParticipantID()%>&eventId=<%= eventIdFromRequest %>" 
                                           class="btn btn-danger ms-2"
                                           onclick="return confirm('Are you sure you want to mark this user as absent?');">Absent</a>
                                        <a href="./event?action=attended&eventParticipantID=<%= u.getEventParticipantID()%>&eventId=<%= eventIdFromRequest %>" 
                                           class="btn btn-primary ms-2"
                                           onclick="return confirm('Are you sure you want to mark this user as attended?');">Attended</a>
                                        <% 
                                            }
                                        %>
                                    </td>
                                </tr>
                                <% 
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                    <% } %>
                </div>
                <% } else if ("feedback".equals(op)) { %>

                <div class="container-fluid pt-4 px-4">
                    <div class="col-sm-12 col-xl-8">
                        <div class="bg-light rounded h-100 p-4">
                            <h6 style="color: #009cff;" class="mb-4">Feedback</h6>
                            <form action="event" method="POST">
                                <input type="hidden" name="action" value="submitFeedback">
                                <% Event event = (Event) request.getAttribute("event"); %>
                                <input type="hidden" name="eventId" value="<%= event != null ? event.getEventID() : "" %>"> 
                                <!-- Event Name -->
                                <div class="row mb-3">
                                    <label for="inputEventName" class="col-sm-3 col-form-label">Event Name</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" id="inputEventName" name="eventName" value="<%= event != null ? event.getEventName() : "" %>" readonly>
                                    </div>
                                </div>

                                <!-- Rating -->
                                <div class="row mb-3">
                                    <label for="rating" class="col-sm-3 col-form-label">Rating</label>
                                    <div class="col-sm-8">
                                        <div class="product__details__rating">
                                            <i class="fa fa-star" data-rating="1"></i>
                                            <i class="fa fa-star" data-rating="2"></i>
                                            <i class="fa fa-star" data-rating="3"></i>
                                            <i class="fa fa-star" data-rating="4"></i>
                                            <i class="fa fa-star" data-rating="5"></i>
                                            <input type="hidden" name="rating" id="rating-value" value="0">
                                        </div>
                                    </div>
                                </div>

                                <!-- Description -->
                                <div class="row mb-3">
                                    <label for="textareaFullName" class="col-sm-3 col-form-label">Comment</label>
                                    <div class="col-sm-8">
                                        <textarea id="textareaFullName" name="description" rows="5" class="form-control" placeholder="Enter your comment..."></textarea>
                                    </div>
                                </div>

                                <!-- Hiển thị thông báo lỗi từ servlet -->
                                <% String error = (String) request.getAttribute("error"); %>
                                <% if (error != null) { %>
                                <div class="text-danger mb-3"><%= error %></div>
                                <% } %>

                                <div>
                                    <input type="submit" value="Save" class="btn btn-primary">
                                    <a href="./event" class="btn btn-secondary">Cancel</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <% } else if ("listfeedback".equals(op)) { %>
                <% ArrayList<EventFeedback> eventFeedback = (ArrayList<EventFeedback>) request.getAttribute("eventFeedback");
                Event event = (Event) request.getAttribute("event");
                if (eventFeedback == null || eventFeedback.isEmpty() || event == null) { %>
                <div class="container-fluid pt-4 px-4">
                    <p>No feedback yet!</p>
                </div>
                <% } else { 
                double avgRate = EventFeedbackDAO.getAverageRatingByEventId(event.getEventID());
                String.format("%.2f", avgRate);
                %>
                <div class="container-fluid pt-4 px-4">
                    <div class="row g-4">
                        <div class="col-sm-9 col-xl-9">
                            <h4 style="background: #009cff; color: white; border-radius: 15px; padding: 10px 15px">
                                Event Name: <%= event.getEventName()%></br>
                                Average rating: <%=avgRate%><i class="fa fa-star" style="color: #f5c518"></i>
                            </h4>                           
                        </div>
                    </div>
                </div>
                <% int index = 0; // Biến đếm để tạo ID duy nhất %>
                <% for (EventFeedback e : eventFeedback) { %>
                <div class="container-fluid pt-4 px-4">
                    <div class="row g-4">
                        <div class="col-sm-9 col-xl-9">
                            <div class="col-sm-12 col-xl-12">
                                <div class="bg-light rounded h-100 p-4">
                                    <div class="bg-light rounded d-flex align-items-center mb-3">
                                        <div class="d-flex align-items-center">
                                            <img class="rounded-circle me-lg-2" src="img/user/<%= e.getImage() %>" style="width: 35px; height: 35px;">
                                            <h5 class="mb-0"><%= e.getFullName()%></h5>
                                            <p class="mb-0 ms-3"><%= e.getFeedbackDate()%></p>
                                        </div>
                                        <div class="product__details__rating ms-auto" id="rating-container-<%= index %>">
                                            <i class="fa fa-star" data-rating="1"></i>
                                            <i class="fa fa-star" data-rating="2"></i>
                                            <i class="fa fa-star" data-rating="3"></i>
                                            <i class="fa fa-star" data-rating="4"></i>
                                            <i class="fa fa-star" data-rating="5"></i>
                                            <input type="hidden" name="rating" id="rating-value-<%= index %>" value="<%= e.getRating() %>">
                                        </div>
                                    </div>  
                                    <p><%= e.getComments()%></p>
                                    <c:if test="${user.getRole().equals('Admin')}">
                                        <a href="event?action=deleteFb&feedbackID=<%= e.getFeedbackID()%>" class="btn btn-danger mt-2" 
                                           onclick="return confirm('Are you sure you want to delete this feedback?');">Delete</a>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <% index++; %>
                <% } %>


                <!-- JavaScript để hiển thị sao -->
                <script type="text/javascript">
                    document.addEventListener("DOMContentLoaded", function () {
                    <% for (int i = 0; i < eventFeedback.size(); i++) { %>
                        var rating = document.getElementById("rating-value-<%= i %>").value;
                        var stars = document.querySelectorAll("#rating-container-<%= i %> .fa-star");
                        for (var j = 0; j < stars.length; j++) {
                            if (j < rating) {
                                stars[j].classList.add("rated");
                            }
                        }
                    <% } %>
                    });
                </script>

                <% } %>
                <% } %>

            </div>
            <!-- Content End -->

            <!-- Footer -->

        </div>
        <div class="container-xxl position-relative bg-white d-flex p-0">
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
        <script src="js/main.js"></script>

        <% if (request.getAttribute("alert") != null) { %>
        <script type="text/javascript">
                    window.onload = function () {
                        var alertMessage = '<%= request.getAttribute("alert") %>';
                        alert(alertMessage);
                        window.location.href = './event';
                    };
        </script>
        <% } %>

        <script type="text/javascript">
            function previewImage(event) {
                var reader = new FileReader();
                var imageField = document.getElementById("eventImage"); // Chọn thẻ img bằng id

                reader.onload = function () {
                    if (reader.readyState == 2) {
                        imageField.src = reader.result; // Gán ảnh mới vào src
                    }
                }
                reader.readAsDataURL(event.target.files[0]); // Đọc file ảnh được chọn
            }

            // Gắn sự kiện onchange vào input file
            document.getElementById("formFileSm").addEventListener("change", previewImage);
        </script>
        <script type="text/javascript">
            document.addEventListener('DOMContentLoaded', function () {
                const stars = document.querySelectorAll('.product__details__rating .fa-star');
                const ratingInput = document.getElementById('rating-value');

                stars.forEach(star => {
                    star.addEventListener('click', function () {
                        const rating = this.getAttribute('data-rating');
                        ratingInput.value = rating; // Cập nhật giá trị rating vào input ẩn

                        // Cập nhật trạng thái hiển thị của sao
                        stars.forEach(s => {
                            if (s.getAttribute('data-rating') <= rating) {
                                s.classList.add('checked');
                            } else {
                                s.classList.remove('checked');
                            }
                        });
                    });
                });
            });
        </script>
    </body>
</html>