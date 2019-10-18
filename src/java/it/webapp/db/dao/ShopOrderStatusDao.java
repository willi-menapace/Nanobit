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
import it.webapp.db.entities.ShopOrderStatusEntity;
import java.util.List;

public interface ShopOrderStatusDao extends Dao<ShopOrderStatusEntity> {
	
	/**
	 * Gets the order statuses associated with an order
	 * 
	 * @param shopOrderId The associated order
	 * @return The order statuses associated with an order
	 * 
	 * @throws DaoException In case it is not possible to get the order statuses
	 */
	public List<ShopOrderStatusEntity> getByShopOrderId(int shopOrderId) throws DaoException;
	
	/**
	 * Inserts a new order status entry.
	 * 
	 * @param shopOrderStatus The entry to insert
	 * 
	 * @throws DaoException In case it is not possible to insert the entry
	 */
	public void insert(ShopOrderStatusEntity shopOrderStatus) throws DaoException;
	
}
