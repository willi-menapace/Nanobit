<%--
    Document  	: Register.jsp
    Author     	: Daniele Giuliani, Alessio Paternoster
    Desciption 	: Pagina visualizzata durante la registrazione di un utente
    Requirements: Base.css, AddUserForm.css
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.applicationcontroller.AddUserController" %>
<%@page import="it.webapp.applicationcontroller.PrivacyPolicyController" %>
<%@page import="it.webapp.requestprocessor.AddUserPreprocessor" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registrazione | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/AddUserForm.css"/>
    </head>
    <body>
        <div class="container">
            <a class="logo-container">
                <img class="logo" src="${contextPath}/resource/logo.png">
            </a>
            <div class="content">
                <span class="title">Registrazione</span>
                <form method="POST" action="${contextPath}${AddUserController.URL}" class="registration-form">
                    <p class="text-small">Per continuare con la registrazione compila i seguenti campi.</p>
                    <div class="form-group">
                        <label>Nome:</label>
                        <input type="text" name="${AddUserPreprocessor.FIRST_NAME_PARAM}" placeholder="Mario" class="input-text"/>
                    </div>
                    <div class="form-group">
                        <label>Cognome:</label>
                        <input type="text" name="${AddUserPreprocessor.LAST_NAME_PARAM}" placeholder="Rossi" class="input-text"/>
                    </div>
                    <div class="form-group">
                        <label>Username:</label>
                        <input type="text" name="${AddUserPreprocessor.USERNAME_PARAM}" placeholder="" class="input-text"/>
                    </div>
                    <div class="form-group">
                        <label>Password:</label>
                        <input type="password" name="${AddUserPreprocessor.PASSWORD_PARAM}" placeholder="" class="input-text"/>
                    </div>
                    <div class="form-group">
                        <label>Conferma la password:</label>
                        <input type="password" name="${AddUserPreprocessor.PASSWORD_CONFIRM_PARAM}" placeholder="" class="input-text"/>
                    </div>
                    <div class="form-group">
                        <label>Email:</label>
                        <input type="email" name="${AddUserPreprocessor.EMAIL_PARAM}" placeholder="mario@rossi.it" class="input-text"/>
                    </div>
                    <div class="form-group">
                        <label>Conferma l'email:</label>
                        <input type="email" name="${AddUserPreprocessor.EMAIL_CONFIRM_PARAM}" class="input-text"/>
                    </div>
                    <div class="form-group">
                        <label>Via/Viale:</label>
                        <input type="text" name="${AddUserPreprocessor.STREET_NAME_PARAM}" placeholder="Viale dei Fiori" class="input-text"/>
                    </div>
                    <div class="form-group">
                        <label>Numero Civico:</label>
                        <input type="text" name="${AddUserPreprocessor.STREET_NUMBER_PARAM}" placeholder="3A" class="input-text"/>
                    </div>
                    <div class="form-group">
                        <label>Città:</label>
                        <input type="text" name="${AddUserPreprocessor.CITY_PARAM}" placeholder="" class="input-text"/>
                    </div>
                    <div class="form-group">
                        <label>Provincia:</label>
                        <input type="text" name="${AddUserPreprocessor.DISTRICT_PARAM}" placeholder="" class="input-text"/>
                    </div>
                    <div class="form-group">
                        <label>Codice Postale:</label>
                        <input type="text" name="${AddUserPreprocessor.ZIP_PARAM}" placeholder="" class="input-text"/>
                    </div>
                    <div class="form-group">
                        <label>Stato:</label>
                        <input type="text" name="${AddUserPreprocessor.COUNTRY_PARAM}" placeholder="" class="input-text"/>
                    </div>
                    <p class="text-small">
                        Cliccando sul pulsante "Registrati" dichiari di aver preso visione e accetti la seguente 
                        <a target="_blank" class="link-small link-dark" href="${contextPath}${PrivacyPolicyController.URL}">politica sulla privacy</a>.
                    </p>
                    <div class="form-group flex-centered">
                        <button type="submit" class="input-button">Registrati</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
