/*
    Author     	: Daniele Giuliani
    Desciption 	: Codice javascript usato per fare apparire finestre popup per lasciare recensioni di item e reviews
    Requirements: Per funzionare la libreria deve essere importata solamente dopo l'importazione di JQuery
    TODO: completare con le funzionalità AJAX, i parametri sono giò stati racoolti, basta effettuare la richiesta e in base alla risposta modificare la pagina.
*/

function leaveItemFeedback(buttonClicked) {
	var shopOrderItemId = $(buttonClicked).parent().parent().parent().parent().children(".shop-order-item-id").html();

	//Create popup input everything
	$("body").append("<div class=feedback-overlay></div>");
	$(".feedback-overlay").append("<div class=feedback></div>");
	$(".feedback").append("<p class=feedback-title>Lascia feedback articolo</p>");
	// i seguenti campi vanno recuperati da la funzione javascript che si occupa di inviare la richiesta con ajax
	$(".feedback").append("<input type=text value=" + "\"" + shopOrderItemId + "\"" + "  id=shopOrderItemId hidden/>");
	$(".feedback").append("<input type=text class=feedback-input-title placeholder='Inserisci un titolo' id=reviewTitleAxax>");
	$(".feedback").append("<textarea type=text class=feedback-input-description placeholder='Inserisci una breve descrizione della tua esperienza' id=reviewDescriptionAxax/>");
	$(".feedback").append("<p>Giudizio complessivo (1 pessimo - 10 ottimo) <input type=number min=1 max=10 class=feedback-review-rating id=reviewRatingAjax></input></p>");

	// bottoni conferma invio e chiusura
	$(".feedback").append("<div class=feedback-buttons></div>");
	$(".feedback-buttons").append("<button onclick=removeFeedbackBox()>Chiudi</button>");
	$(".feedback-buttons").append("<button onclick=sendItemFeedback()>Invia</button>");

	//make layout appear
	$(".feedback-overlay").animate({opacity: '1'});
}

function leaveShopFeedback(buttonClicked) {
	var shopOrderId = $(".shop-order-id").html();

	//Create popup input everything
	$("body").append("<div class=feedback-overlay></div>");
	$(".feedback-overlay").append("<div class=feedback></div>");
	$(".feedback").append("<p class=feedback-title>Lascia feedback venditore</p>");
	// i seguenti campi vanno recuperati da la funzione javascript che si occupa di inviare la richiesta con ajax
	$(".feedback").append("<input type=text value=" + "\"" + shopOrderId + "\"" + "  id=shopOrderId hidden/>");
	$(".feedback").append("<input type=text class=feedback-input-title placeholder='Inserisci un titolo' id=reviewTitleAxax>");
	$(".feedback").append("<textarea type=text class=feedback-input-description placeholder='Inserisci una breve descrizione della tua esperienza' id=reviewDescriptionAxax/>");
	$(".feedback").append("<p>Giudizio complessivo (1 pessimo - 10 ottimo) <input type=number min=1 max=10 class=feedback-review-rating id=reviewRatingAjax></input></p>");

	// bottoni conferma invio e chiusura
	$(".feedback").append("<div class=feedback-buttons></div>");
	$(".feedback-buttons").append("<button onclick=removeFeedbackBox()>Chiudi</button>");
	$(".feedback-buttons").append("<button onclick=sendShopFeedback()>Invia</button>");

	//make layout appear
	$(".feedback-overlay").animate({opacity: '1'});
}

function removeFeedbackBox() {
	$(".feedback-overlay").animate(
		{
			opacity: '0'
		},
		function() {
			$(".feedback-overlay").remove();
		}
	);
}

