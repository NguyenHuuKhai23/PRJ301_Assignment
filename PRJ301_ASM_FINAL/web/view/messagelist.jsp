<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Messages</title>
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
                <div>
                    <h1 style="margin: 10px 30px 0 30px; background: #009cff; color: white; border-radius: 15px; padding: 5px 10px">Message List</h1>
                </div>
                <%  
                    User user = (User) session.getAttribute("user");
                    String action = request.getParameter("action");
                    if (action == null) {
                        if (request.getAttribute("activeTab") == null) {
                            request.setAttribute("activeTab", "receiver");
                        }
                %>                
                <!-- Messages Section -->
                <div class="container-fluid pt-4 px-4">
                    <div class="col-sm-12 col-xl-12">
                        <div class="bg-light rounded h-100 p-4">
                            <div class="d-flex justify-content-between align-items-center flex-wrap">
                                <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "receiver".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-receiver-tab" data-bs-toggle="pill" data-bs-target="#pills-receiver" 
                                                type="button" role="tab" aria-controls="pills-receiver" aria-selected="true">Receiver</button>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <button class="nav-link <%= "sender".equals(request.getAttribute("activeTab")) ? "active" : "" %>" 
                                                id="pills-sender-tab" data-bs-toggle="pill" data-bs-target="#pills-sender" 
                                                type="button" role="tab" aria-controls="pills-sender" aria-selected="false">Sender</button>
                                    </li>
                                </ul>
                                <a  class="btn btn-success d-none d-md-flex ms-4" href="message?action=mess&senderID=${user.userID}" class="btn btn-primary">New Question</a>
                            </div>
                            <div class="tab-content" id="pills-tabContent">
                                <!-- Receiver Tab -->
                                <div class="tab-pane fade <%= "receiver".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-receiver" role="tabpanel" aria-labelledby="pills-receiver-tab">
                                    <div class="col-sm-12 col-xl-12">
                                        <table class="table table-striped">
                                            <thead>
                                                <tr>
                                                    <th scope="col">#</th>
                                                    <th scope="col">Receiver ID</th>
                                                    <th scope="col">Subject</th>
                                                    <th scope="col">Status</th>
                                                    <th scope="col">Date</th>
                                                    <th scope="col" class="text-center">Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="message" items="${mess}" varStatus="loop">
                                                    <c:choose>
                                                        <c:when test="${user.role eq 'Admin'}">
                                                            <!-- Admin sees messages with receiverID 1, 2, or 3 -->
                                                            <c:if test="${message.receiverID == 1 || message.receiverID == 2 || message.receiverID == 3}">
                                                                <tr>
                                                                    <td>${loop.count}</td>
                                                                    <td>${message.receiverID}</td>
                                                                    <td>${message.subject}</td>
                                                                    <td>${message.status}</td>
                                                                    <td>${message.sentDate}</td>
                                                                    <td class="text-center">
                                                                        <a class="btn btn-primary ms-2" href="message?action=view&messageID=${message.messageID}">View</a>
                                                                        <a class="btn btn-danger ms-2" href="message?action=delete&messageID=${message.messageID}" 
                                                                           onclick="return confirm('Are you sure you want to delete this message?');">Delete</a>
                                                                    </td>
                                                                </tr>
                                                            </c:if>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:if test="${message.receiverID == user.userID && (message.senderID == 1 || message.senderID == 2 || message.senderID == 3)}">
                                                                <tr>
                                                                    <td>${loop.count}</td>
                                                                    <td>${message.receiverID}</td>
                                                                    <td>${message.subject}</td>
                                                                    <td>${message.status}</td>
                                                                    <td>${message.sentDate}</td>
                                                                    <td class="text-center">
                                                                        <a class="btn btn-primary ms-2" href="message?action=view&messageID=${message.messageID}">View</a>
                                                                    </td>
                                                                </tr>
                                                            </c:if>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <!-- Sender Tab -->
                                <div class="tab-pane fade <%= "sender".equals(request.getAttribute("activeTab")) ? "show active" : "" %>" 
                                     id="pills-sender" role="tabpanel" aria-labelledby="pills-sender-tab">
                                    <div class="col-sm-12 col-xl-12">
                                        <table class="table table-striped">
                                            <thead>
                                                <tr>
                                                    <th scope="col">#</th>
                                                    <th scope="col">Sender ID</th>
                                                    <th scope="col">Subject</th>
                                                    <th scope="col">Status</th>
                                                    <th scope="col">Date</th>
                                                    <th scope="col" class="text-center">Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="message" items="${mess}" varStatus="loop">
                                                    <c:if test="${message.senderID == user.userID}">
                                                        <tr>
                                                            <td>${loop.count}</td>
                                                            <td>${message.senderID}</td>
                                                            <td>${message.subject}</td>
                                                            <td>${message.status}</td>
                                                            <td>${message.sentDate}</td>
                                                            <td class="text-center">
                                                                <a class="btn btn-primary ms-2" href="message?action=view&messageID=${message.messageID}">View</a>
                                                                <c:if test="${user.role eq 'Admin'}">
                                                                    <a class="btn btn-danger ms-2" href="message?action=delete&messageID=${message.messageID}" 
                                                                       onclick="return confirm('Are you sure you want to delete this message?');">Delete</a>
                                                                </c:if>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <% } else if ("view".equals(action)) { %>
                <!-- View Message Section -->
                <div class="container-fluid pt-4 px-4">
                    <div class="col-sm-12 col-xl-8">
                        <div class="bg-light rounded h-100 p-4">
                            <h6 style="color: #009cff;" class="mb-4">Message Details</h6>
                            <div class="row mb-3">
                                <label class="col-sm-3 col-form-label">Receiver ID</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" value="${message.receiverID}" readonly>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-sm-3 col-form-label">Subject</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" value="${message.subject}" readonly>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-sm-3 col-form-label">Content</label>
                                <div class="col-sm-8">
                                    <textarea class="form-control" rows="5" readonly>${message.content}</textarea>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-sm-3 col-form-label">Sent Date</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" value="${message.sentDate}" readonly>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-sm-3 col-form-label">Status</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" value="${message.status}" readonly>
                                </div>
                            </div>
                            <div>
                                <a href="message" class="btn btn-secondary">Back</a>
                                <a href="message?action=reply&messageID=${message.messageID}" class="btn btn-primary">Reply</a>
                            </div>
                        </div>
                    </div>
                </div>
                <% } else if ("reply".equals(action)) { %>
                <!-- Reply Form Section -->
                <div class="container-fluid pt-4 px-4">
                    <div class="col-sm-12 col-xl-8">
                        <div class="bg-light rounded h-100 p-4">
                            <h6 style="color: #009cff;" class="mb-4">Reply to Message</h6>
                            <form action="message" method="post">
                                <input type="hidden" name="action" value="reply">
                                <input type="hidden" name="messageID" value="${originalMessage.messageID}">
                                <div class="row mb-3">
                                    <label class="col-sm-3 col-form-label">Receiver ID</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" 
                                               value="${originalMessage.senderID}" readonly>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label class="col-sm-3 col-form-label">Subject</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" name="subject" 
                                               value="${originalMessage.subject}" readonly>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label class="col-sm-3 col-form-label">Original Message</label>
                                    <div class="col-sm-8">
                                        <textarea class="form-control" rows="3" readonly>${originalMessage.content}</textarea>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label class="col-sm-3 col-form-label">Your Reply</label>
                                    <div class="col-sm-8">
                                        <textarea class="form-control" rows="5" name="content"></textarea>
                                        <c:if test="${not empty sessionScope.error}">
                                            <div style="color: red" class="mt-2">${sessionScope.error}</div>
                                            <% session.removeAttribute("error"); %>
                                        </c:if>
                                    </div>
                                </div>
                                <div>
                                    <a href="message" class="btn btn-secondary">Back</a>
                                    <button type="submit" class="btn btn-primary">Send Reply</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <% } else if ("mess".equals(action)) { %>
                <!-- New Question Form Section -->
                <div class="container-fluid pt-4 px-4">
                    <div class="col-sm-12 col-xl-8">
                        <div class="bg-light rounded h-100 p-4">
                            <h6 style="color: #009cff;" class="mb-4">New Question</h6>
                            <form action="message" method="post">
                                <input type="hidden" name="action" value="mess">
                                <input type="hidden" name="senderID" value="<%= user.getUserID() %>">
                                <div class="row mb-3">
                                    <c:if test="${user.role == 'User'}" >
                                        <label class="col-sm-3 col-form-label">Receiver ID</label>
                                        <div class="col-sm-8">
                                            <select class="form-control" name="receiverID">
                                                <option value="1">Admin 1</option>
                                                <option value="2">Admin 2</option>
                                                <option value="3">Admin 3</option>
                                            </select>
                                        </div>
                                    </c:if>
                                    <c:if test="${user.role == 'Admin'}" >
                                            <label class="col-sm-3 col-form-label">Receiver ID</label>
                                            <div class="col-sm-8">
                                                <input type="text" class="form-control" name="receiverID">
                                            </div>
                                    </c:if>
                                </div>
                                <div class="row mb-3">
                                    <label class="col-sm-3 col-form-label">Subject</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" name="subject">
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label class="col-sm-3 col-form-label">Content</label>
                                    <div class="col-sm-8">
                                        <textarea class="form-control" rows="5" name="content"></textarea>
                                        <c:if test="${not empty sessionScope.error}">
                                            <div style="color: red" class="mt-2">${sessionScope.error}</div>
                                            <% session.removeAttribute("error"); %>
                                        </c:if>
                                    </div>
                                </div>
                                <div>
                                    <a href="message" class="btn btn-secondary">Back</a>
                                    <button type="submit" class="btn btn-primary">Send Question</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <% } %>


                <%-- JavaScript Alert for sessionScope.alert (success/failure messages) --%>
                <c:if test="${not empty sessionScope.alert}">
                    <script type="text/javascript">
                        window.onload = function () {
                            var alertMessage = '<%= session.getAttribute("alert") %>';
                            alert(alertMessage);
                        <% session.removeAttribute("alert"); %>
                            window.location.href = 'message';
                        };
                    </script>
                </c:if>
            </div>
            <!-- Content End -->
        </div>

        <!-- Footer -->
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

        <!-- Template Javascript -->
        <script src="js/main.js"></script>
    </body>
</html>