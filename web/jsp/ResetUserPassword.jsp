<%--
    Document  	: ResetUserPassword.jsp
    Author     	: Daniele Giuliani, Alessio Paternoster
    Desciption 	: Pagina per confermare il reset della password, ci si accede via link mandato per email
    Requirements: Base.css, AddUserForm.css
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.applicationcontroller.EndResetUserPasswordController" %>
<%@page import="it.webapp.view.ResetUserPasswordView" %>
<%@page import="it.webapp.db.entities.SecurityCode" %>
<%@page import="it.webapp.requestprocessor.EndResetUserPasswordPreprocessor" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="userId" value="<%= (Integer) request.getAttribute(ResetUserPasswordView.USER_ID_ATTRIBUTE) %>"></c:set>
<c:set var="resetCode" value="<%= (String) ((SecurityCode) request.getAttribute(ResetUserPasswordView.PASSWORD_RESET_CODE_ATTRIBUTE)).getString() %>"></c:set>

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
        <div class="container">
            <a class="logo-container">
                <img class="logo" src="${contextPath}/resource/logo.png">
            </a>
            <div class="content">
                <span class="title">Reimposta password</span>
                <p class="text-small">Hai quasi finito, è ora di scegliere la nuova password.</p>
                <form method="POST" action="${contextPath}${EndResetUserPasswordController.URL}" class="registration-form">
                    <div class="form-group">
                        <label>UserID:</label>
                        <input type="text" class="input-text" value="${userId}" disabled/>
                        <input type="hidden" name="${EndResetUserPasswordPreprocessor.USER_ID_PARAM}" class="input-text" value="${userId}"/>
                    </div>
                    <div class="form-group">
                        <label>Codice di recupero:</label>
                        <input type="text" class="input-text" value="${resetCode}" disabled/>
                        <input type="hidden" name="${EndResetUserPasswordPreprocessor.PASSWORD_RESET_CODE_PARAM}" class="input-text" value="${resetCode}"/>
                    </div>
                    <div class="form-group">
                        <label>Nuova password:</label>
                        <input type="password" name="${EndResetUserPasswordPreprocessor.NEW_PASSWORD_PARAM}" class="input-text"/>
                    </div>
                    <div class="form-group flex-centered">
                        <button type="submit" class="input-button">Cambia Password</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
