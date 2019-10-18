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

package it.webapp.db.dao;

import it.webapp.db.dao.jdbc.DaoException;
import it.webapp.db.entities.ShipmentType;
import it.webapp.db.entities.ShopShipmentTypeEntity;
import java.util.List;

public interface ShopShipmentTypeDao extends Dao<ShopShipmentTypeEntity> {
	
	/**
	 * Gets the ShopShipmentTypeEntity associated to the given shop and shipment type
	 * 
	 * @param shopId The associated shop
	 * @param shipmentType The associated shipment type
	 * @return The ShopShipmentTypeEntity that matches the given filters, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the ShopShipmentTypeEntity
	 */
	public ShopShipmentTypeEntity getByShopAndShipmentType(int shopId, ShipmentType shipmentType) throws DaoException;
	
	/**
	 * Gets the shop shipment types associated with a shop
	 * 
	 * @param shopId The shop to search
	 * @return The shop shipment types associated with a shop
	 * 
	 * @throws DaoException In case it is not possible to get the results
	 */
	public List<ShopShipmentTypeEntity> getByShopId(int shopId) throws DaoException;
	
	/**
	 * Inserts a new shop shipment type
	 * 
	 * @param shopShipmentType The shop shipment type to insert
	 * 
	 * @throws DaoException In case it is not possible to insert the ShopShipmentType
	 */
	public void insert(ShopShipmentTypeEntity shopShipmentType) throws DaoException;
	
	/**
	 * Deletes a shop shipment type
	 * 
	 * @param shopId The shop shipment type to delete (partial key)
	 * @param shipmentType The shop shipment type to delete (partial key)
	 * 
	 * @throws DaoException In case it is not possible to delete the entry
	 */
	public void delete(int shopId, ShipmentType shipmentType) throws DaoException;
	
	/**
	 * Updates a shop shipment type
	 * 
	 * @param shopShipmentType The shop shipment type to update
	 * 
	 * @throws DaoException In case it is not possible to update the entry
	 */
	public void update(ShopShipmentTypeEntity shopShipmentType) throws DaoException;
	
}
