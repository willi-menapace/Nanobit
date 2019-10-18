<%--
    Document    : UpdateShop.jsp
    Author      : Daniele Giuliani, Willi Menapace, Alessio Paternoster
    Desciption  : Pagina utilizzata dall'utente per aggiornare i dati relativi al proprio negozio
    Requiriments: Base.css, UpdateUser.css
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="it.webapp.applicationcontroller.UpdateShopController"%>
<%@page import="it.webapp.requestprocessor.UpdateShopPreprocessor"%>
<%@page import="it.webapp.view.UpdateShopView"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="error_message" value="<%= (String) request.getAttribute(UpdateShopView.MESSAGE_ATTRIBUTE) %>"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Aggiornamento dati negozio | Nanobit</title>
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu:700" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Base.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/UpdateUser.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/TopBar.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/Footer.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/jquery-ui.min.css">
    </head>
    <body>
        <c:if test="${not empty error_message}">
            <div class="message-overlay">
              <div class="message message-info">
                  <span class="message-text">${error_message}</span>
                  <button class="message-close">Chiudi</button>
              </div>
            </div>
        </c:if>
        <div class="container">
            <%@include file="/WEB-INF/jspf/TopBar.jspf" %>
				<div class="content">
					<div class="content-box content-box-middle main">
						<span class="title">Modifica negozio</span>
						<p>Completa il modulo inserendo i nuovi dati relativi al tuo negozio.</p>
						<div class="spacer"></div>
						<form action="${contextPath}${UpdateShopController.URL}" method="POST">
							<div class="info">
								<p class="info-label">Descrizione </p>
								<textarea name="${UpdateShopPreprocessor.DESCRIPTION_PARAMAM}" class="textbox"></textarea>
							</div>
							<div class="info">
								<p class="info-label">Numero di telefono</p>
								<input type="text" name="${UpdateShopPreprocessor.PHONE_PARAMAM}" class="textbox"/>
							</div>
							<div class="submit-wrap">
								<button type="submit" class="link-button">Conferma</button>
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
</html>