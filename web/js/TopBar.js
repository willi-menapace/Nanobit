/*
    Author     	: Alessio Paternoster
    Desciption 	: Script JS per il file TopBar.jspf
*/

/* VARIABILI GLOBALI ESTERNE - INIZIALIZZATE IN TopBar.jspf */
var itemSearchHintControllerURL;
var itemSearchHintQueryParam;
var itemSearchHintCount;
var itemSearchHintInputObjectID;

var itemSearchHint = {
        
    minLength: 2,   // Numero di caratteri dopo il quale Ajax inizia a ottenere 'Hint'
    delay: 500,     // Ajax aspetterà questo tot di millisecondi senza che l'utente abbia scritto prima di fare richieste

    /* Funzione che si occupa di fare richieste Ajax ed ottenere gli 'Hint' 
     * NOTA: La funzione deve chiamarsi source e non altro ! (è richiamata così in JQuery UI) */
    source: function (request, response) {
        // Preparo i dati da inviare
        var dataToSend = {};
        dataToSend[itemSearchHintQueryParam] = request.term;
        dataToSend[itemSearchHintCount] = 5;
        
        // Faccio la richiesta Ajax
        $.ajax({
            type: "POST",
            url: itemSearchHintControllerURL,
            datatype: 'json',
            data: dataToSend,
            success: function (data) {
                response(data);
            },
            error: function (jqXHR, textStatus, thrownError) {
                console.log("ItemSearchHint Ajax - Error ! Not possible to retrive 'hint' data.");
            }
        });
    }

};

/* La funzione di qutocompletamento (le Hint) sono associate al campo di ricerca */
$(document).ready(function() {
    $(itemSearchHintInputObjectID).autocomplete(itemSearchHint);
});
