/*
    Author     	: Alessio Paternoster
    Desciption 	: Script JS per il file UserProfile.jsp
*/
// VARIABILI GLOBALI - DICHIARATE IN UserProfile.jsp
var formInputsInfo;

var general =  {
    
    // Variabili inizializzate esternamente nel file UserProfile.jsp
    formOpened : false,
    showHideFormButtonID : "",
    formID : "",
    errorMessages : {
        empty : "*Campo vuoto. Compilalo",
        notmatch : "*Il campo non combacia",
        error: "*Campo errato"
    },
    
    
    /* Funzione che permette di mostrare il form per l'aggiornamento dei dati */
    showForm : function(){
        $(general.showHideFormButtonID).text("Annulla");
        $(general.formID).show();
        general.formOpened = true;
    },
    
    /* Funzione che permette di nascondere il form per l'aggiornamento dei dati */
    hideForm : function(){
        $(general.showHideFormButtonID).text("Modifica Dati");
        $(general.formID).trigger("reset");
        $(general.formID).hide();
        general.formOpened = false;
    },
    
    /* Funzione che è associata al pulsante per mostrare/nascondere il form per l'aggiornamento dei dati */
    onMouseClick : function(mouseEvent){
        if(general.formOpened === true){
            general.hideForm();
        }else{
            general.showForm();
        }
    },
    
    /* Funzione che si occupa di mostrare il bordo rosso attorno ai campi errati */
    showInputErrorShadow : function(inputElement){
        inputElement.addClass("field-error");
    },
    
    /* Funzione che si occupa di visualizzare il label di errore sui campi vuoti */
    showFieldError : function(inputElement, message){
        /* Se l'errore non è in mostra.. */
        if(!inputElement.hasClass("field-error")){
            // Costruisco l'elemento "campi vuoti"
            var emptyErrorElement = $("<p></p>");
            emptyErrorElement.addClass("field-error");
            emptyErrorElement.text(message);
            emptyErrorElement.insertBefore(inputElement);

            // Inserisco il bordo rosso sugli input
            general.showInputErrorShadow(inputElement);
        }
    },

    /* Funzione che si occupa di rimuovere / nascondere i label di errore e i bordi rossi sui campi */
    removeFieldError : function(fieldContainer, inputElement){
        fieldContainer.find("p.field-error").remove();
        inputElement.removeClass("field-error");
    },
    
    /* Funzione che controlla se ci sono campi vuoti, e in caso li segnala */
    checkIfEmptyInputFields : function(formContainer){
        var fieldsNotEmpty = true;
        var inputElements = formContainer.find(".info input[type='text']");
        
        // Controllo tutti gli input e vedo se sono vuoti
        $.each(inputElements, function(index, element){
            if(!$(this).val() && $(this).val() === ""){
                general.showFieldError($(this), general.errorMessages.empty);
                fieldsNotEmpty = false;
            }
        });
        
        return fieldsNotEmpty;
    },
    
    /* Metodo che controlla se campi per uno stesso input (mail, password) corrispondono oppure no */
    checkIfFieldMatches : function(firstInput, secondInput){
        if(firstInput.val() === "" || secondInput.val() === ""){
            return false;
        }
        if(firstInput.val() !== secondInput.val()){
            general.showFieldError(secondInput, general.errorMessages.notmatch);
            return false;
        }
        if(firstInput.val() === secondInput.val()){
            return true;
        }
    },
    
    /* Funzione che restituisce un preciso input dato un name */
    getInputByName : function(container, name){
        return container.find("input[name='" + name + "']");
    },
    
    /* Imposta lo stato del form */
    setFormStatus : function(status, newStatus){
        if(status !== false){
            status = newStatus;
        }
        return status;
    },
    
    /* Funzione che controlla se il form è compilato bene o meno */
    onSubmit : function(event){
        var formOK = true;
        var retVal = null;
        
        // Per prima cosa controllo che tutti i campi siano compilati
        retVal = general.checkIfEmptyInputFields($(general.formID));
        formOK = general.setFormStatus(formOK, retVal);
        
        // Inizio col controllare se le password sono uguali
        var password = general.getInputByName($(general.formID), formInputsInfo.password);
        var passwordConfirm = general.getInputByName($(general.formID), formInputsInfo.passwordConfirm);
        if(password !== null && password.val() !== ""){
            if(passwordConfirm === null || passwordConfirm.val() === ""){
                general.showFieldError(passwordConfirm, general.errorMessages.empty);
                retval = false;
            }else{
                retVal = general.checkIfFieldMatches(password, passwordConfirm);
            }
            formOK = general.setFormStatus(formOK, retVal);
        }
        
        var emailRegex = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+[a-zA-Z0-9-.]+$/;
        var email = general.getInputByName($(general.formID), formInputsInfo.email);
        var emailConfirm = general.getInputByName($(general.formID), formInputsInfo.emailConfirm);
        
        // Controllo se le email sono uguali (entra sia che sia vuota sia che sia non valida)
        if(email.val() !== "" && !emailRegex.test(email.val())){
            general.showFieldError(email, general.messages.error);
            emailConfirm.val("");
            formOK = general.setFormStatus(formOK, false);
        }else{
            retVal = general.checkIfFieldMatches(email, emailConfirm);
            formOK = general.setFormStatus(formOK, retVal);
        }
        
        // Regex utilizzato per città, via, provincia e stato
        var nameRegex = /^[a-zA-Z'\s]+$/;
        
        /* Controllo la via */
        var street = general.getInputByName($(general.formID), formInputsInfo.streetName);
        if(street.val() !== "" && !nameRegex.test(street.val())){
            general.showFieldError(street, general.messages.error);
            formOK = general.setFormStatus(formOK, false);
        }
        
        /* Controllo numero civico */
        var streetNumber = general.getInputByName($(general.formID), formInputsInfo.streetNumber);
        if(streetNumber.val() !== "" && isNaN(streetNumber.val())){
            general.showFieldError(streetNumber, general.messages.error);
            formOK = general.setFormStatus(formOK, false);
        }
        
        /* Controllo sulla città */
        var city = general.getInputByName($(general.formID), formInputsInfo.city);
        if(city.val() !== "" && !nameRegex.test(city.val())){
            general.showFieldError(city, general.messages.error);
            formOK = general.setFormStatus(formOK, false);
        }
        
        /* Controllo sulla provincia */
        var district = general.getInputByName($(general.formID), formInputsInfo.district);
        if(district.val() !== "" && !nameRegex.test(district.val())){
            general.showFieldError(district, general.messages.error);
            formOK = general.setFormStatus(formOK, false);
        }
        
        /* Controllo sul codice postale */
        var zip = general.getInputByName($(general.formID), formInputsInfo.zip);
        if(zip.val() !== "" && isNaN(zip.val()) || zip.val().toString().length !== 5){
            general.showFieldError(zip, general.messages.error);
            formOK = general.setFormStatus(formOK, false);
        }

        /* Controllo sullo Stato */
        var country = general.getInputByName($(general.formID), formInputsInfo.country);
        if(country.val() !== "" && !nameRegex.test(country.val())){
            general.showFieldError(country, general.messages.error);
            formOK = general.setFormStatus(formOK, false);
        }
        
        return formOK;
    }
    
};

$(document).ready(function() {
    $(general.showHideFormButtonID).click(general.onMouseClick);
    $(general.formID).submit(function(event){
        return general.onSubmit(event);
    });
    $(general.formID + " input").on("change", function(){
        general.removeFieldError($(this).parents(".info"), $(this));
    });
});