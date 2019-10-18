/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.webapp.applicationcontroller;

import it.webapp.db.dao.jdbc.DaoException;
import it.webapp.db.dao.jdbc.DaoFactoryException;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcShopShipmentTypeDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.ShopShipmentTypeEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.UpdateShopShipmentTypeFormPreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.UpdateShopShipmentTypeFormView;
import it.webapp.view.ViewFactory;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Daniele Giuliani
 */
public class UpdateShopShipmentTypeFormController extends ApplicationControllerSkeleton {
    public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "UpdateShopShipmentTypeForm";   
    
    private JdbcShopShipmentTypeDao shipmentTypeDao;
    
    public UpdateShopShipmentTypeFormController() throws ApplicationControllerException {
        super(new UpdateShopShipmentTypeFormPreprocessor());
        try {
            shipmentTypeDao = JdbcDaoFactory.getShopShipmentTypeDao();
        } catch (DaoFactoryException e) {
            throw new ApplicationControllerException("Cannot initialize controller", e);
        }
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        
        int shopId = (int) request.getAttribute(UpdateShopShipmentTypeFormPreprocessor.SHOP_ID_ATTRIBUTE);
        
        try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
            try {
                // Fetch information about the shipment types of the current logged in shop
                List<ShopShipmentTypeEntity> shopShipmentTypes = shipmentTypeDao.getByShopId(shopId, transaction);

                // Prepare the view and then we call it
                UpdateShopShipmentTypeFormView usstfv = ViewFactory.getView(UpdateShopShipmentTypeFormView.class);
                UpdateShopShipmentTypeFormView.setRequestParameters("", shopShipmentTypes, request);
                usstfv.view(request, response);
            } catch(DaoException e) {
                //An error occurred, we must undo the changes
                transaction.rollback();

                Logger.log("Failed to show cart to user", e);
                ErrorView errorView = ViewFactory.getView(ErrorView.class);

                //Sets the parameters and send the error
                ErrorView.setRequestParameters("Database error", "An internal server error caused the request to fail", request);
                errorView.view(request, response);
            }
        } catch(DaoFactoryException e) {
            Logger.log("Failed to open transaction", e);
        } catch(SQLException e) {
            Logger.log("Failed to close transaction", e);
        }
    }
}
