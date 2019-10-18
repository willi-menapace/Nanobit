/*******************************************************************************
 *        __                  _     _ _   
 *     /\ \ \__ _ _ __   ___ | |__ (_) |_ 
 *    /  \/ / _` | '_ \ / _ \| '_ \| | __|
 *   / /\  / (_| | | | | (_) | |_) | | |_ 
 *   \_\ \/ \__,_|_| |_|\___/|_.__/|_|\__|
 * 
 * *****************************************************************************
 * 
 *  Author: Willi Menapace <willi.menapace@gmail.com>
 * 
 ******************************************************************************/

package it.webapp.applicationcontroller;

import it.webapp.db.dao.jdbc.DaoException;
import it.webapp.db.dao.jdbc.DaoFactoryException;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcShopShipmentTypeDao;
import it.webapp.db.dao.jdbc.Transaction;
import it.webapp.db.entities.ShipmentType;
import it.webapp.db.entities.ShopShipmentTypeEntity;
import it.webapp.logging.Logger;
import it.webapp.requestprocessor.UpdateShopShipmentTypePreprocessor;
import it.webapp.servlet.FrontPageController;
import it.webapp.view.ErrorView;
import it.webapp.view.MessageView;
import it.webapp.view.UpdateShopShipmentTypeFormView;
import it.webapp.view.ViewFactory;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Updates shipment type information about the current shop
 */
public class UpdateShopShipmentTypeController extends ApplicationControllerSkeleton {

	public static final String URL = FrontPageController.CONTROLLERS_BASE_PATH + "/" + "UpdateShopShipmentType";
	
	private JdbcShopShipmentTypeDao shopShipmentTypeDao;
	
	public UpdateShopShipmentTypeController() throws ApplicationControllerException {
		super(new UpdateShopShipmentTypePreprocessor());
		
		try {
			shopShipmentTypeDao = JdbcDaoFactory.getShopShipmentTypeDao();
		} catch(DaoFactoryException e) {
			throw new ApplicationControllerException("Cannot initialize controller", e);
		}
	}
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		int shopId = (int) request.getAttribute(UpdateShopShipmentTypePreprocessor.SHOP_ID_ATTRIBUTE);
		List<ShipmentType> shipmentTypes = (List<ShipmentType>) request.getAttribute(UpdateShopShipmentTypePreprocessor.SHIPMENT_TYPE_IDS_ATTRIBUTE);
		List<BigDecimal> shipmentPrices = (List<BigDecimal>) request.getAttribute(UpdateShopShipmentTypePreprocessor.PRICES_ATTRIBUTE);
		
		Iterator<ShipmentType> shipmentIterator = shipmentTypes.iterator();
		Iterator<BigDecimal> pricesIterator = shipmentPrices.iterator();
		
		//We will take multiple actions so a transaction is required
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
		
			try {
				//Processes every input element
				while(shipmentIterator.hasNext() && pricesIterator.hasNext()) {
					BigDecimal currentPrice = pricesIterator.next();
					ShipmentType currentShipmentType = shipmentIterator.next();

					ShopShipmentTypeEntity shipmentTypeEntity = shopShipmentTypeDao.getByShopAndShipmentType(shopId, currentShipmentType, transaction);

					//We must delete an existing entry
					if(currentPrice.doubleValue() < 0 && shipmentTypeEntity != null) {

						shopShipmentTypeDao.delete(shopId, currentShipmentType, transaction);

					//Insert
					} else if(shipmentTypeEntity == null) {

						shipmentTypeEntity = new ShopShipmentTypeEntity(shopId, currentShipmentType, currentPrice);
						shopShipmentTypeDao.insert(shipmentTypeEntity, transaction);

					//Update
					} else {

						shipmentTypeEntity.setPrice(currentPrice);
						shopShipmentTypeDao.update(shipmentTypeEntity, transaction);

					}
				}
                                
				MessageView mv = ViewFactory.getView(MessageView.class);
				MessageView.setRequestParameters("Congratulazioni", "Le tue preferenze sono state aggiornate.", request);
				mv.view(request, response);
                                
			} catch(DaoException e) {
				transaction.rollback();
				
				Logger.log("A Database error occurred while updating shop shipment types", e);
				
				ErrorView errorView = ViewFactory.getView(ErrorView.class);

				//Sets the parameters and send the error
				ErrorView.setRequestParameters("Database error", "An internal server error caused the request to fail", request);
				errorView.view(request, response);
			}
			
		} catch(DaoFactoryException e) {
			Logger.log("Could not open transaction", e);
		} catch(SQLException e) {
			Logger.log("Could not close transaction", e);
		}
	}

	@Override
	public void processParseOfValidationFailure(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Bad Parameters", "Request parameters were not set correctly", request);
		errorView.view(request, response);
	}

	@Override
	public void processAuthentication(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Authentication failure", "User must be authenticated", request);
		errorView.view(request, response);
	}

	@Override
	public void processAuthorization(HttpServletRequest request, HttpServletResponse response) {
		ErrorView errorView = ViewFactory.getView(ErrorView.class);
		
		//Sets the parameters and send the error
		ErrorView.setRequestParameters("Authorization failure", "User must be a Shop", request);
		errorView.view(request, response);
	}

}
