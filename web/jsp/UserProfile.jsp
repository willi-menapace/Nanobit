<%--
    Document    : UserProfile
    Author      : Daniele Giuliani, Alessio Paternoster, Willi Menapace
    Desciption  : Pagina dove l'utente loggato può visualizzare i dati riguardanti il suo profilo e modificarli
    Requirements: Base.css, UserProfile.css
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.requestprocessor.UpdateUserPreprocessor" %>
<%@page import="it.webapp.applicationcontroller.UpdateUserController" %>
<%@page import="it.webapp.db.entities.AddressEntity"%>
<%@page import="it.webapp.view.UserProfileView"%>
<%@page import="it.webapp.db.entities.UserEntity"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="user" value="<%= (UserEntity) request.getAttribute(UserProfileView.USER_ATTRIBUTE) %>"></c:set>
<c:set var="address" value="<%= (AddressEntity) request.getAttribute(UserProfileView.ADDRESS_ATTRIBUTE) %>"></c:set>
<c:set var="message" value="<%= (String) request.getAttribute(UserProfileView.MESSAGE_ATTRIBUTE) %>"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Profilo | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/UserProfile.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/external/jquery-ui.min.css">
    </head>
    <body>
        <c:if test="${not empty message}">
            <div class="message-overlay">
                <div class="message message-warning">
                    <span class="message-title">Attenzione!</span>
                    <span class="message-text">${message}</span>
                    <button class="message-close">Chiudi</button>
                </div>
            </div>
        </c:if>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
            <div class="content">
                <div class="content-box main">
                    <span class="title">Il mio profilo</span>
                    <div class="spacer"></div>
                    <div class="info">
                        <p class="info-label">Username</p>
                        <p class="info-value">${user.username}</p>
                    </div>
                    <div class="info">
                        <p class="info-label">Password</p>
                        <p class="info-value">***************</p>
                    </div>
                    <div class="info">
                        <p class="info-label">Nome</p>
                        <p class="info-value">${user.firstName}</p>
                    </div>
                    <div class="info">
                        <p class="info-label">Cognome</p>
                        <p class="info-value">${user.lastName}</p>
                    </div>
                    <div class="info">
                        <p class="info-label">Email</p>
                        <p class="info-value">${user.email}</p>
                    </div>
                    <div class="info">
                        <p class="info-label">Indirizzo</p>
                        <p class="info-value">${address.street} ${address.streetNumber}</p>
                    </div>
                    <div class="info">
                        <p class="info-label">Città</p>
                        <p class="info-value">${address.city}</p>
                    </div>
                    <div class="info">
                        <p class="info-label">Provincia</p>
                        <p class="info-value">${address.district}</p>
                    </div>
                    <div class="info">
                        <p class="info-label">Codice postale</p>
                        <p class="info-value">${address.zip}</p>
                    </div>
                    <div class="info">
                        <p class="info-label">Stato</p>
                        <p class="info-value">${address.country}</p>
                    </div>
                </div>
                <div class="content-box side">
                    <p>Ti stai trasferendo? Vuoi cambiare password? Aggiorna il tuo profilo.</p>
                    <div class="spacer"></div>
                    <div class="button-container">
                        <button class="link-medium link-button" id="showHideUpdateUserDataForm">Modifica Dati</button>
                    </div>
                    <div class="spacer"></div>
                    <form id="updateUserDataForm" method="POST" action="${contextPath}${UpdateUserController.URL}">
                        <div class="info">
                            <p class="info-label">Password</p>
                            <input type="password" name="${UpdateUserPreprocessor.PASSWORD_PARAM}" class="info-value"/>
                        </div>
                        <div class="info">
                            <p class="info-label">Conferma Password</p>
                            <input type="password" name="${UpdateUserPreprocessor.PASSWORD_CONFIRM_PARAM}" class="info-value"/>
                        </div>
                        <div class="info">
                            <p class="info-label">Email</p>
                            <input type="text" name="${UpdateUserPreprocessor.EMAIL_PARAM}" value="${user.email}" class="info-value"/>
                        </div>
                        <div class="info">
                            <p class="info-label">Conferma Email</p>
                            <input type="text" name="${UpdateUserPreprocessor.EMAIL_CONFIRM_PARAM}" value="${user.email}" class="info-value"/>
                        </div>
                        <div class="info">
                            <p class="info-label">Via / Viale</p>
                            <input type="text" name="${UpdateUserPreprocessor.STREET_NAME_PARAM}" value="${address.street}" class="info-value"/>
                        </div>
                        <div class="info">
                            <p class="info-label">Numero civico</p>
                            <input type="text" name="${UpdateUserPreprocessor.STREET_NUMBER_PARAM}" value="${address.streetNumber}" class="info-value"/>
                        </div>
                        <div class="info">
                            <p class="info-label">Citt&agrave;</p>
                            <input type="text" name="${UpdateUserPreprocessor.CITY_PARAM}" value="${address.city}" class="info-value"/>
                        </div>
                        <div class="info">
                            <p class="info-label">Provincia</p>
                            <input type="text" name="${UpdateUserPreprocessor.DISTRICT_PARAM}" value="${address.district}" class="info-value"/>
                        </div>
                        <div class="info">
                            <p class="info-label">Codice postale</p>
                            <input type="text" name="${UpdateUserPreprocessor.ZIP_PARAM}" value="${address.zip}" class="info-value"/>
                        </div>
                        <div class="info">
                            <p class="info-label">Stato</p>
                            <input type="text" name="${UpdateUserPreprocessor.COUNTRY_PARAM}" value="${address.country}" class="info-value"/>
                        </div>
                        <div class="spacer"></div>
                        <div class="button-container">
                            <input type="submit" class="link-medium link-button" value="Aggiorna Dati" />
                        </div>
                    </form>
                </div>
            </div>
            <%@include file="/WEB-INF/jspf/Footer.jspf" %>
        </div>
    </body>
    <!-- Script -->
    <script src="https://code.jquery.com/jquery-3.2.1.min.js" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" crossorigin="anonymous"></script>
    <script src="${contextPath}/js/TopBar.js"></script>
    <script src="${contextPath}/js/util.js"></script>
    <script src="${contextPath}/js/UserProfile.js"></script>
    <script>
        general.formOpened = false;
        general.showHideFormButtonID = "#showHideUpdateUserDataForm";
        general.formID = "#updateUserDataForm";
        var formInputsInfo = {
            password : "${UpdateUserPreprocessor.PASSWORD_PARAM}",
            passwordConfirm : "${UpdateUserPreprocessor.PASSWORD_CONFIRM_PARAM}",
            email : "${UpdateUserPreprocessor.EMAIL_PARAM}",
            emailConfirm : "${UpdateUserPreprocessor.EMAIL_CONFIRM_PARAM}",
            streetName : "${UpdateUserPreprocessor.STREET_NAME_PARAM}",
            streetNumber : "${UpdateUserPreprocessor.STREET_NUMBER_PARAM}",
            city : "${UpdateUserPreprocessor.CITY_PARAM}",
            district : "${UpdateUserPreprocessor.DISTRICT_PARAM}",
            zip : "${UpdateUserPreprocessor.ZIP_PARAM}",
            country : "${UpdateUserPreprocessor.COUNTRY_PARAM}"
        };
    </script>
</html>