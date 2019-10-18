<%--
    Document     : Login.jsp
    Author       : Daniele Giuliani, Alessio Paternoster
    Desciption   : Pagina per il login di un utente
    Requirements : Base.css, Login.css, utils.js
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.applicationcontroller.LoginController"%>
<%@page import="it.webapp.applicationcontroller.HomepageController"%>
<%@page import="it.webapp.applicationcontroller.AddUserFormController"%>
<%@page import="it.webapp.applicationcontroller.StartResetUserPasswordFormController"%>
<%@page import="it.webapp.requestprocessor.LoginPreprocessor"%>
<%@page import="it.webapp.view.LoginView"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="redirectURL" value="<%= (String) request.getAttribute(LoginView.REDIRECT_ATTRIBUTE) %>"></c:set>
<c:set var="error_message" value="<%= (String) request.getAttribute(LoginView.ERROR_MESSAGE_ATTRIBUTE) %>"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Login.css"/>
    </head>
    <body>
        <c:if test="${not empty error_message}">
            <div class="message-overlay">
                <div class="message message-info">
                    <span class="message-title">Attenzione!</span>
                    <span class="message-text">${error_message}</span>
                    <button class="message-close">Chiudi</button>
                </div>
            </div>
        </c:if>
        <div class="container">
            <div class="content">
                <form method="POST" action="${contextPath}${LoginController.URL}" class="login-form">
                    <a class="logo-container" href="${contextPath}${HomepageController.URL}">
                        <img class="logo" src="${contextPath}/resource/logo.png">
                    </a>
                    <input type="text" name="${LoginPreprocessor.USERNAME_PARAMETER}" placeholder="Username" class="input-text input-user"/>
                    <input type="password" name="${LoginPreprocessor.PASSWORD_PARAMETER}" placeholder="Password" class="input-text input-password"/>
                    <input type="hidden" name="${LoginPreprocessor.REDIRECT_PARAMETER}" readonly value="${redirectURL}">
                    <button type="submit" class="input-button">Login</button>
                    <div>
                        <a href="${contextPath}${StartResetUserPasswordFormController.URL}" class="link-small link-light"><label>Recupera password</label></a>
                        <a href="${contextPath}${AddUserFormController.URL}" class="link-small link-light"><label>Iscriviti</label></a>
                    </div>
                </form>
            </div>
        </div>
    </body>
    <!-- Script -->
    <script src="https://code.jquery.com/jquery-3.2.1.min.js" crossorigin="anonymous"></script>
    <script src="${contextPath}/js/util.js"></script>
</html>
