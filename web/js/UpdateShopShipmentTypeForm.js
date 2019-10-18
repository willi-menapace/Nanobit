/*
    Author      : Daniele Giuliani
		Description : Funzionalità ausiliarie della pagina
*/

var buttonDisable = "<button class=toggle type=button onclick=disableShipment(this)>Disabilita</button>";
var buttonEnable = "<button class=toggle type=button onclick=enableShipment(this)>Abilita</button>";

function disableShipment(target) {
	var textbox = $(target).prev();
	//rimuovi pulsante
	target.remove();
	//inserisci pulsante Abilita
	textbox.after(buttonEnable);
	//modifica stile textbox
	$(textbox).removeClass("price").addClass("price-disabled");
	//disabilita textbox
	$(textbox).prop('disabled', true);
	//imposta valore negativo
	$(textbox).prop('value', -1);
}

function enableShipment(target) {
	var textbox = $(target).prev();
	//rimuovi pulsante
	target.remove();
	//inserisci pulsante Abilita
	textbox.after(buttonDisable);
	//modifica stile textbox
	$(textbox).removeClass("price-disabled").addClass("price");
	//abilita textbox
	$(textbox).prop('disabled', false);
	//imposta costo a 0
	$(textbox).prop('value', 0);
}

function formatRequest() {
	var idInput = $(".submit-wrap").children().first();
	var priceInput = $(idInput).next();

	var idList = "";
	var priceList = "";

	$('.info').each(function(){
		var currentInput = $(this).children("input");
		var currentID = $(currentInput).attr("id");
		var currentPrice = $(currentInput).val();

		idList = idList + currentID + " ";
		priceList = priceList + currentPrice + " ";
 });

 $(idInput).prop('value', idList);
 $(priceInput).prop('value', priceList);
}
