<%--
    Document  	: StartResetUserPassword.jsp
    Author     	: Daniele Giuliani, Alessio Paternoster
    Desciption 	: Pagina per iniziare il processo di recupero della password
    Requirements: Base.css, AddUserForm.css, utils.js
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.applicationcontroller.StartResetUserPasswordController" %>
<%@page import="it.webapp.requestprocessor.StartResetUserPasswordPreprocessor" %>
<%@page import="it.webapp.view.StartResetUserPasswordView"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="error_message" value="<%= (String) request.getAttribute(StartResetUserPasswordView.MESSAGE_ATTRIBUTE) %>"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reset Password | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
		<link rel="stylesheet" type="text/css" href="${contextPath}/css/AddUserForm.css"/>
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
            <a class="logo-container">
                <img class="logo" src="${contextPath}/resource/logo.png">
            </a>
            <div class="content">
                <span class="title">Password dimenticata?</span>
                <p class="text-small">Inserisci l'username del tuo account, ti invieremo una email contenente le istruzioni per recuperare la password.</p>
                <form method="POST" action="${contextPath}${StartResetUserPasswordController.URL}" class="registration-form">
                    <div class="form-group">
                        <label>Username:</label>
                        <input type="text" name="${StartResetUserPasswordPreprocessor.USERNAME_PARAM}" class="input-text"/>
                    </div>
                    <div class="form-group flex-centered">
                        <button type="submit" class="input-button">Recupera Password</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
    <!-- Script -->
    <script src="https://code.jquery.com/jquery-3.2.1.min.js" crossorigin="anonymous"></script>
    <script src="${contextPath}/js/util.js"></script>
</html>
