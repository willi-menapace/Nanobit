/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.webapp.view;

import it.webapp.db.entities.ShopShipmentTypeEntity;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Daniele
 */
public class UpdateShopShipmentTypeFormView implements View{

    private static final String PAGE_URL = "/jsp/UpdateShopShipmentTypeForm.jsp";
    public static final String MESSAGE = "information_message";
    public static final String SHOP_SHIPMENT_TYPE_ENTITIES = "shop_shipment_type_entities";
    
    /**
    * Convenience method for setting View parameters
    * @param message Error message or confirmation message, empty if no message has to be shown
    * @param shopShipmentTypeEntities The various shop shipment capability of the current shop
    * @param request The request on which to set the parameters
    */
    public static final void setRequestParameters(String message, List<ShopShipmentTypeEntity> shopShipmentTypeEntities, HttpServletRequest request) {
        if(request == null) {
                throw new IllegalArgumentException("Request must be non null");
        }
        if(message == null) {
                throw new IllegalArgumentException("Message must be non null");
        }
        if(shopShipmentTypeEntities == null) {
                throw new IllegalArgumentException("shopShipmentTypeEntities must be non null");
        }

        request.setAttribute(UpdateShopShipmentTypeFormView.MESSAGE, message);
        request.setAttribute(UpdateShopShipmentTypeFormView.SHOP_SHIPMENT_TYPE_ENTITIES, shopShipmentTypeEntities);
    }
    
    @Override
    public void view(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher(PAGE_URL).forward(request, response);
        } catch (IOException | ServletException exception) {
            throw new IllegalArgumentException("Invalid redirect path " +  request.getContextPath() + PAGE_URL, exception);
        }
    }
    
}
