/*
    Author     	: Alessio Paternoster
    Desciption 	: Script JS per il file UpdateShop.jsp
*/
var general = {
    
    errorMessages : {
        empty : "*Campo vuoto. Compilalo",
        notmatch : "*Il campo non combacia",
        error: "*Campo errato"
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
        
        
        
    }
    
};

