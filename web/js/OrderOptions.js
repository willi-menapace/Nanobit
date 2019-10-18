/* 
 * Author       : Alessio Paternoster
 * Description  : Libreria JS / JQuery per la pagina OrderOptions 
 */

/* VARIABILI GLOBALI - INIZIALIZZATE in OrderOptions.jsp */
var creditCardExpirationMonthParam;
var creditCardExpirationYearParam;
var creditCardNumberParam;
var creditCardCVVParam;
var shipmentIDInputName;

/* VARIABILI LOCALI */
var shipmentPriceInputName = "shipment-price";
var totalCostElementID = "#total-order-cost";
var expirationDateField = $("#credit-card-expiration-date");
var numberField = $("#credit-card-number");
var CVVField = $("#credit-card-cvv");

var form = {
    
    // Regex utili a controllare la correttezza dei valori mese o anno (MM o YYYY)
    monthRegex  : /^((0[1-9])|(1[0-2]))$/,
    yearRegex   : /^((201[7-9])|(20[2-3][0-9]))$/,
    
    /* Funzione che si occupa di visualizzare i label di errore e i bordi rossi sui campi */
    showFieldError : function(fieldContainer, inputElement){
        fieldContainer.find("label.field-error").show();
        inputElement.addClass("field-error");
    },
    
    /* Funzione che si occupa di rimuovere / nascondere i label di errore e i bordi rossi sui campi */
    removeFieldError : function(fieldContainer, inputElement){
        fieldContainer.find("label.field-error").hide();
        inputElement.removeClass("field-error");
    },
    
    /* Funzione che si occupa di controllare se per ciascun menu dropdown degli shipment è stata selezionata un'opzione */
    checkShipmentID : function(){
        var shipmentIDElements = $("input[name='" + shipmentIDInputName + "']");
        var allShipmentIDElementsSet = true;
        
        /* Controllo tutti i menu dropdown e se qualquno non è impostato, mostro il label di campo non impostato */
        shipmentIDElements.each(function() {
            if($(this).val() === ""){
                $(this).parents("div.info-value").find("label.field-error").show();
                allShipmentIDElementsSet = false;
            }
        });
        
        return allShipmentIDElementsSet;
    },
    
    /* Funzione che si occupa di controllare una determinata parte della data (MM o YYYY) */
    checkDateFieldPart : function (fieldContainer, datePartParam, dateFieldPartRegex){
        // Estraggo i dati che mi servono
        var expirationInput = fieldContainer.find("input[name='" + datePartParam + "']");
        var expirationValue = expirationInput.val().replace(/\s+/g, '');
        var isValidDateFieldPart = dateFieldPartRegex.test(expirationValue);
        
        // Controllo se il mese o l'anno (in base a come viene richiamata la funzione) è corretto o meno
        if(!isValidDateFieldPart){
            form.showFieldError(fieldContainer, expirationInput);
            return false;
        }
        return true;
    },
    
    /* Funzione che si occupa di controllare i dati del Form */
    check : function(){
        
        var formIsValid = true;
        
        /* CONTROLLO DATA - MESE / ANNO */
        if(!form.checkDateFieldPart(expirationDateField, creditCardExpirationMonthParam, form.monthRegex)){
            formIsValid = false;
        }
        if(!form.checkDateFieldPart(expirationDateField, creditCardExpirationYearParam, form.yearRegex)){
            formIsValid = false;
        }
        
        /* CONTROLLO NUMERO CARTA */
        var creditCardNumberInput = numberField.find("input[name='" + creditCardNumberParam + "']");
        var creditCardNumberValue = creditCardNumberInput.val().replace(/\s+/g, '');
        
        // Deve essere un numero maggiore di 0 e avere almeno 12 cifre
        if(creditCardNumberValue <= 0 || creditCardNumberValue.toString().length < 12){
            form.showFieldError(numberField, creditCardNumberInput);
            formIsValid = false;
        }
        
        /* CONTROLLO CVV CARTA */
        var creditCardCVVInput = CVVField.find("input[name='" + creditCardCVVParam + "']");
        var creditCardCVVValue = creditCardCVVInput.val().replace(/\s+/g, '');

        if(creditCardCVVValue <= 0 || creditCardCVVValue.toString().length !== 3){
            form.showFieldError(CVVField, creditCardCVVInput);
            formIsValid = false;
        }
        
        /* CONTROLLO MENU DROPDOWN SHIPMENT */
        if(!form.checkShipmentID()){
            formIsValid = false;
        }
        
        /* Se c'è qualcosa che non va informo l'utente */
        if(!formIsValid){
            alert("Attenzione ! Hai dimenticato di compilare qualche campo. Ricontrolla e riprova.");
        }
        
        return formIsValid;
    }
};

var shipmentDropdown = {
    
    /* Funzione che si occupa dell'aggiornamento del shipmentPrice, shipmentID e totalOrderCost 
     * quando si cambia seleziona nel menu dropdown degli shipment */
    onShipmentSelect : function(){
        
        /* Cambio l'ID dello shipment */
        var selectedShipmentID = $(this).find("span.value").text();
        $(this).parents("div.info-value").find("input[name='" + shipmentIDInputName + "']").val(selectedShipmentID);

        /* Prendo i prezzi e vari campi associati */
        var shipmentPriceElement = $(this).parents("div.info-value").find("input[name='" + shipmentPriceInputName + "']");
        var oldShipmentPrice = parseFloat(shipmentPriceElement.val());
        var newShipmentPrice = parseFloat($(this).find("input").val());
        
        /* Controllo che siano numeri validi */
        if(isNaN(oldShipmentPrice) || isNaN(newShipmentPrice)){
            console.log("Errore durante il parsing dei campi 'shipment-price'. Errore NaN.");
            return false;
        }
        
        /* Cambio il prezzo. E se è cambiato il totale lo aggiorno ! */
        if(oldShipmentPrice !== newShipmentPrice){
            var totalCost = parseFloat($(totalCostElementID).text());
            
            /* Controllo che sia un numero valido */
            if(isNaN(totalCost)){
                console.log("Errore durante il parsing dei campi 'shipment-price'. Errore NaN.");
                return false;
            }
            
            totalCost = totalCost - oldShipmentPrice;
            totalCost = totalCost + newShipmentPrice;

            $(totalCostElementID).text(totalCost);
            shipmentPriceElement.val(newShipmentPrice);
        }
        
        /* Se erano visualizzati dei label '*Campo mancante' o '*Selezionare campo', ora nascondo tutto */
        $(this).parents("div.info-value").find("label.field-error").hide();
    }
    
};

$(document).ready(function(){         
    $("#order-info-form").submit(function(){
        return form.check();
    });     
    $(".area-payment-info input").on("input", function(){
        form.removeFieldError($(this).parent(".info-noborder"), $(this));
    });
    $(".dropdown-menu ul.dropdown-menu-list li").click(shipmentDropdown.onShipmentSelect);
});